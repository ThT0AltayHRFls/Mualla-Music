/*
 * Mualla-Music (2026)
 * © 🖤 Muallaltay — github.com/ThT0AltayHR
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.Muallaltay.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnboardingPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _hasCompletedOnboarding = MutableStateFlow(getHasCompletedOnboarding())
    val hasCompletedOnboarding: StateFlow<Boolean> = _hasCompletedOnboarding.asStateFlow()

    private val _userGender = MutableStateFlow(getUserGender())
    val userGender: StateFlow<String?> = _userGender.asStateFlow()

    private val _favoriteGenres = MutableStateFlow(getFavoriteGenres())
    val favoriteGenres: StateFlow<List<String>> = _favoriteGenres.asStateFlow()

    private val _favoriteArtists = MutableStateFlow(getFavoriteArtists())
    val favoriteArtists: StateFlow<List<String>> = _favoriteArtists.asStateFlow()

    fun saveOnboardingData(
        gender: String?,
        genres: List<String>,
        artists: List<String>,
    ) {
        sharedPreferences.edit().apply {
            putBoolean(KEY_ONBOARDING_COMPLETED, true)
            gender?.let { putString(KEY_USER_GENDER, it) }
            putString(KEY_FAVORITE_GENRES, gson.toJson(genres))
            putString(KEY_FAVORITE_ARTISTS, gson.toJson(artists))
            apply()
        }

        _hasCompletedOnboarding.value = true
        _userGender.value = gender
        _favoriteGenres.value = genres
        _favoriteArtists.value = artists
    }

    private fun getHasCompletedOnboarding(): Boolean =
        sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)

    private fun getUserGender(): String? = sharedPreferences.getString(KEY_USER_GENDER, null)

    private fun getFavoriteGenres(): List<String> {
        val json = sharedPreferences.getString(KEY_FAVORITE_GENRES, "[]")
        return try {
            gson.fromJson(json, Array<String>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun getFavoriteArtists(): List<String> {
        val json = sharedPreferences.getString(KEY_FAVORITE_ARTISTS, "[]")
        return try {
            gson.fromJson(json, Array<String>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun resetOnboarding() {
        sharedPreferences.edit().apply {
            remove(KEY_ONBOARDING_COMPLETED)
            remove(KEY_USER_GENDER)
            remove(KEY_FAVORITE_GENRES)
            remove(KEY_FAVORITE_ARTISTS)
            apply()
        }
        _hasCompletedOnboarding.value = false
        _userGender.value = null
        _favoriteGenres.value = emptyList()
        _favoriteArtists.value = emptyList()
    }

    companion object {
        private const val PREFS_NAME = "onboarding_prefs"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_USER_GENDER = "user_gender"
        private const val KEY_FAVORITE_GENRES = "favorite_genres"
        private const val KEY_FAVORITE_ARTISTS = "favorite_artists"
    }
}
