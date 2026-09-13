package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist_items")
data class WatchlistEntity(
  @PrimaryKey
  val mediaId: String,
  val title: String,
  val platformName: String,
  val mediaType: String,
  val genre: String,
  val rating: Double,
  val year: Int,
  val posterUrl: String,
  val watchUrl: String,
  val isWatched: Boolean = false,
  val userRating: Int = 0, // 0-5 stars
  val userNotes: String = "",
  val addedAt: Long = System.currentTimeMillis()
)
