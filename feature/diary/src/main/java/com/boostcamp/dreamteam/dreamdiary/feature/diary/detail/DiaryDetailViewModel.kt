package com.boostcamp.dreamteam.dreamdiary.feature.diary.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDreamDiaryUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.model.DiaryDetailUiState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.model.toUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getDreamDiaryUseCase: GetDreamDiaryUseCase,
) : ViewModel() {
    private val id: String? = savedStateHandle.get<String>("id")

    private val _uiState = MutableStateFlow(DiaryDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if (id != null) {
                val dreamDiary = getDreamDiaryUseCase.invoke(id)
                _uiState.update {
                    it.copy(
                        diaryUIState = dreamDiary.toUIState(),
                        loading = false,
                    )
                }
            }
        }
    }
}
