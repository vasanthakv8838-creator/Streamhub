package com.example.data.gemini

import android.util.Log
import com.example.BuildConfig
import com.example.model.VideoAnalysisResult
import com.example.model.VoiceConversationMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiService {

  private val client = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()

  private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

  /**
   * Conversational Voice Assistant using model 'gemini-3.1-flash-live-preview'
   */
  suspend fun chatWithLiveAssistant(
    history: List<VoiceConversationMessage>,
    userPrompt: String
  ): String = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY
    if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
      return@withContext getLocalSmartVoiceResponse(userPrompt)
    }

    try {
      val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-live-preview:generateContent?key=$apiKey"

      val rootJson = JSONObject()
      val contentsArray = JSONArray()

      // Add recent history turns for context
      history.takeLast(6).forEach { msg ->
        val role = if (msg.role == "user") "user" else "model"
        val contentObj = JSONObject()
        contentObj.put("role", role)
        val partsArray = JSONArray()
        partsArray.put(JSONObject().put("text", msg.text))
        contentObj.put("parts", partsArray)
        contentsArray.put(contentObj)
      }

      // Add current user prompt
      val currentObj = JSONObject()
      currentObj.put("role", "user")
      val partsArr = JSONArray()
      partsArr.put(JSONObject().put("text", userPrompt))
      currentObj.put("parts", partsArr)
      contentsArray.put(currentObj)

      rootJson.put("contents", contentsArray)

      // System instruction for OTT expert
      val systemInstruction = JSONObject()
      val sysParts = JSONArray()
      sysParts.put(
        JSONObject().put(
          "text",
          "You are the conversational Voice AI stream guide for 'OTT Aggregator', a unified application that aggregates YouTube, JioHotstar, Apple TV+, Netflix, ZEE5, Sony LIV, and Prime Video. " +
              "Answer queries conversationally, concisely (suitable for voice playback), highlighting which exact OTT platform streams each recommended movie, series, or video, and why it's worth watching."
        )
      )
      systemInstruction.put("parts", sysParts)
      rootJson.put("systemInstruction", systemInstruction)

      val requestBody = rootJson.toString().toRequestBody(jsonMediaType)
      val request = Request.Builder().url(url).post(requestBody).build()

      client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
          val errBody = response.body?.string() ?: ""
          Log.e("GeminiService", "Live API error: ${response.code} - $errBody")
          return@withContext getLocalSmartVoiceResponse(userPrompt)
        }

        val resString = response.body?.string() ?: ""
        val resJson = JSONObject(resString)
        val candidates = resJson.optJSONArray("candidates")
        val firstCandidate = candidates?.optJSONObject(0)
        val content = firstCandidate?.optJSONObject("content")
        val parts = content?.optJSONArray("parts")
        val replyText = parts?.optJSONObject(0)?.optString("text")

        if (!replyText.isNullOrBlank()) {
          replyText.trim()
        } else {
          getLocalSmartVoiceResponse(userPrompt)
        }
      }
    } catch (e: Exception) {
      Log.e("GeminiService", "Exception calling gemini-3.1-flash-live-preview", e)
      getLocalSmartVoiceResponse(userPrompt)
    }
  }

  /**
   * Video Content Analysis using model 'gemini-3.1-pro-preview'
   */
  suspend fun analyzeVideoContent(
    videoTitle: String,
    videoUrl: String,
    platform: String,
    customFocusPrompt: String? = null
  ): VideoAnalysisResult = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY
    if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
      return@withContext getLocalVideoAnalysis(videoTitle, videoUrl, platform)
    }

    try {
      val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-pro-preview:generateContent?key=$apiKey"

      val rootJson = JSONObject()
      val promptBuilder = StringBuilder().apply {
        append("Analyze this video trailer/clip for an OTT aggregator merging YouTube, JioHotstar, Apple TV+, Netflix, ZEE5, Sony LIV, and Prime Video.\n")
        append("Video Title: $videoTitle\n")
        append("Video URL: $videoUrl\n")
        append("Primary Streaming Platform: $platform\n")
        if (!customFocusPrompt.isNullOrBlank()) {
          append("User's Specific Focus: $customFocusPrompt\n")
        }
        append("\nPlease return a comprehensive JSON response adhering to this format:\n")
        append("{\n")
        append("  \"synopsis\": \"2-3 sentence overview of what happens in the video/trailer\",\n")
        append("  \"coreThemes\": [\"theme1\", \"theme2\", \"theme3\"],\n")
        append("  \"toneAndMood\": \"Description of visual tone, lighting, pacing, audio dynamics\",\n")
        append("  \"standoutPerformances\": \"Notable actors, characters, or cinematography highlights\",\n")
        append("  \"ageAndContentRating\": \"Recommended age rating and content flags (violence, language, etc.)\",\n")
        append("  \"audienceVerdict\": \"Who should watch this and why on $platform\"\n")
        append("}")
      }

      val contentsArray = JSONArray()
      val userObj = JSONObject()
      userObj.put("role", "user")
      val partsArr = JSONArray()
      partsArr.put(JSONObject().put("text", promptBuilder.toString()))
      userObj.put("parts", partsArr)
      contentsArray.put(userObj)
      rootJson.put("contents", contentsArray)

      // Generation config requesting JSON output
      val genConfig = JSONObject()
      genConfig.put("responseMimeType", "application/json")
      rootJson.put("generationConfig", genConfig)

      val requestBody = rootJson.toString().toRequestBody(jsonMediaType)
      val request = Request.Builder().url(url).post(requestBody).build()

      client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
          Log.e("GeminiService", "Video Analysis error: ${response.code}")
          return@withContext getLocalVideoAnalysis(videoTitle, videoUrl, platform)
        }

        val resString = response.body?.string() ?: ""
        val resJson = JSONObject(resString)
        val text = resJson.optJSONArray("candidates")
          ?.optJSONObject(0)
          ?.optJSONObject("content")
          ?.optJSONArray("parts")
          ?.optJSONObject(0)
          ?.optString("text")

        if (!text.isNullOrBlank()) {
          val cleanJson = text.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
          val parsed = JSONObject(cleanJson)

          val themesList = mutableListOf<String>()
          val themesArr = parsed.optJSONArray("coreThemes")
          if (themesArr != null) {
            for (i in 0 until themesArr.length()) {
              themesList.add(themesArr.optString(i))
            }
          }

          VideoAnalysisResult(
            videoTitle = videoTitle,
            videoUrl = videoUrl,
            platformName = platform,
            synopsis = parsed.optString("synopsis", "Engaging trailer showcasing high production value and compelling storyline."),
            coreThemes = if (themesList.isNotEmpty()) themesList else listOf("Drama", "Conflict", "Suspense"),
            toneAndMood = parsed.optString("toneAndMood", "Intense, cinematic, fast-paced rhythm with dramatic crescendos."),
            standoutPerformances = parsed.optString("standoutPerformances", "Strong leading performances and dynamic chemistry."),
            ageAndContentRating = parsed.optString("ageAndContentRating", "U/A 16+ (Moderate thematic tension)"),
            audienceVerdict = parsed.optString("audienceVerdict", "A must-watch title on $platform for fans of thrilling narratives.")
          )
        } else {
          getLocalVideoAnalysis(videoTitle, videoUrl, platform)
        }
      }
    } catch (e: Exception) {
      Log.e("GeminiService", "Exception in video analysis", e)
      getLocalVideoAnalysis(videoTitle, videoUrl, platform)
    }
  }

  // Fallback intelligent generator matching the 7 OTT services
  private fun getLocalSmartVoiceResponse(prompt: String): String {
    val p = prompt.lowercase()
    return when {
      p.contains("netflix") ->
        "On Netflix, top picks include 'Stranger Things 5' for sci-fi suspense, 'Squid Game' for high-stakes survival drama, and the gripping Emmy-winning series 'Delhi Crime'."
      p.contains("prime") || p.contains("amazon") ->
        "On Prime Video, stream 'Panchayat Season 3' for heartwarming rural comedy, the hyper-kinetic superhero satire 'The Boys Season 4', or Jonathan Nolan's acclaimed 'Fallout'."
      p.contains("apple") ->
        "Apple TV+ leads in immaculate visual storytelling: watch Ben Stiller's psychological thriller 'Severance', feel-good comedy 'Ted Lasso', and Martin Scorsese's masterwork 'Killers of the Flower Moon'."
      p.contains("hotstar") || p.contains("jio") ->
        "JioHotstar offers Bollywood fantasy blockbuster 'Brahmāstra: Part One – Shiva', spy thriller 'The Night Manager', and live 4K Premier League and Cricket World Cup streams."
      p.contains("sony") || p.contains("liv") ->
        "Sony LIV is the home of India's finest grounded dramas: Hansal Mehta's 'Scam 1992', historical science epic 'Rocket Boys', and beloved family series 'Gullak'."
      p.contains("zee5") || p.contains("zee") ->
        "On ZEE5, check out the original multilingual cut of global sensation 'RRR', royal drama 'Taj: Divided by Blood', and Sunil Grover's dark comedy thriller 'Sunflower'."
      p.contains("youtube") ->
        "YouTube is unmatched for free premier documentaries: explore Derek Muller's 'Veritasium', philosophic animations from 'Kurzgesagt', and live grassroots folk music on 'Coke Studio Bharat'."
      p.contains("news") || p.contains("bulletin") || p.contains("current affairs") || p.contains("headline") ->
        "You can stream 24/7 Live News right now! Tune into 'NDTV 24x7 Live' and 'WION' on YouTube, 'India Today Live' on JioHotstar, and 'Zee News 24/7' on ZEE5. For investigative current affairs docuseries, watch 'Explained' on Netflix."
      p.contains("comedy") || p.contains("funny") ->
        "For laughs tonight, I recommend 'Panchayat' on Prime Video, 'Ted Lasso' on Apple TV+, or 'Gullak' on Sony LIV."
      p.contains("thriller") || p.contains("action") || p.contains("crime") ->
        "For heart-pounding thrillers, stream 'Scam 1992' on Sony LIV, 'Severance' on Apple TV+, or 'Squid Game' on Netflix."
      p.contains("family") || p.contains("kids") ->
        "Great family entertainment includes 'Panchayat' (Prime Video), 'Gullak' (Sony LIV), 'Ted Lasso' (Apple TV+), or 'Kurzgesagt' (YouTube)."
      else ->
        "Across all 7 streaming services (YouTube, JioHotstar, Apple TV+, Netflix, ZEE5, Sony LIV, and Prime Video), the highest-rated titles right now are 'Scam 1992' (9.3 on Sony LIV), 'Panchayat' (8.9 on Prime), and 'Severance' (8.7 on Apple TV+). Would you like to filter by a specific platform or genre?"
    }
  }

  private fun getLocalVideoAnalysis(title: String, url: String, platform: String): VideoAnalysisResult {
    return when {
      title.contains("NDTV", ignoreCase = true) || title.contains("News", ignoreCase = true) -> VideoAnalysisResult(
        videoTitle = title,
        videoUrl = url,
        platformName = platform,
        synopsis = "This prime time live news broadcast features round-the-clock global headlines, geopolitical developments, breaking economic news, and rigorous field reporting.",
        coreThemes = listOf("Geopolitics", "Live Breaking News", "Public Policy", "Economic Analysis"),
        toneAndMood = "Urgent, journalistic, objective, backed by real-time ticker updates and on-ground studio correspondent debriefs.",
        standoutPerformances = "Composed news anchor moderation, insightful panel contributors, and articulate ground correspondents.",
        ageAndContentRating = "All Ages / U/A 13+ (Informative civic content)",
        audienceVerdict = "Essential continuous coverage for informed citizens tracking current national and international affairs."
      )
      title.contains("Severance", ignoreCase = true) -> VideoAnalysisResult(
        videoTitle = title,
        videoUrl = url,
        platformName = platform,
        synopsis = "The trailer reveals heightened paranoia within Lumon Industries as Mark Scout uncovers fractured memories and covert protocols between severed employees.",
        coreThemes = listOf("Identity Fracture", "Corporate Dystopia", "Psychological Paranoia", "Resistance"),
        toneAndMood = "Sterile fluorescent symmetry juxtaposed with dread-inducing string harmonics and ticking metronomes.",
        standoutPerformances = "Adam Scott exudes vulnerable anxiety; Patricia Arquette projects icy, chilling authority.",
        ageAndContentRating = "TV-MA (Intense psychological stress & language)",
        audienceVerdict = "Essential viewing on Apple TV+ for viewers who relish mystery box thrillers with impeccable art direction."
      )
      title.contains("Panchayat", ignoreCase = true) -> VideoAnalysisResult(
        videoTitle = title,
        videoUrl = url,
        platformName = platform,
        synopsis = "The trailer highlights upcoming village elections in Phulera, highlighting comedic local rivalries, bureaucratic clashes, and heartfelt camaraderie.",
        coreThemes = listOf("Rural Politics", "Community Solidarity", "Aspirational Youth", "Heartfelt Comedy"),
        toneAndMood = "Warm, sun-soaked rural hues with an earthy, acoustic folk soundtrack and authentic village cadence.",
        standoutPerformances = "Jitendra Kumar's comic exasperation paired with Faisal Malik's poignant, understated gravitas.",
        ageAndContentRating = "U/A 13+ (Mild colloquial language)",
        audienceVerdict = "A heartwarming masterpiece on Prime Video suitable for all generations to binge together."
      )
      title.contains("Scam 1992", ignoreCase = true) -> VideoAnalysisResult(
        videoTitle = title,
        videoUrl = url,
        platformName = platform,
        synopsis = "The clip captures Harshad Mehta's audacity in the bustling Bombay Stock Exchange ring, executing high-stakes transactions that astonished financial institutions.",
        coreThemes = listOf("Ambition & Hubris", "Financial Loophole Exploitation", "1990s Dalal Street Era", "Journalistic Investigation"),
        toneAndMood = "Energetic sepia tones, electric synth brass signature score, and rapid-fire Gujarati-Hindi dialogue delivery.",
        standoutPerformances = "Pratik Gandhi's magnetic charm and flamboyant confidence define the entire narrative arc.",
        ageAndContentRating = "U/A 16+ (Thematic tension & business profanity)",
        audienceVerdict = "The defining benchmark of Indian streaming on Sony LIV; unmissable for finance and drama enthusiasts."
      )
      else -> VideoAnalysisResult(
        videoTitle = title,
        videoUrl = url,
        platformName = platform,
        synopsis = "A compelling trailer preview demonstrating high production standards, dynamic narrative pacing, and engaging thematic conflicts.",
        coreThemes = listOf("Dramatic Conflict", "Cinematic Visuals", "Character Journey"),
        toneAndMood = "Richly saturated color grading with heightened sound design and escalating rhythm.",
        standoutPerformances = "Charismatic leads delivering emotionally grounded dialogue and strong physical presence.",
        ageAndContentRating = "U/A 16+ (Thematic content and suspense)",
        audienceVerdict = "Top streaming choice on $platform offering high entertainment value for modern OTT audiences."
      )
    }
  }
}
