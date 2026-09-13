package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.StreamProfile
import com.example.model.UserProfile
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class UserSessionManager(context: Context) {
  private val prefs: SharedPreferences =
    context.getSharedPreferences("ott_user_session", Context.MODE_PRIVATE)

  companion object {
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_NAME = "user_name"
    private const val KEY_PHONE = "user_phone"
    private const val KEY_JOINED_AT = "user_joined_at"
    private const val KEY_PROFILES_JSON = "user_profiles_json"
    private const val KEY_ACTIVE_PROFILE_ID = "active_profile_id"
    private const val KEY_IS_VIP = "is_vip_active"

    val PROFILE_COLORS = listOf(
      "#FFB800", // Gold
      "#00A8E1", // Cyan
      "#E50914", // Netflix Red
      "#8B5CF6", // Purple
      "#10B981", // Emerald
      "#F43F5E", // Rose
      "#06B6D4", // Teal
      "#F97316"  // Orange
    )
  }

  fun isLoggedIn(): Boolean {
    return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
  }

  fun getUserProfile(): UserProfile? {
    if (!isLoggedIn()) return null
    val name = prefs.getString(KEY_NAME, null) ?: return null
    val phone = prefs.getString(KEY_PHONE, null) ?: return null
    val joinedAt = prefs.getLong(KEY_JOINED_AT, System.currentTimeMillis())
    val isVip = isVip()
    return UserProfile(name = name, phoneNumber = phone, joinedAt = joinedAt, isVip = isVip)
  }

  fun isVip(): Boolean {
    return prefs.getBoolean(KEY_IS_VIP, false)
  }

  fun setVip(active: Boolean) {
    prefs.edit().putBoolean(KEY_IS_VIP, active).apply()
  }

  fun saveUser(name: String, phoneNumber: String): UserProfile {
    val now = System.currentTimeMillis()
    val initialProfileId = UUID.randomUUID().toString()
    val defaultProfile = StreamProfile(
      id = initialProfileId,
      name = name.trim(),
      avatarColorHex = PROFILE_COLORS[0],
      isKids = false,
      createdAt = now
    )

    val profilesJson = JSONArray().apply {
      put(profileToJson(defaultProfile))
    }.toString()

    prefs.edit()
      .putBoolean(KEY_IS_LOGGED_IN, true)
      .putString(KEY_NAME, name.trim())
      .putString(KEY_PHONE, phoneNumber.trim())
      .putLong(KEY_JOINED_AT, now)
      .putString(KEY_PROFILES_JSON, profilesJson)
      .putString(KEY_ACTIVE_PROFILE_ID, initialProfileId)
      .apply()

    return UserProfile(name = name.trim(), phoneNumber = phoneNumber.trim(), joinedAt = now)
  }

  fun getProfiles(): List<StreamProfile> {
    val jsonStr = prefs.getString(KEY_PROFILES_JSON, null) ?: return emptyList()
    val list = mutableListOf<StreamProfile>()
    try {
      val array = JSONArray(jsonStr)
      for (i in 0 until array.length()) {
        val obj = array.getJSONObject(i)
        list.add(
          StreamProfile(
            id = obj.optString("id", UUID.randomUUID().toString()),
            name = obj.optString("name", "User"),
            avatarColorHex = obj.optString("color", PROFILE_COLORS[i % PROFILE_COLORS.size]),
            isKids = obj.optBoolean("isKids", false),
            createdAt = obj.optLong("createdAt", System.currentTimeMillis())
          )
        )
      }
    } catch (e: Exception) {
      // Fallback to primary user profile if parsing error
      val user = getUserProfile()
      if (user != null) {
        return listOf(StreamProfile(id = "primary", name = user.name))
      }
    }
    return list
  }

  fun addProfile(name: String, isKids: Boolean = false): StreamProfile {
    val currentProfiles = getProfiles().toMutableList()
    val colorIndex = currentProfiles.size % PROFILE_COLORS.size
    val newProfile = StreamProfile(
      id = UUID.randomUUID().toString(),
      name = name.trim(),
      avatarColorHex = PROFILE_COLORS[colorIndex],
      isKids = isKids,
      createdAt = System.currentTimeMillis()
    )
    currentProfiles.add(newProfile)
    saveProfilesList(currentProfiles)
    return newProfile
  }

  fun deleteProfile(profileId: String): Boolean {
    val currentProfiles = getProfiles().toMutableList()
    if (currentProfiles.size <= 1) {
      // Cannot delete the only profile
      return false
    }
    val removed = currentProfiles.removeAll { it.id == profileId }
    if (removed) {
      saveProfilesList(currentProfiles)
      if (getActiveProfileId() == profileId) {
        setActiveProfileId(currentProfiles.first().id)
      }
    }
    return removed
  }

  private fun saveProfilesList(profiles: List<StreamProfile>) {
    val array = JSONArray()
    profiles.forEach { profile ->
      array.put(profileToJson(profile))
    }
    prefs.edit().putString(KEY_PROFILES_JSON, array.toString()).apply()
  }

  fun getActiveProfileId(): String? {
    return prefs.getString(KEY_ACTIVE_PROFILE_ID, null) ?: getProfiles().firstOrNull()?.id
  }

  fun setActiveProfileId(id: String) {
    prefs.edit().putString(KEY_ACTIVE_PROFILE_ID, id).apply()
  }

  fun getActiveProfile(): StreamProfile? {
    val profiles = getProfiles()
    if (profiles.isEmpty()) {
      val user = getUserProfile() ?: return null
      return StreamProfile(id = "primary", name = user.name)
    }
    val activeId = getActiveProfileId()
    return profiles.firstOrNull { it.id == activeId } ?: profiles.firstOrNull()
  }

  private fun profileToJson(profile: StreamProfile): JSONObject {
    return JSONObject().apply {
      put("id", profile.id)
      put("name", profile.name)
      put("color", profile.avatarColorHex)
      put("isKids", profile.isKids)
      put("createdAt", profile.createdAt)
    }
  }

  fun clearSession() {
    prefs.edit().clear().apply()
  }
}
