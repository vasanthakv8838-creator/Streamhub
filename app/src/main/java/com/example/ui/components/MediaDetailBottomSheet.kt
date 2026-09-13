package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.model.CuratedTrailer
import com.example.model.MediaItem
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MediaDetailBottomSheet(
  mediaItem: MediaItem,
  isSavedInWatchlist: Boolean,
  sheetState: SheetState,
  onDismiss: () -> Unit,
  onWatchlistToggle: () -> Unit,
  onAnalyzeWithGemini: (CuratedTrailer) -> Unit,
  modifier: Modifier = Modifier,
  isVip: Boolean = false
) {
  val context = LocalContext.current

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = SurfaceDark,
    dragHandle = null,
    modifier = modifier.testTag("media_detail_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
    ) {
      // Top Backdrop Image Header
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(220.dp)
      ) {
        AsyncImage(
          model = ImageRequest.Builder(context)
            .data(mediaItem.backdropUrl)
            .crossfade(true)
            .error(R.drawable.hero_streaming_banner)
            .placeholder(R.drawable.hero_streaming_banner)
            .build(),
          contentDescription = mediaItem.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )

        // Gradient fade into sheet background
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                listOf(Color(0x33000000), Color(0x66000000), SurfaceDark)
              )
            )
        )

        // Close button
        IconButton(
          onClick = onDismiss,
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(12.dp)
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0x99000000))
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color.White
          )
        }

        // Platform Brand Pill & Quality Badge
        Row(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(horizontal = 16.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(mediaItem.platform.brandColor)
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = mediaItem.platform.displayName,
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(Color(0x80000000))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = mediaItem.qualityBadge,
              color = Color(0xFFE2E8F0),
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      // Title & Key Metadata
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        Text(
          text = mediaItem.title,
          color = Color.White,
          fontSize = 24.sp,
          fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(SurfaceElevated)
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = null,
              tint = AccentGold,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${mediaItem.rating}/10",
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "(${mediaItem.votes})",
              color = Color(0xFFA0A7B8),
              fontSize = 11.sp
            )
          }

          Text(
            text = "${mediaItem.year} • ${mediaItem.duration}",
            color = Color(0xFFC5CBD9),
            fontSize = 12.sp
          )

          Box(
            modifier = Modifier
              .border(1.dp, BorderSubtle, RoundedCornerShape(4.dp))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = mediaItem.contentAdvisory,
              color = Color(0xFF94A3B8),
              fontSize = 10.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (isVip) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0x33EAB308))
              .border(1.dp, Color(0x66EAB308), RoundedCornerShape(10.dp))
              .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "👑", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "VIP All-Access Tier Active",
                color = AccentGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Free full stream access across all 7 streaming apps.",
                color = Color(0xFFD1D5DB),
                fontSize = 11.sp
              )
            }
          }
          Spacer(modifier = Modifier.height(10.dp))
        }

        // Main CTA: Watch on Platform Button
        Button(
          onClick = {
            launchUrlIntent(context, mediaItem.watchUrl)
          },
          colors = ButtonDefaults.buttonColors(containerColor = mediaItem.platform.brandColor),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("launch_ott_platform_button")
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isVip) "Stream on ${mediaItem.platform.displayName} (VIP)" else "Watch on ${mediaItem.platform.displayName}",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(6.dp))
          Icon(
            imageVector = Icons.Default.OpenInNew,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Secondary Action Row: Trailer, Watchlist, Gemini Video Analyzer
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = {
              launchUrlIntent(context, mediaItem.trailerUrl)
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Trailer", fontSize = 13.sp)
          }

          OutlinedButton(
            onClick = onWatchlistToggle,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              contentColor = if (isSavedInWatchlist) AccentGold else Color.White
            ),
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
          ) {
            Icon(
              imageVector = if (isSavedInWatchlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = null,
              tint = if (isSavedInWatchlist) AccentGold else Color.White,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isSavedInWatchlist) "Saved" else "Watchlist",
              fontSize = 13.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Gemini Video Analyzer Button
        Button(
          onClick = {
            onAnalyzeWithGemini(
              CuratedTrailer(
                title = "${mediaItem.title} - Official Video Preview",
                platform = mediaItem.platform,
                duration = mediaItem.duration,
                videoUrl = mediaItem.trailerUrl,
                defaultPrompt = "Analyze ${mediaItem.title} for key plot conflicts, tone, cast performances, and viewer suitability."
              )
            )
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("analyze_with_gemini_button")
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Analyze Trailer with Gemini Pro",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Synopsis
        Text(
          text = "Synopsis",
          color = Color.White,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = mediaItem.synopsis,
          color = Color(0xFFC5CBD9),
          fontSize = 13.sp,
          lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Cast Chips
        Text(
          text = "Cast & Crew",
          color = Color.White,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          mediaItem.cast.forEach { actor ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceElevated)
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = actor,
                color = Color(0xFFE2E8F0),
                fontSize = 12.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Directed by: ${mediaItem.director}",
          color = Color(0xFFA0A7B8),
          fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(30.dp))
      }
    }
  }
}

private fun launchUrlIntent(context: Context, url: String) {
  try {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "Opening streaming destination...", Toast.LENGTH_SHORT).show()
  }
}
