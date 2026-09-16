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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.CountryAccessLibrary
import com.example.data.RegionalCatalog
import com.example.model.CuratedTrailer
import com.example.model.LiveChannel
import com.example.model.MediaItem
import com.example.model.VpnConnectionState
import com.example.model.VpnServer
import com.example.model.VpnStatus

// Surfshark Official Brand Colors
private val SurfsharkNavy = Color(0xFF0F1E29)
private val SurfsharkCardBg = Color(0xFF172836)
private val SurfsharkTeal = Color(0xFF00D1B2)
private val SurfsharkDarkTeal = Color(0xFF0A4F48)
private val SurfsharkBlue = Color(0xFF1E88E5)
private val SurfsharkBorder = Color(0xFF263D52)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurfsharkVpnView(
  vpnState: VpnConnectionState,
  availableServers: List<VpnServer>,
  onConnect: () -> Unit,
  onDisconnect: () -> Unit,
  onSelectServer: (VpnServer) -> Unit,
  onToggleCleanWeb: () -> Unit,
  onToggleKillSwitch: () -> Unit,
  onMediaClick: (MediaItem) -> Unit,
  onChannelClick: (LiveChannel) -> Unit = {},
  onVideoClick: (CuratedTrailer) -> Unit = {},
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var isServerPickerVisible by remember { mutableStateOf(false) }
  var serverSearchQuery by remember { mutableStateOf("") }
  var selectedRegion by remember { mutableStateOf("All 195") }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val filteredServers = remember(availableServers, serverSearchQuery, selectedRegion) {
    availableServers.filter { server ->
      val matchesRegion = when (selectedRegion) {
        "All 195" -> true
        "Popular" -> listOf("US", "GB", "IN", "JP", "DE", "CA", "AU", "FR", "BR", "KR", "SG", "IT", "ES", "NL", "CH", "SE", "MX", "AE", "ZA").contains(server.countryCode)
        else -> server.region.equals(selectedRegion, ignoreCase = true)
      }
      val matchesQuery = serverSearchQuery.isBlank() ||
          server.countryName.contains(serverSearchQuery, ignoreCase = true) ||
          server.countryCode.contains(serverSearchQuery, ignoreCase = true) ||
          server.city.contains(serverSearchQuery, ignoreCase = true) ||
          server.unlockedServices.any { it.contains(serverSearchQuery, ignoreCase = true) }
      matchesRegion && matchesQuery
    }
  }

  val isConnected = vpnState.status == VpnStatus.CONNECTED
  val isConnecting = vpnState.status == VpnStatus.CONNECTING

  val infiniteTransition = rememberInfiniteTransition(label = "pulse_anim")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1.0f,
    targetValue = if (isConnected || isConnecting) 1.08f else 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_scale"
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(SurfsharkNavy)
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Surfshark Brand Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(
                Brush.linearGradient(
                  listOf(SurfsharkTeal, SurfsharkBlue)
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = "Surfshark Shield",
              tint = Color.Black,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Surfshark VPN",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
              )
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(SurfsharkDarkTeal)
                  .padding(horizontal = 5.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "BUILT-IN",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = SurfsharkTeal
                )
              }
            }
            Text(
              text = "Powered by Surfshark • WireGuard® Ultra",
              fontSize = 11.sp,
              color = Color(0xFFA0A7B8)
            )
          }
        }

        // External Launcher pill
        Surface(
          onClick = {
            launchSurfsharkApp(context)
          },
          shape = RoundedCornerShape(20.dp),
          color = SurfsharkCardBg,
          border = BorderStroke(1.dp, SurfsharkBorder),
          modifier = Modifier.testTag("launch_surfshark_app_button")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Surfshark App",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = SurfsharkTeal
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.OpenInNew,
              contentDescription = "Open App",
              tint = SurfsharkTeal,
              modifier = Modifier.size(14.dp)
            )
          }
        }
      }
    }

    // Main Power Button Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("vpn_main_control_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfsharkCardBg),
        border = BorderStroke(
          1.5.dp,
          if (isConnected) SurfsharkTeal else SurfsharkBorder
        )
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Animated Pulse Glow behind Power Button
          Box(contentAlignment = Alignment.Center) {
            if (isConnected || isConnecting) {
              Box(
                modifier = Modifier
                  .size(140.dp)
                  .scale(pulseScale)
                  .clip(CircleShape)
                  .background(
                    if (isConnected) SurfsharkTeal.copy(alpha = 0.15f)
                    else Color(0xFFFFB800).copy(alpha = 0.15f)
                  )
              )
            }

            // Power Toggle Button
            Surface(
              onClick = {
                if (isConnected) onDisconnect() else onConnect()
              },
              shape = CircleShape,
              color = when (vpnState.status) {
                VpnStatus.CONNECTED -> SurfsharkTeal
                VpnStatus.CONNECTING -> Color(0xFFFFB800)
                VpnStatus.DISCONNECTED -> Color(0xFF263D52)
              },
              shadowElevation = 8.dp,
              modifier = Modifier
                .size(100.dp)
                .testTag("vpn_power_button")
            ) {
              Box(contentAlignment = Alignment.Center) {
                if (isConnecting) {
                  CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(44.dp)
                  )
                } else {
                  Icon(
                    imageVector = Icons.Default.PowerSettingsNew,
                    contentDescription = "Power",
                    tint = if (isConnected) Color.Black else Color.White,
                    modifier = Modifier.size(48.dp)
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Status Heading
          Text(
            text = when (vpnState.status) {
              VpnStatus.CONNECTED -> "SECURELY CONNECTED"
              VpnStatus.CONNECTING -> "CONNECTING TO TUNNEL..."
              VpnStatus.DISCONNECTED -> "VPN DISCONNECTED"
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = when (vpnState.status) {
              VpnStatus.CONNECTED -> SurfsharkTeal
              VpnStatus.CONNECTING -> Color(0xFFFFB800)
              VpnStatus.DISCONNECTED -> Color(0xFFA0A7B8)
            },
            letterSpacing = 1.sp
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = if (isConnected) {
              "Virtual IP: ${vpnState.assignedVirtualIp} • CleanWeb™ Active"
            } else {
              "Tap to connect and unlock regional movies, shows & Live TV"
            },
            fontSize = 12.sp,
            color = Color(0xFFA0A7B8)
          )

          // Live Metrics Row when connected
          if (isConnected) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfsharkNavy)
                .padding(vertical = 10.dp, horizontal = 14.dp),
              horizontalArrangement = Arrangement.SpaceAround,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "LATENCY", fontSize = 9.sp, color = Color(0xFFA0A7B8), fontWeight = FontWeight.Bold)
                Text(text = "${vpnState.server.pingMs} ms", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = SurfsharkTeal)
              }
              Box(modifier = Modifier.width(1.dp).height(24.dp).background(SurfsharkBorder))
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "PROTOCOL", fontSize = 9.sp, color = Color(0xFFA0A7B8), fontWeight = FontWeight.Bold)
                Text(text = "WireGuard®", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
              }
              Box(modifier = Modifier.width(1.dp).height(24.dp).background(SurfsharkBorder))
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "DATA ENCRYPTED", fontSize = 9.sp, color = Color(0xFFA0A7B8), fontWeight = FontWeight.Bold)
                Text(text = "${vpnState.downloadedMb} MB", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
              }
            }
          }
        }
      }
    }

    // Selected Location Card with Tap to Change Location
    item {
      Column {
        Text(
          text = "VPN LOCATION & REGION",
          fontSize = 12.sp,
          fontWeight = FontWeight.ExtraBold,
          color = Color(0xFFA0A7B8),
          letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { isServerPickerVisible = true }
            .testTag("vpn_select_location_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = SurfsharkCardBg),
          border = BorderStroke(1.dp, SurfsharkBorder)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Text(
                text = vpnState.server.flagEmoji,
                fontSize = 32.sp
              )
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = vpnState.server.countryName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "• ${vpnState.server.city}",
                    fontSize = 12.sp,
                    color = Color(0xFFA0A7B8)
                  )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = vpnState.server.catalogDescription,
                  fontSize = 11.sp,
                  color = SurfsharkTeal,
                  maxLines = 1
                )
              }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(SurfsharkDarkTeal)
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "CHANGE",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = SurfsharkTeal
                )
              }
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = "Change Location",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }
      }
    }

    // Quick Country Selector Carousel
    item {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "FAST LOCATION SWITCHER (195 COUNTRIES)",
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFA0A7B8),
            letterSpacing = 0.5.sp
          )
          Text(
            text = "View All 195 ›",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = SurfsharkTeal,
            modifier = Modifier
              .clickable { isServerPickerVisible = true }
              .padding(4.dp)
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(availableServers) { server ->
            val isSelected = server.id == vpnState.server.id
            Surface(
              onClick = {
                onSelectServer(server)
              },
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) SurfsharkDarkTeal else SurfsharkCardBg,
              border = BorderStroke(
                1.dp,
                if (isSelected) SurfsharkTeal else SurfsharkBorder
              ),
              modifier = Modifier.testTag("quick_server_${server.countryCode}")
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = server.flagEmoji, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                  Text(
                    text = server.countryName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) SurfsharkTeal else Color.White
                  )
                  Text(
                    text = "${server.pingMs}ms",
                    fontSize = 10.sp,
                    color = Color(0xFFA0A7B8)
                  )
                }
              }
            }
          }
        }
      }
    }

    // Complete Country Access Hub (Movies, TV Shows, Live TV, Videos for active country)
    item {
      val countryPkg = remember(vpnState.server.countryCode) {
        RegionalCatalog.getCountryAccessLibrary(vpnState.server.countryCode)
      }
      var selectedContentTab by remember { mutableStateOf(0) }

      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfsharkCardBg),
        border = BorderStroke(1.dp, if (isConnected) SurfsharkTeal.copy(alpha = 0.6f) else SurfsharkBorder),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("country_access_hub")
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
              Text(text = countryPkg.flagEmoji, fontSize = 24.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "${countryPkg.countryName.uppercase()} CATALOG",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SurfsharkTeal,
                    letterSpacing = 0.5.sp
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(4.dp))
                      .background(if (isConnected) SurfsharkDarkTeal else Color(0xFF3B2E10))
                      .padding(horizontal = 4.dp, vertical = 1.dp)
                  ) {
                    Text(
                      text = if (isConnected) "UNLOCKED" else "READY TO UNLOCK",
                      fontSize = 8.sp,
                      fontWeight = FontWeight.Black,
                      color = if (isConnected) SurfsharkTeal else Color(0xFFFFB800)
                    )
                  }
                }
                Text(
                  text = if (isConnected) "Virtual Location: ${countryPkg.city} • All Content Unlocked"
                         else "Connect Surfshark to stream ${countryPkg.countryName} library",
                  fontSize = 10.sp,
                  color = Color(0xFFA0A7B8),
                  maxLines = 1
                )
              }
            }

            if (!isConnected) {
              Button(
                onClick = onConnect,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurfsharkTeal),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(30.dp)
              ) {
                Text("Connect", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // 4 Category Tabs: Movies, TV Shows, Live TV, Videos
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            val tabs = listOf(
              Triple(0, "🎬 Movies", countryPkg.movies.size),
              Triple(1, "📺 TV Shows", countryPkg.tvShows.size),
              Triple(2, "📡 Live TV", countryPkg.liveTvChannels.size),
              Triple(3, "▶️ Videos", countryPkg.videos.size)
            )
            tabs.forEach { (index, title, count) ->
              val isSelected = selectedContentTab == index
              Surface(
                onClick = { selectedContentTab = index },
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) SurfsharkTeal else SurfsharkNavy,
                border = BorderStroke(1.dp, if (isSelected) SurfsharkTeal else SurfsharkBorder),
                modifier = Modifier.testTag("country_tab_$index")
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.Black else Color.White
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Box(
                    modifier = Modifier
                      .clip(CircleShape)
                      .background(if (isSelected) Color.Black.copy(alpha = 0.2f) else SurfsharkDarkTeal)
                      .padding(horizontal = 5.dp, vertical = 1.dp)
                  ) {
                    Text(
                      text = "$count",
                      fontSize = 9.sp,
                      fontWeight = FontWeight.ExtraBold,
                      color = if (isSelected) Color.Black else SurfsharkTeal
                    )
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          when (selectedContentTab) {
            0 -> {
              LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                items(countryPkg.movies, key = { it.id }) { movie ->
                  CountryMediaItemCard(
                    item = movie,
                    onClick = { onMediaClick(movie) },
                    onWatch = { launchStreamingUrl(context, movie.watchUrl) }
                  )
                }
              }
            }
            1 -> {
              LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                items(countryPkg.tvShows, key = { it.id }) { show ->
                  CountryMediaItemCard(
                    item = show,
                    onClick = { onMediaClick(show) },
                    onWatch = { launchStreamingUrl(context, show.watchUrl) }
                  )
                }
              }
            }
            2 -> {
              LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                items(countryPkg.liveTvChannels, key = { it.id }) { channel ->
                  CountryLiveChannelCard(
                    channel = channel,
                    onClick = { onChannelClick(channel) },
                    onWatch = { launchStreamingUrl(context, channel.streamUrl) }
                  )
                }
              }
            }
            3 -> {
              LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                items(countryPkg.videos, key = { it.title }) { video ->
                  CountryVideoCard(
                    video = video,
                    onClick = { onVideoClick(video) },
                    onPlay = { launchStreamingUrl(context, video.videoUrl) }
                  )
                }
              }
            }
          }
        }
      }
    }

    // Surfshark Protection Tools & Toggles
    item {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
          text = "SURFSHARK SHIELD SETTINGS",
          fontSize = 12.sp,
          fontWeight = FontWeight.ExtraBold,
          color = Color(0xFFA0A7B8),
          letterSpacing = 0.5.sp
        )

        // CleanWeb Feature
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = SurfsharkCardBg),
          border = BorderStroke(1.dp, SurfsharkBorder)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "CleanWeb™",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(SurfsharkDarkTeal)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "${vpnState.blockedThreats} BLOCKED",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = SurfsharkTeal
                  )
                }
              }
              Text(
                text = "Blocks ads, video popups, trackers, and phishing attempts while streaming",
                fontSize = 11.sp,
                color = Color(0xFFA0A7B8)
              )
            }
            Switch(
              checked = vpnState.cleanWebEnabled,
              onCheckedChange = { onToggleCleanWeb() },
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = SurfsharkTeal,
                uncheckedThumbColor = Color(0xFFA0A7B8),
                uncheckedTrackColor = SurfsharkNavy
              ),
              modifier = Modifier.testTag("vpn_toggle_cleanweb")
            )
          }
        }

        // Kill Switch Feature
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = SurfsharkCardBg),
          border = BorderStroke(1.dp, SurfsharkBorder)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Kill Switch",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = "Disconnects internet traffic automatically if VPN tunnel drops to prevent leaks",
                fontSize = 11.sp,
                color = Color(0xFFA0A7B8)
              )
            }
            Switch(
              checked = vpnState.killSwitchEnabled,
              onCheckedChange = { onToggleKillSwitch() },
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = SurfsharkTeal,
                uncheckedThumbColor = Color(0xFFA0A7B8),
                uncheckedTrackColor = SurfsharkNavy
              ),
              modifier = Modifier.testTag("vpn_toggle_killswitch")
            )
          }
        }
      }
    }
    
    item {
      Spacer(modifier = Modifier.height(20.dp))
    }
  }

  // Location Picker Bottom Sheet
  if (isServerPickerVisible) {
    ModalBottomSheet(
      onDismissRequest = { isServerPickerVisible = false },
      sheetState = sheetState,
      containerColor = SurfsharkNavy
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Select Surfshark VPN Server",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(SurfsharkDarkTeal)
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = "${filteredServers.size} / ${availableServers.size} Countries",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = SurfsharkTeal
            )
          }
        }
        Text(
          text = "Unlocks native Netflix libraries, HBO Max, BBC iPlayer, JioCinema & Live TV broadcasts",
          fontSize = 11.sp,
          color = Color(0xFFA0A7B8),
          modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
        )

        // Search Input
        OutlinedTextField(
          value = serverSearchQuery,
          onValueChange = { serverSearchQuery = it },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("vpn_server_search_input"),
          placeholder = {
            Text("Search 195 countries, cities, or services...", color = Color(0xFFA0A7B8), fontSize = 12.sp)
          },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = SurfsharkTeal,
              modifier = Modifier.size(18.dp)
            )
          },
          trailingIcon = {
            if (serverSearchQuery.isNotBlank()) {
              IconButton(onClick = { serverSearchQuery = "" }) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Clear",
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          },
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SurfsharkCardBg,
            unfocusedContainerColor = SurfsharkCardBg,
            focusedBorderColor = SurfsharkTeal,
            unfocusedBorderColor = SurfsharkBorder,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          ),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Region Filter Pills Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf("All 195", "Popular", "Europe", "Americas", "Asia & Pacific", "Middle East", "Africa").forEach { regionName ->
            val isSelected = selectedRegion.equals(regionName, ignoreCase = true)
            Surface(
              onClick = { selectedRegion = regionName },
              shape = RoundedCornerShape(16.dp),
              color = if (isSelected) SurfsharkTeal else SurfsharkCardBg,
              border = BorderStroke(1.dp, if (isSelected) SurfsharkTeal else SurfsharkBorder),
              modifier = Modifier.testTag("vpn_region_tab_${regionName.lowercase().replace(" ", "_")}")
            ) {
              Text(
                text = regionName,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.Black else Color.White,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
        ) {
          items(filteredServers) { server ->
            val isCurrent = server.id == vpnState.server.id
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  onSelectServer(server)
                  isServerPickerVisible = false
                }
                .testTag("server_item_${server.countryCode}"),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isCurrent) SurfsharkDarkTeal else SurfsharkCardBg
              ),
              border = BorderStroke(
                1.dp,
                if (isCurrent) SurfsharkTeal else SurfsharkBorder
              )
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.weight(1f)
                ) {
                  Text(text = server.flagEmoji, fontSize = 28.sp)
                  Spacer(modifier = Modifier.width(12.dp))
                  Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Text(
                        text = "${server.countryName} (${server.city})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                      )
                      Spacer(modifier = Modifier.width(6.dp))
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(4.dp))
                          .background(SurfsharkNavy)
                          .padding(horizontal = 4.dp, vertical = 1.dp)
                      ) {
                        Text(
                          text = server.region,
                          fontSize = 9.sp,
                          color = SurfsharkTeal
                        )
                      }
                    }
                    Text(
                      text = server.catalogDescription,
                      fontSize = 11.sp,
                      color = if (isCurrent) SurfsharkTeal else Color(0xFFA0A7B8)
                    )
                  }
                }
                Column(horizontalAlignment = Alignment.End) {
                  Text(
                    text = "${server.pingMs}ms",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SurfsharkTeal
                  )
                  Text(
                    text = "Load ${server.serverLoadPercent}%",
                    fontSize = 10.sp,
                    color = Color(0xFFA0A7B8)
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

private fun launchSurfsharkApp(context: Context) {
  try {
    val intent = context.packageManager.getLaunchIntentForPackage("com.surfshark.vpnclient.android")
    if (intent != null) {
      context.startActivity(intent)
    } else {
      val playStoreIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://play.google.com/store/apps/details?id=com.surfshark.vpnclient.android")
      )
      context.startActivity(playStoreIntent)
    }
  } catch (e: Exception) {
    Toast.makeText(context, "Opening Surfshark VPN...", Toast.LENGTH_SHORT).show()
  }
}

private fun launchStreamingUrl(context: Context, url: String) {
  try {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Opening stream...", Toast.LENGTH_SHORT).show()
  }
}

@Composable
private fun CountryMediaItemCard(
  item: MediaItem,
  onClick: () -> Unit,
  onWatch: () -> Unit
) {
  Card(
    modifier = Modifier
      .width(140.dp)
      .clickable { onClick() }
      .testTag("country_media_${item.id}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = SurfsharkCardBg),
    border = BorderStroke(1.dp, SurfsharkBorder)
  ) {
    Column {
      Box(modifier = Modifier.height(110.dp).fillMaxWidth()) {
        AsyncImage(
          model = item.posterUrl,
          contentDescription = item.title,
          modifier = Modifier.fillMaxSize(),
          contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
        // Regional Badge
        Box(
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(SurfsharkTeal)
            .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
          Text(
            text = item.vpnRegionBadge,
            fontSize = 8.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
          )
        }
        // Rating Badge
        Box(
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
          Text(
            text = "★ ${item.rating}",
            fontSize = 9.sp,
            color = Color(0xFFFFD700),
            fontWeight = FontWeight.Bold
          )
        }
      }
      Column(modifier = Modifier.padding(8.dp)) {
        Text(
          text = item.title,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White,
          maxLines = 1
        )
        Text(
          text = "${item.genre} • ${item.year}",
          fontSize = 10.sp,
          color = Color(0xFFA0A7B8),
          maxLines = 1
        )
        Spacer(modifier = Modifier.height(6.dp))
        Button(
          onClick = onWatch,
          shape = RoundedCornerShape(6.dp),
          colors = ButtonDefaults.buttonColors(containerColor = SurfsharkTeal),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(26.dp)
        ) {
          Text("Watch", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
      }
    }
  }
}

@Composable
private fun CountryLiveChannelCard(
  channel: LiveChannel,
  onClick: () -> Unit,
  onWatch: () -> Unit
) {
  Card(
    modifier = Modifier
      .width(160.dp)
      .clickable { onClick() }
      .testTag("country_channel_${channel.id}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = SurfsharkCardBg),
    border = BorderStroke(1.dp, SurfsharkBorder)
  ) {
    Column {
      Box(modifier = Modifier.height(95.dp).fillMaxWidth()) {
        AsyncImage(
          model = channel.logoUrl,
          contentDescription = channel.name,
          modifier = Modifier.fillMaxSize(),
          contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
        // Live indicator
        Box(
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFE50914))
            .padding(horizontal = 5.dp, vertical = 2.dp)
        ) {
          Text(
            text = "● LIVE",
            fontSize = 8.sp,
            color = Color.White,
            fontWeight = FontWeight.Black
          )
        }
        // Resolution
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
            color = SurfsharkTeal,
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
        Spacer(modifier = Modifier.height(6.dp))
        Button(
          onClick = onWatch,
          shape = RoundedCornerShape(6.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(26.dp)
        ) {
          Text("Watch Live", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
      }
    }
  }
}

@Composable
private fun CountryVideoCard(
  video: CuratedTrailer,
  onClick: () -> Unit,
  onPlay: () -> Unit
) {
  Card(
    modifier = Modifier
      .width(180.dp)
      .clickable { onClick() }
      .testTag("country_video_${video.title.hashCode()}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = SurfsharkCardBg),
    border = BorderStroke(1.dp, SurfsharkBorder)
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
            .background(SurfsharkDarkTeal)
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = video.platform.displayName,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = SurfsharkTeal
          )
        }
        Text(
          text = video.duration,
          fontSize = 9.sp,
          color = Color(0xFFA0A7B8)
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = video.title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        maxLines = 2,
        lineHeight = 16.sp
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Button(
          onClick = onPlay,
          shape = RoundedCornerShape(6.dp),
          colors = ButtonDefaults.buttonColors(containerColor = SurfsharkTeal),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp, vertical = 2.dp),
          modifier = Modifier
            .weight(1f)
            .height(26.dp)
        ) {
          Text("Play", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
        OutlinedButton(
          onClick = onClick,
          shape = RoundedCornerShape(6.dp),
          border = BorderStroke(1.dp, SurfsharkTeal),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp, vertical = 2.dp),
          modifier = Modifier
            .weight(1f)
            .height(26.dp)
        ) {
          Text("Analyze", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SurfsharkTeal)
        }
      }
    }
  }
}
