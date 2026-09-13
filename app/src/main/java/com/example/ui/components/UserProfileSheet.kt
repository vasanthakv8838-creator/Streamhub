package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StreamProfile
import com.example.model.UserProfile
import com.example.ui.auth.AddProfileOnlyNameDialog
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceHighlight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileSheet(
  user: UserProfile?,
  profiles: List<StreamProfile>,
  activeProfile: StreamProfile?,
  sheetState: SheetState,
  onDismiss: () -> Unit,
  onSelectProfile: (StreamProfile) -> Unit,
  onAddProfile: (name: String, isKids: Boolean, colorHex: String) -> Unit,
  onOpenWhoIsWatching: () -> Unit,
  onOpenPlans: () -> Unit = {},
  onLogout: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (user == null) return

  var isAddDialogOpen by remember { mutableStateOf(false) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = SurfaceDark,
    contentColor = Color.White,
    modifier = modifier.testTag("user_profile_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
        .padding(bottom = 32.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Current Active Profile Avatar
      val activeColor = try {
        Color(android.graphics.Color.parseColor(activeProfile?.avatarColorHex ?: "#FFB800"))
      } catch (e: Exception) {
        AccentGold
      }

      val activeInitial = (activeProfile?.name ?: user.name)
        .firstOrNull()?.uppercase() ?: "U"

      Box(
        modifier = Modifier
          .size(68.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(activeColor)
          .border(2.dp, Color.White, RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = activeInitial,
          color = Color.Black,
          fontSize = 28.sp,
          fontWeight = FontWeight.ExtraBold
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = activeProfile?.name ?: user.name,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Phone,
          contentDescription = null,
          tint = Color(0xFFA0A7B8),
          modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
          text = "+91 ${user.phoneNumber}",
          fontSize = 12.sp,
          color = Color(0xFFA0A7B8)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF10B981).copy(alpha = 0.2f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = "OTP VERIFIED",
            color = Color(0xFF10B981),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
          )
        }
        if (user.isVip) {
          Spacer(modifier = Modifier.width(6.dp))
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(AccentGold)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = "👑 VIP ALL-ACCESS",
              color = Color.Black,
              fontSize = 9.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // PROFILES SECTION: Add profile with only name / switch profiles
      Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Group,
                contentDescription = null,
                tint = AccentGold,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Profiles (${profiles.size})",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }

            Text(
              text = "Add with only name",
              fontSize = 11.sp,
              color = AccentCyan
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Horizontal list of profiles + Add Profile Button
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            items(profiles) { profile ->
              val isSelected = profile.id == activeProfile?.id
              val pColor = try {
                Color(android.graphics.Color.parseColor(profile.avatarColorHex))
              } catch (e: Exception) {
                AccentGold
              }

              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                  .clickable {
                    onSelectProfile(profile)
                    onDismiss()
                  }
                  .padding(4.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(pColor)
                    .border(
                      width = if (isSelected) 2.dp else 1.dp,
                      color = if (isSelected) Color.White else Color.Transparent,
                      shape = RoundedCornerShape(14.dp)
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = profile.name.firstOrNull()?.uppercase() ?: "P",
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                  )

                  if (isSelected) {
                    Box(
                      modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(AccentGold)
                        .align(Alignment.BottomEnd),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = BackgroundDark,
                        modifier = Modifier.size(12.dp)
                      )
                    }
                  }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                  text = profile.name,
                  color = if (isSelected) Color.White else Color(0xFFA0A7B8),
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  modifier = Modifier.width(60.dp),
                  textAlign = TextAlign.Center
                )
              }
            }

            // Inline "+ Add Profile" item
            item {
              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                  .clickable { isAddDialogOpen = true }
                  .padding(4.dp)
                  .testTag("sheet_add_profile_button")
              ) {
                Box(
                  modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceDark)
                    .border(
                      width = 1.dp,
                      color = AccentCyan.copy(alpha = 0.8f),
                      shape = RoundedCornerShape(14.dp)
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Profile",
                    tint = AccentCyan,
                    modifier = Modifier.size(24.dp)
                  )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                  text = "+ Add",
                  color = AccentCyan,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  textAlign = TextAlign.Center
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Switch / Manage Profiles button
          OutlinedButton(
            onClick = {
              onDismiss()
              onOpenWhoIsWatching()
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(40.dp)
              .testTag("sheet_who_is_watching_button"),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
            shape = RoundedCornerShape(10.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.SwitchAccount,
                contentDescription = null,
                tint = AccentGold,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Who's Watching Screen",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Status Card
      Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = AccentCyan,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Streamer Account Tier",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
              )
            }
            Text(
              text = "VIP Pass",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = AccentGold
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(
              imageVector = Icons.Default.Tv,
              contentDescription = null,
              tint = AccentGold,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Connected: YouTube • Hotstar • Apple TV+ • Netflix • ZEE5 • Sony LIV • Prime",
              fontSize = 10.sp,
              color = Color(0xFFA0A7B8),
              lineHeight = 14.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // View All OTT Subscription Plans button
      OutlinedButton(
        onClick = {
          onDismiss()
          onOpenPlans()
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp)
          .testTag("sheet_view_plans_button"),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(12.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.CardMembership,
            contentDescription = null,
            tint = AccentGold,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (user?.isVip == true) "👑 VIP All-Access Active • View Plans" else "View All OTT Plans (Free VIP Tier)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = AccentGold
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Logout Button
      Button(
        onClick = {
          onLogout()
          onDismiss()
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(46.dp)
          .testTag("logout_button"),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFFEF4444).copy(alpha = 0.15f),
          contentColor = Color(0xFFEF4444)
        ),
        shape = RoundedCornerShape(12.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Logout,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Log Out / Switch Account",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }

  // Dialog to Add Profile (Only Name required)
  if (isAddDialogOpen) {
    AddProfileOnlyNameDialog(
      onDismiss = { isAddDialogOpen = false },
      onConfirm = { name, isKids, colorHex ->
        onAddProfile(name, isKids, colorHex)
        isAddDialogOpen = false
      }
    )
  }
}
