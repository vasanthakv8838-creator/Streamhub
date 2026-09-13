package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.data.repository.MockSearchRepository
import com.example.model.MediaItem
import com.example.model.MediaType
import com.example.model.OttPlatform
import com.example.ui.components.SearchScreen
import com.example.ui.theme.BackgroundDark
import com.example.viewmodel.SearchUiState
import com.example.viewmodel.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class SearchFlowRobolectricTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  private val testSampleItems = listOf(
    MediaItem(
      id = "test_item_inception",
      title = "Inception",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.MOVIE,
      genre = "Sci-Fi",
      rating = 8.8,
      votes = "2.4M",
      year = 2010,
      duration = "2h 28m",
      synopsis = "A thief who steals secrets through dream technology.",
      cast = listOf("Leonardo DiCaprio"),
      director = "Christopher Nolan",
      posterUrl = "",
      backdropUrl = "",
      trailerUrl = "",
      watchUrl = "https://netflix.com",
      isTrending = true
    ),
    MediaItem(
      id = "test_item_ted_lasso",
      title = "Ted Lasso",
      platform = OttPlatform.APPLE_TV,
      mediaType = MediaType.SERIES,
      genre = "Comedy",
      rating = 8.8,
      votes = "300K",
      year = 2020,
      duration = "3 Seasons",
      synopsis = "American coach manages British football team.",
      cast = listOf("Jason Sudeikis"),
      director = "Brendan Hunt",
      posterUrl = "",
      backdropUrl = "",
      trailerUrl = "",
      watchUrl = "https://tv.apple.com",
      isTrending = false
    )
  )

  @Test
  fun `mock repository searches correctly by title, cast, and platform`() = runTest {
    val repository = MockSearchRepository(initialItems = testSampleItems)

    // 1. Search by title
    val resultsInception = repository.search("Inception", OttPlatform.ALL)
    assertEquals(1, resultsInception.size)
    assertEquals("Inception", resultsInception.first().title)

    // 2. Search by cast member
    val resultsCast = repository.search("Jason Sudeikis", OttPlatform.ALL)
    assertEquals(1, resultsCast.size)
    assertEquals("Ted Lasso", resultsCast.first().title)

    // 3. Search with non-matching platform filter
    val resultsWrongPlatform = repository.search("Inception", OttPlatform.APPLE_TV)
    assertTrue(resultsWrongPlatform.isEmpty())

    // 4. Search with matching platform filter
    val resultsRightPlatform = repository.search("Inception", OttPlatform.NETFLIX)
    assertEquals(1, resultsRightPlatform.size)
  }

  @Test
  fun `mock repository records and clears recent searches`() = runTest {
    val repository = MockSearchRepository(initialItems = testSampleItems)
    repository.clearRecentSearches()
    assertTrue(repository.getRecentSearches().isEmpty())

    repository.saveRecentSearch("Christopher Nolan")
    repository.saveRecentSearch("Comedy Movies")

    val recents = repository.getRecentSearches()
    assertEquals(2, recents.size)
    assertEquals("Comedy Movies", recents[0])
    assertEquals("Christopher Nolan", recents[1])

    repository.clearRecentSearches()
    assertTrue(repository.getRecentSearches().isEmpty())
  }

  @Test
  fun `search screen UI displays text input field, accepts input and reveals results list`() {
    val repository = MockSearchRepository(initialItems = testSampleItems)
    var clickedItem: MediaItem? = null

    composeTestRule.setContent {
      SearchScreen(
        repository = repository,
        onItemClick = { clickedItem = it }
      )
    }

    composeTestRule.waitForIdle()

    // Verify text input is present
    composeTestRule.onNodeWithTag("search_text_input").assertIsDisplayed()

    // Type "Inception" into text input field
    composeTestRule.onNodeWithTag("search_text_input").performTextInput("Inception")
    composeTestRule.waitForIdle()

    // Results list should appear containing Inception card
    composeTestRule.onNodeWithTag("search_results_list").assertIsDisplayed()
    composeTestRule.onNodeWithTag("search_result_item_test_item_inception").assertIsDisplayed()

    // Click item to verify callback
    composeTestRule.onNodeWithTag("search_result_item_test_item_inception").performClick()
    assertEquals("test_item_inception", clickedItem?.id)
  }

  @Test
  fun `search screen UI displays empty state when no results match`() {
    val repository = MockSearchRepository(initialItems = testSampleItems)

    composeTestRule.setContent {
      SearchScreen(repository = repository)
    }

    composeTestRule.waitForIdle()

    // Enter nonexistent query
    composeTestRule.onNodeWithTag("search_text_input").performTextInput("ZzzNonExistentMovie999")
    composeTestRule.waitForIdle()

    // Verify empty state is displayed
    composeTestRule.onNodeWithTag("search_empty_state").assertIsDisplayed()
  }

  @Test
  fun `clearing search query restores initial state with recent searches`() {
    val repository = MockSearchRepository(initialItems = testSampleItems)

    composeTestRule.setContent {
      SearchScreen(repository = repository)
    }

    composeTestRule.waitForIdle()

    // Enter query
    composeTestRule.onNodeWithTag("search_text_input").performTextInput("Ted")
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithTag("search_clear_button").assertIsDisplayed()
    composeTestRule.onNodeWithTag("search_clear_button").performClick()
    composeTestRule.waitForIdle()

    // Clear button should disappear after clearing
    composeTestRule.onNodeWithTag("search_clear_button").assertDoesNotExist()
  }
}
