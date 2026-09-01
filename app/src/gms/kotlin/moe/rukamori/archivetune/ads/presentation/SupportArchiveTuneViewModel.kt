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

internal sealed interface SupportMuallaMusicScreenState {
    @Immutable
    data object Loading : SupportMuallaMusicScreenState

    @Immutable
    data object Success : SupportMuallaMusicScreenState

    @Immutable
    data object Empty : SupportMuallaMusicScreenState

    @Immutable
    data class Error(
        val reason: SupportMuallaMusicError,
    ) : SupportMuallaMusicScreenState
}

internal enum class SupportMuallaMusicError {
    PageUnavailable,
}

internal enum class SupportMuallaMusicUiEvent {
    OpenFailed,
}

@HiltViewModel
internal class SupportMuallaMusicViewModel
    @Inject
    constructor(
        private val openSupportPage: OpenSupportPageUseCase,
    ) : ViewModel() {
        private val _screenState =
            MutableStateFlow<SupportMuallaMusicScreenState>(SupportMuallaMusicScreenState.Success)
        val screenState: StateFlow<SupportMuallaMusicScreenState> = _screenState.asStateFlow()

        private val eventChannel = Channel<SupportMuallaMusicUiEvent>(Channel.BUFFERED)
        val events = eventChannel.receiveAsFlow()

        fun onSupportMuallaMusicClick() {
            if (_screenState.value is SupportMuallaMusicScreenState.Loading) return
            _screenState.value = SupportMuallaMusicScreenState.Loading
            when (openSupportPage()) {
                SupportPageOpenResult.Opened -> {
                    _screenState.value = SupportMuallaMusicScreenState.Success
                }

                SupportPageOpenResult.Unavailable -> {
                    _screenState.value =
                        SupportMuallaMusicScreenState.Error(SupportMuallaMusicError.PageUnavailable)
                    eventChannel.trySend(SupportMuallaMusicUiEvent.OpenFailed)
                }
            }
        }
    }
