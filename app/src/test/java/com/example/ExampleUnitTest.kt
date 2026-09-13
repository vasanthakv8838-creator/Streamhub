package com.example

import com.example.model.OttPlatform
import com.example.model.PlatformPlansRepository
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun `verify all 7 platforms have subscription plans configured`() {
    val plans = PlatformPlansRepository.allPlans
    assertTrue(plans.isNotEmpty())

    val platformsInPlans = plans.map { it.platform }.toSet()
    val expectedPlatforms = setOf(
      OttPlatform.NETFLIX,
      OttPlatform.PRIME_VIDEO,
      OttPlatform.JIO_HOTSTAR,
      OttPlatform.YOUTUBE,
      OttPlatform.APPLE_TV,
      OttPlatform.SONY_LIV,
      OttPlatform.ZEE5
    )

    assertEquals(expectedPlatforms, platformsInPlans)

    // Check that each plan has valid attributes
    for (plan in plans) {
      assertTrue(plan.id.isNotBlank())
      assertTrue(plan.planName.isNotBlank())
      assertTrue(plan.priceDisplay.startsWith("₹"))
      assertTrue(plan.perks.isNotEmpty())
      assertTrue(plan.subscribeUrl.startsWith("http"))
      assertTrue(plan.screensCount >= 1)
    }
  }

  @Test
  fun `verify all 7 platforms have rich content in OttCatalog`() {
    val items = com.example.data.OttCatalog.items
    assertTrue(items.size >= 40)

    val platformsInCatalog = items.map { it.platform }.toSet()
    val expectedPlatforms = setOf(
      OttPlatform.NETFLIX,
      OttPlatform.PRIME_VIDEO,
      OttPlatform.JIO_HOTSTAR,
      OttPlatform.YOUTUBE,
      OttPlatform.APPLE_TV,
      OttPlatform.SONY_LIV,
      OttPlatform.ZEE5
    )

    assertEquals(expectedPlatforms, platformsInCatalog)

    for (p in expectedPlatforms) {
      val platformItems = items.filter { it.platform == p }
      assertTrue("Platform ${p.displayName} should have at least 5 items", platformItems.size >= 5)
      assertTrue("Platform ${p.displayName} should have at least 1 trending item", platformItems.any { it.isTrending })
    }
  }

  @Test
  fun `verify vip tier is free and gives access to all platforms`() {
    val vipPlan = PlatformPlansRepository.vipPlan
    assertNotNull(vipPlan)
    assertEquals(OttPlatform.ALL, vipPlan.platform)
    assertEquals("FREE", vipPlan.priceDisplay)
    assertTrue(vipPlan.period.contains("₹0") || vipPlan.period.contains("free", ignoreCase = true))
    assertTrue(vipPlan.perks.any { it.contains("all", ignoreCase = true) })
    assertTrue(vipPlan.screensCount > 4)
  }
}
