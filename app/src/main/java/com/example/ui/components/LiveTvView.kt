package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.LiveTvCatalog
import com.example.model.LiveCategory
import com.example.model.LiveChannel
import com.example.model.VpnConnectionState
import com.example.model.VpnStatus
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveTvView(
  channels: List<LiveChannel>,
  selectedCategory: LiveCategory,
  onSelectCategory: (LiveCategory) -> Unit,
  searchQuery: String,
  onSearchQueryChanged: (String) -> Unit,
  vpnState: VpnConnectionState,
  onOpenVpnSettings: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var activeChannelForPlayback by remember { mutableStateOf<LiveChannel?>(null) }
  var selectedCountryFilter by remember { mutableStateOf("All Countries") }
  var showCountryPickerSheet by remember { mutableStateOf(false) }
  var countrySheetSearchQuery by remember { mutableStateOf("") }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val filteredChannels = remember(channels, selectedCountryFilter) {
    if (selectedCountryFilter == "All Countries" || selectedCountryFilter == "GLOBAL") {
      channels
    } else {
      channels.filter { channel ->
        channel.countryName.contains(selectedCountryFilter, ignoreCase = true) ||
        channel.countryCode.equals(selectedCountryFilter, ignoreCase = true) ||
        selectedCountryFilter.contains(channel.countryName, ignoreCase = true) ||
        selectedCountryFilter.contains(channel.countryCode, ignoreCase = true)
      }
    }
  }

  // Pulsing animation for "LIVE" red indicator
  val infiniteTransition = rememberInfiniteTransition(label = "live_badge_pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "live_pulse_alpha"
  )

  val featuredChannel = channels.firstOrNull { it.isFeatured } ?: channels.firstOrNull()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundDark)
      .padding(horizontal = 16.dp, vertical = 10.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Top Live TV Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(
                Brush.linearGradient(
                  listOf(Color(0xFFE50914), Color(0xFFFF4500))
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Tv,
              contentDescription = "Live TV",
              tint = Color.White,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Live TV & Channels",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
              )
              Spacer(modifier = Modifier.width(8.dp))
              // Live red pill
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(Color(0xFFE50914).copy(alpha = pulseAlpha))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "● ON AIR",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Black,
                  color = Color.White
                )
              }
            }
            Text(
              text = "24/7 Free Live Streams • News, Sports & Entertainment",
              fontSize = 11.sp,
              color = Color(0xFFA0A7B8)
            )
          }
        }

        // VPN status indicator shortcut
        Surface(
          onClick = onOpenVpnSettings,
          shape = RoundedCornerShape(16.dp),
          color = if (vpnState.status == VpnStatus.CONNECTED) Color(0xFF00D1B2).copy(alpha = 0.15f) else SurfaceElevated,
          border = BorderStroke(
            1.dp,
            if (vpnState.status == VpnStatus.CONNECTED) Color(0xFF00D1B2) else BorderSubtle
          ),
          modifier = Modifier.testTag("live_tv_vpn_pill")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = "VPN",
              tint = if (vpnState.status == VpnStatus.CONNECTED) Color(0xFF00D1B2) else Color(0xFFA0A7B8),
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (vpnState.status == VpnStatus.CONNECTED) {
                "${vpnState.server.flagEmoji} ${vpnState.server.countryCode}"
              } else {
                "Surfshark"
              },
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (vpnState.status == VpnStatus.CONNECTED) Color(0xFF00D1B2) else Color(0xFFA0A7B8)
            )
          }
        }
      }
    }

    // Search Bar for Channels
    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("live_tv_search_input"),
        placeholder = {
          Text(
            text = "Search live channels, shows, or networks...",
            color = Color(0xFF6E7687),
            fontSize = 13.sp
          )
        },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color(0xFFA0A7B8),
            modifier = Modifier.size(18.dp)
          )
        },
        trailingIcon = {
          if (searchQuery.isNotBlank()) {
            IconButton(onClick = { onSearchQueryChanged("") }) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Clear",
                tint = Color(0xFFA0A7B8),
                modifier = Modifier.size(18.dp)
              )
            }
          }
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = SurfaceDark,
          unfocusedContainerColor = SurfaceDark,
          focusedBorderColor = Color(0xFFE50914),
          unfocusedBorderColor = BorderSubtle,
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White
        ),
        singleLine = true
      )
    }

    // Category Selector Chips Row
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        LiveCategory.values().forEach { category ->
          val isSelected = category == selectedCategory
          Surface(
            onClick = { onSelectCategory(category) },
            shape = RoundedCornerShape(20.dp),
            color = if (isSelected) Color(0xFFE50914) else SurfaceDark,
            border = BorderStroke(1.dp, if (isSelected) Color(0xFFE50914) else BorderSubtle),
            modifier = Modifier.testTag("live_category_${category.name.lowercase()}")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = category.emoji, fontSize = 13.sp)
              Spacer(modifier = Modifier.width(5.dp))
              Text(
                text = category.displayName,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFFA0A7B8)
              )
            }
          }
        }
      }
    }

    // Country / World Region Selector Chips Row
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // Quick button to open all 195 countries picker
        Surface(
          onClick = { showCountryPickerSheet = true },
          shape = RoundedCornerShape(16.dp),
          color = Color(0xFF0C2B36),
          border = BorderStroke(1.dp, Color(0xFF00D1B2)),
          modifier = Modifier.testTag("open_all_countries_picker")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "🌍 All 195 Countries ▾", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF00D1B2))
          }
        }

        val countryFilters = listOf(
          "All Countries" to "🌐",
          "United States" to "🇺🇸",
          "United Kingdom" to "🇬🇧",
          "India" to "🇮🇳",
          "Japan" to "🇯🇵",
          "South Korea" to "🇰🇷",
          "Germany" to "🇩🇪",
          "France" to "🇫🇷",
          "Italy" to "🇮🇹",
          "Spain" to "🇪🇸",
          "Brazil" to "🇧🇷",
          "Canada" to "🇨🇦",
          "Australia" to "🇦🇺",
          "Mexico" to "🇲🇽",
          "Netherlands" to "🇳🇱",
          "Switzerland" to "🇨🇭",
          "Sweden" to "🇸🇪",
          "United Arab Emirates" to "🇦🇪",
          "Saudi Arabia" to "🇸🇦",
          "South Africa" to "🇿🇦",
          "Argentina" to "🇦🇷",
          "Egypt" to "🇪🇬",
          "Turkey" to "🇹🇷",
          "Poland" to "🇵🇱",
          "Greece" to "🇬🇷",
          "Norway" to "🇳🇴",
          "Denmark" to "🇩🇰",
          "Ireland" to "🇮🇪",
          "New Zealand" to "🇳🇿",
          "Singapore" to "🇸🇬",
          "Indonesia" to "🇮🇩",
          "Thailand" to "🇹🇭",
          "Vietnam" to "🇻🇳",
          "Philippines" to "🇵🇭",
          "Chile" to "🇨🇱",
          "Colombia" to "🇨🇴",
          "Austria" to "🇦🇹",
          "Qatar" to "🇶🇦"
        )
        countryFilters.forEach { (countryName, flag) ->
          val isSelected = selectedCountryFilter == countryName
          val isVpnConnectedCountry = vpnState.status == VpnStatus.CONNECTED &&
              (vpnState.server.countryName.contains(countryName, ignoreCase = true) ||
               countryName.contains(vpnState.server.countryName, ignoreCase = true))
          Surface(
            onClick = { selectedCountryFilter = countryName },
            shape = RoundedCornerShape(16.dp),
            color = if (isSelected) Color(0xFF00D1B2) else SurfaceDark,
            border = BorderStroke(
              1.dp,
              if (isSelected) Color(0xFF00D1B2) else if (isVpnConnectedCountry) Color(0xFF00D1B2).copy(alpha = 0.6f) else BorderSubtle
            ),
            modifier = Modifier.testTag("live_country_filter_${countryName.lowercase().replace(" ", "_")}")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = flag, fontSize = 11.sp)
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = countryName,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.Black else Color.White
              )
              if (isVpnConnectedCountry) {
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(if (isSelected) Color.Black else Color(0xFF00D1B2))
                    .padding(horizontal = 3.dp, vertical = 1.dp)
                ) {
                  Text(
                    text = "VPN",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isSelected) Color(0xFF00D1B2) else Color.Black
                  )
                }
              }
            }
          }
        }
      }
    }

    // Channel Count and Active Filter Status Bar
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (selectedCountryFilter == "All Countries") {
            "📺 ${filteredChannels.size} Live Channels Worldwide (195 Countries)"
          } else {
            "📺 ${filteredChannels.size} Live Channels in $selectedCountryFilter"
          },
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = AccentGold
        )
        if (selectedCountryFilter != "All Countries") {
          Surface(
            onClick = { selectedCountryFilter = "All Countries" },
            shape = RoundedCornerShape(8.dp),
            color = SurfaceElevated
          ) {
            Text(
              text = "Show All (195 Nations)",
              fontSize = 10.sp,
              color = Color(0xFF00D1B2),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
          }
        }
      }
    }

    // Featured Broadcast Hero Card
    if (featuredChannel != null && searchQuery.isBlank() && selectedCategory == LiveCategory.ALL) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { activeChannelForPlayback = featuredChannel }
            .testTag("featured_live_channel_card"),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceDark),
          border = BorderStroke(1.5.dp, Color(0xFFE50914).copy(alpha = 0.5f))
        ) {
          Column {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
            ) {
              AsyncImage(
                model = featuredChannel.bannerUrl,
                contentDescription = featuredChannel.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
              )
              // Gradient Overlay
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .background(
                    Brush.verticalGradient(
                      listOf(Color.Transparent, Color(0xCC000000), Color(0xFF141923))
                    )
                  )
              )
              // Live Red Badge (Top Left)
              Row(
                modifier = Modifier
                  .align(Alignment.TopStart)
                  .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFE50914))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                  Text(
                    text = "● LIVE STREAM",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                  )
                }
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                  Text(
                    text = featuredChannel.viewersCount,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentGold
                  )
                }
              }

              // Play Icon Overlay
              Surface(
                onClick = { activeChannelForPlayback = featuredChannel },
                shape = CircleShape,
                color = Color(0xFFE50914),
                shadowElevation = 6.dp,
                modifier = Modifier
                  .align(Alignment.Center)
                  .size(54.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play Live Channel",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                  )
                }
              }

              // Quality badge (Bottom Right)
              Box(
                modifier = Modifier
                  .align(Alignment.BottomEnd)
                  .padding(10.dp)
                  .clip(RoundedCornerShape(4.dp))
                  .background(Color.Black.copy(alpha = 0.8f))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = featuredChannel.resolution,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = AccentCyan
                )
              }
            }

            // Featured Details
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = featuredChannel.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AccentGold
              )
              Text(
                text = featuredChannel.currentProgram.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = featuredChannel.currentProgram.description,
                fontSize = 12.sp,
                color = Color(0xFFA0A7B8),
                maxLines = 2
              )
              Spacer(modifier = Modifier.height(10.dp))
              // Progress indicator
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = featuredChannel.currentProgram.startTime,
                  fontSize = 10.sp,
                  color = Color(0xFFA0A7B8)
                )
                Spacer(modifier = Modifier.width(8.dp))
                LinearProgressIndicator(
                  progress = { featuredChannel.currentProgram.progressPercent },
                  modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                  color = Color(0xFFE50914),
                  trackColor = Color(0xFF263D52)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = featuredChannel.currentProgram.endTime,
                  fontSize = 10.sp,
                  color = Color(0xFFA0A7B8)
                )
              }
            }
          }
        }
      }
    }

    // Channels Grid / EPG Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "ALL LIVE BROADCASTS (${filteredChannels.size})",
          fontSize = 12.sp,
          fontWeight = FontWeight.ExtraBold,
          color = Color(0xFFA0A7B8),
          letterSpacing = 0.5.sp
        )
        if (vpnState.status == VpnStatus.CONNECTED) {
          Text(
            text = "Prioritizing ${vpnState.server.flagEmoji} ${vpnState.server.countryName}",
            fontSize = 11.sp,
            color = Color(0xFF00D1B2),
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    // Channel Items
    items(filteredChannels) { channel ->
      LiveChannelCard(
        channel = channel,
        onClick = { activeChannelForPlayback = channel },
        onDirectStream = { launchLiveStream(context, channel.streamUrl) }
      )
    }

    item {
      Spacer(modifier = Modifier.height(20.dp))
    }
  }

  // Live Stream Playback Dialog / Bottom Sheet
  activeChannelForPlayback?.let { channel ->
    ModalBottomSheet(
      onDismissRequest = { activeChannelForPlayback = null },
      sheetState = sheetState,
      containerColor = BackgroundDark
    ) {
      LivePlaybackSheetContent(
        channel = channel,
        onClose = { activeChannelForPlayback = null },
        onOpenInExternal = { launchLiveStream(context, channel.streamUrl) }
      )
    }
  }

  // All 195 Sovereign Countries Picker Bottom Sheet
  if (showCountryPickerSheet) {
    ModalBottomSheet(
      onDismissRequest = {
        showCountryPickerSheet = false
        countrySheetSearchQuery = ""
      },
      containerColor = SurfaceDark
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "🌍 All 195 Sovereign Countries",
              fontSize = 18.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White
            )
            Text(
              text = "Explore 600+ live national TV channels worldwide",
              fontSize = 11.sp,
              color = Color(0xFFA0A7B8)
            )
          }
          IconButton(onClick = {
            showCountryPickerSheet = false
            countrySheetSearchQuery = ""
          }) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = countrySheetSearchQuery,
          onValueChange = { countrySheetSearchQuery = it },
          placeholder = { Text("Search 195 countries (e.g. Brazil, Japan, Kenya...)", color = Color(0xFF6E7687), fontSize = 13.sp) },
          leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFFA0A7B8), modifier = Modifier.size(18.dp))
          },
          trailingIcon = {
            if (countrySheetSearchQuery.isNotBlank()) {
              IconButton(onClick = { countrySheetSearchQuery = "" }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color(0xFFA0A7B8), modifier = Modifier.size(16.dp))
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = BackgroundDark,
            unfocusedContainerColor = BackgroundDark,
            focusedBorderColor = Color(0xFF00D1B2),
            unfocusedBorderColor = BorderSubtle,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          ),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        val allCountries = remember { LiveTvCatalog.worldCountriesList }
        val searchedCountries = remember(countrySheetSearchQuery, allCountries) {
          if (countrySheetSearchQuery.isBlank()) {
            allCountries
          } else {
            allCountries.filter { (name, _) ->
              name.contains(countrySheetSearchQuery, ignoreCase = true)
            }
          }
        }

        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 440.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          items(searchedCountries) { (countryName, flag) ->
            val isSelected = selectedCountryFilter == countryName
            Surface(
              onClick = {
                selectedCountryFilter = countryName
                showCountryPickerSheet = false
                countrySheetSearchQuery = ""
              },
              shape = RoundedCornerShape(10.dp),
              color = if (isSelected) Color(0xFF0A4F48) else Color.Transparent,
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(text = flag, fontSize = 22.sp)
                  Spacer(modifier = Modifier.width(12.dp))
                  Text(
                    text = countryName,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color(0xFF00D1B2) else Color.White
                  )
                }
                if (isSelected) {
                  Text(
                    text = "✓ Selected",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00D1B2)
                  )
                }
              }
            }
          }
        }
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

@Composable
fun LiveChannelCard(
  channel: LiveChannel,
  onClick: () -> Unit,
  onDirectStream: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("live_channel_${channel.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
    border = BorderStroke(1.dp, BorderSubtle)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Channel Logo / Thumbnail
      Box(
        modifier = Modifier
          .size(80.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(SurfaceElevated)
      ) {
        AsyncImage(
          model = channel.bannerUrl,
          contentDescription = channel.name,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )
        // Red Live Tag
        Box(
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFE50914))
            .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
          Text(
            text = "LIVE",
            fontSize = 8.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Channel Info & EPG
      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = channel.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = AccentGold
          )
          Text(
            text = channel.viewersCount,
            fontSize = 10.sp,
            color = Color(0xFFA0A7B8),
            fontWeight = FontWeight.SemiBold
          )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "${channel.flagEmoji} ${channel.countryName}",
            fontSize = 10.sp,
            color = Color(0xFFA0A7B8),
            fontWeight = FontWeight.Medium
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "• ${channel.language}",
            fontSize = 10.sp,
            color = Color(0xFF6E7687)
          )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = channel.currentProgram.title,
          fontSize = 14.sp,
          fontWeight = FontWeight.ExtraBold,
          color = Color.White,
          maxLines = 1
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Progress bar
        LinearProgressIndicator(
          progress = { channel.currentProgram.progressPercent },
          modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
            .clip(RoundedCornerShape(1.5.dp)),
          color = Color(0xFFE50914),
          trackColor = Color(0xFF263D52)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "NEXT: ${channel.nextProgram.title}",
            fontSize = 10.sp,
            color = Color(0xFFA0A7B8),
            maxLines = 1,
            modifier = Modifier.weight(1f)
          )
          Text(
            text = channel.resolution,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = AccentCyan
          )
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Quick Play Button
      Surface(
        onClick = onDirectStream,
        shape = CircleShape,
        color = SurfaceElevated,
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier.size(36.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Watch Stream",
            tint = Color(0xFFE50914),
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}

@Composable
fun LivePlaybackSheetContent(
  channel: LiveChannel,
  onClose: () -> Unit,
  onOpenInExternal: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 12.dp)
  ) {
    // Top Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFFE50914))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = "● LIVE ON AIR",
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = channel.name,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }
      IconButton(onClick = onClose) {
        Icon(
          imageVector = Icons.Default.Close,
          contentDescription = "Close",
          tint = Color.White
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Simulated Live Player Viewport
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(Color.Black)
    ) {
      AsyncImage(
        model = channel.bannerUrl,
        contentDescription = channel.name,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
      )
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color(0x55000000))
      )

      // Player Controls overlay
      Row(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .fillMaxWidth()
          .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xCC000000))))
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.FiberManualRecord,
            contentDescription = null,
            tint = Color(0xFFE50914),
            modifier = Modifier.size(12.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "LIVE", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.width(10.dp))
          Text(text = channel.viewersCount, fontSize = 11.sp, color = AccentGold)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription = "Audio",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Icon(
            imageVector = Icons.Default.Fullscreen,
            contentDescription = "Full Screen",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Current Program Title & Description
    Text(
      text = channel.currentProgram.title,
      fontSize = 18.sp,
      fontWeight = FontWeight.ExtraBold,
      color = Color.White
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = channel.currentProgram.description,
      fontSize = 13.sp,
      color = Color(0xFFA0A7B8)
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Schedule / EPG Details
    Card(
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceDark),
      border = BorderStroke(1.dp, BorderSubtle),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text(
          text = "PROGRAM GUIDE (EPG)",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = AccentGold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = "NOW PLAYING", fontSize = 11.sp, color = Color(0xFF00D1B2), fontWeight = FontWeight.Bold)
          Text(text = "${channel.currentProgram.startTime} - ${channel.currentProgram.endTime}", fontSize = 11.sp, color = Color(0xFFA0A7B8))
        }
        Text(text = channel.currentProgram.title, fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = "UP NEXT", fontSize = 11.sp, color = Color(0xFFA0A7B8), fontWeight = FontWeight.Bold)
          Text(text = channel.nextProgram.startTime, fontSize = 11.sp, color = Color(0xFFA0A7B8))
        }
        Text(text = channel.nextProgram.title, fontSize = 13.sp, color = Color.White)
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Launch Full Stream in Browser / YouTube
    Button(
      onClick = onOpenInExternal,
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .testTag("launch_full_stream_button"),
      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
      shape = RoundedCornerShape(12.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.OpenInNew,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Watch Full 4K Live Stream",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}

private fun launchLiveStream(context: Context, url: String) {
  try {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Opening stream feed...", Toast.LENGTH_SHORT).show()
  }
}
