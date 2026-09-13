package com.example.model

data class PlatformPlan(
  val id: String,
  val platform: OttPlatform,
  val planName: String,
  val priceDisplay: String,
  val period: String,
  val resolution: String,
  val devicesSupported: String,
  val screensCount: Int,
  val audioQuality: String,
  val adPolicy: String,
  val perks: List<String>,
  val isPopular: Boolean = false,
  val isBestValue: Boolean = false,
  val subscribeUrl: String
)

object PlatformPlansRepository {
  val allPlans: List<PlatformPlan> = listOf(
    // NETFLIX
    PlatformPlan(
      id = "netflix_mobile",
      platform = OttPlatform.NETFLIX,
      planName = "Mobile",
      priceDisplay = "₹149",
      period = "/month",
      resolution = "480p / 720p HD",
      devicesSupported = "Phone & Tablet only",
      screensCount = 1,
      audioQuality = "Stereo Sound",
      adPolicy = "100% Ad-Free",
      perks = listOf(
        "Watch all Netflix movies & shows",
        "Download on 1 phone or tablet",
        "Unlimited ad-free mobile entertainment"
      ),
      subscribeUrl = "https://www.netflix.com/signup"
    ),
    PlatformPlan(
      id = "netflix_basic",
      platform = OttPlatform.NETFLIX,
      planName = "Basic",
      priceDisplay = "₹199",
      period = "/month",
      resolution = "720p HD",
      devicesSupported = "TV, Computer, Phone, Tablet",
      screensCount = 1,
      audioQuality = "Stereo Sound",
      adPolicy = "100% Ad-Free",
      perks = listOf(
        "Watch on your Smart TV or laptop",
        "Download on 1 supported device",
        "Full unlimited Netflix catalog"
      ),
      subscribeUrl = "https://www.netflix.com/signup"
    ),
    PlatformPlan(
      id = "netflix_standard",
      platform = OttPlatform.NETFLIX,
      planName = "Standard",
      priceDisplay = "₹499",
      period = "/month",
      resolution = "1080p Full HD",
      devicesSupported = "TV, Computer, Phone, Tablet",
      screensCount = 2,
      audioQuality = "Spatial Audio",
      adPolicy = "100% Ad-Free",
      perks = listOf(
        "Watch on 2 screens at the same time",
        "Full HD crisp resolution",
        "Download on 2 supported devices"
      ),
      isPopular = true,
      subscribeUrl = "https://www.netflix.com/signup"
    ),
    PlatformPlan(
      id = "netflix_premium",
      platform = OttPlatform.NETFLIX,
      planName = "Premium 4K",
      priceDisplay = "₹649",
      period = "/month",
      resolution = "4K Ultra HD + HDR",
      devicesSupported = "TV, Computer, Phone, Tablet",
      screensCount = 4,
      audioQuality = "Dolby Atmos & Spatial Audio",
      adPolicy = "100% Ad-Free",
      perks = listOf(
        "Watch on 4 screens at the same time",
        "Best video quality: 4K Ultra HD + HDR",
        "Netflix Spatial Audio included",
        "Download on up to 6 devices"
      ),
      isBestValue = true,
      subscribeUrl = "https://www.netflix.com/signup"
    ),

    // PRIME VIDEO
    PlatformPlan(
      id = "prime_monthly",
      platform = OttPlatform.PRIME_VIDEO,
      planName = "Prime Monthly",
      priceDisplay = "₹299",
      period = "/month",
      resolution = "4K Ultra HD + HDR",
      devicesSupported = "All Smart TVs, PC, Mobile",
      screensCount = 3,
      audioQuality = "Dolby Atmos / 5.1",
      adPolicy = "Ad-Free Streaming",
      perks = listOf(
        "Watch on 3 devices simultaneously",
        "Prime Video + Amazon Music streaming",
        "Free fast Amazon Shopping delivery",
        "Prime Gaming exclusive perks"
      ),
      subscribeUrl = "https://www.amazon.in/prime"
    ),
    PlatformPlan(
      id = "prime_annual",
      platform = OttPlatform.PRIME_VIDEO,
      planName = "Prime Annual",
      priceDisplay = "₹1,499",
      period = "/year",
      resolution = "4K Ultra HD + HDR",
      devicesSupported = "All Smart TVs, PC, Mobile",
      screensCount = 3,
      audioQuality = "Dolby Atmos / 5.1",
      adPolicy = "Ad-Free Streaming",
      perks = listOf(
        "Save 58% over monthly subscription",
        "Unlimited 4K HDR streaming & downloads",
        "Free One-Day & Same-Day delivery on Amazon",
        "Ad-free Amazon Music + Prime Reading"
      ),
      isBestValue = true,
      isPopular = true,
      subscribeUrl = "https://www.amazon.in/prime"
    ),
    PlatformPlan(
      id = "prime_lite",
      platform = OttPlatform.PRIME_VIDEO,
      planName = "Prime Lite",
      priceDisplay = "₹799",
      period = "/year",
      resolution = "720p HD",
      devicesSupported = "Mobile & TV (1 device)",
      screensCount = 1,
      audioQuality = "Stereo Sound",
      adPolicy = "Includes Limited Ads",
      perks = listOf(
        "Budget-friendly full year of Prime Video",
        "Free Two-Day shipping on Amazon",
        "Early access to Lightning Deals"
      ),
      subscribeUrl = "https://www.amazon.in/prime"
    ),

    // JIO_HOTSTAR
    PlatformPlan(
      id = "hotstar_super",
      platform = OttPlatform.JIO_HOTSTAR,
      planName = "Super (Annual)",
      priceDisplay = "₹899",
      period = "/year",
      resolution = "1080p Full HD",
      devicesSupported = "All Devices (TV, Mobile, Web)",
      screensCount = 2,
      audioQuality = "Dolby 5.1 Surround",
      adPolicy = "Limited Ads on Live Sports",
      perks = listOf(
        "All Disney, Marvel, HBO & Hotstar Specials",
        "Watch live cricket, IPL, Premier League & F1",
        "Simultaneous streaming on 2 screens"
      ),
      subscribeUrl = "https://www.hotstar.com/subscribe"
    ),
    PlatformPlan(
      id = "hotstar_premium_monthly",
      platform = OttPlatform.JIO_HOTSTAR,
      planName = "Premium Monthly",
      priceDisplay = "₹299",
      period = "/month",
      resolution = "4K Ultra HD + Dolby Vision",
      devicesSupported = "TV, Phone, Laptop, Tablet",
      screensCount = 4,
      audioQuality = "Dolby Atmos",
      adPolicy = "Ad-Free (Except Sports)",
      perks = listOf(
        "Stream on 4 devices simultaneously",
        "4K 2160p resolution with Dolby Vision",
        "Ad-free on all movies & series"
      ),
      subscribeUrl = "https://www.hotstar.com/subscribe"
    ),
    PlatformPlan(
      id = "hotstar_premium_annual",
      platform = OttPlatform.JIO_HOTSTAR,
      planName = "Premium Annual",
      priceDisplay = "₹1,499",
      period = "/year",
      resolution = "4K Ultra HD + Dolby Vision",
      devicesSupported = "TV, Phone, Laptop, Tablet",
      screensCount = 4,
      audioQuality = "Dolby Atmos",
      adPolicy = "Ad-Free (Except Sports)",
      perks = listOf(
        "Save 58% compared to monthly plan",
        "Stream on 4 devices at once in 4K HDR",
        "Dolby Atmos crystal-clear audio",
        "All live sports, tournaments & blockbusters"
      ),
      isBestValue = true,
      isPopular = true,
      subscribeUrl = "https://www.hotstar.com/subscribe"
    ),

    // YOUTUBE
    PlatformPlan(
      id = "yt_premium_individual",
      platform = OttPlatform.YOUTUBE,
      planName = "Premium Individual",
      priceDisplay = "₹149",
      period = "/month",
      resolution = "Up to 4K / 8K + 1080p Premium",
      devicesSupported = "All Devices (TV, Web, Mobile)",
      screensCount = 2,
      audioQuality = "High Bitrate 256kbps",
      adPolicy = "100% Completely Ad-Free",
      perks = listOf(
        "Zero ads before, during, and after videos",
        "Background playback while using other apps",
        "Full YouTube Music Premium app included",
        "Smart downloads for offline viewing"
      ),
      isPopular = true,
      subscribeUrl = "https://www.youtube.com/premium"
    ),
    PlatformPlan(
      id = "yt_premium_family",
      platform = OttPlatform.YOUTUBE,
      planName = "Premium Family",
      priceDisplay = "₹299",
      period = "/month",
      resolution = "Up to 4K / 8K + 1080p Premium",
      devicesSupported = "Up to 5 Family Members",
      screensCount = 5,
      audioQuality = "High Bitrate 256kbps",
      adPolicy = "100% Completely Ad-Free",
      perks = listOf(
        "Add up to 5 household members (age 13+)",
        "Each member gets individual private accounts",
        "Ad-free YouTube + YouTube Music for all 5",
        "Best shared family entertainment value"
      ),
      isBestValue = true,
      subscribeUrl = "https://www.youtube.com/premium"
    ),
    PlatformPlan(
      id = "yt_premium_annual",
      platform = OttPlatform.YOUTUBE,
      planName = "Premium Annual (Prepaid)",
      priceDisplay = "₹1,490",
      period = "/year",
      resolution = "Up to 4K / 8K + 1080p Premium",
      devicesSupported = "All Devices",
      screensCount = 2,
      audioQuality = "High Bitrate 256kbps",
      adPolicy = "100% Completely Ad-Free",
      perks = listOf(
        "12 months upfront prepaid - no auto renewal",
        "Save 2 months of subscription cost",
        "Full YouTube Music Premium included"
      ),
      subscribeUrl = "https://www.youtube.com/premium"
    ),

    // APPLE TV+
    PlatformPlan(
      id = "apple_tv_monthly",
      platform = OttPlatform.APPLE_TV,
      planName = "Apple TV+ Monthly",
      priceDisplay = "₹99",
      period = "/month",
      resolution = "4K Ultra HD + Dolby Vision",
      devicesSupported = "Apple TV, Smart TVs, Web, iOS",
      screensCount = 6,
      audioQuality = "Dolby Atmos & Spatial Audio",
      adPolicy = "100% Ad-Free",
      perks = listOf(
        "Award-winning Apple Originals & movies",
        "Family Sharing with up to 5 people at no extra cost",
        "Industry-leading 4K HDR & Dolby Atmos bitrate",
        "Download & watch offline on Apple devices"
      ),
      isPopular = true,
      subscribeUrl = "https://tv.apple.com"
    ),
    PlatformPlan(
      id = "apple_one_individual",
      platform = OttPlatform.APPLE_TV,
      planName = "Apple One Bundle",
      priceDisplay = "₹195",
      period = "/month",
      resolution = "4K Ultra HD + Dolby Vision",
      devicesSupported = "All Devices",
      screensCount = 2,
      audioQuality = "Dolby Atmos & Spatial Audio",
      adPolicy = "100% Ad-Free",
      perks = listOf(
        "Includes Apple TV+ Original catalog",
        "Includes Apple Music with Lossless audio",
        "Includes Apple Arcade (200+ games)",
        "50 GB iCloud+ storage included"
      ),
      isBestValue = true,
      subscribeUrl = "https://www.apple.com/apple-one/"
    ),

    // SONY LIV
    PlatformPlan(
      id = "sonyliv_mobile",
      platform = OttPlatform.SONY_LIV,
      planName = "LIV Mobile",
      priceDisplay = "₹599",
      period = "/year",
      resolution = "720p HD",
      devicesSupported = "Mobile Only (1 device)",
      screensCount = 1,
      audioQuality = "Stereo Sound",
      adPolicy = "Limited Ads on Sports",
      perks = listOf(
        "All Sony LIV Originals & international series",
        "Live sports: Champions League, WWE, Tennis",
        "Affordable full-year mobile pass"
      ),
      subscribeUrl = "https://www.sonyliv.com/subscription"
    ),
    PlatformPlan(
      id = "sonyliv_premium_annual",
      platform = OttPlatform.SONY_LIV,
      planName = "LIV Premium Annual",
      priceDisplay = "₹999",
      period = "/year",
      resolution = "1080p Full HD",
      devicesSupported = "TV, Laptop, Mobile, Web",
      screensCount = 2,
      audioQuality = "Dolby 5.1 Surround",
      adPolicy = "Ad-Free on Shows & Movies",
      perks = listOf(
        "Watch on 2 screens simultaneously",
        "All live UEFA Champions League, WWE Raw & SmackDown",
        "Sony Pictures Hollywood blockbuster library",
        "Ad-free entertainment on all TV shows"
      ),
      isBestValue = true,
      isPopular = true,
      subscribeUrl = "https://www.sonyliv.com/subscription"
    ),

    // ZEE5
    PlatformPlan(
      id = "zee5_premium_annual",
      platform = OttPlatform.ZEE5,
      planName = "ZEE5 Premium 4K (Annual)",
      priceDisplay = "₹899",
      period = "/year",
      resolution = "4K Ultra HD + Dolby Atmos",
      devicesSupported = "TV, Mobile, Laptop, Tablet",
      screensCount = 4,
      audioQuality = "Dolby Atmos",
      adPolicy = "100% Ad-Free",
      perks = listOf(
        "Stream on 4 screens at the same time",
        "4K Ultra HD video with Dolby Atmos audio",
        "4000+ blockbuster movies & 50+ live TV channels",
        "Original web series across 12 regional languages"
      ),
      isBestValue = true,
      isPopular = true,
      subscribeUrl = "https://www.zee5.com/myaccount/subscription"
    ),
    PlatformPlan(
      id = "zee5_premium_monthly",
      platform = OttPlatform.ZEE5,
      planName = "ZEE5 Premium Monthly",
      priceDisplay = "₹199",
      period = "/month",
      resolution = "1080p Full HD",
      devicesSupported = "TV, Mobile, Laptop",
      screensCount = 2,
      audioQuality = "Stereo / 5.1",
      adPolicy = "Ad-Free Shows & Movies",
      perks = listOf(
        "Full access to ZEE5 Originals & new theatrical releases",
        "Watch on 2 devices simultaneously",
        "No long-term commitment"
      ),
      subscribeUrl = "https://www.zee5.com/myaccount/subscription"
    )
  )

  val vipPlan: PlatformPlan = PlatformPlan(
    id = "vip_free_all_access",
    platform = OttPlatform.ALL,
    planName = "VIP All-Access Pass",
    priceDisplay = "FREE",
    period = " / ₹0 Forever",
    resolution = "4K Ultra HD + Dolby Vision",
    devicesSupported = "All Devices (Unlimited Screens)",
    screensCount = 99,
    audioQuality = "Dolby Atmos & Spatial Audio",
    adPolicy = "100% Ad-Free on All Platforms",
    perks = listOf(
      "Unrestricted free access to all 7 OTT streaming platforms",
      "Includes Netflix, Prime Video, JioHotstar, YouTube, Apple TV+, Sony LIV & ZEE5",
      "Stream on unlimited screens simultaneously",
      "Crystal-clear 4K Ultra HD resolution & Dolby Atmos",
      "Zero commercial ads across all movies, shows & live sports",
      "Lifetime 100% Free VIP Tier - No subscription or card needed"
    ),
    isPopular = true,
    isBestValue = true,
    subscribeUrl = "https://ott.aggregator/vip"
  )
}
