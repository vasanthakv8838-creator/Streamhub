package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.automirrored.filled.ScreenShare
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SurroundSound
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OttPlatform
import com.example.model.PlatformPlan
import com.example.model.PlatformPlansRepository
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceHighlight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatformPlansSheet(
  sheetState: SheetState,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  initialPlatform: OttPlatform = OttPlatform.ALL,
  isVip: Boolean = false,
  onActivateVip: () -> Unit = {}
) {
  val context = LocalContext.current
  var selectedPlatform by remember { mutableStateOf(initialPlatform) }
  var searchQuery by remember { mutableStateOf("") }

  val platformsList = listOf(
    OttPlatform.ALL,
    OttPlatform.NETFLIX,
    OttPlatform.PRIME_VIDEO,
    OttPlatform.JIO_HOTSTAR,
    OttPlatform.YOUTUBE,
    OttPlatform.APPLE_TV,
    OttPlatform.SONY_LIV,
    OttPlatform.ZEE5
  )

  val filteredPlans = remember(selectedPlatform, searchQuery) {
    PlatformPlansRepository.allPlans.filter { plan ->
      val matchesPlatform = selectedPlatform == OttPlatform.ALL || plan.platform == selectedPlatform
      val query = searchQuery.trim().lowercase()
      val matchesSearch = query.isEmpty() ||
        plan.planName.lowercase().contains(query) ||
        plan.platform.displayName.lowercase().contains(query) ||
        plan.resolution.lowercase().contains(query) ||
        plan.priceDisplay.lowercase().contains(query) ||
        plan.devicesSupported.lowercase().contains(query)
      matchesPlatform && matchesSearch
    }
  }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = BackgroundDark,
    contentColor = Color.White,
    dragHandle = null,
    modifier = modifier
      .fillMaxHeight(0.92f)
      .testTag("platform_plans_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(top = 16.dp)
    ) {
      // Header Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
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
                  listOf(Color(0xFFFFB800), Color(0xFFE50914), Color(0xFF00A8E1))
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.CardMembership,
              contentDescription = "Plans",
              tint = Color.Black,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = "OTT Subscription Plans",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Pricing, 4K resolution & screen limits across 7 platforms",
              fontSize = 11.sp,
              color = Color(0xFFA0A7B8)
            )
          }
        }

        IconButton(
          onClick = onDismiss,
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(SurfaceElevated)
            .testTag("close_plans_sheet_button")
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Search bar
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = {
          Text(
            "Search plans (e.g. 4K, annual, mobile, family)...",
            fontSize = 12.sp,
            color = Color(0xFF8E95A5)
          )
        },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = AccentGold,
            modifier = Modifier.size(18.dp)
          )
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Clear",
                tint = Color(0xFFA0A7B8),
                modifier = Modifier.size(16.dp)
              )
            }
          }
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = SurfaceDark,
          unfocusedContainerColor = SurfaceDark,
          focusedBorderColor = AccentGold,
          unfocusedBorderColor = BorderSubtle,
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp)
          .height(50.dp)
          .testTag("plans_search_field")
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Platform selector chips
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState())
          .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        platformsList.forEach { platform ->
          val isSelected = selectedPlatform == platform
          val chipBg = if (isSelected) {
            platform.brandColor.copy(alpha = 0.25f)
          } else {
            SurfaceDark
          }
          val borderColor = if (isSelected) platform.brandColor else BorderSubtle

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(chipBg)
              .border(1.dp, borderColor, RoundedCornerShape(20.dp))
              .clickable { selectedPlatform = platform }
              .padding(horizontal = 14.dp, vertical = 7.dp)
              .testTag("plan_filter_${platform.name.lowercase()}")
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              if (platform != OttPlatform.ALL) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(platform.brandColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
              }
              Text(
                text = platform.displayName,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFFA0A7B8)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Plans Count & Status text
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "${filteredPlans.size} Plans Available",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFFA0A7B8)
        )
        Text(
          text = if (selectedPlatform == OttPlatform.ALL) "Showing All Platforms" else selectedPlatform.displayName,
          fontSize = 11.sp,
          color = AccentGold,
          fontWeight = FontWeight.SemiBold
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Plans List
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        items(filteredPlans, key = { it.id }) { plan ->
          PlanCard(
            plan = plan,
            onSubscribeClick = {
              launchWebUrl(context, plan.subscribeUrl)
            }
          )
        }

        // VIP TIER: 100% FREE AND GIVES ACCESS TO ALL PLATFORMS (AT THE BOTTOM OF THE PLANS SECTION)
        item(key = "bottom_vip_tier_section") {
          VipTierCard(
            vipPlan = PlatformPlansRepository.vipPlan,
            isVipActive = isVip,
            onActivateVip = onActivateVip
          )
        }
      }
    }
  }
}

@Composable
fun PlanCard(
  plan: PlatformPlan,
  onSubscribeClick: () -> Unit
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
    shape = RoundedCornerShape(18.dp),
    border = androidx.compose.foundation.BorderStroke(
      width = if (plan.isBestValue) 1.5.dp else 1.dp,
      color = if (plan.isBestValue) AccentGold.copy(alpha = 0.8f) else BorderSubtle
    ),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("plan_card_${plan.id}")
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Platform badge & Highlighting Tags
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Platform Label
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(plan.platform.badgeBgColor)
            .border(1.dp, plan.platform.brandColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = plan.platform.displayName.uppercase(),
            color = plan.platform.brandColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp
          )
        }

        // Value Badges
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          if (plan.isBestValue) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(
                  Brush.horizontalGradient(listOf(Color(0xFFFFB800), Color(0xFFF59E0B)))
                )
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = null,
                  tint = Color.Black,
                  modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = "BEST VALUE",
                  color = Color.Black,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Black
                )
              }
            }
          }

          if (plan.isPopular) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(AccentCyan.copy(alpha = 0.25f))
                .border(1.dp, AccentCyan, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = "POPULAR",
                color = AccentCyan,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Plan Name & Price Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = plan.planName,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
          )
          Text(
            text = plan.devicesSupported,
            fontSize = 12.sp,
            color = Color(0xFFA0A7B8)
          )
        }

        // Price display
        Row(verticalAlignment = Alignment.Bottom) {
          Text(
            text = plan.priceDisplay,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = if (plan.isBestValue) AccentGold else Color.White
          )
          Text(
            text = plan.period,
            fontSize = 12.sp,
            color = Color(0xFFA0A7B8),
            modifier = Modifier.padding(bottom = 3.dp, start = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Key Specs Grid / Row (Screens, Resolution, Audio, Ads)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(SurfaceElevated)
          .padding(vertical = 10.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Screens Spec
        SpecItem(
          icon = Icons.AutoMirrored.Filled.ScreenShare,
          label = "Screens",
          value = "${plan.screensCount} ${if (plan.screensCount > 1) "Active" else "Device"}"
        )

        // Resolution Spec
        SpecItem(
          icon = Icons.Default.HighQuality,
          label = "Quality",
          value = plan.resolution.split("+").first().trim()
        )

        // Audio Spec
        SpecItem(
          icon = Icons.Default.SurroundSound,
          label = "Audio",
          value = plan.audioQuality.split("&").first().trim()
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Perks Checklist
      Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        plan.perks.forEach { perk ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(Color(0xFF10B981).copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF10B981),
                modifier = Modifier.size(11.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = perk,
              fontSize = 12.sp,
              color = Color(0xFFD1D5DB),
              lineHeight = 16.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Action Button to Subscribe / Open Official Platform
      Button(
        onClick = onSubscribeClick,
        colors = ButtonDefaults.buttonColors(
          containerColor = if (plan.isBestValue) AccentGold else SurfaceHighlight,
          contentColor = if (plan.isBestValue) Color.Black else Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp)
          .testTag("subscribe_button_${plan.id}")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(
            text = "View Plan on ${plan.platform.displayName}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(6.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
            contentDescription = null,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun SpecItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  value: String
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = AccentGold,
      modifier = Modifier.size(16.dp)
    )
    Spacer(modifier = Modifier.height(3.dp))
    Text(
      text = value,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
    Text(
      text = label,
      fontSize = 9.sp,
      color = Color(0xFFA0A7B8)
    )
  }
}

private fun launchWebUrl(context: Context, url: String) {
  try {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
  } catch (e: Exception) {
    // Fallback gracefully
  }
}

@Composable
fun VipTierCard(
  vipPlan: PlatformPlan,
  isVipActive: Boolean,
  onActivateVip: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showCelebrationMessage by remember { mutableStateOf(false) }

  Column(modifier = modifier.fillMaxWidth()) {
    // Section Header / Divider at bottom of plans
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp, bottom = 14.dp)
    ) {
      Box(
        modifier = Modifier
          .weight(1f)
          .height(1.dp)
          .background(
            Brush.horizontalGradient(
              listOf(Color.Transparent, AccentGold.copy(alpha = 0.5f))
            )
          )
      )
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .background(Color(0xFF261D12))
          .border(1.dp, AccentGold.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
          .padding(horizontal = 14.dp, vertical = 6.dp)
      ) {
        Icon(
          imageVector = Icons.Default.WorkspacePremium,
          contentDescription = null,
          tint = AccentGold,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "ALL-PLATFORMS VIP PASS",
          fontSize = 11.sp,
          fontWeight = FontWeight.ExtraBold,
          color = AccentGold,
          letterSpacing = 0.8.sp
        )
      }
      Box(
        modifier = Modifier
          .weight(1f)
          .height(1.dp)
          .background(
            Brush.horizontalGradient(
              listOf(AccentGold.copy(alpha = 0.5f), Color.Transparent)
            )
          )
      )
    }

    // VIP Card Container
    Card(
      colors = CardDefaults.cardColors(
        containerColor = Color(0xFF13111C)
      ),
      shape = RoundedCornerShape(22.dp),
      border = androidx.compose.foundation.BorderStroke(
        width = 2.dp,
        brush = Brush.linearGradient(
          listOf(
            Color(0xFFFFB800), // Gold
            Color(0xFF10B981), // Emerald
            Color(0xFF00A8E1), // Cyan
            Color(0xFF8B5CF6)  // Purple
          )
        )
      ),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("vip_tier_card")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              listOf(
                Color(0xFF231935),
                Color(0xFF13111C)
              )
            )
          )
          .padding(20.dp)
      ) {
        // Badges Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // VIP Gold Tag
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(
                Brush.horizontalGradient(
                  listOf(Color(0xFFFFB800), Color(0xFFF59E0B))
                )
              )
              .padding(horizontal = 10.dp, vertical = 5.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "VIP TIER",
                color = Color.Black,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
              )
            }
          }

          // 100% FREE Tag
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF10B981).copy(alpha = 0.2f))
              .border(1.dp, Color(0xFF10B981), RoundedCornerShape(8.dp))
              .padding(horizontal = 10.dp, vertical = 5.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF10B981),
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "100% FREE",
                color = Color(0xFF10B981),
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Title and Subtitle
        Text(
          text = "VIP All-Access Pass",
          fontSize = 22.sp,
          fontWeight = FontWeight.Black,
          color = Color.White
        )
        Text(
          text = "Unrestricted free access to all 7 streaming services in one unified tier",
          fontSize = 12.sp,
          color = Color(0xFFD1D5DB),
          modifier = Modifier.padding(top = 2.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Price Section
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1F1A2E))
            .padding(horizontal = 14.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "TOTAL SUBSCRIPTION COST",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFFA0A7B8)
            )
            Row(verticalAlignment = Alignment.Bottom) {
              Text(
                text = "FREE",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF10B981)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "₹0 Lifetime",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = AccentGold,
                modifier = Modifier.padding(bottom = 3.dp)
              )
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF374151))
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text(
              text = "SAVE 100%",
              color = Color(0xFFE5E7EB),
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // All 7 Platforms Included Section
        Text(
          text = "Gives Unlocked Access to All Platforms:",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = AccentGold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Platform Badges
        val allPlatforms = listOf(
          OttPlatform.NETFLIX,
          OttPlatform.PRIME_VIDEO,
          OttPlatform.JIO_HOTSTAR,
          OttPlatform.YOUTUBE,
          OttPlatform.APPLE_TV,
          OttPlatform.SONY_LIV,
          OttPlatform.ZEE5
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          allPlatforms.forEach { platform ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(platform.badgeBgColor)
                .border(1.dp, platform.brandColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = platform.brandColor,
                  modifier = Modifier.size(11.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = platform.displayName,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = platform.brandColor
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Specs Highlights Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A162B))
            .padding(vertical = 10.dp, horizontal = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          SpecItem(
            icon = Icons.AutoMirrored.Filled.ScreenShare,
            label = "Screens",
            value = "Unlimited"
          )
          SpecItem(
            icon = Icons.Default.HighQuality,
            label = "Resolution",
            value = "4K UHD"
          )
          SpecItem(
            icon = Icons.Default.SurroundSound,
            label = "Audio",
            value = "Dolby Atmos"
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Perks Checklist
        Column(
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          vipPlan.perks.forEach { perk ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.fillMaxWidth()
            ) {
              Box(
                modifier = Modifier
                  .size(18.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF10B981).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = Color(0xFF10B981),
                  modifier = Modifier.size(12.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = perk,
                fontSize = 12.sp,
                color = Color(0xFFE5E7EB),
                lineHeight = 16.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // VIP Activation Feedback
        if (showCelebrationMessage || isVipActive) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFF10B981).copy(alpha = 0.15f))
              .border(1.dp, Color(0xFF10B981).copy(alpha = 0.6f), RoundedCornerShape(12.dp))
              .padding(12.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF10B981),
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "VIP Tier Active! Unrestricted access enabled for all 7 platforms.",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFE5E7EB)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))
        }

        // Action Button: Activate or Active indicator
        if (isVipActive) {
          Button(
            onClick = {
              showCelebrationMessage = true
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFF10B981),
              contentColor = Color.Black
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("vip_active_button")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "VIP ALL-ACCESS ACTIVE",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black
              )
            }
          }
        } else {
          Button(
            onClick = {
              onActivateVip()
              showCelebrationMessage = true
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = AccentGold,
              contentColor = Color.Black
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("claim_vip_button")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "CLAIM FREE VIP ACCESS (ALL PLATFORMS)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black
              )
            }
          }
        }
      }
    }
  }
}
