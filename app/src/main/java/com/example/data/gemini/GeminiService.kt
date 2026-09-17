package com.example.data.gemini

import android.util.Log
import com.example.BuildConfig
import com.example.model.MediaItem
import com.example.model.MediaType
import com.example.model.OttPlatform
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
import java.net.URLEncoder
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

  /**
   * Universal Live Catalog Discovery using model 'gemini-3.5-flash'.
   * Searches the entire universe of global movies, series, and videos across all 7 streaming networks.
   */
  suspend fun searchGlobalTitlesWithAi(
    query: String,
    platformFilter: OttPlatform = OttPlatform.ALL
  ): List<MediaItem> = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY
    if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
      return@withContext getLocalAiCatalogFallback(query, platformFilter)
    }

    try {
      val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
      val prompt = """
        You are the global catalog indexer for 'OTT Aggregator', which consolidates libraries from 7 major streaming networks:
        NETFLIX, PRIME_VIDEO, JIO_HOTSTAR, APPLE_TV, SONY_LIV, ZEE5, and YOUTUBE.
        The user is searching for: "$query"
        Target platform filter: "${platformFilter.name}"

        Find up to 8 real, accurate movies, series, or video shows matching this query from global entertainment.
        Return ONLY a JSON array with this schema:
        [
          {
            "title": "Exact Title",
            "platform": "NETFLIX" | "PRIME_VIDEO" | "JIO_HOTSTAR" | "APPLE_TV" | "SONY_LIV" | "ZEE5" | "YOUTUBE",
            "mediaType": "MOVIE" | "SERIES" | "DOCUMENTARY" | "NEWS" | "SPORTS",
            "genre": "Genre • Genre2",
            "rating": 8.4,
            "votes": "1.2M",
            "year": 2023,
            "duration": "2h 15m" or "3 Seasons",
            "synopsis": "Overview of plot...",
            "cast": ["Actor 1", "Actor 2"],
            "director": "Director Name",
            "posterUrl": "optional URL or empty",
            "qualityBadge": "4K Ultra HD • HDR10+",
            "contentAdvisory": "U/A 16+"
          }
        ]
      """.trimIndent()

      val rootJson = JSONObject()
      val contentsArray = JSONArray()
      val userObj = JSONObject()
      userObj.put("role", "user")
      val partsArr = JSONArray()
      partsArr.put(JSONObject().put("text", prompt))
      userObj.put("parts", partsArr)
      contentsArray.put(userObj)
      rootJson.put("contents", contentsArray)

      val genConfig = JSONObject()
      genConfig.put("responseMimeType", "application/json")
      rootJson.put("generationConfig", genConfig)

      val requestBody = rootJson.toString().toRequestBody(jsonMediaType)
      val request = Request.Builder().url(url).post(requestBody).build()

      client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
          Log.w("GeminiService", "AI search failed: ${response.code}")
          return@withContext getLocalAiCatalogFallback(query, platformFilter)
        }

        val resString = response.body?.string() ?: ""
        val resJson = JSONObject(resString)
        val candidates = resJson.optJSONArray("candidates")
        val firstCandidate = candidates?.optJSONObject(0)
        val content = firstCandidate?.optJSONObject("content")
        val parts = content?.optJSONArray("parts")
        val replyText = parts?.optJSONObject(0)?.optString("text") ?: ""

        val parsed = parseMediaItemsFromJson(replyText, platformFilter)
        if (parsed.isNotEmpty()) parsed else getLocalAiCatalogFallback(query, platformFilter)
      }
    } catch (e: Exception) {
      Log.e("GeminiService", "Exception in AI catalog search", e)
      getLocalAiCatalogFallback(query, platformFilter)
    }
  }

  private fun parseMediaItemsFromJson(jsonString: String, targetPlatform: OttPlatform): List<MediaItem> {
    return try {
      val trimmed = jsonString.trim()
      val jsonArray = if (trimmed.startsWith("[")) {
        JSONArray(trimmed)
      } else {
        val start = trimmed.indexOf('[')
        val end = trimmed.lastIndexOf(']')
        if (start != -1 && end != -1 && end > start) {
          JSONArray(trimmed.substring(start, end + 1))
        } else {
          return emptyList()
        }
      }

      val items = mutableListOf<MediaItem>()
      for (i in 0 until jsonArray.length()) {
        val obj = jsonArray.optJSONObject(i) ?: continue
        val title = obj.optString("title", "").trim()
        if (title.isBlank()) continue

        val rawPlatform = obj.optString("platform", "")
        val platform = resolvePlatform(rawPlatform, targetPlatform)

        val rawMediaType = obj.optString("mediaType", "MOVIE")
        val mediaType = try {
          MediaType.valueOf(rawMediaType.uppercase())
        } catch (_: Exception) {
          if (rawMediaType.contains("series", ignoreCase = true)) MediaType.SERIES else MediaType.MOVIE
        }

        val genre = obj.optString("genre", "Cinema • Streaming")
        val rating = obj.optDouble("rating", 8.0)
        val votes = obj.optString("votes", "850K")
        val year = obj.optInt("year", 2024)
        val duration = obj.optString("duration", if (mediaType == MediaType.SERIES) "Multi-Season" else "2h 10m")
        val synopsis = obj.optString("synopsis", "Streaming globally on ${platform.displayName}.")
        
        val castArray = obj.optJSONArray("cast")
        val castList = mutableListOf<String>()
        if (castArray != null) {
          for (c in 0 until castArray.length()) {
            castList.add(castArray.optString(c))
          }
        }
        if (castList.isEmpty()) castList.add("Ensemble Cast")

        val director = obj.optString("director", "Visionary Director")
        val givenPoster = obj.optString("posterUrl", "")
        val posterUrl = if (givenPoster.isNotBlank() && givenPoster.startsWith("http")) {
          givenPoster
        } else {
          getFallbackPosterForPlatform(platform)
        }

        val qualityBadge = obj.optString("qualityBadge", "4K Ultra HD • Dolby Atmos")
        val contentAdvisory = obj.optString("contentAdvisory", "U/A 16+")

        val encodedTitle = URLEncoder.encode(title, "UTF-8")
        val watchUrl = when (platform) {
          OttPlatform.NETFLIX -> "https://www.netflix.com/search?q=$encodedTitle"
          OttPlatform.PRIME_VIDEO -> "https://www.primevideo.com/search/ref=atv_nb_sr?phrase=$encodedTitle"
          OttPlatform.JIO_HOTSTAR -> "https://www.hotstar.com/in/explore?search_query=$encodedTitle"
          OttPlatform.APPLE_TV -> "https://tv.apple.com/search?term=$encodedTitle"
          OttPlatform.SONY_LIV -> "https://www.sonyliv.com/search/$encodedTitle"
          OttPlatform.ZEE5 -> "https://www.zee5.com/search?q=$encodedTitle"
          OttPlatform.YOUTUBE -> "https://www.youtube.com/results?search_query=$encodedTitle"
          OttPlatform.THEATRE -> "https://in.bookmyshow.com/explore/movies?search=$encodedTitle"
          OttPlatform.ALL -> "https://www.google.com/search?q=watch+$encodedTitle+online"
        }

        items.add(
          MediaItem(
            id = "ai_${platform.name.lowercase()}_${title.lowercase().replace("[^a-z0-9]".toRegex(), "_")}",
            title = title,
            platform = platform,
            mediaType = mediaType,
            genre = genre,
            rating = rating,
            votes = votes,
            year = year,
            duration = duration,
            synopsis = synopsis,
            cast = castList,
            director = director,
            posterUrl = posterUrl,
            backdropUrl = posterUrl,
            trailerUrl = "https://www.youtube.com/results?search_query=${URLEncoder.encode("$title trailer", "UTF-8")}",
            watchUrl = watchUrl,
            qualityBadge = qualityBadge,
            contentAdvisory = contentAdvisory,
            isTrending = rating >= 8.2
          )
        )
      }

      if (targetPlatform != OttPlatform.ALL) {
        items.filter { it.platform == targetPlatform }
      } else {
        items
      }
    } catch (e: Exception) {
      Log.e("GeminiService", "Failed parsing media items JSON", e)
      emptyList()
    }
  }

  private fun resolvePlatform(raw: String, targetPlatform: OttPlatform): OttPlatform {
    if (targetPlatform != OttPlatform.ALL) return targetPlatform
    val upper = raw.uppercase()
    return when {
      upper.contains("NETFLIX") -> OttPlatform.NETFLIX
      upper.contains("PRIME") || upper.contains("AMAZON") -> OttPlatform.PRIME_VIDEO
      upper.contains("HOTSTAR") || upper.contains("DISNEY") || upper.contains("JIO") -> OttPlatform.JIO_HOTSTAR
      upper.contains("APPLE") -> OttPlatform.APPLE_TV
      upper.contains("SONY") || upper.contains("LIV") -> OttPlatform.SONY_LIV
      upper.contains("ZEE") -> OttPlatform.ZEE5
      upper.contains("YOUTUBE") -> OttPlatform.YOUTUBE
      else -> OttPlatform.NETFLIX
    }
  }

  private fun getFallbackPosterForPlatform(platform: OttPlatform): String {
    return when (platform) {
      OttPlatform.NETFLIX -> "https://images.unsplash.com/photo-1574375927938-d5a98e8ffe85?w=600&auto=format&fit=crop&q=80"
      OttPlatform.PRIME_VIDEO -> "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=600&auto=format&fit=crop&q=80"
      OttPlatform.JIO_HOTSTAR -> "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80"
      OttPlatform.APPLE_TV -> "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80"
      OttPlatform.SONY_LIV -> "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600&auto=format&fit=crop&q=80"
      OttPlatform.ZEE5 -> "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=600&auto=format&fit=crop&q=80"
      OttPlatform.YOUTUBE -> "https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=600&auto=format&fit=crop&q=80"
      OttPlatform.THEATRE -> "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600&auto=format&fit=crop&q=80"
      OttPlatform.ALL -> "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600&auto=format&fit=crop&q=80"
    }
  }

  private fun getLocalAiCatalogFallback(query: String, platformFilter: OttPlatform): List<MediaItem> {
    val q = query.lowercase().trim()
    val allFallback = listOf(
      MediaItem(
        id = "ai_dune_2",
        title = "Dune: Part Two",
        platform = OttPlatform.JIO_HOTSTAR,
        mediaType = MediaType.MOVIE,
        genre = "Sci-Fi • Epic Adventure",
        rating = 8.6,
        votes = "540K",
        year = 2024,
        duration = "2h 46m",
        synopsis = "Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators who destroyed his family.",
        cast = listOf("Timothée Chalamet", "Zendaya", "Rebecca Ferguson", "Javier Bardem"),
        director = "Denis Villeneuve",
        posterUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
        trailerUrl = "https://www.youtube.com/watch?v=Way9Dexny3w",
        watchUrl = "https://www.hotstar.com/in/explore?search_query=Dune+Part+Two",
        qualityBadge = "IMAX Enhanced • Dolby Atmos",
        isTrending = true
      ),
      MediaItem(
        id = "ai_oppenheimer",
        title = "Oppenheimer",
        platform = OttPlatform.JIO_HOTSTAR,
        mediaType = MediaType.MOVIE,
        genre = "Biographical Drama • History",
        rating = 8.9,
        votes = "780K",
        year = 2023,
        duration = "3h 00m",
        synopsis = "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb during World War II.",
        cast = listOf("Cillian Murphy", "Emily Blunt", "Matt Damon", "Robert Downey Jr."),
        director = "Christopher Nolan",
        posterUrl = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=600&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=1200&auto=format&fit=crop&q=80",
        trailerUrl = "https://www.youtube.com/watch?v=uYPbbksJxIg",
        watchUrl = "https://www.hotstar.com/in/explore?search_query=Oppenheimer",
        qualityBadge = "4K Dolby Vision • Atmos",
        isTrending = true
      ),
      MediaItem(
        id = "ai_interstellar",
        title = "Interstellar",
        platform = OttPlatform.PRIME_VIDEO,
        mediaType = MediaType.MOVIE,
        genre = "Sci-Fi • Space Adventure",
        rating = 8.7,
        votes = "2.1M",
        year = 2014,
        duration = "2h 49m",
        synopsis = "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
        cast = listOf("Matthew McConaughey", "Anne Hathaway", "Jessica Chastain"),
        director = "Christopher Nolan",
        posterUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=1200&auto=format&fit=crop&q=80",
        trailerUrl = "https://www.youtube.com/watch?v=zSWdZVtXT7E",
        watchUrl = "https://www.primevideo.com/search/ref=atv_nb_sr?phrase=Interstellar",
        qualityBadge = "4K Ultra HD",
        isTrending = true
      ),
      MediaItem(
        id = "ai_breaking_bad",
        title = "Breaking Bad",
        platform = OttPlatform.NETFLIX,
        mediaType = MediaType.SERIES,
        genre = "Crime • Drama • Suspense",
        rating = 9.5,
        votes = "2.2M",
        year = 2013,
        duration = "5 Seasons (62 Eps)",
        synopsis = "A chemistry teacher diagnosed with cancer turns to manufacturing methamphetamine to secure his family's future.",
        cast = listOf("Bryan Cranston", "Aaron Paul", "Anna Gunn", "Giancarlo Esposito"),
        director = "Vince Gilligan",
        posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
        trailerUrl = "https://www.youtube.com/watch?v=HhesaQXLuRY",
        watchUrl = "https://www.netflix.com/search?q=Breaking+Bad",
        qualityBadge = "4K Ultra HD • 5.1 Audio",
        isTrending = true
      ),
      MediaItem(
        id = "ai_dark_knight",
        title = "The Dark Knight",
        platform = OttPlatform.NETFLIX,
        mediaType = MediaType.MOVIE,
        genre = "Action • Crime • Drama",
        rating = 9.0,
        votes = "2.9M",
        year = 2008,
        duration = "2h 32m",
        synopsis = "When the menace known as the Joker wreaks havoc and chaos on Gotham City, Batman must accept one of the greatest tests.",
        cast = listOf("Christian Bale", "Heath Ledger", "Aaron Eckhart", "Michael Caine"),
        director = "Christopher Nolan",
        posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=1200&auto=format&fit=crop&q=80",
        trailerUrl = "https://www.youtube.com/watch?v=EXeTwQWrcwY",
        watchUrl = "https://www.netflix.com/search?q=The+Dark+Knight",
        qualityBadge = "4K Dolby Vision",
        isTrending = true
      )
    )

    val matched = allFallback.filter { item ->
      (platformFilter == OttPlatform.ALL || item.platform == platformFilter) &&
          (item.title.lowercase().contains(q) ||
              item.genre.lowercase().contains(q) ||
              item.cast.any { it.lowercase().contains(q) } ||
              item.director.lowercase().contains(q) ||
              q.contains(item.title.lowercase()))
    }

    if (matched.isNotEmpty()) {
      return matched
    }

    return emptyList()
  }
}

