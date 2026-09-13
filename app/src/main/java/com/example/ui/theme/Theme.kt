package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = AccentGold,
  onPrimary = Color.Black,
  primaryContainer = SurfaceHighlight,
  onPrimaryContainer = TextPrimary,
  secondary = AccentCyan,
  onSecondary = Color.Black,
  secondaryContainer = SurfaceElevated,
  onSecondaryContainer = TextPrimary,
  tertiary = AccentPurple,
  background = BackgroundDark,
  onBackground = TextPrimary,
  surface = SurfaceDark,
  onSurface = TextPrimary,
  surfaceVariant = SurfaceElevated,
  onSurfaceVariant = TextSecondary,
  outline = BorderSubtle
)

@Composable
fun OTTTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit
) {
  OTTTheme(content = content)
}
