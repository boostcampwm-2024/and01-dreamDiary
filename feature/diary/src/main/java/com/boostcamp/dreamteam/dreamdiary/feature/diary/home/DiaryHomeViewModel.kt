package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDreamDiariesUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.diary.models.toDiaryUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class DiaryHomeViewModel @Inject constructor(
    getDreamDiariesUseCase: GetDreamDiariesUseCase,
) : ViewModel() {
    private val _diaryHomeUIState: MutableStateFlow<DiaryHomeUIState> =
        MutableStateFlow(DiaryHomeUIState())
    val diaryHomeUIState = _diaryHomeUIState.asStateFlow()

    val dreamDiaries = getDreamDiariesUseCase()
        .map { pagingData ->
            pagingData.map {
                it.toDiaryUi()
            }
        }.cachedIn(viewModelScope)
}
