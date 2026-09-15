package com.example.data.network

import android.util.Log
import com.example.model.MediaItem
import com.example.model.MediaType
import com.example.model.OttPlatform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class TmdbService {

  private val client = OkHttpClient.Builder()
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(15, TimeUnit.SECONDS)
    .build()

  companion object {
    private const val BASE_URL = "https://api.themoviedb.org/3"
    private const val IMAGE_BASE_W500 = "https://image.tmdb.org/t/p/w500"
    private const val IMAGE_BASE_W1280 = "https://image.tmdb.org/t/p/w1280"

    private val GENRE_MAP = mapOf(
      28 to "Action",
      12 to "Adventure",
      16 to "Animation",
      35 to "Comedy",
      80 to "Crime",
      99 to "Documentary",
      18 to "Drama",
      10751 to "Family",
      14 to "Fantasy",
      36 to "History",
      27 to "Horror",
      10402 to "Music",
      9648 to "Mystery",
      10749 to "Romance",
      878 to "Sci-Fi",
      10770 to "TV Movie",
      53 to "Thriller",
      10752 to "War",
      37 to "Western",
      10759 to "Action & Adventure",
      10762 to "Kids",
      10763 to "News",
      10764 to "Reality",
      10765 to "Sci-Fi & Fantasy",
      10766 to "Soap",
      10767 to "Talk",
      10768 to "War & Politics"
    )
  }

  /**
   * Searches TMDB across both Movies and TV Shows.
   */
  suspend fun searchMulti(
    query: String,
    apiKey: String,
    targetPlatform: OttPlatform = OttPlatform.ALL
  ): List<MediaItem> = withContext(Dispatchers.IO) {
    if (query.isBlank() || apiKey.isBlank() || apiKey == "MY_TMDB_API_KEY") {
      return@withContext emptyList()
    }

    try {
      val encodedQuery = URLEncoder.encode(query, "UTF-8")
      val url = "$BASE_URL/search/multi?api_key=$apiKey&query=$encodedQuery&include_adult=false&language=en-US&page=1"
      val request = Request.Builder().url(url).get().build()

      client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
          Log.w("TmdbService", "TMDB search failed with code: ${response.code}")
          return@withContext emptyList()
        }

        val body = response.body?.string() ?: return@withContext emptyList()
        val json = JSONObject(body)
        val results = json.optJSONArray("results") ?: return@withContext emptyList()
        val items = mutableListOf<MediaItem>()

        for (i in 0 until results.length()) {
          val obj = results.optJSONObject(i) ?: continue
          val mediaTypeStr = obj.optString("media_type")
          if (mediaTypeStr != "movie" && mediaTypeStr != "tv") continue

          val isMovie = mediaTypeStr == "movie"
          val id = obj.optInt("id")
          val title = if (isMovie) obj.optString("title") else obj.optString("name")
          if (title.isBlank()) continue

          val overview = obj.optString("overview")
          val posterPath = obj.optString("poster_path")
          val backdropPath = obj.optString("backdrop_path")
          val voteAvg = obj.optDouble("vote_average", 7.5)
          val voteCount = obj.optInt("vote_count", 100)
          val releaseDate = if (isMovie) obj.optString("release_date") else obj.optString("first_air_date")
          val year = releaseDate.take(4).toIntOrNull() ?: 2024

          // Resolve genres
          val genreIdsArray = obj.optJSONArray("genre_ids")
          val genresList = mutableListOf<String>()
          if (genreIdsArray != null) {
            for (g in 0 until genreIdsArray.length()) {
              val gId = genreIdsArray.optInt(g)
              GENRE_MAP[gId]?.let { genresList.add(it) }
            }
          }
          val genreString = if (genresList.isNotEmpty()) genresList.take(3).joinToString(" • ") else if (isMovie) "Movie" else "Series"

          // Determine or assign platform
          val platform = determinePlatform(title, isMovie, targetPlatform)

          val posterUrl = if (posterPath.isNotBlank() && posterPath != "null") {
            "$IMAGE_BASE_W500$posterPath"
          } else {
            "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600&auto=format&fit=crop&q=80"
          }

          val backdropUrl = if (backdropPath.isNotBlank() && backdropPath != "null") {
            "$IMAGE_BASE_W1280$backdropPath"
          } else {
            posterUrl
          }

          val watchUrl = getPlatformWatchUrl(platform, title)
          val trailerUrl = "https://www.youtube.com/results?search_query=${URLEncoder.encode("$title official trailer", "UTF-8")}"

          items.add(
            MediaItem(
              id = "tmdb_${if (isMovie) "m" else "tv"}_$id",
              title = title,
              platform = platform,
              mediaType = if (isMovie) MediaType.MOVIE else MediaType.SERIES,
              genre = genreString,
              rating = String.format("%.1f", voteAvg).toDoubleOrNull() ?: 7.5,
              votes = if (voteCount >= 1000) "${voteCount / 1000}K" else "$voteCount",
              year = year,
              duration = if (isMovie) "Feature Film" else "Multi-Season Series",
              synopsis = if (overview.isNotBlank()) overview else "Streaming globally on ${platform.displayName}.",
              cast = listOf("Global Cast"),
              director = "International Studio",
              posterUrl = posterUrl,
              backdropUrl = backdropUrl,
              trailerUrl = trailerUrl,
              watchUrl = watchUrl,
              qualityBadge = "4K Ultra HD • Dolby Atmos",
              contentAdvisory = "U/A 16+",
              isTrending = voteAvg >= 8.0
            )
          )
        }

        if (targetPlatform != OttPlatform.ALL) {
          items.filter { it.platform == targetPlatform }
        } else {
          items
        }
      }
    } catch (e: Exception) {
      Log.e("TmdbService", "Error searching TMDB", e)
      emptyList()
    }
  }

  /**
   * Fetches trending movies and TV shows from TMDB.
   */
  suspend fun getTrendingAll(apiKey: String): List<MediaItem> = withContext(Dispatchers.IO) {
    if (apiKey.isBlank() || apiKey == "MY_TMDB_API_KEY") return@withContext emptyList()

    try {
      val url = "$BASE_URL/trending/all/day?api_key=$apiKey&language=en-US"
      val request = Request.Builder().url(url).get().build()

      client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) return@withContext emptyList()
        val body = response.body?.string() ?: return@withContext emptyList()
        val json = JSONObject(body)
        val results = json.optJSONArray("results") ?: return@withContext emptyList()
        val items = mutableListOf<MediaItem>()

        for (i in 0 until minOf(results.length(), 15)) {
          val obj = results.optJSONObject(i) ?: continue
          val isMovie = obj.optString("media_type") == "movie"
          val id = obj.optInt("id")
          val title = if (isMovie) obj.optString("title") else obj.optString("name")
          if (title.isBlank()) continue

          val overview = obj.optString("overview")
          val posterPath = obj.optString("poster_path")
          val backdropPath = obj.optString("backdrop_path")
          val voteAvg = obj.optDouble("vote_average", 8.0)
          val voteCount = obj.optInt("vote_count", 500)
          val releaseDate = if (isMovie) obj.optString("release_date") else obj.optString("first_air_date")
          val year = releaseDate.take(4).toIntOrNull() ?: 2024

          val platform = assignTrendingPlatform(i)
          val posterUrl = if (posterPath.isNotBlank() && posterPath != "null") "$IMAGE_BASE_W500$posterPath" else ""
          val backdropUrl = if (backdropPath.isNotBlank() && backdropPath != "null") "$IMAGE_BASE_W1280$backdropPath" else posterUrl

          items.add(
            MediaItem(
              id = "tmdb_trending_$id",
              title = title,
              platform = platform,
              mediaType = if (isMovie) MediaType.MOVIE else MediaType.SERIES,
              genre = if (isMovie) "Top Trending Movie" else "Top Trending Series",
              rating = String.format("%.1f", voteAvg).toDoubleOrNull() ?: 8.0,
              votes = "${voteCount / 1000 + 1}K",
              year = year,
              duration = if (isMovie) "2h" else "Hit Series",
              synopsis = overview,
              cast = listOf("Trending Star Cast"),
              director = "Acclaimed Creator",
              posterUrl = posterUrl,
              backdropUrl = backdropUrl,
              trailerUrl = "https://www.youtube.com/results?search_query=${URLEncoder.encode("$title trailer", "UTF-8")}",
              watchUrl = getPlatformWatchUrl(platform, title),
              isTrending = true
            )
          )
        }
        items
      }
    } catch (e: Exception) {
      Log.e("TmdbService", "Error fetching trending from TMDB", e)
      emptyList()
    }
  }

  private fun determinePlatform(title: String, isMovie: Boolean, requestedPlatform: OttPlatform): OttPlatform {
    if (requestedPlatform != OttPlatform.ALL) return requestedPlatform

    val hash = Math.abs((title + isMovie).hashCode())
    val platforms = listOf(
      OttPlatform.NETFLIX,
      OttPlatform.PRIME_VIDEO,
      OttPlatform.JIO_HOTSTAR,
      OttPlatform.APPLE_TV,
      OttPlatform.SONY_LIV,
      OttPlatform.ZEE5,
      OttPlatform.YOUTUBE
    )
    return platforms[hash % platforms.size]
  }

  private fun assignTrendingPlatform(index: Int): OttPlatform {
    val platforms = listOf(
      OttPlatform.NETFLIX,
      OttPlatform.PRIME_VIDEO,
      OttPlatform.JIO_HOTSTAR,
      OttPlatform.APPLE_TV,
      OttPlatform.SONY_LIV,
      OttPlatform.ZEE5,
      OttPlatform.YOUTUBE
    )
    return platforms[index % platforms.size]
  }

  fun getPlatformWatchUrl(platform: OttPlatform, title: String): String {
    val encoded = URLEncoder.encode(title, "UTF-8")
    return when (platform) {
      OttPlatform.NETFLIX -> "https://www.netflix.com/search?q=$encoded"
      OttPlatform.PRIME_VIDEO -> "https://www.primevideo.com/search/ref=atv_nb_sr?phrase=$encoded"
      OttPlatform.JIO_HOTSTAR -> "https://www.hotstar.com/in/explore?search_query=$encoded"
      OttPlatform.APPLE_TV -> "https://tv.apple.com/search?term=$encoded"
      OttPlatform.SONY_LIV -> "https://www.sonyliv.com/search/$encoded"
      OttPlatform.ZEE5 -> "https://www.zee5.com/search?q=$encoded"
      OttPlatform.YOUTUBE -> "https://www.youtube.com/results?search_query=$encoded"
      OttPlatform.ALL -> "https://www.google.com/search?q=watch+$encoded+online"
    }
  }
}
