package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DeleteDreamDiariesUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySort
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortOrder.DESC
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortType.CREATED
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDiariesFilterType.SLEEP_END_AT
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDreamDiariesByFilterUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDreamDiariesInRangeByUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetLabelsUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.DiaryHomeTabCalendarUIState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.toDiaryUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.toLabelUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class DiaryHomeViewModel @Inject constructor(
    getDreamDiariesByFilterUseCase: GetDreamDiariesByFilterUseCase,
    getDreamDiariesInRangeByUseCase: GetDreamDiariesInRangeByUseCase,
    private val deleteDreamDiariesUseCase: DeleteDreamDiariesUseCase,
    private val authRepository: AuthRepository,
    getLabelsUseCase: GetLabelsUseCase,
) : ViewModel() {
    private val _event = Channel<DiaryHomeEvent>(64)
    val event = _event.receiveAsFlow()

    val dreamLabels = getLabelsUseCase("")
        .map { labels -> labels.map { it.toLabelUi() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList(),
        )

    private val _labelOptions = MutableStateFlow(setOf<LabelUi>())
    val labelOptions = _labelOptions.asStateFlow()

    private val _sortOption = MutableStateFlow(DiarySort(CREATED, DESC))
    val sortOption = _sortOption.asStateFlow()

    private val _email = MutableStateFlow<String?>(authRepository.getUserEmail())
    val email = _email.asStateFlow()

    val dreamDiaries = combine(_labelOptions, _sortOption) { labels, sortOption ->
        labels to sortOption
    }.flatMapLatest { (labels, sortOption) ->
        getDreamDiariesByFilterUseCase(
            labels = labels.map { it.name },
            sortOption = sortOption,
        ).map { pagingData ->
            pagingData.map { it.toDiaryUi() }
        }
    }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            authRepository.emailFlow.collect { newEmail ->
                _email.value = newEmail
            }
        }
    }

    fun setSort(diarySort: DiarySort) {
        _sortOption.update { diarySort }
    }

    fun toggleLabel(labelUi: LabelUi) {
        _labelOptions.update {
            val options = it.toMutableSet()
            if (options.contains(labelUi)) {
                options.remove(labelUi)
            } else {
                options.add(labelUi)
            }
            options
        }
    }

    fun deleteDiary(diaryId: String) {
        viewModelScope.launch {
            runCatching {
                deleteDreamDiariesUseCase(diaryId)
            }.onSuccess {
                Timber.d("Diary deleted: diaryId = $diaryId")
                _event.trySend(DiaryHomeEvent.Delete.Success)
            }.onFailure {
                Timber.e(it)
            }
        }
    }

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
