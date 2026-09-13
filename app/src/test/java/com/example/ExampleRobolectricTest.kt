package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("OTT Aggregator", appName)
  }

  @Test
  fun `verify user can add profiles with only names`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val session = com.example.data.UserSessionManager(context)
    session.clearSession()

    // 1. Initial verification & user save
    val user = session.saveUser("Alex Rivera", "9876543210")
    assertEquals("Alex Rivera", user.name)
    assertEquals(1, session.getProfiles().size)
    assertEquals("Alex Rivera", session.getProfiles().first().name)

    // 2. Add profile with ONLY name (e.g. "Kids")
    val kidsProfile = session.addProfile("Kids", isKids = true)
    assertEquals("Kids", kidsProfile.name)
    assertEquals(true, kidsProfile.isKids)

    // 3. Add another profile with ONLY name (e.g. "Family")
    val familyProfile = session.addProfile("Family", isKids = false)
    assertEquals("Family", familyProfile.name)
    assertEquals(false, familyProfile.isKids)

    // Total profiles should now be 3
    val profiles = session.getProfiles()
    assertEquals(3, profiles.size)
    assertEquals(listOf("Alex Rivera", "Kids", "Family"), profiles.map { it.name })

    // 4. Switch active profile
    session.setActiveProfileId(kidsProfile.id)
    assertEquals("Kids", session.getActiveProfile()?.name)

    // 5. Delete an extra profile
    val deleted = session.deleteProfile(familyProfile.id)
    assertEquals(true, deleted)
    assertEquals(2, session.getProfiles().size)
  }
}
