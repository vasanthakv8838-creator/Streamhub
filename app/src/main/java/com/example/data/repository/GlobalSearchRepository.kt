package com.example.data.repository

import android.util.Log
import com.example.BuildConfig
import com.example.data.OttCatalog
import com.example.data.gemini.GeminiService
import com.example.data.network.TmdbService
import com.example.model.MediaItem
import com.example.model.OttPlatform
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Production implementation of [SearchRepository] providing global access to movies,
 * TV series, and video content across all 7 streaming services (Netflix, Prime Video,
 * JioHotstar, Apple TV+, Sony LIV, ZEE5, YouTube).
 *
 * Integrates:
 * 1. Curated zero-latency local catalog.
 * 2. Live TMDB (The Movie Database) multi-search API.
 * 3. Gemini AI Universal Streaming Catalog Indexer for discovering any global title.
 */
class GlobalSearchRepository(
  private val tmdbService: TmdbService = TmdbService(),
  private val geminiService: GeminiService = GeminiService(),
  private val localCatalog: List<MediaItem> = OttCatalog.items
) : SearchRepository {

  private val _recentSearches = mutableListOf(
    "Stranger Things",
    "Oppenheimer",
    "Dune",
    "Panchayat",
    "Breaking Bad",
    "Cricket",
    "Interstellar"
  )

  override suspend fun search(
    query: String,
    platform: OttPlatform
  ): List<MediaItem> = coroutineScope {
    val trimmed = query.trim()
    if (trimmed.isNotBlank()) {
      saveRecentSearch(trimmed)
    }

    // 1. Instant local matching
    val localMatches = localCatalog.filter { item ->
      val matchesPlatform = platform == OttPlatform.ALL || item.platform == platform
      val matchesQuery = trimmed.isBlank() ||
          item.title.contains(trimmed, ignoreCase = true) ||
          item.genre.contains(trimmed, ignoreCase = true) ||
          item.synopsis.contains(trimmed, ignoreCase = true) ||
          item.cast.any { it.contains(trimmed, ignoreCase = true) } ||
          item.director.contains(trimmed, ignoreCase = true)

      matchesPlatform && matchesQuery
    }

    // If query is short or empty, return local matches directly
    if (trimmed.length < 2) {
      return@coroutineScope localMatches
    }

    // If local matches already satisfy the query, return them immediately
    if (localMatches.any { it.title.contains(trimmed, ignoreCase = true) }) {
      return@coroutineScope localMatches
    }

    // 2. Fetch remote / AI global catalog results concurrently
    val tmdbApiKey = try {
      val keyField = BuildConfig::class.java.getField("TMDB_API_KEY")
      keyField.get(null) as? String ?: ""
    } catch (_: Exception) {
      ""
    }

    val tmdbDeferred = async {
      if (tmdbApiKey.isNotBlank() && tmdbApiKey != "MY_TMDB_API_KEY") {
        tmdbService.searchMulti(trimmed, tmdbApiKey, platform)
      } else {
        emptyList()
      }
    }

    val aiDeferred = async {
      try {
        geminiService.searchGlobalTitlesWithAi(trimmed, platform)
      } catch (e: Exception) {
        Log.w("GlobalSearchRepository", "AI search failed: ${e.message}")
        emptyList()
      }
    }

    val tmdbResults = tmdbDeferred.await()
    val aiResults = aiDeferred.await()

    // 3. Merge and deduplicate by normalized title
    val existingTitles = localMatches.map { it.title.lowercase().trim() }.toMutableSet()
    val combinedList = mutableListOf<MediaItem>().apply {
      addAll(localMatches)
    }

    for (item in tmdbResults) {
      if (platform != OttPlatform.ALL && item.platform != platform) continue
      val norm = item.title.lowercase().trim()
      if (!existingTitles.contains(norm)) {
        existingTitles.add(norm)
        combinedList.add(item)
      }
    }

    for (item in aiResults) {
      if (platform != OttPlatform.ALL && item.platform != platform) continue
      val norm = item.title.lowercase().trim()
      if (!existingTitles.contains(norm)) {
        existingTitles.add(norm)
        combinedList.add(item)
      }
    }

    combinedList
  }

  override suspend fun getRecentSearches(): List<String> {
    return _recentSearches.toList()
  }

  override suspend fun saveRecentSearch(query: String) {
    val trimmed = query.trim()
    if (trimmed.isBlank()) return
    _recentSearches.remove(trimmed)
    _recentSearches.add(0, trimmed)
    if (_recentSearches.size > 10) {
      _recentSearches.removeAt(_recentSearches.lastIndex)
    }
  }

  override suspend fun clearRecentSearches() {
    _recentSearches.clear()
  }

  override suspend fun getTrendingSuggestions(): List<MediaItem> {
    return localCatalog.filter { it.isTrending }.take(6).ifEmpty { localCatalog.take(6) }
  }
}
