package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.model.WatchlistEntity
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceHighlight

@Composable
fun WatchlistView(
  watchlist: List<WatchlistEntity>,
  onToggleWatched: (WatchlistEntity) -> Unit,
  onDelete: (String) -> Unit,
  onExploreClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundDark)
  ) {
    // Header
    Surface(
      color = SurfaceDark,
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, BorderSubtle)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Unified Watchlist",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "${watchlist.size} saved titles across all OTTs",
            color = Color(0xFFA0A7B8),
            fontSize = 12.sp
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceHighlight)
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = "Room DB Synced",
            color = AccentGold,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    if (watchlist.isEmpty()) {
      // Empty state
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Box(
            modifier = Modifier
              .size(72.dp)
              .clip(CircleShape)
              .background(SurfaceElevated),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.BookmarkBorder,
              contentDescription = null,
              tint = AccentGold,
              modifier = Modifier.size(36.dp)
            )
          }
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "Your Watchlist is Empty",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Tap the bookmark icon on any movie or series across YouTube, JioHotstar, Apple TV+, Netflix, ZEE5, Sony LIV, or Prime Video to save it here.",
            color = Color(0xFFA0A7B8),
            fontSize = 13.sp,
            lineHeight = 18.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(watchlist, key = { it.mediaId }) { item ->
          WatchlistCard(
            item = item,
            onToggleWatched = { onToggleWatched(item) },
            onDelete = { onDelete(item.mediaId) },
            onLaunch = {
              try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.watchUrl)).apply {
                  flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
              } catch (e: Exception) {
                // Fallback
              }
            }
          )
        }
      }
    }
  }
}

@Composable
fun WatchlistCard(
  item: WatchlistEntity,
  onToggleWatched: () -> Unit,
  onDelete: () -> Unit,
  onLaunch: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("watchlist_item_${item.mediaId}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Thumbnail
      Box(
        modifier = Modifier
          .size(60.dp, 80.dp)
          .clip(RoundedCornerShape(8.dp))
      ) {
        AsyncImage(
          model = ImageRequest.Builder(LocalContext.current)
            .data(item.posterUrl)
            .crossfade(true)
            .error(R.drawable.hero_streaming_banner)
            .placeholder(R.drawable.hero_streaming_banner)
            .build(),
          contentDescription = item.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Metadata
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(SurfaceHighlight)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = item.platformName,
              color = AccentGold,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${item.year} • ${item.genre.split("•").firstOrNull()?.trim() ?: ""}",
            color = Color(0xFFA0A7B8),
            fontSize = 11.sp
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = item.title,
          color = if (item.isWatched) Color(0xFF94A3B8) else Color.White,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          textDecoration = if (item.isWatched) TextDecoration.LineThrough else TextDecoration.None,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = AccentGold,
            modifier = Modifier.size(12.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "${item.rating}",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Actions: Watch Now, Checkbox, Delete
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        // Watch on OTT button
        IconButton(
          onClick = onLaunch,
          modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(SurfaceHighlight)
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Watch",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }

        // Toggle Watched status
        IconButton(
          onClick = onToggleWatched,
          modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(if (item.isWatched) Color(0xFF16A34A) else SurfaceHighlight)
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Mark as Watched",
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
        }

        // Delete from watchlist
        IconButton(
          onClick = onDelete,
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }
}
