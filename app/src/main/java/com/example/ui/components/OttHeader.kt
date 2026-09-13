package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StreamProfile
import com.example.model.UserProfile
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated

@Composable
fun OttHeader(
  searchQuery: String,
  onSearchQueryChanged: (String) -> Unit,
  onVoiceClick: () -> Unit,
  onVideoAnalyzerClick: () -> Unit,
  modifier: Modifier = Modifier,
  userProfile: UserProfile? = null,
  activeProfile: StreamProfile? = null,
  onProfileClick: () -> Unit = {},
  onPlansClick: () -> Unit = {}
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(BackgroundDark)
      .padding(horizontal = 16.dp, vertical = 10.dp)
  ) {
    // Title row with Brand & Quick Action shortcuts
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
              Brush.linearGradient(
                listOf(Color(0xFFE50914), Color(0xFF00A8E1), Color(0xFFFFB800))
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = "OTT Logo",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "OTT Aggregator",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            letterSpacing = 0.5.sp
          )
          Text(
            text = "7 Streaming Giants Unified",
            fontSize = 11.sp,
            color = AccentGold,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      // Action buttons: Voice AI & Video Analysis
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Surface(
          onClick = onVoiceClick,
          shape = CircleShape,
          color = SurfaceElevated,
          modifier = Modifier.testTag("header_voice_button")
        ) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .padding(2.dp),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Mic,
              contentDescription = "Voice Assistant",
              tint = AccentCyan,
              modifier = Modifier.size(20.dp)
            )
          }
        }

        Surface(
          onClick = onVideoAnalyzerClick,
          shape = CircleShape,
          color = SurfaceElevated,
          modifier = Modifier.testTag("header_video_analyzer_button")
        ) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .padding(2.dp),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.VideoLibrary,
              contentDescription = "Video Analyzer",
              tint = AccentPurple,
              modifier = Modifier.size(20.dp)
            )
          }
        }

        // OTT Platform Plans & Pricing Button (Next to Profile)
        Surface(
          onClick = onPlansClick,
          shape = CircleShape,
          color = SurfaceElevated,
          border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
          modifier = Modifier.testTag("header_plans_button")
        ) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .padding(2.dp),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.CardMembership,
              contentDescription = "Subscription Plans",
              tint = AccentGold,
              modifier = Modifier.size(20.dp)
            )
          }
        }

        // User Profile Avatar Button
        val avatarBgColor = try {
          if (activeProfile != null) {
            Color(android.graphics.Color.parseColor(activeProfile.avatarColorHex))
          } else if (userProfile != null) {
            AccentGold
          } else {
            SurfaceElevated
          }
        } catch (e: Exception) {
          AccentGold
        }

        Box(contentAlignment = Alignment.TopEnd) {
          Surface(
            onClick = onProfileClick,
            shape = CircleShape,
            color = avatarBgColor,
            border = if (userProfile?.isVip == true) {
              androidx.compose.foundation.BorderStroke(1.5.dp, AccentGold)
            } else null,
            modifier = Modifier.testTag("header_profile_button")
          ) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .padding(2.dp),
              contentAlignment = Alignment.Center
            ) {
              val displayName = activeProfile?.name ?: userProfile?.name
              if (displayName != null) {
                val initial = displayName.firstOrNull()?.uppercase() ?: "U"
                Text(
                  text = initial,
                  color = Color.Black,
                  fontWeight = FontWeight.ExtraBold,
                  fontSize = 15.sp
                )
              } else {
                Icon(
                  imageVector = Icons.Default.AutoAwesome,
                  contentDescription = "Profile",
                  tint = Color.White,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }

          if (userProfile?.isVip == true) {
            Box(
              modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(AccentGold)
                .border(1.dp, Color.Black, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "👑",
                fontSize = 8.sp,
                lineHeight = 8.sp
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Universal Search Bar across all 7 platforms
    OutlinedTextField(
      value = searchQuery,
      onValueChange = onSearchQueryChanged,
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("universal_search_input"),
      placeholder = {
        Text(
          text = "Search Netflix, Hotstar, Prime, YouTube...",
          color = Color(0xFF6E7687),
          fontSize = 13.sp
        )
      },
      leadingIcon = {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = "Search",
          tint = Color(0xFFA0A7B8),
          modifier = Modifier.size(20.dp)
        )
      },
      trailingIcon = {
        if (searchQuery.isNotBlank()) {
          IconButton(
            onClick = { onSearchQueryChanged("") },
            modifier = Modifier.size(24.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Clear",
              tint = Color(0xFFA0A7B8)
            )
          }
        }
      },
      shape = RoundedCornerShape(14.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = SurfaceDark,
        unfocusedContainerColor = SurfaceDark,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = BorderSubtle,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White
      ),
      singleLine = true
    )
  }
}
