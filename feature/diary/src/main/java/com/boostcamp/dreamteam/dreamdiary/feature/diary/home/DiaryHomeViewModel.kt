package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDreamDiariesUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.DiaryHomeTabCalendarUIState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.toDiaryUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class DiaryHomeViewModel @Inject constructor(
    getDreamDiariesUseCase: GetDreamDiariesUseCase,
) : ViewModel() {
    val dreamDiaries = getDreamDiariesUseCase()
        .map { pagingData ->
            pagingData.map {
                it.toDiaryUi()
            }
        }.cachedIn(viewModelScope)

    private val _tabCalendarUiState =
        MutableStateFlow(DiaryHomeTabCalendarUIState())
    val tabCalendarUiState = _tabCalendarUiState.asStateFlow()

    fun setCalendarYearMonth(yearMonth: YearMonth) = _tabCalendarUiState.update { it.copy(yearMonth = yearMonth) }
}
