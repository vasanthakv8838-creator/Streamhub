package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MediaType
import com.example.model.OttPlatform
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated

@Composable
fun PlatformSelectorRow(
  selectedPlatform: OttPlatform,
  onPlatformSelected: (OttPlatform) -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Row(
    modifier = modifier
      .fillMaxWidth()
      .horizontalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    OttPlatform.values().forEach { platform ->
      val isSelected = platform == selectedPlatform
      val animatedBg by animateColorAsState(
        targetValue = if (isSelected) platform.badgeBgColor else SurfaceDark,
        label = "chipBg"
      )
      val animatedBorder by animateColorAsState(
        targetValue = if (isSelected) platform.brandColor else BorderSubtle,
        label = "chipBorder"
      )

      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .background(animatedBg)
          .border(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = animatedBorder,
            shape = RoundedCornerShape(20.dp)
          )
          .clickable { onPlatformSelected(platform) }
          .padding(horizontal = 14.dp, vertical = 8.dp)
          .testTag("platform_chip_${platform.name.lowercase()}"),
        contentAlignment = Alignment.Center
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Color indicator dot
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(platform.brandColor)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = platform.displayName,
            color = if (isSelected) Color.White else Color(0xFFC5CBD9),
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
          )
        }
      }
    }
  }
}

@Composable
fun MediaTypeChipsRow(
  selectedType: MediaType,
  onTypeSelected: (MediaType) -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Row(
    modifier = modifier
      .fillMaxWidth()
      .horizontalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 4.dp),
    horizontalArrangement = Arrangement.spacedBy(6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    MediaType.values().forEach { type ->
      val isSelected = type == selectedType
      val isLiveCategory = type == MediaType.NEWS || type == MediaType.SPORTS
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(if (isSelected) MaterialTheme.colorScheme.primary else SurfaceElevated)
          .clickable { onTypeSelected(type) }
          .padding(horizontal = 12.dp, vertical = 6.dp)
          .testTag("type_chip_${type.name.lowercase()}"),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          if (isLiveCategory) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color(0xFFD32F2F) else Color(0xFFFF5252))
            )
          }
          Text(
            text = type.label,
            color = if (isSelected) Color.Black else Color(0xFFA0A7B8),
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
          )
        }
      }
    }
  }
}
