package com.example.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.model.MediaItem
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BackgroundDark

@Composable
fun FeaturedHeroBanner(
  mediaItem: MediaItem,
  isSavedInWatchlist: Boolean,
  onWatchlistToggle: () -> Unit,
  onItemClick: () -> Unit,
  onWatchClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(230.dp)
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .clip(RoundedCornerShape(18.dp))
      .clickable { onItemClick() }
      .testTag("featured_hero_banner")
  ) {
    // Backdrop background with local hero fallback
    AsyncImage(
      model = ImageRequest.Builder(LocalContext.current)
        .data(mediaItem.backdropUrl)
        .crossfade(true)
        .error(R.drawable.hero_streaming_banner)
        .placeholder(R.drawable.hero_streaming_banner)
        .build(),
      contentDescription = mediaItem.title,
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize()
    )

    // Gradient overlay for readability
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color.Transparent,
              Color(0x99000000),
              BackgroundDark
            ),
            startY = 50f
          )
        )
    )

    // Content inside banner
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(14.dp),
      verticalArrangement = Arrangement.Bottom
    ) {
      // Platform badge & rating
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(mediaItem.platform.brandColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = mediaItem.platform.displayName,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0x80000000))
            .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Rating",
            tint = AccentGold,
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "${mediaItem.rating}",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Text(
          text = mediaItem.genre,
          color = Color(0xFFD1D5DB),
          fontSize = 11.sp,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = mediaItem.title,
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.ExtraBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Text(
        text = mediaItem.synopsis,
        color = Color(0xFFA0A7B8),
        fontSize = 12.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(top = 2.dp)
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Action buttons: Watch, Details, Watchlist
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Button(
          onClick = onWatchClick,
          colors = ButtonDefaults.buttonColors(containerColor = mediaItem.platform.brandColor),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .weight(1f)
            .height(38.dp)
            .testTag("hero_watch_button")
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Watch on ${mediaItem.platform.shortTag}",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
          )
        }

        OutlinedButton(
          onClick = onItemClick,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
          modifier = Modifier.height(38.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Details",
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "Info", fontSize = 12.sp)
        }

        Surface(
          onClick = onWatchlistToggle,
          shape = CircleShape,
          color = Color(0x66000000),
          modifier = Modifier.size(38.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = if (isSavedInWatchlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = "Watchlist",
              tint = if (isSavedInWatchlist) AccentGold else Color.White,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }
  }
}
