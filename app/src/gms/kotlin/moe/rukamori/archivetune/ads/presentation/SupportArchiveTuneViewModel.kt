/*
 * Mualla-Music (2026)
 * © 🖤 Muallaltay — github.com/ThT0AltayHR
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.Muallaltay.ads.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import com.Muallaltay.ads.domain.OpenSupportPageUseCase
import com.Muallaltay.ads.domain.SupportPageOpenResult
import javax.inject.Inject

internal sealed interface SupportMualla-MusicScreenState {
    @Immutable
    data object Loading : SupportMualla-MusicScreenState

    @Immutable
    data object Success : SupportMualla-MusicScreenState

    @Immutable
    data object Empty : SupportMualla-MusicScreenState

    @Immutable
    data class Error(
        val reason: SupportMualla-MusicError,
    ) : SupportMualla-MusicScreenState
}

internal enum class SupportMualla-MusicError {
    PageUnavailable,
}

internal enum class SupportMualla-MusicUiEvent {
    OpenFailed,
}

@HiltViewModel
internal class SupportMualla-MusicViewModel
    @Inject
    constructor(
        private val openSupportPage: OpenSupportPageUseCase,
    ) : ViewModel() {
        private val _screenState =
            MutableStateFlow<SupportMualla-MusicScreenState>(SupportMualla-MusicScreenState.Success)
        val screenState: StateFlow<SupportMualla-MusicScreenState> = _screenState.asStateFlow()

        private val eventChannel = Channel<SupportMualla-MusicUiEvent>(Channel.BUFFERED)
        val events = eventChannel.receiveAsFlow()

        fun onSupportMualla-MusicClick() {
            if (_screenState.value is SupportMualla-MusicScreenState.Loading) return
            _screenState.value = SupportMualla-MusicScreenState.Loading
            when (openSupportPage()) {
                SupportPageOpenResult.Opened -> {
                    _screenState.value = SupportMualla-MusicScreenState.Success
                }

                SupportPageOpenResult.Unavailable -> {
                    _screenState.value =
                        SupportMualla-MusicScreenState.Error(SupportMualla-MusicError.PageUnavailable)
                    eventChannel.trySend(SupportMualla-MusicUiEvent.OpenFailed)
                }
            }
        }
    }
