package com.example.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserSessionManager
import com.example.model.StreamProfile
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceHighlight

@Composable
fun ProfileSelectionScreen(
  profiles: List<StreamProfile>,
  activeProfile: StreamProfile?,
  onSelectProfile: (StreamProfile) -> Unit,
  onAddProfile: (name: String, isKids: Boolean, colorHex: String) -> Unit,
  onDeleteProfile: (StreamProfile) -> Unit,
  onSignOut: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isAddDialogOpen by remember { mutableStateOf(false) }
  var isManageMode by remember { mutableStateOf(false) }
  var profileToDelete by remember { mutableStateOf<StreamProfile?>(null) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundDark)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp, vertical = 32.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      // Header Branding Badge
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(
            Brush.linearGradient(
              listOf(Color(0xFFE50914), Color(0xFF00A8E1), Color(0xFFFFB800))
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Face,
          contentDescription = "Profiles",
          tint = Color.White,
          modifier = Modifier.size(28.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Who's Watching?",
        fontSize = 28.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.White,
        letterSpacing = 0.5.sp
      )

      Text(
        text = "Select a profile or add new profiles with only a name",
        fontSize = 13.sp,
        color = Color(0xFFA0A7B8),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Profiles Grid (Display existing profiles + Add Profile card)
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
      ) {
        items(profiles, key = { it.id }) { profile ->
          val isCurrentActive = profile.id == activeProfile?.id
          val profileColor = try {
            Color(android.graphics.Color.parseColor(profile.avatarColorHex))
          } catch (e: Exception) {
            AccentGold
          }

          ProfileCardItem(
            profile = profile,
            profileColor = profileColor,
            isActive = isCurrentActive,
            isManageMode = isManageMode,
            canDelete = profiles.size > 1,
            onClick = {
              if (isManageMode) {
                if (profiles.size > 1) {
                  profileToDelete = profile
                }
              } else {
                onSelectProfile(profile)
              }
            },
            onDeleteClick = {
              profileToDelete = profile
            }
          )
        }

        // Add Profile Card
        item {
          AddProfileCard(
            onClick = { isAddDialogOpen = true }
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Manage Profiles / Done Toggle
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedButton(
          onClick = { isManageMode = !isManageMode },
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (isManageMode) AccentGold else Color(0xFFA0A7B8)
          ),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isManageMode) AccentGold else BorderSubtle
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.testTag("manage_profiles_button")
        ) {
          Text(
            text = if (isManageMode) "Done Editing" else "Manage Profiles",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        Spacer(modifier = Modifier.width(16.dp))

        TextButton(
          onClick = onSignOut,
          modifier = Modifier.testTag("profile_screen_logout")
        ) {
          Text(
            text = "Switch Account",
            color = Color(0xFFEF4444),
            fontSize = 13.sp
          )
        }
      }
    }
  }

  // Add Profile Dialog (Only Name Required)
  if (isAddDialogOpen) {
    AddProfileOnlyNameDialog(
      onDismiss = { isAddDialogOpen = false },
      onConfirm = { name, isKids, colorHex ->
        onAddProfile(name, isKids, colorHex)
        isAddDialogOpen = false
      }
    )
  }

  // Confirm Delete Dialog
  if (profileToDelete != null) {
    AlertDialog(
      onDismissRequest = { profileToDelete = null },
      containerColor = SurfaceDark,
      title = {
        Text("Delete Profile?", color = Color.White, fontWeight = FontWeight.Bold)
      },
      text = {
        Text(
          "Are you sure you want to delete '${profileToDelete?.name}'? Watchlist and history will be cleared.",
          color = Color(0xFFA0A7B8),
          fontSize = 13.sp
        )
      },
      confirmButton = {
        Button(
          onClick = {
            profileToDelete?.let { onDeleteProfile(it) }
            profileToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
        ) {
          Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { profileToDelete = null }) {
          Text("Cancel", color = Color.White)
        }
      }
    )
  }
}

@Composable
fun ProfileCardItem(
  profile: StreamProfile,
  profileColor: Color,
  isActive: Boolean,
  isManageMode: Boolean,
  canDelete: Boolean,
  onClick: () -> Unit,
  onDeleteClick: () -> Unit
) {
  val initial = profile.name.firstOrNull()?.uppercase() ?: "P"

  Card(
    onClick = onClick,
    colors = CardDefaults.cardColors(
      containerColor = if (isActive && !isManageMode) SurfaceHighlight else SurfaceElevated
    ),
    shape = RoundedCornerShape(18.dp),
    border = androidx.compose.foundation.BorderStroke(
      width = if (isActive && !isManageMode) 2.dp else 1.dp,
      color = if (isActive && !isManageMode) AccentGold else BorderSubtle
    ),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("profile_item_${profile.id}")
  ) {
    Box(modifier = Modifier.fillMaxWidth()) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 20.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Large Avatar Badge
        Box(
          modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(profileColor)
            .border(2.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = initial,
            color = Color.Black,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
          )

          if (profile.isKids) {
            Box(
              modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.7f))
                .fillMaxWidth()
                .padding(vertical = 2.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "KIDS",
                color = AccentGold,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = profile.name,
          color = Color.White,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          textAlign = TextAlign.Center
        )

        if (isActive && !isManageMode) {
          Spacer(modifier = Modifier.height(4.dp))
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = "Active",
              tint = AccentGold,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "ACTIVE",
              color = AccentGold,
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
        }
      }

      // Delete badge in Manage Mode
      if (isManageMode && canDelete) {
        IconButton(
          onClick = onDeleteClick,
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(4.dp)
            .size(32.dp)
            .clip(CircleShape)
            .background(Color(0xFFEF4444))
        ) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Profile",
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }
}

@Composable
fun AddProfileCard(
  onClick: () -> Unit
) {
  Card(
    onClick = onClick,
    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
    shape = RoundedCornerShape(18.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("add_profile_card_button")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 20.dp, horizontal = 12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(72.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(SurfaceElevated)
          .border(1.5.dp, AccentCyan.copy(alpha = 0.6f), RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Add Profile",
          tint = AccentCyan,
          modifier = Modifier.size(34.dp)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "+ Add Profile",
        color = AccentCyan,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "Name only",
        color = Color(0xFFA0A7B8),
        fontSize = 11.sp
      )
    }
  }
}

@Composable
fun AddProfileOnlyNameDialog(
  onDismiss: () -> Unit,
  onConfirm: (name: String, isKids: Boolean, colorHex: String) -> Unit
) {
  var nameInput by remember { mutableStateOf("") }
  var isKidsChecked by remember { mutableStateOf(false) }
  var selectedColorHex by remember { mutableStateOf(UserSessionManager.PROFILE_COLORS[0]) }

  val quickNames = listOf("Kids", "Family", "Mom", "Dad", "Guest", "Roommate")
  val isValid = nameInput.trim().isNotEmpty()

  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = SurfaceDark,
    shape = RoundedCornerShape(24.dp),
    modifier = Modifier.testTag("add_profile_dialog"),
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = "Add Profile",
          color = Color.White,
          fontSize = 20.sp,
          fontWeight = FontWeight.ExtraBold
        )
        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color(0xFFA0A7B8),
            modifier = Modifier.size(20.dp)
          )
        }
      }
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Enter a name for this profile. No phone or email required.",
          color = Color(0xFFA0A7B8),
          fontSize = 12.sp,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Avatar Preview
        val previewColor = try {
          Color(android.graphics.Color.parseColor(selectedColorHex))
        } catch (e: Exception) {
          AccentGold
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          Box(
            modifier = Modifier
              .size(52.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(previewColor),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = nameInput.trim().firstOrNull()?.uppercase() ?: "P",
              color = Color.Black,
              fontSize = 24.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }

          Spacer(modifier = Modifier.width(14.dp))

          // Name Input Field (Only Name Needed)
          OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it.take(20) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_new_profile_name"),
            label = { Text("Profile Name") },
            placeholder = { Text("e.g. Kids, Alex, Sister") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
              capitalization = KeyboardCapitalization.Words,
              imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
              onDone = {
                if (isValid) {
                  onConfirm(nameInput.trim(), isKidsChecked, selectedColorHex)
                }
              }
            ),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = SurfaceElevated,
              unfocusedContainerColor = SurfaceElevated,
              focusedBorderColor = AccentGold,
              unfocusedBorderColor = BorderSubtle,
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(14.dp)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quick suggestions chips
        Text(
          text = "Quick suggestions:",
          fontSize = 11.sp,
          color = Color(0xFF8E95A5)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          quickNames.take(4).forEach { suggested ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceElevated)
                .clickable {
                  nameInput = suggested
                  if (suggested.equals("Kids", ignoreCase = true)) {
                    isKidsChecked = true
                  }
                }
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = suggested,
                color = AccentCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Color palette selection
        Text(
          text = "Choose avatar theme:",
          fontSize = 11.sp,
          color = Color(0xFF8E95A5)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          UserSessionManager.PROFILE_COLORS.take(6).forEach { hex ->
            val color = try {
              Color(android.graphics.Color.parseColor(hex))
            } catch (e: Exception) {
              AccentGold
            }
            val isSelected = hex == selectedColorHex

            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(color)
                .border(
                  width = if (isSelected) 2.dp else 0.dp,
                  color = if (isSelected) Color.White else Color.Transparent,
                  shape = CircleShape
                )
                .clickable { selectedColorHex = hex },
              contentAlignment = Alignment.Center
            ) {
              if (isSelected) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = Color.Black,
                  modifier = Modifier.size(14.dp)
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Kids Profile switch
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceElevated)
            .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
          Column {
            Text(
              text = "Kids Profile",
              color = Color.White,
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "Family & child-friendly titles only",
              color = Color(0xFFA0A7B8),
              fontSize = 11.sp
            )
          }

          Switch(
            checked = isKidsChecked,
            onCheckedChange = { isKidsChecked = it },
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color.Black,
              checkedTrackColor = AccentGold,
              uncheckedThumbColor = Color(0xFFA0A7B8),
              uncheckedTrackColor = SurfaceDark
            )
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (isValid) {
            onConfirm(nameInput.trim(), isKidsChecked, selectedColorHex)
          }
        },
        enabled = isValid,
        colors = ButtonDefaults.buttonColors(
          containerColor = AccentGold,
          contentColor = Color.Black,
          disabledContainerColor = SurfaceElevated,
          disabledContentColor = Color(0xFF6E7687)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.testTag("confirm_add_profile_button")
      ) {
        Text(
          text = "Create Profile",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel", color = Color(0xFFA0A7B8))
      }
    }
  )
}
