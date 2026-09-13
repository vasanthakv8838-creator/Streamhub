package com.example.model

data class VoiceConversationMessage(
  val id: String,
  val role: String, // "user" or "model"
  val text: String,
  val timestamp: Long = System.currentTimeMillis(),
  val matchedTitles: List<String> = emptyList()
)

data class VideoAnalysisResult(
  val videoTitle: String,
  val videoUrl: String,
  val platformName: String,
  val synopsis: String,
  val coreThemes: List<String>,
  val toneAndMood: String,
  val standoutPerformances: String,
  val ageAndContentRating: String,
  val audienceVerdict: String,
  val isAnalyzing: Boolean = false
)

data class CuratedTrailer(
  val title: String,
  val platform: OttPlatform,
  val duration: String,
  val videoUrl: String,
  val defaultPrompt: String
)
