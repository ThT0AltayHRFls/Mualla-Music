/*
 * Mualla-Music (2026)
 * © 🖤 Muallaltay — github.com/ThT0AltayHR
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.Muallaltay.home

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import com.Muallaltay.constants.DisableBlurKey
import com.Muallaltay.constants.QuickPicks
import com.Muallaltay.constants.QuickPicksKey
import com.Muallaltay.constants.QuickPicksDisplayMode
import com.Muallaltay.constants.QuickPicksDisplayModeKey
import com.Muallaltay.constants.ShowHomeCategoryChipsKey
import com.Muallaltay.extensions.toEnum
import com.Muallaltay.utils.dataStore
import javax.inject.Inject

class HomeRepository
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) {
        val showCategoryChips: Flow<Boolean> =
            context.dataStore.data
                .map { preferences -> preferences[ShowHomeCategoryChipsKey] ?: true }
                .distinctUntilChanged()

        val quickPicksDisplayMode: Flow<QuickPicksDisplayMode> =
            context.dataStore.data
                .map { preferences ->
                    preferences[QuickPicksDisplayModeKey].toEnum(QuickPicksDisplayMode.CARD)
                }.distinctUntilChanged()

        val quickPicksMode: Flow<QuickPicks> =
            context.dataStore.data
                .map { preferences -> preferences[QuickPicksKey].toEnum(QuickPicks.QUICK_PICKS) }
                .distinctUntilChanged()

        val showTonalBackdrop: Flow<Boolean> =
            context.dataStore.data
                .map { preferences -> preferences[DisableBlurKey] != true }
                .distinctUntilChanged()
    }
