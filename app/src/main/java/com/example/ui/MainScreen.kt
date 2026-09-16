package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import coil.compose.AsyncImage
import com.example.data.LiveTvCatalog
import com.example.data.RegionalCatalog
import com.example.model.CuratedTrailer
import com.example.model.LiveChannel
import com.example.model.MediaItem
import com.example.model.OttPlatform
import com.example.model.StreamProfile
import com.example.model.UserProfile
import com.example.model.VpnConnectionState
import com.example.model.VpnStatus
import com.example.ui.auth.LoginScreen
import com.example.ui.auth.ProfileSelectionScreen
import com.example.ui.components.FeaturedHeroBanner
import com.example.ui.components.LiveTvView
import com.example.ui.components.MediaCard
import com.example.ui.components.MediaDetailBottomSheet
import com.example.ui.components.MediaTypeChipsRow
import com.example.ui.components.OttHeader
import com.example.ui.components.PlatformPlansSheet
import com.example.ui.components.PlatformSelectorRow
import com.example.ui.components.SearchScreen
import com.example.ui.components.SurfsharkVpnView
import com.example.ui.components.UserProfileSheet
import com.example.ui.components.VideoAnalyzerView
import com.example.ui.components.VoiceAssistantView
import com.example.ui.components.WatchlistView
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.viewmodel.OttViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  viewModel: OttViewModel = viewModel()
) {
  val context = LocalContext.current
  val isLoggedIn by viewModel.isLoggedIn.collectAsState()
  val currentUser by viewModel.currentUser.collectAsState()
  val isProfileSheetVisible by viewModel.isProfileSheetVisible.collectAsState()

  // If user is not yet logged in, show the Name -> Phone -> OTP flow
  if (!isLoggedIn) {
    LoginScreen(
      onLoginSuccess = { name, phone ->
        viewModel.completeLogin(name, phone)
      }
    )
    return
  }

  val profiles by viewModel.profiles.collectAsState()
  val activeProfile by viewModel.activeProfile.collectAsState()
  val isProfileSelectionVisible by viewModel.isProfileSelectionVisible.collectAsState()

  // Profile Selection / "Who's Watching?" Screen (shown right after verification or on profile switch)
  if (isProfileSelectionVisible) {
    ProfileSelectionScreen(
      profiles = profiles,
      activeProfile = activeProfile,
      onSelectProfile = { profile ->
        viewModel.selectProfile(profile)
      },
      onAddProfile = { name, isKids, colorHex ->
        viewModel.addProfile(name, isKids, colorHex)
      },
      onDeleteProfile = { profile ->
        viewModel.deleteProfile(profile)
      },
      onSignOut = {
        viewModel.logout()
      }
    )
    return
  }

  val selectedTab by viewModel.selectedTab.collectAsState()
  val selectedPlatform by viewModel.selectedPlatform.collectAsState()
  val selectedMediaType by viewModel.selectedMediaType.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val filteredItems by viewModel.filteredItems.collectAsState()
  val watchlistItems by viewModel.watchlistItems.collectAsState()
  val watchlistIds by viewModel.watchlistIds.collectAsState()
  val selectedMedia by viewModel.selectedMedia.collectAsState()

  // Voice AI states
  val voiceMessages by viewModel.voiceMessages.collectAsState()
  val isVoiceLoading by viewModel.isVoiceLoading.collectAsState()
  val isSpeaking by viewModel.isSpeaking.collectAsState()

  // Video Analyzer states
  val selectedTrailer by viewModel.selectedTrailer.collectAsState()
  val availableTrailers by viewModel.availableTrailers.collectAsState()
  val customVideoUrl by viewModel.customVideoUrl.collectAsState()
  val customVideoTitle by viewModel.customVideoTitle.collectAsState()
  val videoAnalysisResult by viewModel.videoAnalysisResult.collectAsState()
  val isVideoAnalyzing by viewModel.isVideoAnalyzing.collectAsState()

  // Surfshark VPN & Live TV states
  val vpnState by viewModel.vpnState.collectAsState()
  val availableVpnServers = viewModel.availableVpnServers
  val liveChannels by viewModel.liveChannels.collectAsState()
  val selectedLiveCategory by viewModel.selectedLiveCategory.collectAsState()
  val liveSearchQuery by viewModel.liveSearchQuery.collectAsState()

  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val profileSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val plansSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val isPlansSheetVisible by viewModel.isPlansSheetVisible.collectAsState()
  val isVip by viewModel.isVip.collectAsState()

  Scaffold(
    containerColor = BackgroundDark,
    bottomBar = {
      NavigationBar(
        containerColor = SurfaceDark,
        tonalElevation = 8.dp,
        modifier = Modifier
          .windowInsetsPadding(WindowInsets.navigationBars)
          .testTag("bottom_nav_bar")
      ) {
        NavigationBarItem(
          selected = selectedTab == 0,
          onClick = { viewModel.selectTab(0) },
          icon = {
            Icon(
              imageVector = if (selectedTab == 0) Icons.Filled.Explore else Icons.Outlined.Explore,
              contentDescription = "Explore"
            )
          },
          label = { Text("Stream", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = AccentGold,
            selectedTextColor = AccentGold,
            indicatorColor = SurfaceElevated,
            unselectedIconColor = Color(0xFFA0A7B8),
            unselectedTextColor = Color(0xFFA0A7B8)
          ),
          modifier = Modifier.testTag("tab_explore")
        )

        NavigationBarItem(
          selected = selectedTab == 1,
          onClick = { viewModel.selectTab(1) },
          icon = {
            Icon(
              imageVector = Icons.Filled.Tv,
              contentDescription = "Live TV"
            )
          },
          label = { Text("Live TV", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFFE50914),
            selectedTextColor = Color(0xFFE50914),
            indicatorColor = SurfaceElevated,
            unselectedIconColor = Color(0xFFA0A7B8),
            unselectedTextColor = Color(0xFFA0A7B8)
          ),
          modifier = Modifier.testTag("tab_live_tv")
        )

        NavigationBarItem(
          selected = selectedTab == 2,
          onClick = { viewModel.selectTab(2) },
          icon = {
            Icon(
              imageVector = Icons.Filled.Security,
              contentDescription = "Surfshark VPN"
            )
          },
          label = {
            Text(
              if (vpnState.status == VpnStatus.CONNECTED) "${vpnState.server.flagEmoji} VPN" else "VPN",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFF00D1B2),
            selectedTextColor = Color(0xFF00D1B2),
            indicatorColor = SurfaceElevated,
            unselectedIconColor = if (vpnState.status == VpnStatus.CONNECTED) Color(0xFF00D1B2) else Color(0xFFA0A7B8),
            unselectedTextColor = if (vpnState.status == VpnStatus.CONNECTED) Color(0xFF00D1B2) else Color(0xFFA0A7B8)
          ),
          modifier = Modifier.testTag("tab_surfshark_vpn")
        )

        NavigationBarItem(
          selected = selectedTab == 3,
          onClick = { viewModel.selectTab(3) },
          icon = {
            Icon(
              imageVector = if (selectedTab == 3) Icons.Filled.Search else Icons.Outlined.Search,
              contentDescription = "Search"
            )
          },
          label = { Text("Search", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = AccentGold,
            selectedTextColor = AccentGold,
            indicatorColor = SurfaceElevated,
            unselectedIconColor = Color(0xFFA0A7B8),
            unselectedTextColor = Color(0xFFA0A7B8)
          ),
          modifier = Modifier.testTag("tab_search")
        )

        NavigationBarItem(
          selected = selectedTab == 4,
          onClick = { viewModel.selectTab(4) },
          icon = {
            Icon(
              imageVector = if (selectedTab == 4) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
              contentDescription = "Watchlist"
            )
          },
          label = { Text("Watchlist", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = AccentGold,
            selectedTextColor = AccentGold,
            indicatorColor = SurfaceElevated,
            unselectedIconColor = Color(0xFFA0A7B8),
            unselectedTextColor = Color(0xFFA0A7B8)
          ),
          modifier = Modifier.testTag("tab_watchlist")
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (selectedTab) {
        0 -> {
          // Unified Explore Feed across YouTube, Hotstar, Apple TV+, Netflix, ZEE5, Sony LIV, Prime Video
          ExploreFeedView(
            searchQuery = searchQuery,
            onSearchQueryChanged = { viewModel.setSearchQuery(it) },
            selectedPlatform = selectedPlatform,
            onPlatformSelected = { viewModel.setPlatform(it) },
            selectedMediaType = selectedMediaType,
            onMediaTypeSelected = { viewModel.setMediaType(it) },
            items = filteredItems,
            watchlistIds = watchlistIds,
            onItemClick = { viewModel.openMediaDetails(it) },
            onWatchlistToggle = { viewModel.toggleWatchlist(it) },
            onVoiceShortcut = { viewModel.selectTab(5) },
            onVideoAnalyzerShortcut = { viewModel.selectTab(6) },
            onVpnShortcut = { viewModel.selectTab(2) },
            onLiveTvShortcut = { viewModel.selectTab(1) },
            onWatchDirect = { item ->
              launchUrl(context, item.watchUrl)
            },
            userProfile = currentUser,
            activeProfile = activeProfile,
            vpnState = vpnState,
            onProfileClick = { viewModel.showProfileSheet() },
            onPlansClick = { viewModel.showPlansSheet() },
            onChannelClick = { viewModel.openCountryLiveChannel(it) },
            onVideoClick = { viewModel.openTrailer(it) }
          )
        }
        1 -> {
          // Dedicated Live TV section with 24/7 channels across News, Sports, Entertainment, Movies & EPG
          LiveTvView(
            channels = liveChannels,
            selectedCategory = selectedLiveCategory,
            onSelectCategory = { viewModel.selectLiveCategory(it) },
            searchQuery = liveSearchQuery,
            onSearchQueryChanged = { viewModel.setLiveSearchQuery(it) },
            vpnState = vpnState,
            onOpenVpnSettings = { viewModel.selectTab(2) }
          )
        }
        2 -> {
          // Built-in Surfshark VPN & Location Manager
          SurfsharkVpnView(
            vpnState = vpnState,
            availableServers = availableVpnServers,
            onConnect = { viewModel.connectVpn() },
            onDisconnect = { viewModel.disconnectVpn() },
            onSelectServer = { viewModel.switchVpnServer(it) },
            onToggleCleanWeb = { viewModel.toggleCleanWeb() },
            onToggleKillSwitch = { viewModel.toggleKillSwitch() },
            onMediaClick = { viewModel.openMediaDetails(it) },
            onChannelClick = { viewModel.openCountryLiveChannel(it) },
            onVideoClick = { viewModel.openTrailer(it) }
          )
        }
        3 -> {
          // Dedicated Search Screen component integrated with MockSearchRepository
          SearchScreen(
            onItemClick = { viewModel.openMediaDetails(it) },
            onWatchlistToggle = { viewModel.toggleWatchlist(it) },
            watchlistIds = watchlistIds,
            isVip = currentUser?.isVip == true
          )
        }
        4 -> {
          // Cross-platform Room Database Watchlist
          WatchlistView(
            watchlist = watchlistItems,
            onToggleWatched = { viewModel.toggleWatchlistWatched(it) },
            onDelete = { viewModel.deleteFromWatchlist(it) },
            onExploreClick = { viewModel.selectTab(0) }
          )
        }
        5 -> {
          // Live Conversational Voice Assistant (gemini-3.1-flash-live-preview)
          VoiceAssistantView(
            messages = voiceMessages,
            isLoading = isVoiceLoading,
            isSpeaking = isSpeaking,
            onSendPrompt = { viewModel.sendVoicePrompt(it) },
            onSpeakText = { viewModel.speakText(it) },
            onStopSpeaking = { viewModel.stopSpeaking() }
          )
        }
        6 -> {
          // Video Content Analyzer (gemini-3.1-pro-preview)
          VideoAnalyzerView(
            selectedTrailer = selectedTrailer,
            customVideoUrl = customVideoUrl,
            customVideoTitle = customVideoTitle,
            analysisResult = videoAnalysisResult,
            isAnalyzing = isVideoAnalyzing,
            onSelectTrailer = { viewModel.selectTrailer(it) },
            onSetCustomUrl = { viewModel.setCustomVideoUrl(it) },
            onSetCustomTitle = { viewModel.setCustomVideoTitle(it) },
            onAnalyze = { prompt -> viewModel.analyzeCurrentVideo(prompt) },
            availableTrailers = availableTrailers
          )
        }
      }

      // Media Detail Modal Bottom Sheet
      selectedMedia?.let { item ->
        MediaDetailBottomSheet(
          mediaItem = item,
          isSavedInWatchlist = watchlistIds.contains(item.id),
          sheetState = sheetState,
          onDismiss = { viewModel.closeMediaDetails() },
          onWatchlistToggle = { viewModel.toggleWatchlist(item) },
          onAnalyzeWithGemini = { trailer ->
            viewModel.selectTrailer(trailer)
            viewModel.selectTab(4)
            viewModel.analyzeCurrentVideo()
          },
          isVip = currentUser?.isVip == true
        )
      }

      // User Profile & Account Modal Bottom Sheet
      if (isProfileSheetVisible) {
        UserProfileSheet(
          user = currentUser,
          profiles = profiles,
          activeProfile = activeProfile,
          sheetState = profileSheetState,
          onDismiss = { viewModel.hideProfileSheet() },
          onSelectProfile = { profile ->
            viewModel.selectProfile(profile)
          },
          onAddProfile = { name, isKids, colorHex ->
            viewModel.addProfile(name, isKids, colorHex)
          },
          onOpenWhoIsWatching = {
            viewModel.openProfileSelection()
          },
          onOpenPlans = {
            viewModel.showPlansSheet()
          },
          onLogout = { viewModel.logout() }
        )
      }

      // Platform Subscription Plans Modal Bottom Sheet
      if (isPlansSheetVisible) {
        PlatformPlansSheet(
          sheetState = plansSheetState,
          onDismiss = { viewModel.hidePlansSheet() },
          initialPlatform = selectedPlatform,
          isVip = isVip,
          onActivateVip = { viewModel.activateVipTier() }
        )
      }
    }
  }
}

@Composable
fun ExploreFeedView(
  searchQuery: String,
  onSearchQueryChanged: (String) -> Unit,
  selectedPlatform: OttPlatform,
  onPlatformSelected: (OttPlatform) -> Unit,
  selectedMediaType: com.example.model.MediaType,
  onMediaTypeSelected: (com.example.model.MediaType) -> Unit,
  items: List<MediaItem>,
  watchlistIds: List<String>,
  onItemClick: (MediaItem) -> Unit,
  onWatchlistToggle: (MediaItem) -> Unit,
  onVoiceShortcut: () -> Unit,
  onVideoAnalyzerShortcut: () -> Unit,
  onVpnShortcut: () -> Unit = {},
  onLiveTvShortcut: () -> Unit = {},
  onWatchDirect: (MediaItem) -> Unit,
  userProfile: UserProfile? = null,
  activeProfile: StreamProfile? = null,
  vpnState: VpnConnectionState? = null,
  onProfileClick: () -> Unit = {},
  onPlansClick: () -> Unit = {},
  onChannelClick: (LiveChannel) -> Unit = {},
  onVideoClick: (CuratedTrailer) -> Unit = {}
) {
  val heroItem = items.firstOrNull { it.isFeaturedHero } ?: items.firstOrNull()
  val trendingList = items.filter { it.isTrending }
  val allPlatformList = items

  val isVip = userProfile?.isVip == true

  val connectedCountryCode = if (vpnState?.status == VpnStatus.CONNECTED) vpnState.server.countryCode else null
  val countryLiveChannels = remember(connectedCountryCode) {
    if (connectedCountryCode != null) LiveTvCatalog.getChannelsForRegion(connectedCountryCode) else emptyList()
  }
  val countryVideos = remember(connectedCountryCode) {
    if (connectedCountryCode != null) RegionalCatalog.getVideosForRegion(connectedCountryCode) else emptyList()
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 24.dp)
  ) {
    // Header with search & shortcuts
    item {
      OttHeader(
        searchQuery = searchQuery,
        onSearchQueryChanged = onSearchQueryChanged,
        onVoiceClick = onVoiceShortcut,
        onVideoAnalyzerClick = onVideoAnalyzerShortcut,
        userProfile = userProfile,
        activeProfile = activeProfile,
        vpnState = vpnState,
        onProfileClick = onProfileClick,
        onPlansClick = onPlansClick,
        onVpnClick = onVpnShortcut
      )
    }

    // Platform Pills (YouTube, Hotstar, Apple TV+, Netflix, ZEE5, Sony LIV, Prime Video)
    item {
      PlatformSelectorRow(
        selectedPlatform = selectedPlatform,
        onPlatformSelected = onPlatformSelected
      )
    }

    // Media Type filter (Movies, Series, Docuseries, Live Sports)
    item {
      MediaTypeChipsRow(
        selectedType = selectedMediaType,
        onTypeSelected = onMediaTypeSelected
      )
    }

    // Surfshark VPN Regional Connection Banner
    item {
      val isVpnConnected = vpnState?.status == VpnStatus.CONNECTED
      androidx.compose.material3.Surface(
        onClick = onVpnShortcut,
        shape = RoundedCornerShape(14.dp),
        color = if (isVpnConnected) Color(0xFF172836) else SurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          if (isVpnConnected) Color(0xFF00D1B2) else BorderSubtle
        ),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp)
          .testTag("explore_vpn_banner")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Text(
              text = if (isVpnConnected && vpnState != null) vpnState.server.flagEmoji else "🛡️",
              fontSize = 22.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = if (isVpnConnected && vpnState != null) {
                    "Surfshark: ${vpnState.server.countryName} (${vpnState.server.city})"
                  } else {
                    "Surfshark VPN: Global Catalog"
                  },
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isVpnConnected) Color(0xFF00D1B2) else Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                      if (isVpnConnected) Color(0xFF0A4F48) else SurfaceDark
                    )
                    .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                  Text(
                    text = if (isVpnConnected) "CONNECTED" else "DISCONNECTED",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isVpnConnected) Color(0xFF00D1B2) else Color(0xFFA0A7B8)
                  )
                }
              }
              Text(
                text = if (isVpnConnected && vpnState != null) {
                  "Streaming ${vpnState.server.catalogDescription} • Tap to change location"
                } else {
                  "Change location to unlock region-exclusive movies, shows & Live TV"
                },
                fontSize = 10.sp,
                color = Color(0xFFA0A7B8),
                maxLines = 1
              )
            }
          }

          Text(
            text = if (isVpnConnected) "CHANGE" else "CONNECT",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF00D1B2)
          )
        }
      }
    }

    // Dedicated Live TV Carousel Shortcut Banner
    if (searchQuery.isBlank() && selectedPlatform == OttPlatform.ALL) {
      item {
        androidx.compose.material3.Surface(
          onClick = onLiveTvShortcut,
          shape = RoundedCornerShape(14.dp),
          color = Color(0xFF221115),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE50914).copy(alpha = 0.5f)),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("explore_live_tv_banner")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFFE50914)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Tv,
                  contentDescription = "Live TV",
                  tint = Color.White,
                  modifier = Modifier.size(18.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "Live TV & 24/7 Channels",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(4.dp))
                      .background(Color(0xFFE50914))
                      .padding(horizontal = 4.dp, vertical = 1.dp)
                  ) {
                    Text(
                      text = "● ON AIR",
                      fontSize = 8.sp,
                      fontWeight = FontWeight.Black,
                      color = Color.White
                    )
                  }
                }
                Text(
                  text = "Watch Sky News, Red Bull Sports, Bloomberg, NASA TV & regional channels",
                  fontSize = 10.sp,
                  color = Color(0xFFA0A7B8),
                  maxLines = 1
                )
              }
            }
            Text(
              text = "WATCH",
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color(0xFFE50914)
            )
          }
        }
      }
    }

    // Regional VPN Unlocked Exclusives Shelf (when connected)
    val unlockedExclusives = items.filter { it.vpnRequired }
    if (unlockedExclusives.isNotEmpty() && searchQuery.isBlank()) {
      item {
        Column(modifier = Modifier.padding(top = 10.dp)) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "${vpnState?.server?.flagEmoji ?: "🌐"} ${vpnState?.server?.countryName ?: "Regional"} Exclusives",
                color = Color(0xFF00D1B2),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(Color(0xFF0A4F48))
                  .padding(horizontal = 5.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "SURFSHARK UNLOCKED",
                  fontSize = 8.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color(0xFF00D1B2)
                )
              }
            }
            Text(
              text = "${unlockedExclusives.size} titles",
              color = Color(0xFFA0A7B8),
              fontSize = 11.sp
            )
          }

          LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            items(unlockedExclusives, key = { it.id }) { media ->
              MediaCard(
                mediaItem = media,
                isSavedInWatchlist = watchlistIds.contains(media.id),
                onWatchlistToggle = { onWatchlistToggle(media) },
                onClick = { onItemClick(media) },
                isVip = isVip
              )
            }
          }
        }
      }
    }

    // Regional Live TV Channels Shelf (when VPN connected)
    if (vpnState?.status == VpnStatus.CONNECTED && searchQuery.isBlank()) {
      if (countryLiveChannels.isNotEmpty()) {
        item {
          Column(modifier = Modifier.padding(top = 10.dp)) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "${vpnState.server.flagEmoji} ${vpnState.server.countryName} Live TV",
                  color = Color.White,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFE50914))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "● ON AIR",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                  )
                }
              }
              Text(
                text = "${countryLiveChannels.size} channels",
                color = Color(0xFFA0A7B8),
                fontSize = 11.sp
              )
            }

            LazyRow(
              contentPadding = PaddingValues(horizontal = 16.dp),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              items(countryLiveChannels, key = { it.id }) { channel ->
                androidx.compose.material3.Card(
                  modifier = Modifier
                    .width(160.dp)
                    .clickable { onChannelClick(channel) }
                    .testTag("explore_live_channel_${channel.id}"),
                  shape = RoundedCornerShape(12.dp),
                  colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = SurfaceDark),
                  border = BorderStroke(1.dp, BorderSubtle)
                ) {
                  Column {
                    Box(modifier = Modifier.height(90.dp).fillMaxWidth()) {
                      AsyncImage(
                        model = channel.logoUrl,
                        contentDescription = channel.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                      )
                      Box(
                        modifier = Modifier
                          .align(Alignment.TopStart)
                          .padding(4.dp)
                          .clip(RoundedCornerShape(4.dp))
                          .background(Color(0xFFE50914))
                          .padding(horizontal = 4.dp, vertical = 2.dp)
                      ) {
                        Text(
                          text = "● LIVE",
                          fontSize = 8.sp,
                          color = Color.White,
                          fontWeight = FontWeight.Black
                        )
                      }
                      Box(
                        modifier = Modifier
                          .align(Alignment.BottomEnd)
                          .padding(4.dp)
                          .clip(RoundedCornerShape(4.dp))
                          .background(Color.Black.copy(alpha = 0.7f))
                          .padding(horizontal = 4.dp, vertical = 2.dp)
                      ) {
                        Text(
                          text = channel.resolution,
                          fontSize = 8.sp,
                          color = Color(0xFF00D1B2),
                          fontWeight = FontWeight.Bold
                        )
                      }
                    }
                    Column(modifier = Modifier.padding(8.dp)) {
                      Text(
                        text = channel.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1
                      )
                      Text(
                        text = channel.currentProgram.title,
                        fontSize = 10.sp,
                        color = Color(0xFFA0A7B8),
                        maxLines = 1
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }

      // Regional Videos & Highlights Shelf (when VPN connected)
      if (countryVideos.isNotEmpty()) {
        item {
          Column(modifier = Modifier.padding(top = 10.dp)) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "▶️ ${vpnState.server.countryName} Videos & Trailers",
                  color = Color.White,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF00D1B2).copy(alpha = 0.2f))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "AI READY",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00D1B2)
                  )
                }
              }
              Text(
                text = "${countryVideos.size} videos",
                color = Color(0xFFA0A7B8),
                fontSize = 11.sp
              )
            }

            LazyRow(
              contentPadding = PaddingValues(horizontal = 16.dp),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              items(countryVideos, key = { it.title }) { video ->
                androidx.compose.material3.Card(
                  modifier = Modifier
                    .width(180.dp)
                    .clickable { onVideoClick(video) }
                    .testTag("explore_video_${video.title.hashCode()}"),
                  shape = RoundedCornerShape(12.dp),
                  colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = SurfaceDark),
                  border = BorderStroke(1.dp, BorderSubtle)
                ) {
                  Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(4.dp))
                          .background(Color(0xFF00D1B2).copy(alpha = 0.2f))
                          .padding(horizontal = 5.dp, vertical = 2.dp)
                      ) {
                        Text(
                          text = video.platform.displayName,
                          fontSize = 8.sp,
                          color = Color(0xFF00D1B2),
                          fontWeight = FontWeight.Bold
                        )
                      }
                      Text(
                        text = video.duration,
                        fontSize = 9.sp,
                        color = Color(0xFFA0A7B8)
                      )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                      text = video.title,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color.White,
                      maxLines = 2,
                      lineHeight = 16.sp
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    // Featured Hero Banner (if not searching)
    if (searchQuery.isBlank() && heroItem != null) {
      item {
        FeaturedHeroBanner(
          mediaItem = heroItem,
          isSavedInWatchlist = watchlistIds.contains(heroItem.id),
          onWatchlistToggle = { onWatchlistToggle(heroItem) },
          onItemClick = { onItemClick(heroItem) },
          onWatchClick = { onWatchDirect(heroItem) }
        )
      }
    }

    // Section 1: Trending Across All OTTs (Horizontal Carousel)
    if (trendingList.isNotEmpty()) {
      item {
        Column(modifier = Modifier.padding(top = 14.dp)) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (selectedPlatform == OttPlatform.ALL) "🔥 Trending Across All Apps" else "🔥 Trending on ${selectedPlatform.displayName}",
              color = Color.White,
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "${trendingList.size} titles",
              color = AccentGold,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            items(trendingList, key = { it.id }) { media ->
              MediaCard(
                mediaItem = media,
                isSavedInWatchlist = watchlistIds.contains(media.id),
                onWatchlistToggle = { onWatchlistToggle(media) },
                onClick = { onItemClick(media) },
                isVip = isVip
              )
            }
          }
        }
      }
    }

    // Section 2: Dedicated Shelf for EACH of the 7 OTT Apps (when browsing All)
    if (selectedPlatform == OttPlatform.ALL && searchQuery.isBlank() && selectedMediaType == com.example.model.MediaType.ALL) {
      val platformShelves = listOf(
        Pair(OttPlatform.NETFLIX, "🎬 Netflix Originals & Global Hits"),
        Pair(OttPlatform.PRIME_VIDEO, "📦 Amazon Prime Video Exclusives"),
        Pair(OttPlatform.JIO_HOTSTAR, "🌟 JioHotstar Blockbusters & Sports"),
        Pair(OttPlatform.APPLE_TV, "🍎 Apple TV+ Award Winners"),
        Pair(OttPlatform.SONY_LIV, "🏆 Sony LIV Dramas & Sports"),
        Pair(OttPlatform.ZEE5, "🎭 ZEE5 Originals & Regional Cinema"),
        Pair(OttPlatform.YOUTUBE, "▶️ YouTube Trending & Creators")
      )

      platformShelves.forEach { (platform, shelfTitle) ->
        val platformItems = items.filter { it.platform == platform }
        if (platformItems.isNotEmpty()) {
          item {
            Column(modifier = Modifier.padding(top = 16.dp)) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(8.dp)
                      .clip(CircleShape)
                      .background(platform.brandColor)
                  )
                  Text(
                    text = shelfTitle,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                  )
                }

                Text(
                  text = "View All (${platformItems.size}) ›",
                  color = platform.brandColor,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onPlatformSelected(platform) }
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }

              LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                items(platformItems, key = { it.id }) { media ->
                  MediaCard(
                    mediaItem = media,
                    isSavedInWatchlist = watchlistIds.contains(media.id),
                    onWatchlistToggle = { onWatchlistToggle(media) },
                    onClick = { onItemClick(media) },
                    isVip = isVip
                  )
                }
              }
            }
          }
        }
      }
    }

    // Section 3: All Aggregated Titles (Grid-style row lists)
    item {
      Column(modifier = Modifier.padding(top = 20.dp)) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (searchQuery.isNotBlank()) "Search Results (${allPlatformList.size})"
                   else if (selectedPlatform != OttPlatform.ALL) "All ${selectedPlatform.displayName} Titles (${allPlatformList.size})"
                   else "Complete All-App Streaming Catalog (${allPlatformList.size})",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
          )
          if (isVip) {
            Text(
              text = "👑 VIP All-Access Active",
              color = AccentGold,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        if (allPlatformList.isEmpty()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(32.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "No streaming titles match your filters. Try selecting 'All OTTs'.",
              color = Color(0xFFA0A7B8),
              fontSize = 13.sp
            )
          }
        } else {
          // Display in pairs for balanced 2-column grid inside LazyColumn
          val chunked = allPlatformList.chunked(2)
          chunked.forEach { rowItems ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              rowItems.forEach { media ->
                Box(modifier = Modifier.weight(1f)) {
                  MediaCard(
                    mediaItem = media,
                    isSavedInWatchlist = watchlistIds.contains(media.id),
                    onWatchlistToggle = { onWatchlistToggle(media) },
                    onClick = { onItemClick(media) },
                    modifier = Modifier.fillMaxWidth(),
                    isVip = isVip
                  )
                }
              }
              if (rowItems.size == 1) {
                Spacer(modifier = Modifier.weight(1f))
              }
            }
          }
        }
      }
    }
  }
}

private fun launchUrl(context: Context, url: String) {
  try {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
  } catch (e: Exception) {
    // Graceful fallback
  }
}
