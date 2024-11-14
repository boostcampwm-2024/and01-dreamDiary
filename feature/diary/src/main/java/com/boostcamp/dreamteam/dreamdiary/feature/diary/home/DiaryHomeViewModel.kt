package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDiariesFilterType.SLEEP_END_AT
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDreamDiariesInRangeByUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDreamDiariesUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.DiaryHomeTabCalendarUIState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.toDiaryUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class DiaryHomeViewModel @Inject constructor(
    getDreamDiariesUseCase: GetDreamDiariesUseCase,
    getDreamDiariesInRangeByUseCase: GetDreamDiariesInRangeByUseCase,
) : ViewModel() {
    val dreamDiaries = getDreamDiariesUseCase()
        .map { pagingData ->
            pagingData.map {
                it.toDiaryUi()
            }
        }.cachedIn(viewModelScope)

    private val yearMonth = MutableStateFlow(YearMonth.now())

    val tabCalendarUiState: StateFlow<DiaryHomeTabCalendarUIState> = yearMonth
        .flatMapLatest { yearMonth ->
            getDreamDiariesInRangeByUseCase(
                filterType = SLEEP_END_AT,
                start = yearMonth
                    .atDay(1)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant(),
                end = yearMonth
                    .plusMonths(1)
                    .atDay(1)
                    .atStartOfDay(ZoneId.systemDefault())
                    .minusNanos(1_000)
                    .toInstant(),
            ).map { diaries ->
                Timber.d("$diaries")
                DiaryHomeTabCalendarUIState(
                    yearMonth = yearMonth,
                    diariesOfMonth = diaries.map { it.toDiaryUi() },
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = DiaryHomeTabCalendarUIState(),
        )

    fun setCalendarYearMonth(newYearMonth: YearMonth) = yearMonth.update { newYearMonth }
}
