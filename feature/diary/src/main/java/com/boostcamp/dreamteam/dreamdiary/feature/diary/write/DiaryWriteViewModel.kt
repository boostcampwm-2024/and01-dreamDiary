package com.boostcamp.dreamteam.dreamdiary.feature.diary.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.AddDreamDiaryUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.DiaryWriteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryWriteViewModel @Inject constructor(
    private val addDreamDiaryUseCase: AddDreamDiaryUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiaryWriteUiState())
    val uiState: StateFlow<DiaryWriteUiState> = _uiState.asStateFlow()

    fun setTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun setContent(content: String) {
        _uiState.value = _uiState.value.copy(content = content)
    }

    fun addDreamDiary() {
        val title = _uiState.value.title
        val content = _uiState.value.content
        viewModelScope.launch {
            addDreamDiaryUseCase(title, content)
        }
    }
}
