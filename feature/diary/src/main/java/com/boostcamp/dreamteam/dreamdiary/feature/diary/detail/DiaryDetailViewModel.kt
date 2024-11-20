package com.boostcamp.dreamteam.dreamdiary.feature.diary.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDreamDiaryAsFlowUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.model.DiaryDetailUiState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.model.toUIState
import com.boostcamp.dreamteam.dreamdiary.feature.flowWithStarted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getDreamDiaryAsFlowUseCase: GetDreamDiaryAsFlowUseCase,
) : ViewModel() {
    private val id: String? = savedStateHandle.get<String>("id")

    private val _uiState = MutableStateFlow(DiaryDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        collectDiary()
    }

    private fun collectDiary() {
        viewModelScope.launch {
            if (id != null) {
                _uiState
                    .flatMapLatest {
                        getDreamDiaryAsFlowUseCase(id)
                    }.flowWithStarted(
                        subscriptionCount = _uiState.subscriptionCount,
                        started = SharingStarted.WhileSubscribed(5000L),
                    ).collect { diary ->
                        _uiState.update {
                            it.copy(
                                diaryUIState = diary.toUIState(),
                                loading = false,
                            )
                        }
                    }
            }
        }
    }
}
