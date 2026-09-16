package com.example.model

data class MediaItem(
  val id: String,
  val title: String,
  val platform: OttPlatform,
  val mediaType: MediaType,
  val genre: String,
  val rating: Double,
  val votes: String,
  val year: Int,
  val duration: String,
  val synopsis: String,
  val cast: List<String>,
  val director: String,
  val posterUrl: String,
  val backdropUrl: String,
  val trailerUrl: String,
  val watchUrl: String,
  val qualityBadge: String = "4K Ultra HD • HDR10+",
  val contentAdvisory: String = "U/A 16+",
  val isTrending: Boolean = false,
  val isFeaturedHero: Boolean = false,
  val regionCode: String = "GLOBAL",
  val vpnRequired: Boolean = false,
  val vpnRegionBadge: String = ""
)
