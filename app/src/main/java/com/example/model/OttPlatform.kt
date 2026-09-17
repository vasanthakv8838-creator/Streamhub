package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.PlatformAppleTv
import com.example.ui.theme.PlatformJioHotstar
import com.example.ui.theme.PlatformNetflix
import com.example.ui.theme.PlatformPrimeVideo
import com.example.ui.theme.PlatformSonyLiv
import com.example.ui.theme.PlatformYouTube
import com.example.ui.theme.PlatformZee5

enum class OttPlatform(
  val displayName: String,
  val shortTag: String,
  val brandColor: Color,
  val badgeBgColor: Color,
  val baseUrl: String,
  val deepLinkPrefix: String
) {
  ALL(
    displayName = "All OTTs",
    shortTag = "ALL",
    brandColor = Color(0xFFFFB800),
    badgeBgColor = Color(0xFF262C3E),
    baseUrl = "",
    deepLinkPrefix = ""
  ),
  THEATRE(
    displayName = "In Theatres",
    shortTag = "Cinemas",
    brandColor = Color(0xFFFF2A55),
    badgeBgColor = Color(0xFF380812),
    baseUrl = "https://in.bookmyshow.com",
    deepLinkPrefix = "https://in.bookmyshow.com"
  ),
  YOUTUBE(
    displayName = "YouTube",
    shortTag = "YT",
    brandColor = PlatformYouTube,
    badgeBgColor = Color(0xFF3B070A),
    baseUrl = "https://www.youtube.com",
    deepLinkPrefix = "vnd.youtube:"
  ),
  JIO_HOTSTAR(
    displayName = "JioHotstar",
    shortTag = "Hotstar",
    brandColor = PlatformJioHotstar,
    badgeBgColor = Color(0xFF04193F),
    baseUrl = "https://www.hotstar.com",
    deepLinkPrefix = "hotstar://"
  ),
  APPLE_TV(
    displayName = "Apple TV+",
    shortTag = "Apple TV+",
    brandColor = PlatformAppleTv,
    badgeBgColor = Color(0xFF242426),
    baseUrl = "https://tv.apple.com",
    deepLinkPrefix = "https://tv.apple.com"
  ),
  NETFLIX(
    displayName = "Netflix",
    shortTag = "Netflix",
    brandColor = PlatformNetflix,
    badgeBgColor = Color(0xFF3D080A),
    baseUrl = "https://www.netflix.com",
    deepLinkPrefix = "nflx://"
  ),
  ZEE5(
    displayName = "ZEE5",
    shortTag = "ZEE5",
    brandColor = PlatformZee5,
    badgeBgColor = Color(0xFF2C0B2C),
    baseUrl = "https://www.zee5.com",
    deepLinkPrefix = "zee5://"
  ),
  SONY_LIV(
    displayName = "Sony LIV",
    shortTag = "Sony LIV",
    brandColor = PlatformSonyLiv,
    badgeBgColor = Color(0xFF381B00),
    baseUrl = "https://www.sonyliv.com",
    deepLinkPrefix = "sonyliv://"
  ),
  PRIME_VIDEO(
    displayName = "Prime Video",
    shortTag = "Prime",
    brandColor = PlatformPrimeVideo,
    badgeBgColor = Color(0xFF032636),
    baseUrl = "https://www.primevideo.com",
    deepLinkPrefix = "primevideo://"
  )
}

enum class MediaType(val label: String) {
  ALL("All"),
  THEATRICAL("Only in Theatres"),
  MOVIE("Movies"),
  SERIES("Series"),
  NEWS("Live News"),
  DOCUMENTARY("Docuseries"),
  SPORTS("Live Sports")
}
