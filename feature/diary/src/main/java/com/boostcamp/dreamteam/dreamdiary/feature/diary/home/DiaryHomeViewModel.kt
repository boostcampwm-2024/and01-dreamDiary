package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDiariesUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist.DiaryHomeTabListUIState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.models.toDiaryUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryHomeViewModel @Inject constructor(
    getDiariesUseCase: GetDiariesUseCase,
) : ViewModel() {
    private val _tabListUIState: MutableStateFlow<DiaryHomeTabListUIState> =
        MutableStateFlow(DiaryHomeTabListUIState())
    val tabListUIState = _tabListUIState.asStateFlow()

    init {
        viewModelScope.launch {
            _tabListUIState.value = DiaryHomeTabListUIState(
                diaries = getDiariesUseCase().map { it.toDiaryUi() },
                loading = false,
            )
        }
    }
}
