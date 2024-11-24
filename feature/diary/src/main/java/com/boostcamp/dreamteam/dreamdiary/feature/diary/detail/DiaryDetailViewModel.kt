package com.boostcamp.dreamteam.dreamdiary.feature.diary.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DeleteDreamDiariesUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDreamDiaryAsFlowUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.model.DiaryDetailEvent
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.model.DiaryDetailUiState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.model.toUIState
import com.boostcamp.dreamteam.dreamdiary.feature.flowWithStarted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getDreamDiaryAsFlowUseCase: GetDreamDiaryAsFlowUseCase,
    private val deleteDreamDiaryUseCase: DeleteDreamDiariesUseCase,
) : ViewModel() {
    private val id: String? = savedStateHandle.get<String>("id")

    private val _uiState = MutableStateFlow(DiaryDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<DiaryDetailEvent>(64)
    val event = _event.receiveAsFlow()

    init {
        collectDiary()
    }

    fun deleteDiary() {
        viewModelScope.launch {
            if (id != null) {
                runCatching {
                    deleteDreamDiaryUseCase(id)
                }.onSuccess {
                    Timber.d("diary deleted: diaryId=$id")
                    _event.trySend(DiaryDetailEvent.DeleteDiary.Success)
                }.onFailure {
                    Timber.e(it)
                    _event.trySend(DiaryDetailEvent.DeleteDiary.Failure)
                }
            }
        }
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
                    ).catch { e ->
                        if (e is NullPointerException) {
                            Timber.e("Diary not found: diaryId=$id")
                        } else {
                            Timber.e(e)
                        }
                        _event.trySend(DiaryDetailEvent.LoadDiary.Failure)
                    }.collect { diary ->
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
