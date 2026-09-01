/*
 * Mualla-Music (2026)
 * © 🖤 Muallaltay — github.com/ThT0AltayHR
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.Muallaltay.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.Muallaltay.innertube.YouTube
import com.Muallaltay.innertube.models.YTItem
import com.Muallaltay.utils.reportException
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val browseId: String? = savedStateHandle.get<String>("browseId")

        val items = MutableStateFlow<List<YTItem>?>(emptyList())
        val title = MutableStateFlow<String?>("")

        init {
            viewModelScope.launch {
                browseId?.let {
                    YouTube
                        .browse(browseId, null)
                        .onSuccess { result ->
                            // Store the title
                            title.value = result.title

                            // Flatten the nested structure to get all YTItems
                            val allItems = result.items.flatMap { it.items }
                            items.value = allItems
                        }.onFailure {
                            reportException(it)
                        }
                }
            }
        }
    }
