package com.boostcamp.dreamteam.dreamdiary.feature.diary.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetSearchSuggestions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DiarySearchViewModel @Inject constructor(
    private val getSearchSuggestions: GetSearchSuggestions,
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

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
        viewModelScope.launch {
            searchQuery
                .sample(0.5.seconds)
                .collect { query ->
                    getSearchSuggestions(query).let { suggestions ->
                        _uiState.update { it.copy(searchSuggestions = suggestions) }
                    }
                }
        }
    }
}
