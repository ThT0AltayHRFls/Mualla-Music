/*
 * Mualla-Music (2026)
 * © 🖤 Muallaltay — github.com/ThT0AltayHR
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.Muallaltay.utils.potoken

/**
 * Holds the two PoToken variants produced by a single BotGuard minting cycle.
 *
 * - [playerToken]: bound to a specific video — sent in the player request's
 *   `serviceIntegrityDimensions.poToken`.
 * - [sessionToken]: bound to the visitor/dataSync session — used as the
 *   `pot=` query-parameter on the actual audio stream URL.
 */
data class PoTokenResult(
    val playerToken: String,
    val sessionToken: String,
)
