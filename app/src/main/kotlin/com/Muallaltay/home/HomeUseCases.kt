/*
 * Mualla-Music (2026)
 * © 🖤 Muallaltay — github.com/ThT0AltayHR
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.Muallaltay.home

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import com.Muallaltay.constants.QuickPicks
import com.Muallaltay.constants.QuickPicksDisplayMode
import javax.inject.Inject

class ObserveHomePresentationPreferencesUseCase
    @Inject
    constructor(
        private val repository: HomeRepository,
    ) {
        operator fun invoke(): Flow<HomePresentationPreferences> =
            combine(
                repository.showCategoryChips,
                repository.quickPicksDisplayMode,
                repository.quickPicksMode,
                repository.showTonalBackdrop,
            ) { showCategoryChips, quickPicksDisplayMode, quickPicksMode, showTonalBackdrop ->
                HomePresentationPreferences(
                    showCategoryChips = showCategoryChips,
                    quickPicksDisplayMode = quickPicksDisplayMode,
                    quickPicksMode = quickPicksMode,
                    showTonalBackdrop = showTonalBackdrop,
                )
            }
    }

@Immutable
data class HomePresentationPreferences(
    val showCategoryChips: Boolean,
    val quickPicksDisplayMode: QuickPicksDisplayMode,
    val quickPicksMode: QuickPicks,
    val showTonalBackdrop: Boolean,
)
