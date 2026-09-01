/*
 * Mualla-Music (2026)
 * © 🖤 Muallaltay — github.com/ThT0AltayHR
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.Muallaltay.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.Muallaltay.aicontentfilter.FilterAiContentUseCase
import com.Muallaltay.aicontentfilter.LoadAiContentFilterPolicyUseCase
import com.Muallaltay.constants.HideExplicitKey
import com.Muallaltay.constants.HideVideoKey
import com.Muallaltay.innertube.YouTube
import com.Muallaltay.innertube.YouTube.SearchFilter.Companion.FILTER_ALBUM
import com.Muallaltay.innertube.YouTube.SearchFilter.Companion.FILTER_ARTIST
import com.Muallaltay.innertube.YouTube.SearchFilter.Companion.FILTER_COMMUNITY_PLAYLIST
import com.Muallaltay.innertube.YouTube.SearchFilter.Companion.FILTER_FEATURED_PLAYLIST
import com.Muallaltay.innertube.YouTube.SearchFilter.Companion.FILTER_SONG
import com.Muallaltay.innertube.YouTube.SearchFilter.Companion.FILTER_VIDEO
import com.Muallaltay.innertube.models.SongItem
import com.Muallaltay.innertube.models.YTItem
import com.Muallaltay.innertube.models.filterExplicit
import com.Muallaltay.innertube.models.filterVideo
import com.Muallaltay.innertube.pages.SearchSummaryPage
import com.Muallaltay.models.ItemsPage
import com.Muallaltay.ui.screens.search.OnlineSearchResultArgument
import com.Muallaltay.ui.screens.search.decodeOnlineSearchQuery
import com.Muallaltay.utils.dataStore
import com.Muallaltay.utils.get
import com.Muallaltay.utils.reportException
import javax.inject.Inject

enum class OnlineSearchSort {
    DEFAULT,
    VIEWS,
}

@HiltViewModel
class OnlineSearchViewModel
    @Inject
    constructor(
        @ApplicationContext val context: Context,
        savedStateHandle: SavedStateHandle,
        private val loadAiContentFilterPolicy: LoadAiContentFilterPolicyUseCase,
        private val filterAiContent: FilterAiContentUseCase,
    ) : ViewModel() {
        val query =
            decodeOnlineSearchQuery(
                savedStateHandle.get<String>(OnlineSearchResultArgument).orEmpty(),
            )
        val filter = MutableStateFlow<YouTube.SearchFilter?>(null)
        var summaryPage by mutableStateOf<SearchSummaryPage?>(null)
        val viewStateMap = mutableStateMapOf<String, ItemsPage?>()

        private val allModeFilters =
            listOf(
                FILTER_SONG,
                FILTER_VIDEO,
                FILTER_ALBUM,
                FILTER_ARTIST,
                FILTER_COMMUNITY_PLAYLIST,
                FILTER_FEATURED_PLAYLIST,
            )
        private var isSummaryLoading = false
        private val loadingFilters = mutableSetOf<String>()

        init {
            viewModelScope.launch {
                filter.collect { selectedFilter ->
                    if (selectedFilter == null) {
                        viewModelScope.launch {
                            loadSummaryIfNeeded()
                        }
                        allModeFilters.forEach { allModeFilter ->
                            viewModelScope.launch {
                                loadFilterIfNeeded(allModeFilter)
                            }
                        }
                    } else {
                        loadFilterIfNeeded(selectedFilter)
                    }
                }
            }
        }

        private suspend fun loadSummaryIfNeeded() {
            if (summaryPage != null || isSummaryLoading) return

            isSummaryLoading = true
            try {
                YouTube
                    .searchSummary(query)
                    .onSuccess {
                        val aiContentFilterPolicy = loadAiContentFilterPolicy()
                        val contentFilteredPage =
                            it
                                .filterExplicit(context.dataStore.get(HideExplicitKey, false))
                                .filterVideo(context.dataStore.get(HideVideoKey, false))
                        summaryPage =
                            contentFilteredPage.copy(
                                summaries =
                                    contentFilteredPage.summaries.mapNotNull { summary ->
                                        summary
                                            .copy(items = filterAiContent(summary.items, aiContentFilterPolicy))
                                            .takeIf { filteredSummary -> filteredSummary.items.isNotEmpty() }
                                    },
                            )
                    }.onFailure {
                        reportException(it)
                    }
            } finally {
                isSummaryLoading = false
            }
        }

        private suspend fun loadFilterIfNeeded(filter: YouTube.SearchFilter) {
            val filterKey = filter.value
            if (viewStateMap.containsKey(filterKey) || !loadingFilters.add(filterKey)) return

            try {
                YouTube
                    .search(query, filter)
                    .onSuccess { result ->
                        val aiContentFilterPolicy = loadAiContentFilterPolicy()
                        viewStateMap[filterKey] =
                            ItemsPage(
                                filterAiContent(
                                    result.items
                                        .distinctBy { it.id }
                                        .filterExplicit(
                                            context.dataStore.get(
                                                HideExplicitKey,
                                                false,
                                            ),
                                        ).filterVideo(context.dataStore.get(HideVideoKey, false)),
                                    aiContentFilterPolicy,
                                ),
                                result.continuation,
                            )
                    }.onFailure {
                        reportException(it)
                    }
            } finally {
                loadingFilters.remove(filterKey)
            }
        }

        fun loadMore() {
            val filter = filter.value?.value
            viewModelScope.launch {
                if (filter == null) return@launch
                val viewState = viewStateMap[filter] ?: return@launch
                val continuation = viewState.continuation
                if (continuation != null) {
                    val searchResult =
                        YouTube.searchContinuation(continuation).getOrNull() ?: return@launch
                    val aiContentFilterPolicy = loadAiContentFilterPolicy()
                    val continuedItems =
                        filterAiContent(
                            searchResult.items
                                .filterExplicit(context.dataStore.get(HideExplicitKey, false))
                                .filterVideo(context.dataStore.get(HideVideoKey, false)),
                            aiContentFilterPolicy,
                        )
                    viewStateMap[filter] =
                        ItemsPage(
                            (viewState.items + continuedItems).distinctBy { it.id },
                            searchResult.continuation,
                        )
                }
            }
        }

        fun sortedItems(
            items: List<YTItem>,
            sort: OnlineSearchSort = OnlineSearchSort.DEFAULT,
        ): List<YTItem> =
            when (sort) {
                OnlineSearchSort.DEFAULT -> {
                    items
                }

                OnlineSearchSort.VIEWS -> {
                    items
                        .withIndex()
                        .sortedWith(
                            compareByDescending<IndexedValue<YTItem>> {
                                (it.value as? SongItem)?.viewCount ?: Long.MIN_VALUE
                            }.thenBy { it.index },
                        ).map { it.value }
                }
            }
    }
