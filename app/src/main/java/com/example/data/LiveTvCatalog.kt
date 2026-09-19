package com.example.data

import com.example.model.LiveCategory
import com.example.model.LiveChannel
import com.example.model.LiveProgram

object LiveTvCatalog {

  val channels: List<LiveChannel> = listOf(
    // ==========================================
    // 🔴 1. GLOBAL & REGIONAL LIVE NEWS (24/7)
    // ==========================================
    LiveChannel(
      id = "live_sky_news",
      name = "Sky News Live",
      network = "Sky Television UK",
      category = LiveCategory.NEWS,
      currentProgram = LiveProgram(
        title = "Sky World News Live & Deep Analysis",
        category = "Breaking News",
        startTime = "07:00 PM",
        endTime = "08:00 PM",
        progressPercent = 0.65f,
        description = "Live global news coverage, breaking market bulletins, and investigative reporting from London."
      ),
      nextProgram = LiveProgram(
        title = "The Global Hour with Mark Austin",
        category = "Special Report",
        startTime = "08:00 PM",
        endTime = "09:00 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=9Auq9mYxFEE",
      logoUrl = "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1504711434969-e33886168f5c?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p 60fps HD",
      viewersCount = "184K Live",
      countryCode = "GB",
      countryName = "United Kingdom",
      flagEmoji = "🇬🇧",
      language = "English",
      isLive = true,
      isFeatured = true
    ),
    LiveChannel(
      id = "live_abc_news_us",
      name = "ABC News Live",
      network = "ABC News America",
      category = LiveCategory.NEWS,
      currentProgram = LiveProgram(
        title = "ABC Prime Live Bulletin & Analysis",
        category = "World & Politics",
        startTime = "06:30 PM",
        endTime = "07:30 PM",
        progressPercent = 0.40f,
        description = "Continuous live news broadcast, real-time national tracking, and breaking international headlines."
      ),
      nextProgram = LiveProgram(
        title = "Nightline Live Tonight",
        category = "Investigative",
        startTime = "07:30 PM",
        endTime = "08:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=w_Ma8oQLmSM",
      logoUrl = "https://images.unsplash.com/photo-1504711434969-e33886168f5c?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1495020689067-958852a7765e?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p 60fps HD",
      viewersCount = "240K Live",
      countryCode = "US",
      countryName = "United States",
      flagEmoji = "🇺🇸",
      language = "English",
      isLive = true
    ),
    LiveChannel(
      id = "live_dw_news",
      name = "DW News Live",
      network = "Deutsche Welle Germany",
      category = LiveCategory.NEWS,
      currentProgram = LiveProgram(
        title = "DW Journal: Global Perspective & Europe",
        category = "World Affairs",
        startTime = "07:00 PM",
        endTime = "07:45 PM",
        progressPercent = 0.50f,
        description = "Unbiased global journalism, European geopolitics, science insights, and culture from Berlin."
      ),
      nextProgram = LiveProgram(
        title = "Conflict Zone: In-Depth Interviews",
        category = "Debate & Politics",
        startTime = "07:45 PM",
        endTime = "08:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=GE_SfNVNyqk",
      logoUrl = "https://images.unsplash.com/photo-1495020689067-958852a7765e?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p HD",
      viewersCount = "92K Live",
      countryCode = "DE",
      countryName = "Germany",
      flagEmoji = "🇩🇪",
      language = "English / German",
      isLive = true
    ),
    LiveChannel(
      id = "live_al_jazeera",
      name = "Al Jazeera English Live",
      network = "Al Jazeera Media Network",
      category = LiveCategory.NEWS,
      currentProgram = LiveProgram(
        title = "Newshour: Global Frontlines Report",
        category = "International Affairs",
        startTime = "06:00 PM",
        endTime = "07:00 PM",
        progressPercent = 0.70f,
        description = "On-the-ground investigative reporting and diverse international voices across the Middle East, Africa & Asia."
      ),
      nextProgram = LiveProgram(
        title = "Inside Story: The Big Debate",
        category = "In-Depth Analysis",
        startTime = "07:00 PM",
        endTime = "07:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=gCNeDWCI0vo",
      logoUrl = "https://images.unsplash.com/photo-1586339949916-3e9457bef6d3?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1586339949916-3e9457bef6d3?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p 60fps HD",
      viewersCount = "175K Live",
      countryCode = "QA",
      countryName = "Qatar",
      flagEmoji = "🇶🇦",
      language = "English",
      isLive = true
    ),
    LiveChannel(
      id = "live_france24_en",
      name = "France 24 English Live",
      network = "France Médias Monde",
      category = LiveCategory.NEWS,
      currentProgram = LiveProgram(
        title = "The World This Hour: Paris Bulletin",
        category = "World News",
        startTime = "07:00 PM",
        endTime = "08:00 PM",
        progressPercent = 0.35f,
        description = "French and European viewpoint on world events, diplomacy, culture, and African development."
      ),
      nextProgram = LiveProgram(
        title = "Eye on Africa: Continent Focus",
        category = "Regional News",
        startTime = "08:00 PM",
        endTime = "08:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=h3MuIUNCCzI",
      logoUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p HD",
      viewersCount = "88K Live",
      countryCode = "FR",
      countryName = "France",
      flagEmoji = "🇫🇷",
      language = "English / French",
      isLive = true
    ),
    LiveChannel(
      id = "live_nhk_world",
      name = "NHK World-Japan Live",
      network = "NHK Japan Broadcasting",
      category = LiveCategory.NEWS,
      currentProgram = LiveProgram(
        title = "Newsline Asia 24: Tokyo Headquarters",
        category = "Asian News",
        startTime = "06:30 PM",
        endTime = "07:00 PM",
        progressPercent = 0.80f,
        description = "Authoritative news from Japan and Asia, plus Japanese technology, culinary art, and lifestyle."
      ),
      nextProgram = LiveProgram(
        title = "Japanology Plus: Traditions & Future",
        category = "Culture & Science",
        startTime = "07:00 PM",
        endTime = "07:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www3.nhk.or.jp/nhkworld/en/live/",
      logoUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p 60fps HD",
      viewersCount = "132K Live",
      countryCode = "JP",
      countryName = "Japan",
      flagEmoji = "🇯🇵",
      language = "English / Japanese",
      isLive = true
    ),
    LiveChannel(
      id = "live_bloomberg_global",
      name = "Bloomberg Global Television",
      network = "Bloomberg Media US",
      category = LiveCategory.NEWS,
      currentProgram = LiveProgram(
        title = "Bloomberg Markets: Global Close",
        category = "Financial Markets",
        startTime = "06:00 PM",
        endTime = "07:30 PM",
        progressPercent = 0.55f,
        description = "Live Wall Street numbers, global bond markets, tech earnings, commodities, and Fed rate predictions."
      ),
      nextProgram = LiveProgram(
        title = "Bloomberg Technology: AI & Silicon Valley",
        category = "Tech News",
        startTime = "07:30 PM",
        endTime = "08:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=dp8PhLsUcFE",
      logoUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=1000&auto=format&fit=crop&q=80",
      resolution = "4K UHD 60fps",
      viewersCount = "310K Live",
      countryCode = "US",
      countryName = "United States",
      flagEmoji = "🇺🇸",
      language = "English",
      isLive = true,
      isFeatured = true
    ),
    LiveChannel(
      id = "live_wion_news",
      name = "WION: World Is One News",
      network = "Zee Media Network India",
      category = LiveCategory.NEWS,
      currentProgram = LiveProgram(
        title = "Gravitas: Global Geopolitics with Molly Gambhir",
        category = "Prime Time Analysis",
        startTime = "09:00 PM",
        endTime = "10:00 PM",
        progressPercent = 0.45f,
        description = "South Asia's premier international news channel delivering sharp analysis on global conflicts and diplomacy."
      ),
      nextProgram = LiveProgram(
        title = "World Order: Strategic Defense",
        category = "Geopolitics",
        startTime = "10:00 PM",
        endTime = "10:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=mC7x5q5P3fE",
      logoUrl = "https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p HD",
      viewersCount = "165K Live",
      countryCode = "IN",
      countryName = "India",
      flagEmoji = "🇮🇳",
      language = "English",
      isLive = true
    ),
    LiveChannel(
      id = "live_euronews_hd",
      name = "Euronews Live HD",
      network = "Euronews Europe",
      category = LiveCategory.NEWS,
      currentProgram = LiveProgram(
        title = "Europe Today: Brussels & Strasbourg",
        category = "European Union News",
        startTime = "07:00 PM",
        endTime = "07:30 PM",
        progressPercent = 0.30f,
        description = "Panoramic European perspective covering European Union developments, economy, environment, and world news."
      ),
      nextProgram = LiveProgram(
        title = "No Comment: Raw Footage",
        category = "Documentary",
        startTime = "07:30 PM",
        endTime = "08:00 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=pykdAUyMffU",
      logoUrl = "https://images.unsplash.com/photo-1467269204594-9661b134dd2b?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1467269204594-9661b134dd2b?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p HD",
      viewersCount = "95K Live",
      countryCode = "FR",
      countryName = "France / Europe",
      flagEmoji = "🇪🇺",
      language = "English / Multi",
      isLive = true
    ),
    LiveChannel(
      id = "live_cna_singapore",
      name = "CNA Channel NewsAsia Live",
      network = "Mediacorp Singapore",
      category = LiveCategory.NEWS,
      currentProgram = LiveProgram(
        title = "Asia First: Singapore Morning Edition",
        category = "Pan-Asian News",
        startTime = "07:00 AM",
        endTime = "09:00 AM",
        progressPercent = 0.60f,
        description = "Top stories across ASEAN, East Asia, and global trade corridors directly from Singapore MediaCity."
      ),
      nextProgram = LiveProgram(
        title = "Insight: Asian Economies Under Watch",
        category = "Documentary",
        startTime = "09:00 AM",
        endTime = "10:00 AM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=XWq5kBlakcQ",
      logoUrl = "https://images.unsplash.com/photo-1525625293386-3f8f99389edd?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1525625293386-3f8f99389edd?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p 60fps HD",
      viewersCount = "118K Live",
      countryCode = "SG",
      countryName = "Singapore",
      flagEmoji = "🇸🇬",
      language = "English",
      isLive = true
    ),
    LiveChannel(
      id = "live_arirang_korea",
      name = "Arirang TV Live",
      network = "Korea International Broadcasting",
      category = LiveCategory.NEWS,
      currentProgram = LiveProgram(
        title = "Arirang News: Seoul Prime Bulletin",
        category = "Korean & Asian Affairs",
        startTime = "07:00 PM",
        endTime = "08:00 PM",
        progressPercent = 0.50f,
        description = "Live reports on the Korean Peninsula, K-Culture, high-tech industry, and Asian diplomacy."
      ),
      nextProgram = LiveProgram(
        title = "Simply K-Pop: Live Stage",
        category = "Music & Entertainment",
        startTime = "08:00 PM",
        endTime = "09:00 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=arirang_live",
      logoUrl = "https://images.unsplash.com/photo-1517154421773-0529f29ea451?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1517154421773-0529f29ea451?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p HD",
      viewersCount = "85K Live",
      countryCode = "KR",
      countryName = "South Korea",
      flagEmoji = "🇰🇷",
      language = "English / Korean",
      isLive = true
    ),
    LiveChannel(
      id = "live_cbc_canada",
      name = "CBC News Explore",
      network = "Canadian Broadcasting Corporation",
      category = LiveCategory.NEWS,
      currentProgram = LiveProgram(
        title = "The National Live: Canadian Perspective",
        category = "National News",
        startTime = "09:00 PM",
        endTime = "10:00 PM",
        progressPercent = 0.40f,
        description = "Canada's flagship news broadcast covering North American politics, Arctic climate, and global headlines."
      ),
      nextProgram = LiveProgram(
        title = "Power & Politics Live Debate",
        category = "Political Analysis",
        startTime = "10:00 PM",
        endTime = "11:00 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.cbc.ca/news",
      logoUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p HD",
      viewersCount = "72K Live",
      countryCode = "CA",
      countryName = "Canada",
      flagEmoji = "🇨🇦",
      language = "English",
      isLive = true
    ),
    LiveChannel(
      id = "live_abc_australia",
      name = "ABC News Australia Live",
      network = "Australian Broadcasting Corporation",
      category = LiveCategory.NEWS,
      currentProgram = LiveProgram(
        title = "ABC News Tonight: Sydney Direct",
        category = "Pacific & World News",
        startTime = "07:00 PM",
        endTime = "08:00 PM",
        progressPercent = 0.55f,
        description = "Live coverage from across the Australian continent and the South Pacific rim."
      ),
      nextProgram = LiveProgram(
        title = "7.30 Report with Sarah Ferguson",
        category = "Investigative Journalism",
        startTime = "08:00 PM",
        endTime = "08:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=W1ilCy6JhQo",
      logoUrl = "https://images.unsplash.com/photo-1523482580672-f109ba8cb9be?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1523482580672-f109ba8cb9be?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p HD",
      viewersCount = "68K Live",
      countryCode = "AU",
      countryName = "Australia",
      flagEmoji = "🇦🇺",
      language = "English",
      isLive = true
    ),
    LiveChannel(
      id = "live_africanews",
      name = "Africanews Live",
      network = "Africanews Pan-African",
      category = LiveCategory.NEWS,
      currentProgram = LiveProgram(
        title = "The Morning Call: Across the Continent",
        category = "Pan-African News",
        startTime = "08:00 AM",
        endTime = "09:30 AM",
        progressPercent = 0.35f,
        description = "Pan-African independent news covering 54 countries: economics, innovation, culture, and governance."
      ),
      nextProgram = LiveProgram(
        title = "Inspire Africa: Tech & Startups",
        category = "Business & Culture",
        startTime = "09:30 AM",
        endTime = "10:00 AM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=africanews_live",
      logoUrl = "https://images.unsplash.com/photo-1547471080-7cc2caa01a7e?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1547471080-7cc2caa01a7e?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p HD",
      viewersCount = "64K Live",
      countryCode = "ZA",
      countryName = "South Africa / Africa",
      flagEmoji = "🌍",
      language = "English / French",
      isLive = true
    ),

    // ==========================================
    // ⚽ 2. WORLD SPORTS LIVE (24/7)
    // ==========================================
    LiveChannel(
      id = "live_redbull_tv",
      name = "Red Bull TV 4K Sports",
      network = "Red Bull Media House",
      category = LiveCategory.SPORTS,
      currentProgram = LiveProgram(
        title = "Red Bull Rampage: Extreme Mountain Biking Final",
        category = "Action Sports 4K",
        startTime = "06:00 PM",
        endTime = "08:30 PM",
        progressPercent = 0.72f,
        description = "World's top freeriders take on the cliffs of Utah in breathtaking 4K HDR broadcast with multi-camera angles."
      ),
      nextProgram = LiveProgram(
        title = "Red Bull Cliff Diving: World Series Polignano",
        category = "Extreme Diving",
        startTime = "08:30 PM",
        endTime = "10:00 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.redbull.com/us-en/live-events",
      logoUrl = "https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=1000&auto=format&fit=crop&q=80",
      resolution = "4K UHD 60fps HDR",
      viewersCount = "420K Live",
      countryCode = "AT",
      countryName = "Austria / Global",
      flagEmoji = "🇦🇹",
      language = "English",
      isLive = true,
      isFeatured = true
    ),
    LiveChannel(
      id = "live_wpt_poker",
      name = "World Poker Tour Live 24/7",
      network = "WPT Enterprises US",
      category = LiveCategory.SPORTS,
      currentProgram = LiveProgram(
        title = "WPT World Championship: High Roller Final Table",
        category = "Poker & Gaming",
        startTime = "05:00 PM",
        endTime = "09:00 PM",
        progressPercent = 0.50f,
        description = "All-in action with open hole cards, pro commentary, and millions of dollars on the line at Bellagio Las Vegas."
      ),
      nextProgram = LiveProgram(
        title = "Best of WPT: Legend Heads-Up Battles",
        category = "Classics",
        startTime = "09:00 PM",
        endTime = "11:00 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.worldpokertour.com",
      logoUrl = "https://images.unsplash.com/photo-1511193311914-0346f16efe90?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1511193311914-0346f16efe90?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p 60fps HD",
      viewersCount = "95K Live",
      countryCode = "US",
      countryName = "United States",
      flagEmoji = "🇺🇸",
      language = "English",
      isLive = true
    ),
    LiveChannel(
      id = "live_fight_network",
      name = "Fight Network Combat Live",
      network = "Anthem Sports & Entertainment",
      category = LiveCategory.SPORTS,
      currentProgram = LiveProgram(
        title = "MMA Champions: World Title Clash Live",
        category = "Combat Sports",
        startTime = "07:00 PM",
        endTime = "10:00 PM",
        progressPercent = 0.35f,
        description = "Complete combat sports coverage: MMA title fights, boxing knockouts, kickboxing, and grappling super-fights."
      ),
      nextProgram = LiveProgram(
        title = "Muay Thai Grand Prix Highlights",
        category = "Kickboxing",
        startTime = "10:00 PM",
        endTime = "11:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://fightnetwork.com",
      logoUrl = "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p 60fps HD",
      viewersCount = "142K Live",
      countryCode = "US",
      countryName = "United States",
      flagEmoji = "🇺🇸",
      language = "English",
      isLive = true
    ),
    LiveChannel(
      id = "live_edge_sport",
      name = "Edge Sport Extreme",
      network = "IMG Worldwide",
      category = LiveCategory.SPORTS,
      currentProgram = LiveProgram(
        title = "World Surf League: Teahupo'o Monster Swell",
        category = "Surfing Live",
        startTime = "06:00 PM",
        endTime = "08:00 PM",
        progressPercent = 0.60f,
        description = "Live extreme sport highlights: massive barrel surfing, freestyle motocross, downhill skiing & skateboarding."
      ),
      nextProgram = LiveProgram(
        title = "X Games Greatest Moments 2026",
        category = "Action Sports",
        startTime = "08:00 PM",
        endTime = "09:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=edge_sport",
      logoUrl = "https://images.unsplash.com/photo-1502680390469-be75c86b636f?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1502680390469-be75c86b636f?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p 60fps HD",
      viewersCount = "110K Live",
      countryCode = "GB",
      countryName = "United Kingdom / Global",
      flagEmoji = "🇬🇧",
      language = "English",
      isLive = true
    ),
    LiveChannel(
      id = "live_motorvision",
      name = "Motorvision Racing TV",
      network = "Motorvision Group Germany",
      category = LiveCategory.SPORTS,
      currentProgram = LiveProgram(
        title = "Nürburgring 24h Endurance Challenge",
        category = "Motorsport Racing",
        startTime = "05:00 PM",
        endTime = "08:00 PM",
        progressPercent = 0.45f,
        description = "Supercars, GT3 endurance championships, drifting competitions, and Formula racing telemetry."
      ),
      nextProgram = LiveProgram(
        title = "Supercar Drag Battles: Porsche vs Ferrari",
        category = "Automotive",
        startTime = "08:00 PM",
        endTime = "09:00 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://motorvision.tv",
      logoUrl = "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p 60fps",
      viewersCount = "82K Live",
      countryCode = "DE",
      countryName = "Germany",
      flagEmoji = "🇩🇪",
      language = "English / German",
      isLive = true
    ),

    // ==========================================
    // 🎬 3. MOVIES & CINEMA 24/7
    // ==========================================
    LiveChannel(
      id = "live_rakuten_cinema",
      name = "Rakuten TV Action Cinema",
      network = "Rakuten TV Europe",
      category = LiveCategory.MOVIES,
      currentProgram = LiveProgram(
        title = "The Sentinel: Uncut High-Octane Action",
        category = "Blockbuster Action",
        startTime = "06:15 PM",
        endTime = "08:15 PM",
        progressPercent = 0.55f,
        description = "Non-stop Hollywood and international action cinema, martial arts thrillers, and explosive blockbusters 24/7."
      ),
      nextProgram = LiveProgram(
        title = "Midnight Sniper: Special Ops Mission",
        category = "Action Thriller",
        startTime = "08:15 PM",
        endTime = "10:00 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://rakuten.tv",
      logoUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p Surround 5.1",
      viewersCount = "156K Live",
      countryCode = "ES",
      countryName = "Spain / Europe",
      flagEmoji = "🇪🇺",
      language = "English / Spanish",
      isLive = true,
      isFeatured = true
    ),
    LiveChannel(
      id = "live_filmrise_movies",
      name = "FilmRise Free Movies 24/7",
      network = "FilmRise New York",
      category = LiveCategory.MOVIES,
      currentProgram = LiveProgram(
        title = "The Illusionist (Starring Edward Norton)",
        category = "Drama & Mystery",
        startTime = "06:30 PM",
        endTime = "08:45 PM",
        progressPercent = 0.40f,
        description = "In turn-of-the-century Vienna, a magician uses his abilities to secure the love of a woman far above his social standing."
      ),
      nextProgram = LiveProgram(
        title = "Memento: Christopher Nolan Masterpiece",
        category = "Psychological Thriller",
        startTime = "08:45 PM",
        endTime = "10:45 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://filmrise.com",
      logoUrl = "https://images.unsplash.com/photo-1518791841217-8f162f1e1131?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1518791841217-8f162f1e1131?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p HD",
      viewersCount = "198K Live",
      countryCode = "US",
      countryName = "United States",
      flagEmoji = "🇺🇸",
      language = "English",
      isLive = true
    ),
    LiveChannel(
      id = "live_bollywood_hd",
      name = "Bollywood Cinema HD 24/7",
      network = "Zee Entertainment India",
      category = LiveCategory.MOVIES,
      currentProgram = LiveProgram(
        title = "Sholay: 4K Restored Anniversary Edition",
        category = "Epic Cinema",
        startTime = "06:00 PM",
        endTime = "09:30 PM",
        progressPercent = 0.65f,
        description = "The ultimate Indian curry-western masterpiece starring Amitabh Bachchan, Dharmendra, and Amjad Khan in crystal 4K."
      ),
      nextProgram = LiveProgram(
        title = "Dilwale Dulhania Le Jayenge (DDLJ)",
        category = "Romance & Drama",
        startTime = "09:30 PM",
        endTime = "12:30 AM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.zee5.com",
      logoUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=1000&auto=format&fit=crop&q=80",
      resolution = "4K Remastered",
      viewersCount = "260K Live",
      countryCode = "IN",
      countryName = "India",
      flagEmoji = "🇮🇳",
      language = "Hindi / Subtitles",
      isLive = true
    ),
    LiveChannel(
      id = "live_pluto_cine",
      name = "Pluto TV Cine 24/7",
      network = "Paramount Global",
      category = LiveCategory.MOVIES,
      currentProgram = LiveProgram(
        title = "Pulp Fiction: Tarantino Classic",
        category = "Cult Classic",
        startTime = "07:00 PM",
        endTime = "09:30 PM",
        progressPercent = 0.35f,
        description = "The lives of two mob hitmen, a boxer, a gangster and his wife intertwine in four tales of violence and redemption."
      ),
      nextProgram = LiveProgram(
        title = "Fight Club Uncut",
        category = "Cult Drama",
        startTime = "09:30 PM",
        endTime = "11:50 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://pluto.tv",
      logoUrl = "https://images.unsplash.com/photo-1478720568477-152d9b164e26?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1478720568477-152d9b164e26?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p HD",
      viewersCount = "215K Live",
      countryCode = "US",
      countryName = "United States",
      flagEmoji = "🇺🇸",
      language = "English",
      isLive = true
    ),

    // ==========================================
    // 🍿 4. ENTERTAINMENT & LIFESTYLE
    // ==========================================
    LiveChannel(
      id = "live_fashion_tv",
      name = "Fashion TV Global 4K",
      network = "FTV International Paris",
      category = LiveCategory.ENTERTAINMENT,
      currentProgram = LiveProgram(
        title = "Paris & Milan Haute Couture Fashion Week",
        category = "Runway & Haute Couture",
        startTime = "07:00 PM",
        endTime = "08:30 PM",
        progressPercent = 0.50f,
        description = "Front row live access to legendary fashion houses: Chanel, Dior, Gucci, and Yves Saint Laurent in pristine 4K."
      ),
      nextProgram = LiveProgram(
        title = "Top Models Worldwide & Luxury Lifestyle",
        category = "Glamour",
        startTime = "08:30 PM",
        endTime = "09:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.fashiontv.com",
      logoUrl = "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=1000&auto=format&fit=crop&q=80",
      resolution = "4K UHD 60fps",
      viewersCount = "168K Live",
      countryCode = "FR",
      countryName = "France",
      flagEmoji = "🇫🇷",
      language = "English / French",
      isLive = true
    ),
    LiveChannel(
      id = "live_travelxp_4k",
      name = "Travelxp 4K HDR",
      network = "Travelxp Global Media",
      category = LiveCategory.ENTERTAINMENT,
      currentProgram = LiveProgram(
        title = "Wonders of the World: Patagonia & Andes",
        category = "Travel Documentary",
        startTime = "06:30 PM",
        endTime = "07:30 PM",
        progressPercent = 0.40f,
        description = "World's premier 4K HDR travel channel exploring remote culinary gems, ancient temples, and luxury hotels."
      ),
      nextProgram = LiveProgram(
        title = "Food Highway: Street Cuisines of Tokyo & Bangkok",
        category = "Culinary Travel",
        startTime = "07:30 PM",
        endTime = "08:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://travelxp.com",
      logoUrl = "https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=1000&auto=format&fit=crop&q=80",
      resolution = "4K UHD HDR",
      viewersCount = "125K Live",
      countryCode = "IN",
      countryName = "India / Global",
      flagEmoji = "🌐",
      language = "English",
      isLive = true
    ),
    LiveChannel(
      id = "live_just_for_laughs",
      name = "Just For Laughs Gags 24/7",
      network = "JFL Comedy Canada",
      category = LiveCategory.ENTERTAINMENT,
      currentProgram = LiveProgram(
        title = "World's Funniest Hidden Camera Pranks",
        category = "Comedy & Gags",
        startTime = "07:00 PM",
        endTime = "08:00 PM",
        progressPercent = 0.70f,
        description = "Hilarious silent comedy pranks filmed on the streets of Montreal, Vancouver, and around the world."
      ),
      nextProgram = LiveProgram(
        title = "Best of JFL Stand-Up Stars",
        category = "Standup Comedy",
        startTime = "08:00 PM",
        endTime = "09:00 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=justforlaughs_live",
      logoUrl = "https://images.unsplash.com/photo-1514306191717-452ec28c7814?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1514306191717-452ec28c7814?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p HD",
      viewersCount = "192K Live",
      countryCode = "CA",
      countryName = "Canada",
      flagEmoji = "🇨🇦",
      language = "Universal",
      isLive = true
    ),

    // ==========================================
    // 🚀 5. TECH, SCIENCE & NATURE
    // ==========================================
    LiveChannel(
      id = "live_nasa_tv",
      name = "NASA TV 4K ISS Earth Feeds",
      network = "National Aeronautics and Space Administration",
      category = LiveCategory.SCIENCE,
      currentProgram = LiveProgram(
        title = "ISS Live Ultra-HD Earth Orbit Stream",
        category = "Space Science 4K",
        startTime = "05:00 PM",
        endTime = "09:00 PM",
        progressPercent = 0.60f,
        description = "Breathtaking real-time live views of planet Earth from the International Space Station flying 250 miles above."
      ),
      nextProgram = LiveProgram(
        title = "Artemis Moon Mission: Lunar Launch Preparation",
        category = "Deep Space",
        startTime = "09:00 PM",
        endTime = "11:00 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=21X5lGlDOfg",
      logoUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=1000&auto=format&fit=crop&q=80",
      resolution = "4K UHD 60fps",
      viewersCount = "520K Live",
      countryCode = "US",
      countryName = "United States",
      flagEmoji = "🇺🇸",
      language = "English",
      isLive = true,
      isFeatured = true
    ),
    LiveChannel(
      id = "live_love_nature",
      name = "Love Nature 4K Wildlife",
      network = "Blue Ant Media Global",
      category = LiveCategory.SCIENCE,
      currentProgram = LiveProgram(
        title = "Serengeti Migration: Lions & Cheetahs",
        category = "Wildlife 4K",
        startTime = "06:30 PM",
        endTime = "08:00 PM",
        progressPercent = 0.45f,
        description = "World-class wildlife cinematographers bring you up close with big cats, ocean giants, and rainforest ecosystems."
      ),
      nextProgram = LiveProgram(
        title = "Secret Life of the Coral Reefs",
        category = "Marine Biology",
        startTime = "08:00 PM",
        endTime = "09:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://lovenature.com",
      logoUrl = "https://images.unsplash.com/photo-1534177616072-ef7dc120449d?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1534177616072-ef7dc120449d?w=1000&auto=format&fit=crop&q=80",
      resolution = "4K UHD HDR",
      viewersCount = "138K Live",
      countryCode = "CA",
      countryName = "Canada / Global",
      flagEmoji = "🌍",
      language = "English",
      isLive = true
    ),

    // ==========================================
    // 🎵 6. MUSIC CHANNELS WORLDWIDE
    // ==========================================
    LiveChannel(
      id = "live_lofi_girl",
      name = "Lofi Girl 24/7 Relax Beats",
      network = "Lofi Records Paris",
      category = LiveCategory.MUSIC,
      currentProgram = LiveProgram(
        title = "beats to relax/study to (24/7 Continuous)",
        category = "Lo-Fi & Ambient",
        startTime = "All Day",
        endTime = "All Night",
        progressPercent = 0.75f,
        description = "The iconic worldwide 24/7 continuous stream of peaceful lo-fi hip hop, cozy anime visual art, and ambient study sounds."
      ),
      nextProgram = LiveProgram(
        title = "Synthwave Chill: Synth & Retro Sunset",
        category = "Retro Electro",
        startTime = "Midnight",
        endTime = "Dawn",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=jfKfPfyJRdk",
      logoUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p Lossless Audio",
      viewersCount = "480K Live",
      countryCode = "FR",
      countryName = "France / Global",
      flagEmoji = "🎧",
      language = "Instrumental",
      isLive = true,
      isFeatured = true
    ),
    LiveChannel(
      id = "live_club_mtv",
      name = "Club MTV Dance Live 24/7",
      network = "Paramount International UK",
      category = LiveCategory.MUSIC,
      currentProgram = LiveProgram(
        title = "Ibiza Club Anthems: Non-Stop Dancefloor Hits",
        category = "Electronic & Dance",
        startTime = "07:00 PM",
        endTime = "10:00 PM",
        progressPercent = 0.50f,
        description = "Biggest EDM club anthems, house, techno, and electronic festival sets from Tomorrowland, Creamfields, and Ultra."
      ),
      nextProgram = LiveProgram(
        title = "Late Night Club Sessions: Deep House",
        category = "Club Mix",
        startTime = "10:00 PM",
        endTime = "02:00 AM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.mtv.co.uk",
      logoUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p 60fps HD",
      viewersCount = "172K Live",
      countryCode = "GB",
      countryName = "United Kingdom",
      flagEmoji = "🇬🇧",
      language = "English",
      isLive = true
    ),
    LiveChannel(
      id = "live_kpop_live",
      name = "K-Pop Idol Live Seoul 24/7",
      network = "CJ ENM South Korea",
      category = LiveCategory.MUSIC,
      currentProgram = LiveProgram(
        title = "K-Pop Countdown: BTS, BLACKPINK, NewJeans & Stray Kids",
        category = "K-Pop Charts",
        startTime = "06:00 PM",
        endTime = "08:30 PM",
        progressPercent = 0.65f,
        description = "Official K-Pop music videos, Mnet Asian Music Awards performances, choreography spotlights, and Seoul concert feeds."
      ),
      nextProgram = LiveProgram(
        title = "M Countdown Special Stage Live",
        category = "Live Concert",
        startTime = "08:30 PM",
        endTime = "10:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=kpop_live",
      logoUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=1000&auto=format&fit=crop&q=80",
      resolution = "4K UHD 60fps",
      viewersCount = "385K Live",
      countryCode = "KR",
      countryName = "South Korea",
      flagEmoji = "🇰🇷",
      language = "Korean / Subtitles",
      isLive = true,
      isFeatured = true
    ),
    LiveChannel(
      id = "live_deluxe_music",
      name = "Deluxe Music Germany",
      network = "High View Germany",
      category = LiveCategory.MUSIC,
      currentProgram = LiveProgram(
        title = "Deluxe Disco: Euro-Dance & Pop Gold",
        category = "Pop Classics",
        startTime = "07:00 PM",
        endTime = "09:00 PM",
        progressPercent = 0.40f,
        description = "High-definition music video curation spanning 80s synth, 90s eurodance, and contemporary European chart-toppers."
      ),
      nextProgram = LiveProgram(
        title = "Nightflight: Acoustic & Chill",
        category = "Acoustic Lounge",
        startTime = "09:00 PM",
        endTime = "11:00 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://deluxemusic.de",
      logoUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p HD",
      viewersCount = "92K Live",
      countryCode = "DE",
      countryName = "Germany",
      flagEmoji = "🇩🇪",
      language = "German / English",
      isLive = true
    ),

    // ==========================================
    // 🧸 7. KIDS & ANIME WORLDWIDE
    // ==========================================
    LiveChannel(
      id = "live_anime_stream",
      name = "Tokyo Anime All-Day Live",
      network = "Tokyo Anime Broadcast Network",
      category = LiveCategory.KIDS,
      currentProgram = LiveProgram(
        title = "Classic Shonen Marathon: English Subbed & Dubbed",
        category = "Anime 24/7",
        startTime = "06:00 PM",
        endTime = "08:30 PM",
        progressPercent = 0.45f,
        description = "Non-stop anime episodes straight from Tokyo animation studios: action battles, slice of life, and fantasy adventures."
      ),
      nextProgram = LiveProgram(
        title = "Studio Ghibli Ambient Soundtracks & Art",
        category = "Anime Music",
        startTime = "08:30 PM",
        endTime = "10:00 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://www.youtube.com/watch?v=anime_stream",
      logoUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p 60fps HD",
      viewersCount = "290K Live",
      countryCode = "JP",
      countryName = "Japan",
      flagEmoji = "🇯🇵",
      language = "Japanese / English Sub",
      isLive = true,
      isFeatured = true
    ),
    LiveChannel(
      id = "live_toonami_retro",
      name = "Toonami Aftermath Retro 24/7",
      network = "Warner Bros. Discovery",
      category = LiveCategory.KIDS,
      currentProgram = LiveProgram(
        title = "Dragon Ball Z & Sailor Moon Classic Block",
        category = "Retro Animation",
        startTime = "06:00 PM",
        endTime = "08:00 PM",
        progressPercent = 0.60f,
        description = "Relive the golden era of 90s and 2000s action animation: Dragon Ball Z, Gundam Wing, Yu Yu Hakusho, and Batman TAS."
      ),
      nextProgram = LiveProgram(
        title = "Cowboy Bebop Uncut Late Night",
        category = "Sci-Fi Anime",
        startTime = "08:00 PM",
        endTime = "09:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://toonamiaftermath.com",
      logoUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p HD",
      viewersCount = "145K Live",
      countryCode = "US",
      countryName = "United States",
      flagEmoji = "🇺🇸",
      language = "English",
      isLive = true
    ),
    LiveChannel(
      id = "live_gulli_kids",
      name = "Gulli Kids & Family Live",
      network = "M6 Group France",
      category = LiveCategory.KIDS,
      currentProgram = LiveProgram(
        title = "Miraculous: Tales of Ladybug & Cat Noir",
        category = "Kids Animation",
        startTime = "06:30 PM",
        endTime = "07:30 PM",
        progressPercent = 0.50f,
        description = "Top animated family series, educational youth adventures, and cartoons beloved by children across Europe."
      ),
      nextProgram = LiveProgram(
        title = "Oggy and the Cockroaches Marathon",
        category = "Slapstick Cartoons",
        startTime = "07:30 PM",
        endTime = "08:30 PM",
        progressPercent = 0.0f
      ),
      streamUrl = "https://gulli.fr",
      logoUrl = "https://images.unsplash.com/photo-1566438480900-0609be27a4be?w=300&auto=format&fit=crop&q=80",
      bannerUrl = "https://images.unsplash.com/photo-1566438480900-0609be27a4be?w=1000&auto=format&fit=crop&q=80",
      resolution = "1080p HD",
      viewersCount = "98K Live",
      countryCode = "FR",
      countryName = "France / Europe",
      flagEmoji = "🇫🇷",
      language = "French / English",
      isLive = true
    )
  )

  fun generateCountryChannels(countryCode: String, name: String, flag: String): List<LiveChannel> {
    val cleanCode = countryCode.lowercase()
    return listOf(
      LiveChannel(
        id = "live_${cleanCode}_nat1",
        name = "$name National 1 HD",
        network = "$name State Telecommunications",
        category = LiveCategory.ENTERTAINMENT,
        currentProgram = LiveProgram(
          title = "$name Prime: National Series & Culture",
          category = "National Prime",
          startTime = "08:00 PM",
          endTime = "09:30 PM",
          progressPercent = 0.45f,
          description = "Premier 24/7 flagship channel from $name featuring top national dramas, variety shows, award galas, and cultural documentaries."
        ),
        nextProgram = LiveProgram(
          title = "Tonight in $name: Night Talk",
          category = "Late Night Variety",
          startTime = "09:30 PM",
          endTime = "10:30 PM",
          progressPercent = 0.0f
        ),
        streamUrl = "https://www.youtube.com/results?search_query=${name.replace(' ', '+')}+live+tv",
        logoUrl = "https://images.unsplash.com/photo-1598899134739-24c46f58b8c0?w=300&auto=format&fit=crop&q=80",
        bannerUrl = "https://images.unsplash.com/photo-1598899134739-24c46f58b8c0?w=1000&auto=format&fit=crop&q=80",
        resolution = "1080p 60fps HD",
        viewersCount = "112K Live",
        countryCode = countryCode,
        countryName = name,
        flagEmoji = flag,
        language = "National Language",
        isLive = true,
        isFeatured = true
      ),
      LiveChannel(
        id = "live_${cleanCode}_news24",
        name = "$name 24/7 News",
        network = "$name News Network",
        category = LiveCategory.NEWS,
        currentProgram = LiveProgram(
          title = "$name Live Bulletin & World Affairs",
          category = "Breaking News",
          startTime = "07:00 PM",
          endTime = "08:00 PM",
          progressPercent = 0.60f,
          description = "Continuous round-the-clock news coverage, live parliament debates, financial markets, and geopolitical analysis from $name."
        ),
        nextProgram = LiveProgram(
          title = "Global Perspective: $name & World",
          category = "International Affairs",
          startTime = "08:00 PM",
          endTime = "08:45 PM",
          progressPercent = 0.0f
        ),
        streamUrl = "https://www.youtube.com/results?search_query=${name.replace(' ', '+')}+news+live",
        logoUrl = "https://images.unsplash.com/photo-1504711434969-e33886168f5c?w=300&auto=format&fit=crop&q=80",
        bannerUrl = "https://images.unsplash.com/photo-1504711434969-e33886168f5c?w=1000&auto=format&fit=crop&q=80",
        resolution = "1080p HD",
        viewersCount = "95K Live",
        countryCode = countryCode,
        countryName = name,
        flagEmoji = flag,
        language = "National Language",
        isLive = true
      ),
      LiveChannel(
        id = "live_${cleanCode}_sports",
        name = "$name Sports Live",
        network = "$name Sports Telecast",
        category = LiveCategory.SPORTS,
        currentProgram = LiveProgram(
          title = "$name National Championship Live",
          category = "Live Sports",
          startTime = "06:30 PM",
          endTime = "08:30 PM",
          progressPercent = 0.70f,
          description = "Live telecast of premier national athletics, league matches, football championships, and racing tournaments across $name."
        ),
        nextProgram = LiveProgram(
          title = "Sports Highlights & Match Analysis",
          category = "Highlights",
          startTime = "08:30 PM",
          endTime = "09:15 PM",
          progressPercent = 0.0f
        ),
        streamUrl = "https://www.youtube.com/results?search_query=${name.replace(' ', '+')}+sports+live",
        logoUrl = "https://images.unsplash.com/photo-1461896836934-ffe607ba8211?w=300&auto=format&fit=crop&q=80",
        bannerUrl = "https://images.unsplash.com/photo-1461896836934-ffe607ba8211?w=1000&auto=format&fit=crop&q=80",
        resolution = "1080p 60fps HD",
        viewersCount = "140K Live",
        countryCode = countryCode,
        countryName = name,
        flagEmoji = flag,
        language = "National Language",
        isLive = true
      )
    )
  }

  val allWorldwideChannels: List<LiveChannel> by lazy {
    val existingIds = channels.map { it.id }.toSet()
    val all195CountriesChannels = WorldVpnCountries.servers.flatMap { server ->
      generateCountryChannels(server.countryCode, server.countryName, server.flagEmoji)
    }.filter { it.id !in existingIds }

    channels + all195CountriesChannels
  }

  val worldCountriesList: List<Pair<String, String>> by lazy {
    listOf("All Countries" to "🌐") + WorldVpnCountries.servers.map { it.countryName to it.flagEmoji }
  }

  fun getChannelsForCountry(countryCode: String): List<LiveChannel> {
    if (countryCode == "GLOBAL" || countryCode.isBlank()) {
      return allWorldwideChannels
    }
    val existing = channels.filter { it.countryCode.equals(countryCode, ignoreCase = true) }
    val server = WorldVpnCountries.servers.firstOrNull { it.countryCode.equals(countryCode, ignoreCase = true) }
    val name = server?.countryName ?: countryCode
    val flag = server?.flagEmoji ?: "🌐"
    val generated = generateCountryChannels(countryCode, name, flag)
    val existingIds = existing.map { it.id }.toSet()

    return existing + generated.filter { it.id !in existingIds }
  }

  fun getChannelsForRegion(countryCode: String): List<LiveChannel> {
    if (countryCode == "GLOBAL" || countryCode.isBlank()) {
      return allWorldwideChannels
    }
    val local = getChannelsForCountry(countryCode)
    val localIds = local.map { it.id }.toSet()
    val others = allWorldwideChannels.filter { it.id !in localIds }
    return local + others
  }
}
