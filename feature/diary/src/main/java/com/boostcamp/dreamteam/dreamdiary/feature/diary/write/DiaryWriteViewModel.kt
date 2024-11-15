package com.boostcamp.dreamteam.dreamdiary.feature.diary.write

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.AddDreamDiaryUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.AddLabelUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetLabelsUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.toLabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.DiaryWriteEvent
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.DiaryWriteUiState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.LabelAddFailureReason
import com.boostcamp.dreamteam.dreamdiary.feature.flowWithStarted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class DiaryWriteViewModel @Inject constructor(
    private val addDreamDiaryUseCase: AddDreamDiaryUseCase,
    private val addLabelUseCase: AddLabelUseCase,
    private val getLabelsUseCase: GetLabelsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiaryWriteUiState())
    val uiState: StateFlow<DiaryWriteUiState> = _uiState.asStateFlow()

    private val _event = Channel<DiaryWriteEvent>(64)
    val event = _event.receiveAsFlow()

    init {
        collectLabels()
    }

    fun setTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun setContent(content: String) {
        _uiState.value = _uiState.value.copy(content = content)
    }

    fun setLabelFilter(labelFilter: String) {
        _uiState.value = _uiState.value.copy(labelFilter = labelFilter)
    }

    fun toggleLabel(labelUi: LabelUi) {
        _uiState.update {
            it.copy(
                selectedLabels = it.selectedLabels.toMutableSet().apply {
                    if (contains(labelUi)) {
                        remove(labelUi)
                    } else {
                        add(labelUi)
                    }
                },
            )
        }
    }

    fun addDreamDiary() {
        val title = _uiState.value.title
        val content = _uiState.value.content
        val labels = _uiState.value.selectedLabels.map { it.name }
        val sleepStartAt = _uiState.value.sleepStartAt
        val sleepEndAt = _uiState.value.sleepEndAt
        viewModelScope.launch {
            addDreamDiaryUseCase(
                title = title,
                body = content,
                labels = labels,
                sleepStartAt = sleepStartAt,
                sleepEndAt = sleepEndAt,
            )
            _event.trySend(DiaryWriteEvent.DiaryAddSuccess)
        }
    }

    fun addLabel() {
        val addLabel = _uiState.value.labelFilter
        viewModelScope.launch {
            try {
                addLabelUseCase(addLabel)
                _event.trySend(DiaryWriteEvent.LabelAddSuccess)
            } catch (e: SQLiteConstraintException) {
                Timber.d("addLabel: Duplicate label error - ${e.message}")
                _event.trySend(DiaryWriteEvent.LabelAddFailure(LabelAddFailureReason.DUPLICATE_LABEL))
            } catch (e: IOException) {
                Timber.d("addLabel: Duplicate label error - ${e.message}")
                _event.trySend(DiaryWriteEvent.LabelAddFailure(LabelAddFailureReason.INSUFFICIENT_STORAGE))
            } catch (e: Exception) {
                Timber.d("addLabel: ${e.message} ${e.cause}")
                _event.trySend(DiaryWriteEvent.LabelAddFailure(LabelAddFailureReason.UNKNOWN_ERROR))
            }
        }
    }

    fun setSleepStartAt(sleepStartAt: ZonedDateTime) {
        _uiState.value = _uiState.value.copy(
            sleepStartAt = sleepStartAt,
        )
    }

    fun setSleepEndAt(sleepEndAt: ZonedDateTime) {
        _uiState.value = _uiState.value.copy(
            sleepEndAt = sleepEndAt,
        )
    }

    private fun collectLabels() {
        viewModelScope.launch {
            _uiState.flatMapLatest {
                getLabelsUseCase(it.labelFilter)
            }.flowWithStarted(
                _uiState.subscriptionCount,
                SharingStarted.WhileSubscribed(5000L),
            ).collect { labels ->
                _uiState.update { uiState ->
                    uiState.copy(
                        filteredLabels = labels.map { it.toLabelUi() },
                    )
                }
            }
        }
    }
}
