package com.example.data.repository

import com.example.model.MediaItem
import com.example.model.OttPlatform

interface SearchRepository {
  /**
   * Searches for media items matching the given query and optional platform filter.
   */
  suspend fun search(
    query: String,
    platform: OttPlatform = OttPlatform.ALL
  ): List<MediaItem>

  /**
   * Retrieves recent search queries recorded by the user.
   */
  suspend fun getRecentSearches(): List<String>

  /**
   * Saves a recent search query.
   */
  suspend fun saveRecentSearch(query: String)

  /**
   * Clears all recent searches.
   */
  suspend fun clearRecentSearches()

  /**
   * Retrieves curated trending suggestions when the search query is blank.
   */
  suspend fun getTrendingSuggestions(): List<MediaItem>
}
