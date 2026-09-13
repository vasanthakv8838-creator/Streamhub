package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.repository.MockSearchRepository
import com.example.data.repository.SearchRepository
import com.example.model.MediaItem
import com.example.model.OttPlatform
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SearchUiState {
  data class Initial(
    val recentSearches: List<String> = emptyList(),
    val suggestions: List<MediaItem> = emptyList()
  ) : SearchUiState

  data class Loading(val query: String) : SearchUiState

  data class Success(
    val query: String,
    val results: List<MediaItem>,
    val platform: OttPlatform = OttPlatform.ALL
  ) : SearchUiState

  data class Empty(
    val query: String,
    val suggestedTerms: List<String> = listOf("Action", "Sci-Fi", "Comedy", "Thriller", "Netflix", "Prime Video")
  ) : SearchUiState

  data class Error(
    val query: String,
    val message: String
  ) : SearchUiState
}

class SearchViewModel(
  private val repository: SearchRepository = MockSearchRepository(),
  private val defaultDebounceMillis: Long = 0L
) : ViewModel() {

  private val _query = MutableStateFlow("")
  val query: StateFlow<String> = _query.asStateFlow()

  private val _selectedPlatform = MutableStateFlow(OttPlatform.ALL)
  val selectedPlatform: StateFlow<OttPlatform> = _selectedPlatform.asStateFlow()

  private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Initial())
  val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

  private var searchJob: Job? = null

  init {
    loadInitialState()
  }

  fun loadInitialState() {
    viewModelScope.launch {
      val recents = repository.getRecentSearches()
      val suggestions = repository.getTrendingSuggestions()
      if (_query.value.isBlank()) {
        _uiState.value = SearchUiState.Initial(
          recentSearches = recents,
          suggestions = suggestions
        )
      }
    }
  }

  fun onQueryChanged(newQuery: String, debounceMillis: Long = defaultDebounceMillis) {
    _query.value = newQuery
    searchJob?.cancel()

    if (newQuery.isBlank()) {
      loadInitialState()
      return
    }

    searchJob = viewModelScope.launch {
      if (debounceMillis > 0) {
        delay(debounceMillis)
      }
      executeSearch(newQuery, _selectedPlatform.value)
    }
  }

  fun onPlatformFilterChanged(platform: OttPlatform) {
    _selectedPlatform.value = platform
    val currentQuery = _query.value
    if (currentQuery.isNotBlank()) {
      searchJob?.cancel()
      searchJob = viewModelScope.launch {
        executeSearch(currentQuery, platform)
      }
    }
  }

  fun clearQuery() {
    _query.value = ""
    searchJob?.cancel()
    loadInitialState()
  }

  fun selectRecentSearch(term: String) {
    _query.value = term
    searchJob?.cancel()
    searchJob = viewModelScope.launch {
      executeSearch(term, _selectedPlatform.value)
    }
  }

  fun clearRecentSearches() {
    viewModelScope.launch {
      repository.clearRecentSearches()
      if (_query.value.isBlank()) {
        val suggestions = repository.getTrendingSuggestions()
        _uiState.value = SearchUiState.Initial(
          recentSearches = emptyList(),
          suggestions = suggestions
        )
      }
    }
  }

  fun retry() {
    val currentQuery = _query.value
    if (currentQuery.isNotBlank()) {
      searchJob?.cancel()
      searchJob = viewModelScope.launch {
        executeSearch(currentQuery, _selectedPlatform.value)
      }
    } else {
      loadInitialState()
    }
  }

  private suspend fun executeSearch(queryStr: String, platform: OttPlatform) {
    _uiState.value = SearchUiState.Loading(query = queryStr)
    try {
      val results = repository.search(queryStr, platform)
      if (results.isEmpty()) {
        _uiState.value = SearchUiState.Empty(query = queryStr)
      } else {
        _uiState.value = SearchUiState.Success(
          query = queryStr,
          results = results,
          platform = platform
        )
      }
    } catch (e: Exception) {
      _uiState.value = SearchUiState.Error(
        query = queryStr,
        message = e.localizedMessage ?: "Failed to perform search"
      )
    }
  }

  companion object {
    fun provideFactory(
      repository: SearchRepository,
      defaultDebounceMillis: Long = 0L
    ): ViewModelProvider.Factory =
      object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          return SearchViewModel(repository, defaultDebounceMillis) as T
        }
      }
  }
}
