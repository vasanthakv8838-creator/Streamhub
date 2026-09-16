package com.example.data

import com.example.model.CuratedTrailer
import com.example.model.LiveChannel
import com.example.model.MediaItem
import com.example.model.MediaType
import com.example.model.OttPlatform
import com.example.model.VpnServer

data class CountryAccessLibrary(
  val countryCode: String,
  val countryName: String,
  val flagEmoji: String,
  val city: String,
  val movies: List<MediaItem>,
  val tvShows: List<MediaItem>,
  val liveTvChannels: List<LiveChannel>,
  val videos: List<CuratedTrailer>,
  val unlockedPlatforms: List<String>
) {
  val allMediaItems: List<MediaItem>
    get() = movies + tvShows
}

object RegionalCatalog {

  val regionalServers: List<VpnServer> = WorldVpnCountries.servers

  // =========================================================================
  // 1. UNITED STATES (US)
  // =========================================================================
  private val usMovies = listOf(
    MediaItem(
      id = "us_mov_dune2",
      title = "Dune: Part Two (IMAX US Cut)",
      platform = OttPlatform.PRIME_VIDEO,
      mediaType = MediaType.MOVIE,
      genre = "Sci-Fi • Adventure",
      rating = 8.9,
      votes = "650K",
      year = 2024,
      duration = "2h 46m",
      synopsis = "Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators who destroyed his family. Exclusive US IMAX Expanded stream.",
      cast = listOf("Timothée Chalamet", "Zendaya", "Rebecca Ferguson", "Javier Bardem"),
      director = "Denis Villeneuve",
      posterUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=Way9Dexny3w",
      watchUrl = "https://www.primevideo.com/detail/dune-two",
      qualityBadge = "4K IMAX Enhanced • Dolby Atmos",
      contentAdvisory = "PG-13",
      isTrending = true,
      regionCode = "US",
      vpnRequired = true,
      vpnRegionBadge = "🇺🇸 US Exclusive"
    ),
    MediaItem(
      id = "us_mov_oppenheimer",
      title = "Oppenheimer (Criterion 4K Edition)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.MOVIE,
      genre = "Biography • Drama • History",
      rating = 8.9,
      votes = "820K",
      year = 2023,
      duration = "3h 00m",
      synopsis = "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb. Unlocked via US Netflix library.",
      cast = listOf("Cillian Murphy", "Emily Blunt", "Matt Damon", "Robert Downey Jr."),
      director = "Christopher Nolan",
      posterUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=uYPbbksJxIg",
      watchUrl = "https://www.netflix.com/title/81601688",
      qualityBadge = "4K Ultra HD • HDR10+",
      contentAdvisory = "R",
      isTrending = true,
      regionCode = "US",
      vpnRequired = true,
      vpnRegionBadge = "🇺🇸 US Exclusive"
    ),
    MediaItem(
      id = "us_mov_topgun",
      title = "Top Gun: Maverick (Director's 4K Audio)",
      platform = OttPlatform.APPLE_TV,
      mediaType = MediaType.MOVIE,
      genre = "Action • Aviation • Drama",
      rating = 8.3,
      votes = "710K",
      year = 2022,
      duration = "2h 10m",
      synopsis = "After thirty years, Maverick is still pushing the envelope as a top naval aviator, but must confront ghosts of his past.",
      cast = listOf("Tom Cruise", "Miles Teller", "Jennifer Connelly", "Jon Hamm"),
      director = "Joseph Kosinski",
      posterUrl = "https://images.unsplash.com/photo-1519074069444-1ba4ea16e902?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1519074069444-1ba4ea16e902?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=giXco2jaZ_4",
      watchUrl = "https://tv.apple.com/us/movie/top-gun-maverick",
      qualityBadge = "4K Dolby Vision • Atmos",
      contentAdvisory = "PG-13",
      isTrending = false,
      regionCode = "US",
      vpnRequired = true,
      vpnRegionBadge = "🇺🇸 US Exclusive"
    )
  )

  private val usTvShows = listOf(
    MediaItem(
      id = "us_show_the_office",
      title = "The Office (US Extended Fan Cut)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.SERIES,
      genre = "Comedy • Mockumentary",
      rating = 9.0,
      votes = "890K",
      year = 2024,
      duration = "9 Seasons (201 Eps)",
      synopsis = "A mockumentary on a group of typical office workers at Dunder Mifflin Paper Company in Scranton, Pennsylvania. Full 9-season cut with deleted scenes.",
      cast = listOf("Steve Carell", "Rainn Wilson", "John Krasinski", "Jenna Fischer"),
      director = "Greg Daniels",
      posterUrl = "https://images.unsplash.com/photo-1517245386807-bb43f82c33c4?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1497215728101-856f4ea42174?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=2iKZmRR99zA",
      watchUrl = "https://www.netflix.com/title/70136120",
      qualityBadge = "4K Remastered • Atmos",
      contentAdvisory = "TV-14",
      isTrending = true,
      regionCode = "US",
      vpnRequired = true,
      vpnRegionBadge = "🇺🇸 US Exclusive"
    ),
    MediaItem(
      id = "us_show_house_of_dragon",
      title = "House of the Dragon: Season 2",
      platform = OttPlatform.PRIME_VIDEO,
      mediaType = MediaType.SERIES,
      genre = "Action • Adventure • Drama",
      rating = 8.5,
      votes = "420K",
      year = 2024,
      duration = "2 Seasons (18 Eps)",
      synopsis = "An internal succession war within House Targaryen at the height of its power, 172 years before the birth of Daenerys Targaryen.",
      cast = listOf("Emma D'Arcy", "Matt Smith", "Olivia Cooke", "Rhys Ifans"),
      director = "Ryan J. Condal",
      posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=DotnJ7tTA34",
      watchUrl = "https://www.primevideo.com/detail/house-of-the-dragon",
      qualityBadge = "4K HDR10+ • Dolby Vision",
      contentAdvisory = "18+",
      isTrending = true,
      regionCode = "US",
      vpnRequired = true,
      vpnRegionBadge = "🇺🇸 US Exclusive"
    ),
    MediaItem(
      id = "us_show_succession",
      title = "Succession (Complete HBO Series)",
      platform = OttPlatform.APPLE_TV,
      mediaType = MediaType.SERIES,
      genre = "Drama • Satire",
      rating = 8.9,
      votes = "380K",
      year = 2023,
      duration = "4 Seasons (39 Eps)",
      synopsis = "The Roy family is known for controlling the biggest media and entertainment company in the world. However, their world changes when their father steps down.",
      cast = listOf("Brian Cox", "Jeremy Strong", "Sarah Snook", "Kieran Culkin"),
      director = "Jesse Armstrong",
      posterUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=oz49vCGtwTU",
      watchUrl = "https://tv.apple.com/us/show/succession",
      qualityBadge = "4K Ultra HD • Atmos",
      contentAdvisory = "TV-MA",
      isTrending = false,
      regionCode = "US",
      vpnRequired = true,
      vpnRegionBadge = "🇺🇸 US Exclusive"
    )
  )

  private val usVideos = listOf(
    CuratedTrailer(
      title = "Dune: Part Two - Official US Teaser & IMAX Breakdown",
      platform = OttPlatform.YOUTUBE,
      duration = "3:12",
      videoUrl = "https://www.youtube.com/watch?v=Way9Dexny3w",
      defaultPrompt = "Analyze the cinematography, Arrakis desert scale, and sound design in this Dune Part Two US trailer."
    ),
    CuratedTrailer(
      title = "House of the Dragon Season 2 - Epic Battle Teaser",
      platform = OttPlatform.YOUTUBE,
      duration = "2:45",
      videoUrl = "https://www.youtube.com/watch?v=DotnJ7tTA34",
      defaultPrompt = "Examine the Targaryen civil war stakes and dragon battle visuals in this trailer."
    ),
    CuratedTrailer(
      title = "The Office US - Best Cold Opens & Deleted Scenes",
      platform = OttPlatform.YOUTUBE,
      duration = "10:15",
      videoUrl = "https://www.youtube.com/watch?v=2iKZmRR99zA",
      defaultPrompt = "Explain why the mockumentary comedic timing in The Office continues to resonate worldwide."
    )
  )

  // =========================================================================
  // 2. UNITED KINGDOM (GB)
  // =========================================================================
  private val gbMovies = listOf(
    MediaItem(
      id = "gb_mov_skyfall",
      title = "Skyfall (BBC UK 4K Master)",
      platform = OttPlatform.PRIME_VIDEO,
      mediaType = MediaType.MOVIE,
      genre = "Action • Thriller • Spy",
      rating = 7.8,
      votes = "780K",
      year = 2022,
      duration = "2h 23m",
      synopsis = "James Bond's loyalty to M is tested when her past comes back to haunt her. When MI6 comes under attack, 007 must track down and destroy the threat.",
      cast = listOf("Daniel Craig", "Javier Bardem", "Judi Dench", "Ralph Fiennes"),
      director = "Sam Mendes",
      posterUrl = "https://images.unsplash.com/photo-1513635269975-59663e0ac1ad?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1486299267070-83823f5448dd?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=6kw1UVovByw",
      watchUrl = "https://www.primevideo.com/detail/skyfall",
      qualityBadge = "4K Remastered • Atmos",
      contentAdvisory = "15",
      isTrending = true,
      regionCode = "GB",
      vpnRequired = true,
      vpnRegionBadge = "🇬🇧 UK Exclusive"
    ),
    MediaItem(
      id = "gb_mov_wonka",
      title = "Wonka (UK Digital 4K)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.MOVIE,
      genre = "Adventure • Comedy • Family",
      rating = 7.2,
      votes = "210K",
      year = 2023,
      duration = "1h 56m",
      synopsis = "Armed with nothing but a hatful of dreams, young chocolatier Willy Wonka manages to change the world, one delectable bite at a time.",
      cast = listOf("Timothée Chalamet", "Hugh Grant", "Calah Lane", "Keegan-Michael Key"),
      director = "Paul King",
      posterUrl = "https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=otNh9bTjXWg",
      watchUrl = "https://www.netflix.com/gb/title/wonka",
      qualityBadge = "4K Dolby Vision",
      contentAdvisory = "PG",
      isTrending = false,
      regionCode = "GB",
      vpnRequired = true,
      vpnRegionBadge = "🇬🇧 UK Exclusive"
    )
  )

  private val gbTvShows = listOf(
    MediaItem(
      id = "gb_show_doctor_who",
      title = "Doctor Who (BBC 60th Special Cut)",
      platform = OttPlatform.JIO_HOTSTAR,
      mediaType = MediaType.SERIES,
      genre = "Sci-Fi • Adventure",
      rating = 8.6,
      votes = "260K",
      year = 2024,
      duration = "14 Seasons (175 Eps)",
      synopsis = "The further adventures in time and space of the alien adventurer known as the Doctor and their companions. Unlocked with BBC UK broadcast timeline.",
      cast = listOf("David Tennant", "Catherine Tate", "Ncuti Gatwa", "Millie Gibson"),
      director = "Russell T Davies",
      posterUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=9_L583j3Tcg",
      watchUrl = "https://www.hotstar.com/gb/tv/doctor-who",
      qualityBadge = "4K Ultra HD • 60fps",
      contentAdvisory = "PG",
      isTrending = true,
      regionCode = "GB",
      vpnRequired = true,
      vpnRegionBadge = "🇬🇧 UK Exclusive"
    ),
    MediaItem(
      id = "gb_show_peaky_blinders",
      title = "Peaky Blinders (BBC Master Edition)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.SERIES,
      genre = "Crime • Drama • History",
      rating = 8.8,
      votes = "620K",
      year = 2023,
      duration = "6 Seasons (36 Eps)",
      synopsis = "A notorious gang in 1919 Birmingham, England, is led by the fierce Tommy Shelby, a crime boss set on moving up in the world no matter the cost.",
      cast = listOf("Cillian Murphy", "Paul Anderson", "Helen McCrory", "Tom Hardy"),
      director = "Steven Knight",
      posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1513635269975-59663e0ac1ad?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=oVzVdvGIC7U",
      watchUrl = "https://www.netflix.com/title/80002479",
      qualityBadge = "4K HDR10+ • Atmos",
      contentAdvisory = "18+",
      isTrending = true,
      regionCode = "GB",
      vpnRequired = true,
      vpnRegionBadge = "🇬🇧 UK Exclusive"
    )
  )

  private val gbVideos = listOf(
    CuratedTrailer(
      title = "Peaky Blinders Season 6 - BBC Final Season Trailer",
      platform = OttPlatform.YOUTUBE,
      duration = "2:30",
      videoUrl = "https://www.youtube.com/watch?v=2nsT9uQPIrk",
      defaultPrompt = "Analyze Tommy Shelby's psychological arc and the British cinematography in this trailer."
    ),
    CuratedTrailer(
      title = "Doctor Who 60th Anniversary Special - Official Teaser",
      platform = OttPlatform.YOUTUBE,
      duration = "2:15",
      videoUrl = "https://www.youtube.com/watch?v=9_L583j3Tcg",
      defaultPrompt = "Examine the return of the Fourteenth Doctor and the sci-fi spectacle."
    )
  )

  // =========================================================================
  // 3. INDIA (IN)
  // =========================================================================
  private val inMovies = listOf(
    MediaItem(
      id = "in_mov_rrr",
      title = "RRR (Uncut Telugu/Hindi 4K Atmos)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.MOVIE,
      genre = "Action • Epic • Historical Drama",
      rating = 8.0,
      votes = "210K",
      year = 2022,
      duration = "3h 07m",
      synopsis = "A fictitious story about two legendary revolutionaries and their journey away from home before they began fighting for their country in the 1920s.",
      cast = listOf("N.T. Rama Rao Jr.", "Ram Charan", "Alia Bhatt", "Ajay Devgn"),
      director = "S.S. Rajamouli",
      posterUrl = "https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=NgBoMJy386M",
      watchUrl = "https://www.netflix.com/in/title/81476453",
      qualityBadge = "4K Dolby Vision • Atmos 7.1",
      contentAdvisory = "U/A 16+",
      isTrending = true,
      regionCode = "IN",
      vpnRequired = true,
      vpnRegionBadge = "🇮🇳 India Exclusive"
    ),
    MediaItem(
      id = "in_mov_kalki",
      title = "Kalki 2898 AD (Sci-Fi Epic)",
      platform = OttPlatform.PRIME_VIDEO,
      mediaType = MediaType.MOVIE,
      genre = "Sci-Fi • Mythology • Action",
      rating = 7.7,
      votes = "130K",
      year = 2024,
      duration = "3h 01m",
      synopsis = "A modern avatar of Vishnu, a Hindu god, who is believed to have descended to the earth to protect the world from evil forces in a dystopian Kasi.",
      cast = listOf("Prabhas", "Amitabh Bachchan", "Kamal Haasan", "Deepika Padukone"),
      director = "Nag Ashwin",
      posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=kQDd1AhGIHk",
      watchUrl = "https://www.primevideo.com/detail/kalki-2898-ad",
      qualityBadge = "4K Ultra HD • HDR10+",
      contentAdvisory = "U/A 13+",
      isTrending = true,
      regionCode = "IN",
      vpnRequired = true,
      vpnRegionBadge = "🇮🇳 India Exclusive"
    )
  )

  private val inTvShows = listOf(
    MediaItem(
      id = "in_show_panchayat3",
      title = "Panchayat Season 3",
      platform = OttPlatform.PRIME_VIDEO,
      mediaType = MediaType.SERIES,
      genre = "Comedy • Drama",
      rating = 9.0,
      votes = "120K",
      year = 2024,
      duration = "3 Seasons (24 Eps)",
      synopsis = "Abhishek Tripathi navigates high-stakes Phulera village politics, new road developments, and community rivalries in Uttar Pradesh.",
      cast = listOf("Jitendra Kumar", "Neena Gupta", "Raghubir Yadav", "Faisal Malik"),
      director = "Deepak Kumar Mishra",
      posterUrl = "https://images.unsplash.com/photo-1509099836639-18ba1795216d?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1509099836639-18ba1795216d?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=kY3Su_h5n48",
      watchUrl = "https://www.primevideo.com/detail/panchayat",
      qualityBadge = "4K Ultra HD • 5.1",
      contentAdvisory = "U/A 16+",
      isTrending = true,
      regionCode = "IN",
      vpnRequired = true,
      vpnRegionBadge = "🇮🇳 India Exclusive"
    ),
    MediaItem(
      id = "in_show_mirzapur3",
      title = "Mirzapur Season 3",
      platform = OttPlatform.PRIME_VIDEO,
      mediaType = MediaType.SERIES,
      genre = "Crime • Thriller • Action",
      rating = 8.5,
      votes = "180K",
      year = 2024,
      duration = "3 Seasons (29 Eps)",
      synopsis = "With Akhandanand Tripathi recovering in exile, Guddu Pandit and Golu Gupta take control of the Purvanchal carpet and gun empire.",
      cast = listOf("Pankaj Tripathi", "Ali Fazal", "Shweta Tripathi", "Rasika Dugal"),
      director = "Gurmmeet Singh",
      posterUrl = "https://images.unsplash.com/photo-1478720568477-152d9b164e26?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1478720568477-152d9b164e26?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=0kQ8wZ9L_O8",
      watchUrl = "https://www.primevideo.com/detail/mirzapur",
      qualityBadge = "4K HDR10+ • Atmos",
      contentAdvisory = "18+",
      isTrending = true,
      regionCode = "IN",
      vpnRequired = true,
      vpnRegionBadge = "🇮🇳 India Exclusive"
    )
  )

  private val inVideos = listOf(
    CuratedTrailer(
      title = "Panchayat Season 3 - Official Trailer",
      platform = OttPlatform.YOUTUBE,
      duration = "2:40",
      videoUrl = "https://www.youtube.com/watch?v=kY3Su_h5n48",
      defaultPrompt = "Highlight the emotional rural narrative, Phulera village dynamic, and comedic timing."
    ),
    CuratedTrailer(
      title = "Kalki 2898 AD - Official Hindi Release Trailer",
      platform = OttPlatform.YOUTUBE,
      duration = "3:03",
      videoUrl = "https://www.youtube.com/watch?v=kQDd1AhGIHk",
      defaultPrompt = "Analyze the futuristic Indian cyberpunk aesthetic and mythological fusion in Kalki 2898 AD."
    )
  )

  // =========================================================================
  // 4. JAPAN (JP)
  // =========================================================================
  private val jpMovies = listOf(
    MediaItem(
      id = "jp_mov_spirited_away",
      title = "Spirited Away (Studio Ghibli 4K Master)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.MOVIE,
      genre = "Anime • Fantasy • Adventure",
      rating = 8.6,
      votes = "830K",
      year = 2023,
      duration = "2h 05m",
      synopsis = "During her family's move to the suburbs, a sullen 10-year-old girl wanders into a world ruled by gods, witches, and spirits.",
      cast = listOf("Rumi Hiiragi", "Miyu Irino", "Mari Natsuki", "Takashi Naito"),
      director = "Hayao Miyazaki",
      posterUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=ByXuk9QqQkk",
      watchUrl = "https://www.netflix.com/jp/title/60023642",
      qualityBadge = "4K Remastered • Atmos",
      contentAdvisory = "PG",
      isTrending = true,
      regionCode = "JP",
      vpnRequired = true,
      vpnRegionBadge = "🇯🇵 Japan Exclusive"
    ),
    MediaItem(
      id = "jp_mov_suzume",
      title = "Suzume (Makoto Shinkai 4K)",
      platform = OttPlatform.PRIME_VIDEO,
      mediaType = MediaType.MOVIE,
      genre = "Anime • Drama • Fantasy",
      rating = 7.7,
      votes = "75K",
      year = 2023,
      duration = "2h 02m",
      synopsis = "A modern action adventure road story where a 17-year-old girl named Suzume helps a mysterious young man close doors from the other side that are releasing disasters all over Japan.",
      cast = listOf("Nanoka Hara", "Hokuto Matsumura", "Eri Fukatsu", "Shota Sometani"),
      director = "Makoto Shinkai",
      posterUrl = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=6c-gkWfYg_w",
      watchUrl = "https://www.primevideo.com/detail/suzume",
      qualityBadge = "4K Ultra HD • 60fps",
      contentAdvisory = "PG",
      isTrending = false,
      regionCode = "JP",
      vpnRequired = true,
      vpnRegionBadge = "🇯🇵 Japan Exclusive"
    )
  )

  private val jpTvShows = listOf(
    MediaItem(
      id = "jp_show_aot",
      title = "Attack on Titan: Final Season (Uncut JP)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.SERIES,
      genre = "Anime • Dark Fantasy • Action",
      rating = 9.1,
      votes = "540K",
      year = 2023,
      duration = "4 Seasons (89 Eps)",
      synopsis = "After his hometown is destroyed and his mother is killed, young Eren Jaeger vows to cleanse the earth of the giant humanoid Titans.",
      cast = listOf("Yuki Kaji", "Yui Ishikawa", "Marina Inoue", "Hiroshi Kamiya"),
      director = "Yuichiro Hayashi",
      posterUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=M_OauHnAFc8",
      watchUrl = "https://www.netflix.com/jp/title/70299043",
      qualityBadge = "1080p 60fps • Uncensored",
      contentAdvisory = "18+",
      isTrending = true,
      regionCode = "JP",
      vpnRequired = true,
      vpnRegionBadge = "🇯🇵 Japan Exclusive"
    ),
    MediaItem(
      id = "jp_show_jjk2",
      title = "Jujutsu Kaisen Season 2 (Shibuya Incident)",
      platform = OttPlatform.JIO_HOTSTAR,
      mediaType = MediaType.SERIES,
      genre = "Anime • Supernatural • Action",
      rating = 8.8,
      votes = "160K",
      year = 2024,
      duration = "2 Seasons (47 Eps)",
      synopsis = "A boy swallows a cursed talisman - the finger of a demon - and becomes cursed himself. The catastrophic Shibuya incident unfolds.",
      cast = listOf("Junya Enoki", "Yuma Uchida", "Asami Seto", "Yuichi Nakamura"),
      director = "Shota Goshozono",
      posterUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=O6qVieflwqs",
      watchUrl = "https://www.hotstar.com/jp/tv/jujutsu-kaisen",
      qualityBadge = "4K 60fps • Atmos",
      contentAdvisory = "16+",
      isTrending = true,
      regionCode = "JP",
      vpnRequired = true,
      vpnRegionBadge = "🇯🇵 Japan Exclusive"
    )
  )

  private val jpVideos = listOf(
    CuratedTrailer(
      title = "Studio Ghibli 4K Film Retrospective & Artistry",
      platform = OttPlatform.YOUTUBE,
      duration = "4:50",
      videoUrl = "https://www.youtube.com/watch?v=ByXuk9QqQkk",
      defaultPrompt = "Examine Hayao Miyazaki's animation techniques, watercolor backdrops, and Japanese folklore."
    ),
    CuratedTrailer(
      title = "Jujutsu Kaisen Shibuya Incident Climax Teaser",
      platform = OttPlatform.YOUTUBE,
      duration = "2:20",
      videoUrl = "https://www.youtube.com/watch?v=O6qVieflwqs",
      defaultPrompt = "Analyze the high-speed animation cuts and sound design in the Shibuya Arc."
    )
  )

  // =========================================================================
  // 5. FRANCE (FR)
  // =========================================================================
  private val frMovies = listOf(
    MediaItem(
      id = "fr_mov_anatomy_of_a_fall",
      title = "Anatomy of a Fall (Palme d'Or 4K)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.MOVIE,
      genre = "Drama • Mystery • Thriller",
      rating = 7.7,
      votes = "190K",
      year = 2023,
      duration = "2h 31m",
      synopsis = "A woman is suspected of murder after her husband's death in the snow near their remote chalet in the French Alps. Winner of Palme d'Or.",
      cast = listOf("Sandra Hüller", "Swann Arlaud", "Milo Machado-Graner", "Antoine Reinartz"),
      director = "Justine Triet",
      posterUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=fTrspdIr61M",
      watchUrl = "https://www.netflix.com/fr/title/81729443",
      qualityBadge = "4K Dolby Vision • French Audio",
      contentAdvisory = "16",
      isTrending = true,
      regionCode = "FR",
      vpnRequired = true,
      vpnRegionBadge = "🇫🇷 France Exclusive"
    ),
    MediaItem(
      id = "fr_mov_amelie",
      title = "Amélie (Le Fabuleux Destin d'Amélie Poulain)",
      platform = OttPlatform.PRIME_VIDEO,
      mediaType = MediaType.MOVIE,
      genre = "Comedy • Romance",
      rating = 8.3,
      votes = "790K",
      year = 2022,
      duration = "2h 02m",
      synopsis = "Amélie is an innocent and naive girl in Paris with her own sense of justice. She decides to help those around her and, along the way, discovers love.",
      cast = listOf("Audrey Tautou", "Mathieu Kassovitz", "Rufus", "Jamel Debbouze"),
      director = "Jean-Pierre Jeunet",
      posterUrl = "https://images.unsplash.com/photo-1499856871958-5b9627545d1a?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1499856871958-5b9627545d1a?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=HUECWi5pX7o",
      watchUrl = "https://www.primevideo.com/detail/amelie",
      qualityBadge = "4K Remastered • Atmos",
      contentAdvisory = "R",
      isTrending = false,
      regionCode = "FR",
      vpnRequired = true,
      vpnRegionBadge = "🇫🇷 France Exclusive"
    )
  )

  private val frTvShows = listOf(
    MediaItem(
      id = "fr_show_lupin",
      title = "Lupin (Part 3 France Cut)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.SERIES,
      genre = "Action • Crime • Drama",
      rating = 7.5,
      votes = "150K",
      year = 2023,
      duration = "3 Parts (17 Eps)",
      synopsis = "Inspired by the adventures of Arsène Lupin, gentleman thief Assane Diop sets out to avenge his father for an injustice inflicted by a wealthy family.",
      cast = listOf("Omar Sy", "Ludivine Sagnier", "Antoine Gouy", "Soufiane Guerrab"),
      director = "Louis Leterrier",
      posterUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1499856871958-5b9627545d1a?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=ga0iTWXCGa8",
      watchUrl = "https://www.netflix.com/fr/title/80994082",
      qualityBadge = "4K HDR10+ • French Atmos",
      contentAdvisory = "16+",
      isTrending = true,
      regionCode = "FR",
      vpnRequired = true,
      vpnRegionBadge = "🇫🇷 France Exclusive"
    ),
    MediaItem(
      id = "fr_show_call_my_agent",
      title = "Call My Agent! (Dix pour cent)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.SERIES,
      genre = "Comedy • Drama",
      rating = 8.3,
      votes = "50K",
      year = 2022,
      duration = "4 Seasons (24 Eps)",
      synopsis = "At a top Paris talent agency, agents scramble to keep their star clients happy and their business afloat after an unexpected crisis.",
      cast = listOf("Camille Cottin", "Thibault de Montalembert", "Grégory Montel", "Fanny Sidney"),
      director = "Fanny Herrero",
      posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=eG_sXf1p4sY",
      watchUrl = "https://www.netflix.com/fr/title/80133335",
      qualityBadge = "1080p HD",
      contentAdvisory = "14+",
      isTrending = false,
      regionCode = "FR",
      vpnRequired = true,
      vpnRegionBadge = "🇫🇷 France Exclusive"
    )
  )

  private val frVideos = listOf(
    CuratedTrailer(
      title = "Anatomy of a Fall - Official Cannes Preview",
      platform = OttPlatform.YOUTUBE,
      duration = "2:10",
      videoUrl = "https://www.youtube.com/watch?v=fTrspdIr61M",
      defaultPrompt = "Examine the courtroom tension, dialogue precision, and moral ambiguity."
    ),
    CuratedTrailer(
      title = "Lupin Part 3 - Paris Rooftop Heist Teaser",
      platform = OttPlatform.YOUTUBE,
      duration = "2:25",
      videoUrl = "https://www.youtube.com/watch?v=ga0iTWXCGa8",
      defaultPrompt = "Analyze Omar Sy's charismatic portrayal and modern Paris setting."
    )
  )

  // =========================================================================
  // 6. GERMANY (DE)
  // =========================================================================
  private val deMovies = listOf(
    MediaItem(
      id = "de_mov_all_quiet",
      title = "All Quiet on the Western Front (4K German Audio)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.MOVIE,
      genre = "Action • Drama • War",
      rating = 7.8,
      votes = "260K",
      year = 2022,
      duration = "2h 28m",
      synopsis = "A young German soldier's terrifying experiences and distress on the western front during World War I. Winner of 4 Academy Awards.",
      cast = listOf("Felix Kammerer", "Albrecht Schuch", "Aaron Hilmer", "Daniel Brühl"),
      director = "Edward Berger",
      posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=hf8EYbVxtCY",
      watchUrl = "https://www.netflix.com/de/title/81260280",
      qualityBadge = "4K Dolby Vision • German Atmos",
      contentAdvisory = "18+",
      isTrending = true,
      regionCode = "DE",
      vpnRequired = true,
      vpnRegionBadge = "🇩🇪 Germany Exclusive"
    )
  )

  private val deTvShows = listOf(
    MediaItem(
      id = "de_show_dark",
      title = "Dark (Complete German Master Edition)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.SERIES,
      genre = "Crime • Drama • Mystery • Sci-Fi",
      rating = 8.7,
      votes = "440K",
      year = 2023,
      duration = "3 Seasons (26 Eps)",
      synopsis = "A family saga with a supernatural twist, set in a German town where the disappearance of two young children exposes the relationships among four families across time.",
      cast = listOf("Louis Hofmann", "Karoline Eichhorn", "Lisa Vicari", "Maja Schöne"),
      director = "Baran bo Odar",
      posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=rrwycJ08PSA",
      watchUrl = "https://www.netflix.com/de/title/80100172",
      qualityBadge = "4K HDR10+ • Atmos",
      contentAdvisory = "16+",
      isTrending = true,
      regionCode = "DE",
      vpnRequired = true,
      vpnRegionBadge = "🇩🇪 Germany Exclusive"
    ),
    MediaItem(
      id = "de_show_babylon_berlin",
      title = "Babylon Berlin: Season 4",
      platform = OttPlatform.PRIME_VIDEO,
      mediaType = MediaType.SERIES,
      genre = "Crime • Drama • History",
      rating = 8.4,
      votes = "40K",
      year = 2023,
      duration = "4 Seasons (40 Eps)",
      synopsis = "Colognian commissioner Gereon Rath moves to Berlin, the epicenter of political and social change in the Golden Twenties.",
      cast = listOf("Volker Bruch", "Liv Lisa Fries", "Peter Kurth", "Matthias Brandt"),
      director = "Tom Tykwer",
      posterUrl = "https://images.unsplash.com/photo-1513635269975-59663e0ac1ad?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1513635269975-59663e0ac1ad?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=y3H_w2dZg8w",
      watchUrl = "https://www.primevideo.com/detail/babylon-berlin",
      qualityBadge = "4K Ultra HD",
      contentAdvisory = "16+",
      isTrending = false,
      regionCode = "DE",
      vpnRequired = true,
      vpnRegionBadge = "🇩🇪 Germany Exclusive"
    )
  )

  private val deVideos = listOf(
    CuratedTrailer(
      title = "Dark Season 3 - The Final Loop Official Trailer",
      platform = OttPlatform.YOUTUBE,
      duration = "2:20",
      videoUrl = "https://www.youtube.com/watch?v=rrwycJ08PSA",
      defaultPrompt = "Examine the philosophical time loops and German existentialism in Dark."
    )
  )

  // =========================================================================
  // 7. SOUTH KOREA (KR)
  // =========================================================================
  private val krMovies = listOf(
    MediaItem(
      id = "kr_mov_parasite",
      title = "Parasite (Bong Joon-ho 4K Dual Cut)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.MOVIE,
      genre = "Drama • Thriller • Black Comedy",
      rating = 8.5,
      votes = "910K",
      year = 2023,
      duration = "2h 12m",
      synopsis = "Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.",
      cast = listOf("Song Kang-ho", "Lee Sun-kyun", "Cho Yeo-jeong", "Choi Woo-shik"),
      director = "Bong Joon-ho",
      posterUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=5xH0RZE9jp4",
      watchUrl = "https://www.netflix.com/kr/title/81221938",
      qualityBadge = "4K Ultra HD • Korean Atmos",
      contentAdvisory = "18+",
      isTrending = true,
      regionCode = "KR",
      vpnRequired = true,
      vpnRegionBadge = "🇰🇷 South Korea Exclusive"
    )
  )

  private val krTvShows = listOf(
    MediaItem(
      id = "kr_show_squid_game2",
      title = "Squid Game: Season 2",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.SERIES,
      genre = "Action • Drama • Mystery • Thriller",
      rating = 8.0,
      votes = "550K",
      year = 2024,
      duration = "2 Seasons (16 Eps)",
      synopsis = "Hundreds of cash-strapped players accept a strange invitation to compete in children's games. Inside awaits a tempting prize with deadly high stakes.",
      cast = listOf("Lee Jung-jae", "Lee Byung-hun", "Wi Ha-joon", "Yim Si-wan"),
      director = "Hwang Dong-hyuk",
      posterUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=oqxAJKy0ii4",
      watchUrl = "https://www.netflix.com/kr/title/81040344",
      qualityBadge = "4K Dolby Vision • Atmos",
      contentAdvisory = "18+",
      isTrending = true,
      regionCode = "KR",
      vpnRequired = true,
      vpnRegionBadge = "🇰🇷 South Korea Exclusive"
    ),
    MediaItem(
      id = "kr_show_the_glory",
      title = "The Glory (Complete Korean Master)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.SERIES,
      genre = "Drama • Thriller",
      rating = 8.1,
      votes = "65K",
      year = 2023,
      duration = "2 Seasons (16 Eps)",
      synopsis = "A woman who survived horrific abuse in high school puts an elaborate revenge scheme into motion to make the perpetrators pay.",
      cast = listOf("Song Hye-kyo", "Lee Do-hyun", "Lim Ji-yeon", "Yeom Hye-ran"),
      director = "Ahn Gil-ho",
      posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=tqFFsN_R2x0",
      watchUrl = "https://www.netflix.com/kr/title/81519223",
      qualityBadge = "4K Ultra HD",
      contentAdvisory = "18+",
      isTrending = true,
      regionCode = "KR",
      vpnRequired = true,
      vpnRegionBadge = "🇰🇷 South Korea Exclusive"
    )
  )

  private val krVideos = listOf(
    CuratedTrailer(
      title = "Squid Game Season 2 - Official Seoul Teaser",
      platform = OttPlatform.YOUTUBE,
      duration = "1:55",
      videoUrl = "https://www.youtube.com/watch?v=oqxAJKy0ii4",
      defaultPrompt = "Analyze the return of Gi-hun and the escalated psychological tension."
    )
  )

  // =========================================================================
  // 8. CANADA (CA)
  // =========================================================================
  private val caMovies = listOf(
    MediaItem(
      id = "ca_mov_blackberry",
      title = "BlackBerry (Canadian Master Edition)",
      platform = OttPlatform.PRIME_VIDEO,
      mediaType = MediaType.MOVIE,
      genre = "Biography • Comedy • Drama",
      rating = 7.4,
      votes = "65K",
      year = 2023,
      duration = "2h 00m",
      synopsis = "The story of the meteoric rise and catastrophic demise of the world's first smartphone in Waterloo, Ontario.",
      cast = listOf("Jay Baruchel", "Glenn Howerton", "Matt Johnson", "Cary Elwes"),
      director = "Matt Johnson",
      posterUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=cXL_HDzBQsM",
      watchUrl = "https://www.primevideo.com/detail/blackberry",
      qualityBadge = "4K HDR • 5.1",
      contentAdvisory = "R",
      isTrending = true,
      regionCode = "CA",
      vpnRequired = true,
      vpnRegionBadge = "🇨🇦 Canada Exclusive"
    )
  )

  private val caTvShows = listOf(
    MediaItem(
      id = "ca_show_schitts_creek",
      title = "Schitt's Creek (Complete CBC Gem Collection)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.SERIES,
      genre = "Comedy",
      rating = 8.5,
      votes = "140K",
      year = 2023,
      duration = "6 Seasons (80 Eps)",
      synopsis = "When rich video-store magnate Johnny Rose and his family suddenly find themselves broke, they are forced to leave their pampered lives to regroup in Schitt's Creek.",
      cast = listOf("Eugene Levy", "Catherine O'Hara", "Dan Levy", "Annie Murphy"),
      director = "Dan Levy",
      posterUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=W01f1U0jFk0",
      watchUrl = "https://www.netflix.com/ca/title/80036165",
      qualityBadge = "1080p HD",
      contentAdvisory = "TV-14",
      isTrending = true,
      regionCode = "CA",
      vpnRequired = true,
      vpnRegionBadge = "🇨🇦 Canada Exclusive"
    )
  )

  private val caVideos = listOf(
    CuratedTrailer(
      title = "BlackBerry - The Rise & Fall Tech Teaser",
      platform = OttPlatform.YOUTUBE,
      duration = "2:15",
      videoUrl = "https://www.youtube.com/watch?v=cXL_HDzBQsM",
      defaultPrompt = "Examine the dynamic comedic pacing and tech hub rise in Canada."
    )
  )

  // =========================================================================
  // 9. AUSTRALIA (AU)
  // =========================================================================
  private val auMovies = listOf(
    MediaItem(
      id = "au_mov_mad_max",
      title = "Mad Max: Fury Road (Black & Chrome Cut)",
      platform = OttPlatform.PRIME_VIDEO,
      mediaType = MediaType.MOVIE,
      genre = "Action • Sci-Fi • Thriller",
      rating = 8.1,
      votes = "1.1M",
      year = 2023,
      duration = "2h 00m",
      synopsis = "In a post-apocalyptic wasteland, a woman rebels against a tyrannical ruler in search for her homeland with the aid of a group of female prisoners.",
      cast = listOf("Tom Hardy", "Charlize Theron", "Nicholas Hoult", "Hugh Keays-Byrne"),
      director = "George Miller",
      posterUrl = "https://images.unsplash.com/photo-1523482580672-f109ba8cb9be?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1523482580672-f109ba8cb9be?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=hEJnMQG9ev8",
      watchUrl = "https://www.primevideo.com/detail/mad-max-fury-road",
      qualityBadge = "4K Dolby Vision • Atmos",
      contentAdvisory = "MA 15+",
      isTrending = true,
      regionCode = "AU",
      vpnRequired = true,
      vpnRegionBadge = "🇦🇺 Australia Exclusive"
    )
  )

  private val auTvShows = listOf(
    MediaItem(
      id = "au_show_boy_swallows_universe",
      title = "Boy Swallows Universe",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.SERIES,
      genre = "Crime • Drama • Mystery",
      rating = 8.0,
      votes = "30K",
      year = 2024,
      duration = "1 Season (7 Eps)",
      synopsis = "A working-class boy in 1980s Brisbane faces the harsh realities of life - and the looming dangers that threaten his family.",
      cast = listOf("Felix Cameron", "Travis Fimmel", "Phoebe Tonkin", "Simon Baker"),
      director = "Bharat Nalluri",
      posterUrl = "https://images.unsplash.com/photo-1523482580672-f109ba8cb9be?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1523482580672-f109ba8cb9be?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=Kz6Pz5x0U8A",
      watchUrl = "https://www.netflix.com/au/title/81449961",
      qualityBadge = "4K Ultra HD",
      contentAdvisory = "MA 15+",
      isTrending = true,
      regionCode = "AU",
      vpnRequired = true,
      vpnRegionBadge = "🇦🇺 Australia Exclusive"
    )
  )

  private val auVideos = listOf(
    CuratedTrailer(
      title = "Boy Swallows Universe - Brisbane Adaptation Trailer",
      platform = OttPlatform.YOUTUBE,
      duration = "2:20",
      videoUrl = "https://www.youtube.com/watch?v=Kz6Pz5x0U8A",
      defaultPrompt = "Analyze the 1980s Australian suburban atmosphere and family bonds."
    )
  )

  // =========================================================================
  // 10. BRAZIL (BR)
  // =========================================================================
  private val brMovies = listOf(
    MediaItem(
      id = "br_mov_city_of_god",
      title = "City of God (Cidade de Deus 4K Remaster)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.MOVIE,
      genre = "Crime • Drama",
      rating = 8.6,
      votes = "790K",
      year = 2023,
      duration = "2h 10m",
      synopsis = "In the slums of Rio, two kids' paths diverge as one struggles to become a photographer and the other a kingpin. Unlocked via Brazilian Netflix feed.",
      cast = listOf("Alexandre Rodrigues", "Leandro Firmino", "Phellipe Haagensen", "Douglas Silva"),
      director = "Fernando Meirelles",
      posterUrl = "https://images.unsplash.com/photo-1483729558449-99ef09a8c325?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1483729558449-99ef09a8c325?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=dcUOO4Itgmw",
      watchUrl = "https://www.netflix.com/br/title/60026106",
      qualityBadge = "4K Remastered • Portuguese 5.1",
      contentAdvisory = "18+",
      isTrending = true,
      regionCode = "BR",
      vpnRequired = true,
      vpnRegionBadge = "🇧🇷 Brazil Exclusive"
    )
  )

  private val brTvShows = listOf(
    MediaItem(
      id = "br_show_sintonia",
      title = "Sintonia: Season 4 (São Paulo Direct)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.SERIES,
      genre = "Crime • Drama • Music",
      rating = 7.3,
      votes = "12K",
      year = 2023,
      duration = "4 Seasons (26 Eps)",
      synopsis = "Three teens living in the same São Paulo favela pursue their dreams while maintaining their friendship, amid a world of music, drugs and religion.",
      cast = listOf("Christian Malheiros", "Jottapê", "Bruna Mascarenhas"),
      director = "KondZilla",
      posterUrl = "https://images.unsplash.com/photo-1483729558449-99ef09a8c325?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1483729558449-99ef09a8c325?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=1F_4_3x6Q7k",
      watchUrl = "https://www.netflix.com/br/title/80217315",
      qualityBadge = "4K Ultra HD",
      contentAdvisory = "16+",
      isTrending = true,
      regionCode = "BR",
      vpnRequired = true,
      vpnRegionBadge = "🇧🇷 Brazil Exclusive"
    )
  )

  private val brVideos = listOf(
    CuratedTrailer(
      title = "Cidade de Deus 20 Years Celebration Teaser",
      platform = OttPlatform.YOUTUBE,
      duration = "2:40",
      videoUrl = "https://www.youtube.com/watch?v=dcUOO4Itgmw",
      defaultPrompt = "Examine the handheld kinetic camera movements and iconic Rio rhythm."
    )
  )

  // =========================================================================
  // 11. SPAIN (ES)
  // =========================================================================
  private val esMovies = listOf(
    MediaItem(
      id = "es_mov_society_of_the_snow",
      title = "Society of the Snow (La sociedad de la nieve)",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.MOVIE,
      genre = "Adventure • Biography • Drama",
      rating = 7.8,
      votes = "150K",
      year = 2023,
      duration = "2h 24m",
      synopsis = "The flight of a rugby team crashes onto a glacier in the heart of the Andes. Survivors rely on each other to survive.",
      cast = listOf("Enzo Vogrincic", "Agustín Pardella", "Matías Recalt"),
      director = "J.A. Bayona",
      posterUrl = "https://images.unsplash.com/photo-1543783207-ec64e4d95325?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1543783207-ec64e4d95325?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=pDak4qLyF2Q",
      watchUrl = "https://www.netflix.com/es/title/81268316",
      qualityBadge = "4K Dolby Vision • Spanish Atmos",
      contentAdvisory = "16+",
      isTrending = true,
      regionCode = "ES",
      vpnRequired = true,
      vpnRegionBadge = "🇪🇸 Spain Exclusive"
    )
  )

  private val esTvShows = listOf(
    MediaItem(
      id = "es_show_berlin",
      title = "Money Heist: Berlin",
      platform = OttPlatform.NETFLIX,
      mediaType = MediaType.SERIES,
      genre = "Action • Crime • Mystery",
      rating = 7.1,
      votes = "45K",
      year = 2023,
      duration = "1 Season (8 Eps)",
      synopsis = "During his golden age, Berlin gathers a gang in Paris to plan one of his most ambitious heists: stealing jewels worth 44 million euros.",
      cast = listOf("Pedro Alonso", "Michelle Jenner", "Tristán Ulloa", "Begoña Vargas"),
      director = "Álex Pina",
      posterUrl = "https://images.unsplash.com/photo-1543783207-ec64e4d95325?w=600&auto=format&fit=crop&q=80",
      backdropUrl = "https://images.unsplash.com/photo-1543783207-ec64e4d95325?w=1200&auto=format&fit=crop&q=80",
      trailerUrl = "https://www.youtube.com/watch?v=xdBXZ_hU4eM",
      watchUrl = "https://www.netflix.com/es/title/81586657",
      qualityBadge = "4K Ultra HD",
      contentAdvisory = "16+",
      isTrending = true,
      regionCode = "ES",
      vpnRequired = true,
      vpnRegionBadge = "🇪🇸 Spain Exclusive"
    )
  )

  private val esVideos = listOf(
    CuratedTrailer(
      title = "Society of the Snow - Andes Survival Teaser",
      platform = OttPlatform.YOUTUBE,
      duration = "2:15",
      videoUrl = "https://www.youtube.com/watch?v=pDak4qLyF2Q",
      defaultPrompt = "Examine J.A. Bayona's realistic sound design and emotional human endurance."
    )
  )

  // =========================================================================
  // Master Country Access Package Builder
  // =========================================================================
  fun getCountryAccessLibrary(countryCode: String): CountryAccessLibrary {
    val upper = countryCode.uppercase().trim()
    val server = WorldVpnCountries.servers.firstOrNull { it.countryCode.equals(upper, ignoreCase = true) }
    val countryName = server?.countryName ?: upper
    val flagEmoji = server?.flagEmoji ?: "🌐"
    val city = server?.city ?: "Capital Region"
    val unlockedServices = server?.unlockedServices ?: listOf("Netflix", "Prime Video", "National TV", "YouTube")

    val liveChannels = LiveTvCatalog.getChannelsForCountry(upper)

    return when (upper) {
      "US" -> CountryAccessLibrary(
        countryCode = "US",
        countryName = "United States",
        flagEmoji = "🇺🇸",
        city = "New York / Los Angeles",
        movies = usMovies,
        tvShows = usTvShows,
        liveTvChannels = liveChannels,
        videos = usVideos,
        unlockedPlatforms = unlockedServices
      )
      "GB" -> CountryAccessLibrary(
        countryCode = "GB",
        countryName = "United Kingdom",
        flagEmoji = "🇬🇧",
        city = "London",
        movies = gbMovies,
        tvShows = gbTvShows,
        liveTvChannels = liveChannels,
        videos = gbVideos,
        unlockedPlatforms = unlockedServices
      )
      "IN" -> CountryAccessLibrary(
        countryCode = "IN",
        countryName = "India",
        flagEmoji = "🇮🇳",
        city = "Mumbai",
        movies = inMovies,
        tvShows = inTvShows,
        liveTvChannels = liveChannels,
        videos = inVideos,
        unlockedPlatforms = unlockedServices
      )
      "JP" -> CountryAccessLibrary(
        countryCode = "JP",
        countryName = "Japan",
        flagEmoji = "🇯🇵",
        city = "Tokyo",
        movies = jpMovies,
        tvShows = jpTvShows,
        liveTvChannels = liveChannels,
        videos = jpVideos,
        unlockedPlatforms = unlockedServices
      )
      "FR" -> CountryAccessLibrary(
        countryCode = "FR",
        countryName = "France",
        flagEmoji = "🇫🇷",
        city = "Paris",
        movies = frMovies,
        tvShows = frTvShows,
        liveTvChannels = liveChannels,
        videos = frVideos,
        unlockedPlatforms = unlockedServices
      )
      "DE" -> CountryAccessLibrary(
        countryCode = "DE",
        countryName = "Germany",
        flagEmoji = "🇩🇪",
        city = "Frankfurt / Berlin",
        movies = deMovies,
        tvShows = deTvShows,
        liveTvChannels = liveChannels,
        videos = deVideos,
        unlockedPlatforms = unlockedServices
      )
      "KR" -> CountryAccessLibrary(
        countryCode = "KR",
        countryName = "South Korea",
        flagEmoji = "🇰🇷",
        city = "Seoul",
        movies = krMovies,
        tvShows = krTvShows,
        liveTvChannels = liveChannels,
        videos = krVideos,
        unlockedPlatforms = unlockedServices
      )
      "CA" -> CountryAccessLibrary(
        countryCode = "CA",
        countryName = "Canada",
        flagEmoji = "🇨🇦",
        city = "Toronto / Montreal",
        movies = caMovies,
        tvShows = caTvShows,
        liveTvChannels = liveChannels,
        videos = caVideos,
        unlockedPlatforms = unlockedServices
      )
      "AU" -> CountryAccessLibrary(
        countryCode = "AU",
        countryName = "Australia",
        flagEmoji = "🇦🇺",
        city = "Sydney",
        movies = auMovies,
        tvShows = auTvShows,
        liveTvChannels = liveChannels,
        videos = auVideos,
        unlockedPlatforms = unlockedServices
      )
      "BR" -> CountryAccessLibrary(
        countryCode = "BR",
        countryName = "Brazil",
        flagEmoji = "🇧🇷",
        city = "São Paulo",
        movies = brMovies,
        tvShows = brTvShows,
        liveTvChannels = liveChannels,
        videos = brVideos,
        unlockedPlatforms = unlockedServices
      )
      "ES" -> CountryAccessLibrary(
        countryCode = "ES",
        countryName = "Spain",
        flagEmoji = "🇪🇸",
        city = "Madrid / Barcelona",
        movies = esMovies,
        tvShows = esTvShows,
        liveTvChannels = liveChannels,
        videos = esVideos,
        unlockedPlatforms = unlockedServices
      )
      else -> {
        // Dynamically generated authentic library for any of the other 184 sovereign countries!
        generateCountryLibrary(upper, countryName, flagEmoji, city, unlockedServices, liveChannels)
      }
    }
  }

  private fun generateCountryLibrary(
    code: String,
    name: String,
    flag: String,
    city: String,
    unlockedServices: List<String>,
    liveChannels: List<LiveChannel>
  ): CountryAccessLibrary {
    val clean = code.lowercase()
    val platform1 = if (unlockedServices.any { it.contains("Netflix", ignoreCase = true) }) OttPlatform.NETFLIX else OttPlatform.PRIME_VIDEO
    val platform2 = if (unlockedServices.any { it.contains("Prime", ignoreCase = true) }) OttPlatform.PRIME_VIDEO else OttPlatform.NETFLIX

    val generatedMovies = listOf(
      MediaItem(
        id = "${clean}_mov_1",
        title = "$name National Cinema: Heritage",
        platform = platform1,
        mediaType = MediaType.MOVIE,
        genre = "Drama • National Cinema",
        rating = 8.4,
        votes = "42K",
        year = 2024,
        duration = "2h 14m",
        synopsis = "Critically acclaimed cinema celebrating the heritage, storytelling, and landscapes of $name. Unlocked exclusively in the $name region.",
        cast = listOf("Leading Star of $name", "Acclaimed Performer", "National Cast"),
        director = "$name Master Filmmaker",
        posterUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=600&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=1200&auto=format&fit=crop&q=80",
        trailerUrl = "https://www.youtube.com/results?search_query=${name.replace(' ', '+')}+movie+trailer",
        watchUrl = "https://www.netflix.com",
        qualityBadge = "4K Ultra HD • Original Language",
        contentAdvisory = "16+",
        isTrending = true,
        regionCode = code,
        vpnRequired = true,
        vpnRegionBadge = "$flag $name Exclusive"
      ),
      MediaItem(
        id = "${clean}_mov_2",
        title = "Chronicles of $city",
        platform = platform2,
        mediaType = MediaType.MOVIE,
        genre = "Thriller • Mystery • Crime",
        rating = 7.9,
        votes = "28K",
        year = 2023,
        duration = "1h 58m",
        synopsis = "A high-stakes thriller navigating the underground currents, culture, and architecture of $city in $name.",
        cast = listOf("Award-Winning Detective", "Leading Protagonist"),
        director = "Visionary $name Director",
        posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
        trailerUrl = "https://www.youtube.com/results?search_query=${name.replace(' ', '+')}+cinema",
        watchUrl = "https://www.primevideo.com",
        qualityBadge = "4K HDR10+ • Atmos",
        contentAdvisory = "16+",
        isTrending = true,
        regionCode = code,
        vpnRequired = true,
        vpnRegionBadge = "$flag $name Exclusive"
      )
    )

    val generatedTvShows = listOf(
      MediaItem(
        id = "${clean}_show_1",
        title = "$name Prime Series: The Genesis",
        platform = platform1,
        mediaType = MediaType.SERIES,
        genre = "Drama • Political Intrigue",
        rating = 8.6,
        votes = "35K",
        year = 2024,
        duration = "2 Seasons (16 Eps)",
        synopsis = "Top-rated television series broadcast in $name following political rivalries, historical intrigue, and personal ambition.",
        cast = listOf("Ensemble Cast of $name", "Prominent Drama Star"),
        director = "Showrunner of $name",
        posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&auto=format&fit=crop&q=80",
        trailerUrl = "https://www.youtube.com/results?search_query=${name.replace(' ', '+')}+tv+series",
        watchUrl = "https://www.netflix.com",
        qualityBadge = "4K Ultra HD • 60fps",
        contentAdvisory = "16+",
        isTrending = true,
        regionCode = code,
        vpnRequired = true,
        vpnRegionBadge = "$flag $name Exclusive"
      ),
      MediaItem(
        id = "${clean}_show_2",
        title = "Underground $name: City Tales",
        platform = platform2,
        mediaType = MediaType.SERIES,
        genre = "Crime • Action • Suspense",
        rating = 8.2,
        votes = "19K",
        year = 2023,
        duration = "1 Season (8 Eps)",
        synopsis = "Dark, gripping urban drama depicting the untold stories, investigative journalism, and nightlife of $name.",
        cast = listOf("Investigative Reporter", "Crime Syndicate Boss"),
        director = "Leading $name Director",
        posterUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=600&auto=format&fit=crop&q=80",
        backdropUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=1200&auto=format&fit=crop&q=80",
        trailerUrl = "https://www.youtube.com/results?search_query=${name.replace(' ', '+')}+crime+series",
        watchUrl = "https://www.primevideo.com",
        qualityBadge = "1080p HD",
        contentAdvisory = "18+",
        isTrending = false,
        regionCode = code,
        vpnRequired = true,
        vpnRegionBadge = "$flag $name Exclusive"
      )
    )

    val generatedVideos = listOf(
      CuratedTrailer(
        title = "$name Cinema Spotlight & 4K Teaser",
        platform = OttPlatform.YOUTUBE,
        duration = "2:30",
        videoUrl = "https://www.youtube.com/results?search_query=${name.replace(' ', '+')}+movie+trailer",
        defaultPrompt = "Examine the cultural aesthetics, regional landscape cinematography, and narrative tone of $name."
      ),
      CuratedTrailer(
        title = "$city Aerial Tour & Cultural Highlights",
        platform = OttPlatform.YOUTUBE,
        duration = "4:15",
        videoUrl = "https://www.youtube.com/results?search_query=${city.replace(' ', '+')}+4k+travel",
        defaultPrompt = "Describe the landmark architecture, cultural rhythm, and visual beauty of $city, $name."
      )
    )

    return CountryAccessLibrary(
      countryCode = code,
      countryName = name,
      flagEmoji = flag,
      city = city,
      movies = generatedMovies,
      tvShows = generatedTvShows,
      liveTvChannels = liveChannels,
      videos = generatedVideos,
      unlockedPlatforms = unlockedServices
    )
  }

  fun getItemsForRegion(countryCode: String): List<MediaItem> {
    val pkg = getCountryAccessLibrary(countryCode)
    return pkg.allMediaItems
  }

  fun getMoviesForRegion(countryCode: String): List<MediaItem> {
    return getCountryAccessLibrary(countryCode).movies
  }

  fun getTvShowsForRegion(countryCode: String): List<MediaItem> {
    return getCountryAccessLibrary(countryCode).tvShows
  }

  fun getVideosForRegion(countryCode: String): List<CuratedTrailer> {
    return getCountryAccessLibrary(countryCode).videos
  }
}
