package com.example.model

enum class LiveCategory(val displayName: String, val emoji: String) {
  ALL("All Channels", "📺"),
  NEWS("Live News", "🔴"),
  SPORTS("Live Sports", "⚽"),
  ENTERTAINMENT("Entertainment", "🍿"),
  MOVIES("Movies 24/7", "🎬"),
  MUSIC("Music Live", "🎵"),
  SCIENCE("Science & Tech", "🚀"),
  KIDS("Kids & Anime", "🧸")
}

data class LiveProgram(
  val title: String,
  val category: String,
  val startTime: String,
  val endTime: String,
  val progressPercent: Float, // 0.0f to 1.0f
  val description: String = ""
)

data class LiveChannel(
  val id: String,
  val name: String,
  val network: String,
  val category: LiveCategory,
  val currentProgram: LiveProgram,
  val nextProgram: LiveProgram,
  val streamUrl: String,
  val logoUrl: String,
  val bannerUrl: String,
  val resolution: String = "1080p 60fps HD",
  val viewersCount: String = "45K",
  val countryCode: String = "GLOBAL",
  val countryName: String = "Global",
  val flagEmoji: String = "🌐",
  val language: String = "English",
  val isLive: Boolean = true,
  val isFeatured: Boolean = false,
  val vpnNotice: String = ""
)
