package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.OttCatalog
import com.example.model.CuratedTrailer
import com.example.model.VideoAnalysisResult
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceHighlight

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoAnalyzerView(
  selectedTrailer: CuratedTrailer,
  customVideoUrl: String,
  customVideoTitle: String,
  analysisResult: VideoAnalysisResult?,
  isAnalyzing: Boolean,
  onSelectTrailer: (CuratedTrailer) -> Unit,
  onSetCustomUrl: (String) -> Unit,
  onSetCustomTitle: (String) -> Unit,
  onAnalyze: (customPrompt: String?) -> Unit,
  availableTrailers: List<CuratedTrailer> = OttCatalog.curatedTrailers,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var customFocusPrompt by remember { mutableStateOf("") }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundDark)
      .verticalScroll(rememberScrollState())
      .padding(bottom = 32.dp)
  ) {
    // Model Header Bar
    Surface(
      color = SurfaceDark,
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, BorderSubtle)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(AccentPurple)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Video Content Understanding",
              color = Color.White,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Text(
            text = "Model: gemini-3.1-pro-preview",
            color = AccentPurple,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(SurfaceHighlight)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "AI Video Pro",
            color = AccentGold,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    // Curated Trailer Selector Carousel
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = "Select Video to Analyze",
        color = Color.White,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        availableTrailers.forEach { trailer ->
          val isSelected = trailer.title == selectedTrailer.title
          Card(
            modifier = Modifier
              .width(210.dp)
              .clip(RoundedCornerShape(12.dp))
              .clickable { onSelectTrailer(trailer) }
              .testTag("trailer_card_${trailer.platform.name.lowercase()}"),
            colors = CardDefaults.cardColors(
              containerColor = if (isSelected) SurfaceHighlight else SurfaceElevated
            ),
            border = androidx.compose.foundation.BorderStroke(
              width = if (isSelected) 1.5.dp else 1.dp,
              color = if (isSelected) trailer.platform.brandColor else BorderSubtle
            )
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(trailer.platform.brandColor)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = trailer.platform.shortTag,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                  )
                }

                Text(
                  text = trailer.duration,
                  color = Color(0xFFA0A7B8),
                  fontSize = 10.sp
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = trailer.title,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Custom URL or Title Input
      Text(
        text = "Or Enter Custom Video / Trailer URL",
        color = Color(0xFFC5CBD9),
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold
      )
      Spacer(modifier = Modifier.height(6.dp))

      OutlinedTextField(
        value = customVideoTitle,
        onValueChange = onSetCustomTitle,
        placeholder = { Text("Video Title (e.g. Interstellar Trailer)", color = Color(0xFF6E7687), fontSize = 12.sp) },
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("custom_video_title_input"),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = SurfaceElevated,
          unfocusedContainerColor = SurfaceElevated,
          focusedBorderColor = AccentPurple,
          unfocusedBorderColor = BorderSubtle,
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White
        ),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(6.dp))

      OutlinedTextField(
        value = customVideoUrl,
        onValueChange = onSetCustomUrl,
        placeholder = { Text("https://www.youtube.com/watch?v=...", color = Color(0xFF6E7687), fontSize = 12.sp) },
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("custom_video_url_input"),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = SurfaceElevated,
          unfocusedContainerColor = SurfaceElevated,
          focusedBorderColor = AccentPurple,
          unfocusedBorderColor = BorderSubtle,
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White
        ),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Analyze Button
      Button(
        onClick = { onAnalyze(customFocusPrompt.ifBlank { null }) },
        enabled = !isAnalyzing,
        colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("trigger_video_analysis_button")
      ) {
        if (isAnalyzing) {
          CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            color = Color.White,
            strokeWidth = 2.dp
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(text = "Gemini Pro Analyzing Video...", color = Color.White, fontSize = 14.sp)
        } else {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Analyze Video with Gemini Pro",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Display Video Analysis Results
      if (analysisResult != null) {
        Spacer(modifier = Modifier.height(20.dp))

        Card(
          colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
          shape = RoundedCornerShape(16.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("video_analysis_result_card")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = Color(0xFF22C55E),
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Analysis Complete",
                  color = Color(0xFF22C55E),
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }

              Button(
                onClick = {
                  try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(analysisResult.videoUrl)).apply {
                      flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                  } catch (e: Exception) {
                    // Fallback
                  }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceHighlight),
                modifier = Modifier.height(32.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.PlayArrow,
                  contentDescription = null,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Watch Clip", fontSize = 11.sp)
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = analysisResult.videoTitle,
              color = Color.White,
              fontSize = 18.sp,
              fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Thematic breakdown chips
            Text(
              text = "CORE THEMES & MOTIFS",
              color = AccentGold,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              analysisResult.coreThemes.forEach { theme ->
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceHighlight)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(text = theme, color = Color(0xFFE2E8F0), fontSize = 11.sp)
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Synopsis
            Text(
              text = "SYNOPSIS & CONFLICT",
              color = AccentCyan,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = analysisResult.synopsis,
              color = Color(0xFFC5CBD9),
              fontSize = 13.sp,
              lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tone & Cinematography
            Text(
              text = "TONE, MOOD & PACING",
              color = AccentPurple,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = analysisResult.toneAndMood,
              color = Color(0xFFC5CBD9),
              fontSize = 13.sp,
              lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Acting & Standout performances
            Text(
              text = "PERFORMANCES & DIRECTION",
              color = Color(0xFFF472B6),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = analysisResult.standoutPerformances,
              color = Color(0xFFC5CBD9),
              fontSize = 13.sp,
              lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Age & Advisory
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "CONTENT ADVISORY",
                  color = Color(0xFFFBBF24),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = analysisResult.ageAndContentRating,
                  color = Color.White,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Verdict
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF1E1B4B))
                .border(1.dp, AccentPurple, RoundedCornerShape(10.dp))
                .padding(12.dp)
            ) {
              Column {
                Text(
                  text = "RECOMMENDATION & PLATFORM VERDICT",
                  color = AccentGold,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = analysisResult.audienceVerdict,
                  color = Color.White,
                  fontSize = 13.sp,
                  lineHeight = 18.sp
                )
              }
            }
          }
        }
      }
    }
  }
}
