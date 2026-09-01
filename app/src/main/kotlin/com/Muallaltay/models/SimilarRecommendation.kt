/*
 * Mualla-Music (2026)
 * © 🖤 Muallaltay — github.com/ThT0AltayHR
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.Muallaltay.models

import com.Muallaltay.db.entities.LocalItem
import com.Muallaltay.innertube.models.YTItem

data class SimilarRecommendation(
    val title: LocalItem,
    val items: List<YTItem>,
)
