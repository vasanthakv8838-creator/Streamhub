package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.data.repository.GlobalSearchRepository
import com.example.data.repository.MockSearchRepository
import com.example.data.repository.SearchRepository
import com.example.model.MediaItem
import com.example.model.OttPlatform
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.SearchUiState
import com.example.viewmodel.SearchViewModel

/**
 * Stateful Search Screen component integrated with [SearchRepository] / [GlobalSearchRepository].
 */
@Composable
fun SearchScreen(
  modifier: Modifier = Modifier,
  repository: SearchRepository = remember { GlobalSearchRepository() },
  viewModel: SearchViewModel = viewModel(factory = SearchViewModel.provideFactory(repository)),
  onItemClick: (MediaItem) -> Unit = {},
  onWatchlistToggle: (MediaItem) -> Unit = {},
  watchlistIds: List<String> = emptyList(),
  onBack: (() -> Unit)? = null,
  isVip: Boolean = false
) {
  val query by viewModel.query.collectAsState()
  val selectedPlatform by viewModel.selectedPlatform.collectAsState()
  val uiState by viewModel.uiState.collectAsState()

  SearchScreenContent(
    query = query,
    onQueryChange = { viewModel.onQueryChanged(it) },
    selectedPlatform = selectedPlatform,
    onPlatformSelect = { viewModel.onPlatformFilterChanged(it) },
    uiState = uiState,
    onClearQuery = { viewModel.clearQuery() },
    onSelectRecentSearch = { viewModel.selectRecentSearch(it) },
    onClearRecentSearches = { viewModel.clearRecentSearches() },
    onRetry = { viewModel.retry() },
    onItemClick = onItemClick,
    onWatchlistToggle = onWatchlistToggle,
    watchlistIds = watchlistIds,
    onBack = onBack,
    isVip = isVip,
    modifier = modifier
  )
}

/**
 * Stateless Search Screen Component for direct rendering and UI tests.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreenContent(
  query: String,
  onQueryChange: (String) -> Unit,
  selectedPlatform: OttPlatform,
  onPlatformSelect: (OttPlatform) -> Unit,
  uiState: SearchUiState,
  onClearQuery: () -> Unit,
  onSelectRecentSearch: (String) -> Unit,
  onClearRecentSearches: () -> Unit,
  onRetry: () -> Unit,
  onItemClick: (MediaItem) -> Unit,
  onWatchlistToggle: (MediaItem) -> Unit,
  watchlistIds: List<String>,
  onBack: (() -> Unit)?,
  isVip: Boolean,
  modifier: Modifier = Modifier
) {
  val focusManager = LocalFocusManager.current

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundDark)
  ) {
    // 1. Search Header Bar with Input Field
    Surface(
      color = SurfaceDark,
      tonalElevation = 4.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          if (onBack != null) {
            IconButton(
              onClick = onBack,
              modifier = Modifier
                .padding(end = 6.dp)
                .testTag("search_back_button")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = TextPrimary
              )
            }
          }

          // Text Input Field for Searching across all 7 OTT platforms
          OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
              .weight(1f)
              .height(52.dp)
              .testTag("search_text_input"),
            placeholder = {
              Text(
                text = "Search 7 OTTs, titles, cast, genres...",
                color = TextMuted,
                fontSize = 13.sp
              )
            },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon",
                tint = if (query.isNotBlank()) AccentGold else TextSecondary,
                modifier = Modifier.size(20.dp)
              )
            },
            trailingIcon = {
              if (query.isNotBlank()) {
                IconButton(
                  onClick = onClearQuery,
                  modifier = Modifier
                    .size(24.dp)
                    .testTag("search_clear_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear search",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = SurfaceElevated,
              unfocusedContainerColor = SurfaceElevated,
              focusedBorderColor = AccentGold,
              unfocusedBorderColor = BorderSubtle,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            )
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Platform Filter Chips Row
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          contentPadding = PaddingValues(horizontal = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          item {
            PlatformFilterPill(
              title = "All Apps (7)",
              isSelected = selectedPlatform == OttPlatform.ALL,
              brandColor = AccentGold,
              onClick = { onPlatformSelect(OttPlatform.ALL) },
              modifier = Modifier.testTag("filter_chip_ALL")
            )
          }

          items(
            listOf(
              OttPlatform.NETFLIX,
              OttPlatform.PRIME_VIDEO,
              OttPlatform.JIO_HOTSTAR,
              OttPlatform.APPLE_TV,
              OttPlatform.SONY_LIV,
              OttPlatform.ZEE5,
              OttPlatform.YOUTUBE
            )
          ) { platform ->
            PlatformFilterPill(
              title = platform.shortTag,
              isSelected = selectedPlatform == platform,
              brandColor = platform.brandColor,
              onClick = { onPlatformSelect(platform) },
              modifier = Modifier.testTag("filter_chip_${platform.name}")
            )
          }
        }
      }
    }

    // 3. Dynamic Results List & State Views
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
    ) {
      when (uiState) {
        is SearchUiState.Loading -> {
          SearchLoadingView(modifier = Modifier.align(Alignment.Center))
        }

        is SearchUiState.Empty -> {
          SearchEmptyView(
            query = uiState.query,
            suggestedTerms = uiState.suggestedTerms,
            onSuggestionClick = onQueryChange,
            modifier = Modifier.align(Alignment.Center)
          )
        }

        is SearchUiState.Error -> {
          SearchErrorView(
            message = uiState.message,
            onRetry = onRetry,
            modifier = Modifier.align(Alignment.Center)
          )
        }

        is SearchUiState.Success -> {
          SearchResultsListView(
            query = uiState.query,
            results = uiState.results,
            selectedPlatform = selectedPlatform,
            watchlistIds = watchlistIds,
            onItemClick = onItemClick,
            onWatchlistToggle = onWatchlistToggle,
            isVip = isVip
          )
        }

        is SearchUiState.Initial -> {
          SearchInitialView(
            recentSearches = uiState.recentSearches,
            trendingSuggestions = uiState.suggestions,
            onSelectTerm = onSelectRecentSearch,
            onClearRecents = onClearRecentSearches,
            onItemClick = onItemClick,
            watchlistIds = watchlistIds,
            onWatchlistToggle = onWatchlistToggle,
            isVip = isVip
          )
        }
      }
    }
  }
}

/**
 * Filter Chip Pill for platform filtering.
 */
@Composable
private fun PlatformFilterPill(
  title: String,
  isSelected: Boolean,
  brandColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(20.dp))
      .background(if (isSelected) brandColor else SurfaceElevated)
      .border(
        width = 1.dp,
        color = if (isSelected) brandColor else BorderSubtle,
        shape = RoundedCornerShape(20.dp)
      )
      .clickable(onClick = onClick)
      .padding(horizontal = 12.dp, vertical = 6.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = title,
      fontSize = 12.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) Color.White else TextSecondary
    )
  }
}

/**
 * Result list rendering the matching items.
 */
@Composable
private fun SearchResultsListView(
  query: String,
  results: List<MediaItem>,
  selectedPlatform: OttPlatform,
  watchlistIds: List<String>,
  onItemClick: (MediaItem) -> Unit,
  onWatchlistToggle: (MediaItem) -> Unit,
  isVip: Boolean,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("search_results_list"),
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Found ${results.size} matches for \"$query\"",
          color = TextPrimary,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )

        if (selectedPlatform != OttPlatform.ALL) {
          Text(
            text = "Filtered by ${selectedPlatform.displayName}",
            color = selectedPlatform.brandColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }

    items(results, key = { it.id }) { item ->
      SearchResultCard(
        mediaItem = item,
        isSaved = watchlistIds.contains(item.id),
        onItemClick = { onItemClick(item) },
        onWatchlistToggle = { onWatchlistToggle(item) },
        isVip = isVip,
        modifier = Modifier.testTag("search_result_item_${item.id}")
      )
    }
  }
}

/**
 * Polished Search Result Item Card.
 */
@Composable
fun SearchResultCard(
  mediaItem: MediaItem,
  isSaved: Boolean,
  onItemClick: () -> Unit,
  onWatchlistToggle: () -> Unit,
  isVip: Boolean,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(14.dp),
    color = SurfaceElevated,
    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onItemClick)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
    ) {
      // Poster Image
      Box(
        modifier = Modifier
          .size(width = 85.dp, height = 120.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(SurfaceDark)
      ) {
        AsyncImage(
          model = mediaItem.posterUrl,
          contentDescription = mediaItem.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )

        // Platform Brand Tag overlay
        Box(
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(mediaItem.platform.brandColor)
            .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
          Text(
            text = mediaItem.platform.shortTag,
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Details Column
      Column(
        modifier = Modifier
          .weight(1f)
          .align(Alignment.CenterVertically)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Top
        ) {
          Text(
            text = mediaItem.title,
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
          )

          IconButton(
            onClick = onWatchlistToggle,
            modifier = Modifier
              .size(24.dp)
              .testTag("search_item_watchlist_${mediaItem.id}")
          ) {
            Icon(
              imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = "Save to watchlist",
              tint = if (isSaved) AccentGold else TextMuted,
              modifier = Modifier.size(20.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(3.dp))

        // Metadata row (Rating, Year, Duration, VIP)
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          // IMDb rating badge
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(Color(0xFF26210A))
              .padding(horizontal = 4.dp, vertical = 1.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = "Rating",
              tint = AccentGold,
              modifier = Modifier.size(11.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = "${mediaItem.rating}",
              color = AccentGold,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Text(
            text = "${mediaItem.year} • ${mediaItem.duration}",
            color = TextSecondary,
            fontSize = 11.sp
          )

          if (isVip) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(AccentGold)
                .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
              Text(
                text = "👑 VIP",
                color = Color.Black,
                fontSize = 8.sp,
                fontWeight = FontWeight.ExtraBold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Genre
        Text(
          text = mediaItem.genre,
          color = AccentCyan,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Synopsis preview
        Text(
          text = mediaItem.synopsis,
          color = TextSecondary,
          fontSize = 12.sp,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          lineHeight = 16.sp
        )
      }
    }
  }
}

/**
 * Initial view with recent searches and trending recommendations.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchInitialView(
  recentSearches: List<String>,
  trendingSuggestions: List<MediaItem>,
  onSelectTerm: (String) -> Unit,
  onClearRecents: () -> Unit,
  onItemClick: (MediaItem) -> Unit,
  watchlistIds: List<String>,
  onWatchlistToggle: (MediaItem) -> Unit,
  isVip: Boolean,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // Recent Searches Section
    if (recentSearches.isNotEmpty()) {
      item {
        Column(modifier = Modifier.testTag("recent_searches_section")) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = AccentGold,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Recent Searches",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Text(
              text = "Clear All",
              color = TextMuted,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier
                .clickable(onClick = onClearRecents)
                .testTag("clear_recent_searches_button")
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            recentSearches.forEach { term ->
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(16.dp))
                  .background(SurfaceElevated)
                  .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                  .clickable { onSelectTerm(term) }
                  .padding(horizontal = 12.dp, vertical = 6.dp)
                  .testTag("recent_chip_$term")
              ) {
                Text(
                  text = term,
                  color = TextSecondary,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }
        }
      }
    }

    // Trending & Suggested Titles
    item {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
            contentDescription = null,
            tint = AccentCyan,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Trending Now Across 7 OTTs",
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    items(trendingSuggestions, key = { "suggested_${it.id}" }) { item ->
      SearchResultCard(
        mediaItem = item,
        isSaved = watchlistIds.contains(item.id),
        onItemClick = { onItemClick(item) },
        onWatchlistToggle = { onWatchlistToggle(item) },
        isVip = isVip,
        modifier = Modifier.testTag("suggested_item_${item.id}")
      )
    }
  }
}

/**
 * Loading state view.
 */
@Composable
private fun SearchLoadingView(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier
      .padding(32.dp)
      .testTag("search_loading_indicator"),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    CircularProgressIndicator(
      color = AccentGold,
      modifier = Modifier.size(44.dp),
      strokeWidth = 3.dp
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = "Searching across 7 OTT networks...",
      color = TextSecondary,
      fontSize = 13.sp,
      fontWeight = FontWeight.Medium
    )
  }
}

/**
 * Empty state view with suggestions.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchEmptyView(
  query: String,
  suggestedTerms: List<String>,
  onSuggestionClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .padding(32.dp)
      .testTag("search_empty_state"),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .size(64.dp)
        .clip(CircleShape)
        .background(SurfaceElevated),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.SearchOff,
        contentDescription = null,
        tint = AccentGold,
        modifier = Modifier.size(32.dp)
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "No results found for \"$query\"",
      color = TextPrimary,
      fontSize = 16.sp,
      fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = "Try searching by titles, actor names, or select a genre tag below:",
      color = TextMuted,
      fontSize = 12.sp,
      lineHeight = 16.sp,
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )

    Spacer(modifier = Modifier.height(16.dp))

    FlowRow(
      horizontalArrangement = Arrangement.Center,
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      suggestedTerms.forEach { tag ->
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceElevated)
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .clickable { onSuggestionClick(tag) }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("empty_suggestion_$tag")
        ) {
          Text(
            text = tag,
            color = AccentCyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }
  }
}

/**
 * Error state view with retry action.
 */
@Composable
private fun SearchErrorView(
  message: String,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.padding(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "Unable to complete search",
      color = Color(0xFFEF4444),
      fontSize = 16.sp,
      fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = message,
      color = TextMuted,
      fontSize = 12.sp,
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
      onClick = onRetry,
      colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
      shape = RoundedCornerShape(10.dp),
      modifier = Modifier.testTag("search_retry_button")
    ) {
      Icon(
        imageVector = Icons.Default.Refresh,
        contentDescription = null,
        tint = Color.Black,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = "Retry",
        color = Color.Black,
        fontWeight = FontWeight.Bold
      )
    }
  }
}
