package com.boostcamp.dreamteam.dreamdiary.feature.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDiariesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DiaryViewModel
    @Inject
    constructor(
        getDiariesUseCase: GetDiariesUseCase,
    ) : ViewModel() {
        private val _diaryHomeUIState: MutableStateFlow<DiaryHomeUIState> = MutableStateFlow(DiaryHomeUIState())
        val diaryHomeUIState = _diaryHomeUIState.asStateFlow()

        init {
            viewModelScope.launch {
                _diaryHomeUIState.value = DiaryHomeUIState(getDiariesUseCase(), false)
            }
        }
    }
