package com.example.data.repository

import com.example.data.OttCatalog
import com.example.model.MediaItem
import com.example.model.MediaType
import com.example.model.OttPlatform
import kotlinx.coroutines.delay

/**
 * In-memory Mock implementation of [SearchRepository] designed for testing UI flows,
 * edge cases (loading delays, empty responses, error recovery), and offline previews.
 */
class MockSearchRepository(
  initialItems: List<MediaItem> = OttCatalog.items,
  var simulatedDelayMillis: Long = 0L,
  var shouldSimulateError: Boolean = false
) : SearchRepository {

  private val _items = mutableListOf<MediaItem>().apply { addAll(initialItems) }
  private val _recentSearches = mutableListOf(
    "Stranger Things",
    "Christopher Nolan",
    "Mirzapur",
    "Cricket",
    "Anime"
  )

  /**
   * Directly inject or replace mock items for isolated UI test scenarios.
   */
  fun setMockItems(newItems: List<MediaItem>) {
    _items.clear()
    _items.addAll(newItems)
  }

  fun addMockItem(item: MediaItem) {
    _items.add(item)
  }

  fun clearMockItems() {
    _items.clear()
  }

  override suspend fun search(
    query: String,
    platform: OttPlatform
  ): List<MediaItem> {
    if (simulatedDelayMillis > 0) {
      delay(simulatedDelayMillis)
    }

    if (shouldSimulateError) {
      throw RuntimeException("Simulated search failure for testing UI error states.")
    }

    val trimmed = query.trim()
    if (trimmed.isNotBlank()) {
      saveRecentSearch(trimmed)
    }

    return _items.filter { item ->
      val matchesPlatform = platform == OttPlatform.ALL || item.platform == platform
      val matchesQuery = trimmed.isBlank() ||
          item.title.contains(trimmed, ignoreCase = true) ||
          item.genre.contains(trimmed, ignoreCase = true) ||
          item.synopsis.contains(trimmed, ignoreCase = true) ||
          item.cast.any { it.contains(trimmed, ignoreCase = true) } ||
          item.director.contains(trimmed, ignoreCase = true) ||
          item.platform.displayName.contains(trimmed, ignoreCase = true)

      matchesPlatform && matchesQuery
    }
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
    return _items.filter { it.isTrending }.take(6).ifEmpty { _items.take(6) }
  }

  companion object {
    /**
     * Helper to create a standalone minimal dataset for rapid component testing.
     */
    fun createSampleTestingData(): List<MediaItem> = listOf(
      MediaItem(
        id = "mock_test_1",
        title = "Inception",
        platform = OttPlatform.NETFLIX,
        mediaType = MediaType.MOVIE,
        genre = "Sci-Fi, Thriller",
        rating = 8.8,
        votes = "2.5M",
        year = 2010,
        duration = "2h 28m",
        synopsis = "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
        cast = listOf("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"),
        director = "Christopher Nolan",
        posterUrl = "https://image.tmdb.org/t/p/w500/o29e9M2qQhC8jYJ9u25pEln2N1w.jpg",
        backdropUrl = "",
        trailerUrl = "https://www.youtube.com/watch?v=YoHD9XEInc0",
        watchUrl = "https://www.netflix.com/title/70131314",
        isTrending = true
      ),
      MediaItem(
        id = "mock_test_2",
        title = "The Boys",
        platform = OttPlatform.PRIME_VIDEO,
        mediaType = MediaType.SERIES,
        genre = "Action, Satire, Sci-Fi",
        rating = 8.7,
        votes = "650K",
        year = 2019,
        duration = "4 Seasons",
        synopsis = "A fun and irreverent take on what happens when superheroes abuse their superpowers rather than use them for good.",
        cast = listOf("Karl Urban", "Jack Quaid", "Antony Starr"),
        director = "Eric Kripke",
        posterUrl = "https://image.tmdb.org/t/p/w500/7Ns6tO3aYjTlER1x0z3p7Z3h7aF.jpg",
        backdropUrl = "",
        trailerUrl = "https://www.youtube.com/watch?v=06rueu_fh30",
        watchUrl = "https://www.primevideo.com/detail/0K3C8W7OQQ4Z7Y4X55G137P",
        isTrending = true
      ),
      MediaItem(
        id = "mock_test_3",
        title = "Ted Lasso",
        platform = OttPlatform.APPLE_TV,
        mediaType = MediaType.SERIES,
        genre = "Comedy, Drama, Sports",
        rating = 8.8,
        votes = "340K",
        year = 2020,
        duration = "3 Seasons",
        synopsis = "American college football coach Ted Lasso heads to London to manage AFC Richmond, a struggling English Premier League football team.",
        cast = listOf("Jason Sudeikis", "Hannah Waddingham", "Brett Goldstein"),
        director = "Brendan Hunt",
        posterUrl = "https://image.tmdb.org/t/p/w500/3Tf8vXykB50H0Zk67gO1UqN6v0A.jpg",
        backdropUrl = "",
        trailerUrl = "https://www.youtube.com/watch?v=3u7EIiohs6U",
        watchUrl = "https://tv.apple.com/us/show/ted-lasso/umc.cmc.vtoh0mn0x10odnvuxhpacqa8",
        isTrending = false
      )
    )
  }
}
