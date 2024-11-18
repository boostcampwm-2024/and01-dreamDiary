package com.boostcamp.dreamteam.dreamdiary.feature.diary.write

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.AddDreamDiaryWithContentsUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.AddLabelUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetLabelsUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryContentUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.toDomain
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
    private val addDreamDiaryUseCase: AddDreamDiaryWithContentsUseCase,
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
        val labels = _uiState.value.selectedLabels.map { it.name }
        val sleepStartAt = _uiState.value.sleepStartAt
        val sleepEndAt = _uiState.value.sleepEndAt
        val diaryContents = _uiState.value.diaryContents
        viewModelScope.launch {
            addDreamDiaryUseCase(
                title = title,
                diaryContents = diaryContents.map { it.toDomain() },
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

    fun setContentText(
        contentIndex: Int,
        text: String,
    ) {
        _uiState.update {
            it.copy(
                diaryContents = it.diaryContents.toMutableList().apply { this[contentIndex] = DiaryContentUi.Text(text) },
            )
        }
    }

    fun addContentImage(
        contentIndex: Int,
        textPosition: Int,
        imagePath: String,
    ) {
        _uiState.update {
            val diaryContents = it.diaryContents.toMutableList()

            val saveContentIndex = minOf(contentIndex, diaryContents.size - 1)
            val currentContent = diaryContents[saveContentIndex]
            val newContents = mutableListOf<DiaryContentUi>()
            if (currentContent is DiaryContentUi.Text) {
                val endIndex = minOf(textPosition, currentContent.text.length)
                val prevText = currentContent.text.substring(0, endIndex)
                if (prevText.isNotEmpty()) {
                    newContents.add(DiaryContentUi.Text(prevText))
                }
                newContents.add(DiaryContentUi.Image(imagePath))
                newContents.add(DiaryContentUi.Text(currentContent.text.substring(endIndex)))
                diaryContents.removeAt(saveContentIndex)
                diaryContents.addAll(saveContentIndex, newContents)
            } else {
                diaryContents.add(saveContentIndex + 1, DiaryContentUi.Image(imagePath))
            }

            it.copy(
                diaryContents = diaryContents,
            )
        }
    }

    fun deleteContentImage(contentIndex: Int) {
        _uiState.update {
            val diaryContents = it.diaryContents.toMutableList()

            val safeContentIndex = minOf(contentIndex, diaryContents.size - 1)
            val currentContent = diaryContents[safeContentIndex]
            if (currentContent is DiaryContentUi.Image) {
                diaryContents.removeAt(safeContentIndex)

                if (0 < safeContentIndex && safeContentIndex < diaryContents.size) {
                    val prev = diaryContents[safeContentIndex - 1]
                    val next = diaryContents[safeContentIndex]
                    if (prev is DiaryContentUi.Text && next is DiaryContentUi.Text) {
                        diaryContents.removeAt(safeContentIndex)
                        diaryContents.removeAt(safeContentIndex - 1)
                        diaryContents.add(
                            safeContentIndex - 1,
                            DiaryContentUi.Text(text = prev.text + "\n" + next.text),
                        )
                    }
                }
            }

            it.copy(
                diaryContents = diaryContents,
            )
        }
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
