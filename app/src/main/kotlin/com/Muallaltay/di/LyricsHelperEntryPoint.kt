/*
 * Mualla-Music (2026)
 * © 🖤 Muallaltay — github.com/ThT0AltayHR
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.Muallaltay.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.Muallaltay.lyrics.LyricsHelper
import com.Muallaltay.lyrics.LyricsPreloadManager

@EntryPoint
@InstallIn(SingletonComponent::class)
interface LyricsHelperEntryPoint {
    fun lyricsHelper(): LyricsHelper

    fun lyricsPreloadManager(): LyricsPreloadManager
}
