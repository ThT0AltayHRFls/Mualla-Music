/*
 * Mualla-Music (2026)
 * © 🖤 Muallaltay — github.com/ThT0AltayHR
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.Muallaltay.lyrics

import android.content.Context
import com.Muallaltay.betterlyrics.BetterLyrics
import com.Muallaltay.constants.EnableBetterLyricsPortatoKey
import com.Muallaltay.utils.dataStore
import com.Muallaltay.utils.get

object BetterLyricsPortatoProvider : LyricsProvider {
    override val name = "BetterLyrics Portato"

    override fun isEnabled(context: Context): Boolean = context.dataStore[EnableBetterLyricsPortatoKey] ?: true

    override suspend fun getLyrics(
        id: String,
        title: String,
        artist: String,
        album: String?,
        duration: Int,
    ): Result<String> =
        BetterLyrics.getPortatoLyrics(
            title = title,
            artist = artist,
            album = album,
            durationSeconds = duration,
        )

    override suspend fun getAllLyrics(
        id: String,
        title: String,
        artist: String,
        album: String?,
        duration: Int,
        callback: (String) -> Unit,
    ) {
        BetterLyrics.getAllPortatoLyrics(
            title = title,
            artist = artist,
            album = album,
            durationSeconds = duration,
            callback = callback,
        )
    }
}
