package com.example.model

enum class VpnStatus {
  DISCONNECTED,
  CONNECTING,
  CONNECTED
}

data class VpnServer(
  val id: String,
  val countryName: String,
  val countryCode: String, // "US", "GB", "IN", "JP", "DE", "CA", "AU", "FR", etc.
  val flagEmoji: String,
  val city: String,
  val pingMs: Int,
  val serverLoadPercent: Int,
  val ipAddress: String,
  val catalogDescription: String,
  val unlockedServices: List<String>,
  val exclusiveTitlesCount: Int,
  val region: String = "Global"
)

data class VpnConnectionState(
  val status: VpnStatus = VpnStatus.DISCONNECTED,
  val server: VpnServer,
  val assignedVirtualIp: String = "",
  val protocol: String = "WireGuard® (Surfshark High-Speed)",
  val cleanWebEnabled: Boolean = true,
  val killSwitchEnabled: Boolean = true,
  val bypasserEnabled: Boolean = false,
  val uptimeSeconds: Long = 0L,
  val downloadedMb: Float = 0f,
  val blockedThreats: Int = 14
)
