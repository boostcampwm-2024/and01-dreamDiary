package com.boostcamp.dreamteam.dreamdiary.feature.diary.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDreamDiariesByTitleUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetSearchSuggestions
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.toDiaryUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DiarySearchViewModel @Inject constructor(
    private val getSearchSuggestions: GetSearchSuggestions,
    private val getDreamDiariesByTitleUseCase: GetDreamDiariesByTitleUseCase,
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults = searchQuery.flatMapLatest { searchQuery ->
        getDreamDiariesByTitleUseCase(searchQuery).map { pagingData ->
            pagingData.map { it.toDiaryUi() }
        }
    }.cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(DiarySearchUiState())
    val uiState = _uiState.asStateFlow()

    init {
        collectSearchSuggestions()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.update { query }
    }

    @OptIn(FlowPreview::class)
    private fun collectSearchSuggestions() {
        searchQuery
            .sample(0.5.seconds)
            .onEach { query ->
                getSearchSuggestions(query).let { suggestions ->
                    _uiState.update { it.copy(searchSuggestions = suggestions) }
                }
            }.launchIn(viewModelScope)
    }
}
