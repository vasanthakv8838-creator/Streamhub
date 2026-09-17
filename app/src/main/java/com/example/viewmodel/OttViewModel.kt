package com.example.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CountryAccessLibrary
import com.example.data.LiveTvCatalog
import com.example.data.OttCatalog
import com.example.data.RegionalCatalog
import com.example.data.TheatricalCatalog
import com.example.data.UserSessionManager
import com.example.data.db.AppDatabase
import com.example.data.gemini.GeminiService
import com.example.model.CuratedTrailer
import com.example.model.LiveCategory
import com.example.model.LiveChannel
import com.example.model.MediaItem
import com.example.model.MediaType
import com.example.model.OttPlatform
import com.example.model.StreamProfile
import com.example.model.UserProfile
import com.example.model.VideoAnalysisResult
import com.example.model.VoiceConversationMessage
import com.example.model.VpnConnectionState
import com.example.model.VpnServer
import com.example.model.VpnStatus
import com.example.model.WatchlistEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class OttViewModel(application: Application) : AndroidViewModel(application) {

  private val sessionManager = UserSessionManager(application)
  private val database = AppDatabase.getDatabase(application)
  private val watchlistDao = database.watchlistDao()
  private val geminiService = GeminiService()

  // Authentication State
  private val _currentUser = MutableStateFlow<UserProfile?>(sessionManager.getUserProfile())
  val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

  private val _isLoggedIn = MutableStateFlow(sessionManager.isLoggedIn())
  val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

  // Profiles State
  private val _profiles = MutableStateFlow<List<StreamProfile>>(sessionManager.getProfiles())
  val profiles: StateFlow<List<StreamProfile>> = _profiles.asStateFlow()

  private val _activeProfile = MutableStateFlow<StreamProfile?>(sessionManager.getActiveProfile())
  val activeProfile: StateFlow<StreamProfile?> = _activeProfile.asStateFlow()

  // Profile Selection ("Who's Watching?") Screen State
  private val _isProfileSelectionVisible = MutableStateFlow(false)
  val isProfileSelectionVisible: StateFlow<Boolean> = _isProfileSelectionVisible.asStateFlow()

  // Profile Dialog State
  private val _isProfileSheetVisible = MutableStateFlow(false)
  val isProfileSheetVisible: StateFlow<Boolean> = _isProfileSheetVisible.asStateFlow()

  // Plans Dialog State
  private val _isPlansSheetVisible = MutableStateFlow(false)
  val isPlansSheetVisible: StateFlow<Boolean> = _isPlansSheetVisible.asStateFlow()

  // VIP All-Access Tier State
  private val _isVip = MutableStateFlow(sessionManager.isVip())
  val isVip: StateFlow<Boolean> = _isVip.asStateFlow()

  private var textToSpeech: TextToSpeech? = null
  private val _isTtsReady = MutableStateFlow(false)

  // Navigation tabs
  val _selectedTab = MutableStateFlow(0) // 0: Explore/Feed, 1: Watchlist, 2: Voice AI, 3: Video Analyzer
  val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

  // Filters & Search
  private val _selectedPlatform = MutableStateFlow(OttPlatform.ALL)
  val selectedPlatform: StateFlow<OttPlatform> = _selectedPlatform.asStateFlow()

  private val _selectedMediaType = MutableStateFlow(MediaType.ALL)
  val selectedMediaType: StateFlow<MediaType> = _selectedMediaType.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  // Detail Sheet
  private val _selectedMedia = MutableStateFlow<MediaItem?>(null)
  val selectedMedia: StateFlow<MediaItem?> = _selectedMedia.asStateFlow()

  // Watchlist Room DB Flow
  val watchlistItems: StateFlow<List<WatchlistEntity>> = watchlistDao.getAllWatchlist()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val watchlistIds: StateFlow<List<String>> = watchlistDao.getAllWatchlistIds()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Surfshark VPN State
  private val defaultVpnServer = RegionalCatalog.regionalServers.first()
  private val _vpnState = MutableStateFlow(
    VpnConnectionState(
      status = VpnStatus.DISCONNECTED,
      server = defaultVpnServer,
      assignedVirtualIp = "",
      uptimeSeconds = 0L,
      downloadedMb = 0f
    )
  )
  val vpnState: StateFlow<VpnConnectionState> = _vpnState.asStateFlow()
  val availableVpnServers: List<VpnServer> = RegionalCatalog.regionalServers

  // Full Country Content Package (Movies, TV Shows, Live TV, Videos) for the active VPN country
  val countryAccessPackage: StateFlow<CountryAccessLibrary> = _vpnState.map { vpn ->
    RegionalCatalog.getCountryAccessLibrary(vpn.server.countryCode)
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    RegionalCatalog.getCountryAccessLibrary(defaultVpnServer.countryCode)
  )

  // Filtered Catalog Items: Dynamically injects regional exclusives when Surfshark VPN is connected!
  val filteredItems: StateFlow<List<MediaItem>> = combine(
    _selectedPlatform,
    _selectedMediaType,
    _searchQuery,
    _vpnState
  ) { platform, mediaType, query, vpn ->
    val baseList = if (vpn.status == VpnStatus.CONNECTED) {
      val regionalPkg = RegionalCatalog.getCountryAccessLibrary(vpn.server.countryCode)
      regionalPkg.allMediaItems + OttCatalog.items
    } else {
      OttCatalog.items
    }

    baseList.filter { item ->
      val matchesPlatform = platform == OttPlatform.ALL ||
          item.platform == platform ||
          (platform == OttPlatform.THEATRE && item.isInTheatresOnly)

      val matchesType = when (mediaType) {
        MediaType.ALL -> true
        MediaType.THEATRICAL -> item.isInTheatresOnly
        MediaType.MOVIE -> item.mediaType == MediaType.MOVIE || item.isInTheatresOnly
        else -> item.mediaType == mediaType
      }

      val matchesQuery = query.isBlank() ||
          item.title.contains(query, ignoreCase = true) ||
          item.genre.contains(query, ignoreCase = true) ||
          item.cast.any { it.contains(query, ignoreCase = true) } ||
          item.director.contains(query, ignoreCase = true) ||
          item.platform.displayName.contains(query, ignoreCase = true) ||
          item.vpnRegionBadge.contains(query, ignoreCase = true) ||
          item.cinemaFormats.contains(query, ignoreCase = true) ||
          item.theatreReleaseDate.contains(query, ignoreCase = true) ||
          (item.isInTheatresOnly && (query.contains("theatre", ignoreCase = true) || query.contains("cinema", ignoreCase = true)))

      matchesPlatform && matchesType && matchesQuery
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OttCatalog.items)

  val theatricalMovies: List<MediaItem> = TheatricalCatalog.theatricalMovies

  fun filterToTheatricalOnly() {
    _selectedMediaType.value = MediaType.THEATRICAL
    _selectedPlatform.value = OttPlatform.THEATRE
    _searchQuery.value = ""
  }

  // Live TV State
  private val _selectedLiveCategory = MutableStateFlow(LiveCategory.ALL)
  val selectedLiveCategory: StateFlow<LiveCategory> = _selectedLiveCategory.asStateFlow()

  private val _liveSearchQuery = MutableStateFlow("")
  val liveSearchQuery: StateFlow<String> = _liveSearchQuery.asStateFlow()

  private val _selectedLiveChannel = MutableStateFlow<LiveChannel?>(null)
  val selectedLiveChannel: StateFlow<LiveChannel?> = _selectedLiveChannel.asStateFlow()

  val liveChannels: StateFlow<List<LiveChannel>> = combine(
    _selectedLiveCategory,
    _liveSearchQuery,
    _vpnState
  ) { category, query, vpn ->
    val country = if (vpn.status == VpnStatus.CONNECTED) vpn.server.countryCode else "GLOBAL"
    val baseList = LiveTvCatalog.getChannelsForRegion(country)

    baseList.filter { channel ->
      val matchesCategory = category == LiveCategory.ALL || channel.category == category
      val matchesQuery = query.isBlank() ||
          channel.name.contains(query, ignoreCase = true) ||
          channel.network.contains(query, ignoreCase = true) ||
          channel.currentProgram.title.contains(query, ignoreCase = true)
      matchesCategory && matchesQuery
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LiveTvCatalog.channels)

  // Voice AI Assistant State (gemini-3.1-flash-live-preview)
  private val _voiceMessages = MutableStateFlow<List<VoiceConversationMessage>>(
    listOf(
      VoiceConversationMessage(
        id = "welcome_msg",
        role = "model",
        text = "Hello! I'm your unified OTT voice assistant powered by Gemini Live API. Ask me what to watch across YouTube, JioHotstar, Apple TV+, Netflix, ZEE5, Sony LIV, and Prime Video!",
        timestamp = System.currentTimeMillis()
      )
    )
  )
  val voiceMessages: StateFlow<List<VoiceConversationMessage>> = _voiceMessages.asStateFlow()

  private val _isVoiceLoading = MutableStateFlow(false)
  val isVoiceLoading: StateFlow<Boolean> = _isVoiceLoading.asStateFlow()

  private val _isSpeaking = MutableStateFlow(false)
  val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

  // Video Analyzer State (gemini-3.1-pro-preview)
  private val _selectedTrailer = MutableStateFlow<CuratedTrailer>(OttCatalog.curatedTrailers.first())
  val selectedTrailer: StateFlow<CuratedTrailer> = _selectedTrailer.asStateFlow()

  val availableTrailers: StateFlow<List<CuratedTrailer>> = _vpnState.map { vpn ->
    val countryVideos = RegionalCatalog.getVideosForRegion(vpn.server.countryCode)
    if (vpn.status == VpnStatus.CONNECTED) {
      countryVideos + OttCatalog.curatedTrailers
    } else {
      OttCatalog.curatedTrailers
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OttCatalog.curatedTrailers)

  private val _customVideoUrl = MutableStateFlow("")
  val customVideoUrl: StateFlow<String> = _customVideoUrl.asStateFlow()

  private val _customVideoTitle = MutableStateFlow("")
  val customVideoTitle: StateFlow<String> = _customVideoTitle.asStateFlow()

  private val _videoAnalysisResult = MutableStateFlow<VideoAnalysisResult?>(null)
  val videoAnalysisResult: StateFlow<VideoAnalysisResult?> = _videoAnalysisResult.asStateFlow()

  private val _isVideoAnalyzing = MutableStateFlow(false)
  val isVideoAnalyzing: StateFlow<Boolean> = _isVideoAnalyzing.asStateFlow()

  init {
    initTts()
  }

  private fun initTts() {
    textToSpeech = TextToSpeech(getApplication()) { status ->
      if (status == TextToSpeech.SUCCESS) {
        textToSpeech?.language = Locale.US
        _isTtsReady.value = true
      }
    }
  }

  fun selectTab(index: Int) {
    _selectedTab.value = index
  }

  fun setPlatform(platform: OttPlatform) {
    _selectedPlatform.value = platform
  }

  fun setMediaType(type: MediaType) {
    _selectedMediaType.value = type
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun openMediaDetails(item: MediaItem) {
    _selectedMedia.value = item
  }

  fun closeMediaDetails() {
    _selectedMedia.value = null
  }

  fun completeLogin(name: String, phoneNumber: String) {
    val user = sessionManager.saveUser(name, phoneNumber)
    _currentUser.value = user
    _isLoggedIn.value = true
    _profiles.value = sessionManager.getProfiles()
    _activeProfile.value = sessionManager.getActiveProfile()
    // Open Who's Watching / Profile screen right after verification
    _isProfileSelectionVisible.value = true
  }

  fun logout() {
    sessionManager.clearSession()
    _currentUser.value = null
    _isLoggedIn.value = false
    _profiles.value = emptyList()
    _activeProfile.value = null
    _isProfileSheetVisible.value = false
    _isProfileSelectionVisible.value = false
  }

  fun showProfileSheet() {
    _isProfileSheetVisible.value = true
  }

  fun hideProfileSheet() {
    _isProfileSheetVisible.value = false
  }

  fun showPlansSheet() {
    _isPlansSheetVisible.value = true
  }

  fun hidePlansSheet() {
    _isPlansSheetVisible.value = false
  }

  fun activateVipTier() {
    sessionManager.setVip(true)
    _isVip.value = true
    _currentUser.value = sessionManager.getUserProfile()
  }

  fun toggleVipTier() {
    val newVip = !_isVip.value
    sessionManager.setVip(newVip)
    _isVip.value = newVip
    _currentUser.value = sessionManager.getUserProfile()
  }

  fun openProfileSelection() {
    _isProfileSheetVisible.value = false
    _isProfileSelectionVisible.value = true
  }

  fun closeProfileSelection() {
    _isProfileSelectionVisible.value = false
  }

  fun selectProfile(profile: StreamProfile) {
    sessionManager.setActiveProfileId(profile.id)
    _activeProfile.value = profile
    _isProfileSelectionVisible.value = false
    _isProfileSheetVisible.value = false
  }

  fun addProfile(name: String, isKids: Boolean = false, colorHex: String? = null) {
    val newProfile = sessionManager.addProfile(name, isKids)
    _profiles.value = sessionManager.getProfiles()
    _activeProfile.value = newProfile
    sessionManager.setActiveProfileId(newProfile.id)
  }

  fun deleteProfile(profile: StreamProfile) {
    sessionManager.deleteProfile(profile.id)
    _profiles.value = sessionManager.getProfiles()
    _activeProfile.value = sessionManager.getActiveProfile()
  }

  fun toggleWatchlist(item: MediaItem) {
    viewModelScope.launch {
      val isSaved = watchlistIds.value.contains(item.id)
      if (isSaved) {
        watchlistDao.deleteById(item.id)
      } else {
        val entity = WatchlistEntity(
          mediaId = item.id,
          title = item.title,
          platformName = item.platform.displayName,
          mediaType = item.mediaType.label,
          genre = item.genre,
          rating = item.rating,
          year = item.year,
          posterUrl = item.posterUrl,
          watchUrl = item.watchUrl,
          isWatched = false
        )
        watchlistDao.insert(entity)
      }
    }
  }

  fun toggleWatchlistWatched(entity: WatchlistEntity) {
    viewModelScope.launch {
      watchlistDao.toggleWatched(entity.mediaId, !entity.isWatched)
    }
  }

  fun deleteFromWatchlist(mediaId: String) {
    viewModelScope.launch {
      watchlistDao.deleteById(mediaId)
    }
  }

  fun updateWatchlistNotes(entity: WatchlistEntity, notes: String, rating: Int) {
    viewModelScope.launch {
      watchlistDao.update(
        entity.copy(userNotes = notes, userRating = rating)
      )
    }
  }

  // --- Voice Assistant (gemini-3.1-flash-live-preview) ---
  fun sendVoicePrompt(prompt: String) {
    if (prompt.isBlank() || _isVoiceLoading.value) return

    val userMsg = VoiceConversationMessage(
      id = "user_${System.currentTimeMillis()}",
      role = "user",
      text = prompt
    )
    _voiceMessages.value = _voiceMessages.value + userMsg
    _isVoiceLoading.value = true

    viewModelScope.launch {
      val reply = geminiService.chatWithLiveAssistant(_voiceMessages.value, prompt)
      val aiMsg = VoiceConversationMessage(
        id = "ai_${System.currentTimeMillis()}",
        role = "model",
        text = reply
      )
      _voiceMessages.value = _voiceMessages.value + aiMsg
      _isVoiceLoading.value = false

      // Speak answer automatically for conversational experience
      speakText(reply)
    }
  }

  fun speakText(text: String) {
    if (_isTtsReady.value && textToSpeech != null) {
      textToSpeech?.stop()
      _isSpeaking.value = true
      textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "LiveUtterance")
    }
  }

  fun stopSpeaking() {
    textToSpeech?.stop()
    _isSpeaking.value = false
  }

  // --- Video Analyzer (gemini-3.1-pro-preview) ---
  fun selectTrailer(trailer: CuratedTrailer) {
    _selectedTrailer.value = trailer
    _customVideoUrl.value = trailer.videoUrl
    _customVideoTitle.value = trailer.title
  }

  fun setCustomVideoUrl(url: String) {
    _customVideoUrl.value = url
  }

  fun setCustomVideoTitle(title: String) {
    _customVideoTitle.value = title
  }

  fun analyzeCurrentVideo(customPrompt: String? = null) {
    val title = if (_customVideoTitle.value.isNotBlank()) _customVideoTitle.value else _selectedTrailer.value.title
    val url = if (_customVideoUrl.value.isNotBlank()) _customVideoUrl.value else _selectedTrailer.value.videoUrl
    val platform = _selectedTrailer.value.platform.displayName

    _isVideoAnalyzing.value = true
    viewModelScope.launch {
      val result = geminiService.analyzeVideoContent(
        videoTitle = title,
        videoUrl = url,
        platform = platform,
        customFocusPrompt = customPrompt
      )
      _videoAnalysisResult.value = result
      _isVideoAnalyzing.value = false
    }
  }

  // Surfshark VPN Actions
  fun connectVpn() {
    viewModelScope.launch {
      _vpnState.value = _vpnState.value.copy(status = VpnStatus.CONNECTING)
      kotlinx.coroutines.delay(1000)
      val current = _vpnState.value
      _vpnState.value = current.copy(
        status = VpnStatus.CONNECTED,
        assignedVirtualIp = current.server.ipAddress,
        uptimeSeconds = 18L,
        downloadedMb = 48.2f
      )
    }
  }

  fun disconnectVpn() {
    _vpnState.value = _vpnState.value.copy(
      status = VpnStatus.DISCONNECTED,
      assignedVirtualIp = "",
      uptimeSeconds = 0L
    )
  }

  fun switchVpnServer(newServer: VpnServer) {
    _vpnState.value = _vpnState.value.copy(
      status = VpnStatus.CONNECTED,
      server = newServer,
      assignedVirtualIp = newServer.ipAddress,
      uptimeSeconds = 1L,
      downloadedMb = 14.8f
    )
    val countryVideos = RegionalCatalog.getVideosForRegion(newServer.countryCode)
    if (countryVideos.isNotEmpty()) {
      selectTrailer(countryVideos.first())
    }
  }

  fun openTrailer(trailer: CuratedTrailer) {
    selectTrailer(trailer)
    selectTab(6) // Navigate to Video Analyzer
  }

  fun openCountryLiveChannel(channel: LiveChannel) {
    openLiveChannel(channel)
    selectTab(1) // Navigate to Live TV
  }

  fun toggleCleanWeb() {
    val current = _vpnState.value
    _vpnState.value = current.copy(cleanWebEnabled = !current.cleanWebEnabled)
  }

  fun toggleKillSwitch() {
    val current = _vpnState.value
    _vpnState.value = current.copy(killSwitchEnabled = !current.killSwitchEnabled)
  }

  // Live TV Actions
  fun selectLiveCategory(category: LiveCategory) {
    _selectedLiveCategory.value = category
  }

  fun setLiveSearchQuery(query: String) {
    _liveSearchQuery.value = query
  }

  fun openLiveChannel(channel: LiveChannel) {
    _selectedLiveChannel.value = channel
  }

  fun closeLiveChannel() {
    _selectedLiveChannel.value = null
  }

  override fun onCleared() {
    super.onCleared()
    textToSpeech?.stop()
    textToSpeech?.shutdown()
  }
}
