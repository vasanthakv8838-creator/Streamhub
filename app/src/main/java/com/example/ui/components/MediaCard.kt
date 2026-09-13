package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.model.MediaItem
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceElevated

@Composable
fun MediaCard(
  mediaItem: MediaItem,
  isSavedInWatchlist: Boolean,
  onWatchlistToggle: () -> Unit,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  isVip: Boolean = false
) {
  Column(
    modifier = modifier
      .width(160.dp)
      .clip(RoundedCornerShape(14.dp))
      .background(SurfaceElevated)
      .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
      .clickable { onClick() }
      .testTag("media_card_${mediaItem.id}")
  ) {
    // Poster image with overlaid badges
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(210.dp)
        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
    ) {
      AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(mediaItem.posterUrl)
          .crossfade(true)
          .error(R.drawable.hero_streaming_banner)
          .placeholder(R.drawable.hero_streaming_banner)
          .build(),
        contentDescription = mediaItem.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )

      // Top-to-bottom subtle gradient
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              listOf(Color(0x66000000), Color.Transparent, Color(0xB3000000))
            )
          )
      )

      // OTT Platform badge & VIP Tag (Top Left)
      Row(
        modifier = Modifier
          .align(Alignment.TopStart)
          .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(mediaItem.platform.brandColor)
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = mediaItem.platform.shortTag,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold
          )
        }

        if (isVip) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(Color(0xFFEAB308))
              .padding(horizontal = 5.dp, vertical = 2.dp)
          ) {
            Text(
              text = "👑 VIP",
              color = Color.Black,
              fontSize = 9.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
        }
      }

      // Bookmark button (Top Right)
      IconButton(
        onClick = onWatchlistToggle,
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(2.dp)
          .size(32.dp)
          .clip(CircleShape)
          .background(Color(0x66000000))
          .testTag("bookmark_${mediaItem.id}")
      ) {
        Icon(
          imageVector = if (isSavedInWatchlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
          contentDescription = "Bookmark",
          tint = if (isSavedInWatchlist) AccentGold else Color.White,
          modifier = Modifier.size(16.dp)
        )
      }

      // Rating badge & quality (Bottom)
      Row(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .fillMaxWidth()
          .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xCC000000))
            .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = AccentGold,
            modifier = Modifier.size(11.dp)
          )
          Spacer(modifier = Modifier.width(2.dp))
          Text(
            text = "${mediaItem.rating}",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Text(
          text = "${mediaItem.year}",
          color = Color(0xFFD1D5DB),
          fontSize = 10.sp,
          fontWeight = FontWeight.Medium
        )
      }
    }

    // Title & details
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      Text(
        text = mediaItem.title,
        color = Color.White,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(2.dp))

      Text(
        text = mediaItem.genre,
        color = Color(0xFF94A3B8),
        fontSize = 10.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = mediaItem.duration,
        color = Color(0xFF64748B),
        fontSize = 10.sp
      )
    }
  }
}
