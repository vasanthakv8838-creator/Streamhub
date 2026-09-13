package com.example.model

data class UserProfile(
  val name: String,
  val phoneNumber: String,
  val joinedAt: Long = System.currentTimeMillis(),
  val isVip: Boolean = false
)

data class StreamProfile(
  val id: String,
  val name: String,
  val avatarColorHex: String = "#FFB800",
  val isKids: Boolean = false,
  val createdAt: Long = System.currentTimeMillis()
)
