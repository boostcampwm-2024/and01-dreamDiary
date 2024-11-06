package com.boostcamp.dreamteam.dreamdiary.feature.diary.write

import androidx.lifecycle.ViewModel
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.DiaryWriteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DiaryWriteViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(DiaryWriteUiState())
    val uiState: StateFlow<DiaryWriteUiState> = _uiState.asStateFlow()

    fun setTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun setContent(content: String) {
        _uiState.value = _uiState.value.copy(content = content)
    }

    fun setSearchValue(searchValue: String) {
        _uiState.value = _uiState.value.copy(searchValue = searchValue)
    }

    fun setLabels(labels: List<String>) {
        _uiState.value = _uiState.value.copy(labels = labels)
    }
}
