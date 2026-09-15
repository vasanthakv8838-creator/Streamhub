package com.example

import com.example.data.network.TmdbService
import com.example.data.repository.GlobalSearchRepository
import com.example.model.MediaItem
import com.example.model.MediaType
import com.example.model.OttPlatform
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GlobalSearchRepositoryTest {

  private val sampleLocalItems = listOf(
    MediaItem(
      id = "item_stranger_things",
      title = "Stranger Things",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.SERIES,
      genre = "Sci-Fi",
      rating = 8.7,
      votes = "1.3M",
      year = 2024,
      duration = "4 Seasons",
      synopsis = "A boy vanishes in a small town.",
      cast = listOf("Millie Bobby Brown"),
      director = "The Duffer Brothers",
      posterUrl = "",
      backdropUrl = "",
      trailerUrl = "",
      watchUrl = "https://netflix.com",
      isTrending = true
    ),
    MediaItem(
      id = "item_panchayat",
      title = "Panchayat",
      platform = OttPlatform.PRIME_VIDEO,
      mediaType = MediaType.SERIES,
      genre = "Comedy • Drama",
      rating = 8.9,
      votes = "120K",
      year = 2024,
      duration = "3 Seasons",
      synopsis = "An engineering graduate takes a job in a remote village.",
      cast = listOf("Jitendra Kumar"),
      director = "Deepak Kumar Mishra",
      posterUrl = "",
      backdropUrl = "",
      trailerUrl = "",
      watchUrl = "https://primevideo.com",
      isTrending = true
    )
  )

  @Test
  fun `global repository finds matching local titles instantly`() = runTest {
    val repository = GlobalSearchRepository(localCatalog = sampleLocalItems)
    val results = repository.search("Stranger", OttPlatform.ALL)

    assertEquals(1, results.size)
    assertEquals("Stranger Things", results.first().title)
    assertEquals(OttPlatform.NETFLIX, results.first().platform)
  }

  @Test
  fun `global repository filters by specific OTT platform`() = runTest {
    val repository = GlobalSearchRepository(localCatalog = sampleLocalItems)

    val netflixResults = repository.search("Stranger", OttPlatform.NETFLIX)
    assertEquals(1, netflixResults.size)

    val primeResults = repository.search("Stranger", OttPlatform.PRIME_VIDEO)
    assertTrue(primeResults.isEmpty())
  }

  @Test
  fun `global repository generates deep link for streaming platform`() {
    val tmdbService = TmdbService()
    val netflixUrl = tmdbService.getPlatformWatchUrl(OttPlatform.NETFLIX, "Inception")
    val hotstarUrl = tmdbService.getPlatformWatchUrl(OttPlatform.JIO_HOTSTAR, "Dune")
    val primeUrl = tmdbService.getPlatformWatchUrl(OttPlatform.PRIME_VIDEO, "The Boys")

    assertTrue(netflixUrl.contains("netflix.com/search"))
    assertTrue(hotstarUrl.contains("hotstar.com/in/explore"))
    assertTrue(primeUrl.contains("primevideo.com/search"))
  }

  @Test
  fun `global repository tracks and clears recent searches`() = runTest {
    val repository = GlobalSearchRepository(localCatalog = sampleLocalItems)
    repository.clearRecentSearches()
    assertTrue(repository.getRecentSearches().isEmpty())

    repository.saveRecentSearch("Oppenheimer")
    repository.saveRecentSearch("Interstellar")

    val recents = repository.getRecentSearches()
    assertEquals(2, recents.size)
    assertEquals("Interstellar", recents[0])
    assertEquals("Oppenheimer", recents[1])
  }
}
