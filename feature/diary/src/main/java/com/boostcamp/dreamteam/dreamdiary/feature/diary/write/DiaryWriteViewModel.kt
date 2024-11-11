package com.boostcamp.dreamteam.dreamdiary.feature.diary.write

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.AddDreamDiaryUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.AddLabelUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetLabelsUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.diary.models.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.models.toLabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.DiaryWriteEvent
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.DiaryWriteUiState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.LabelAddFailureReason
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.SelectableLabel
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
import java.io.IOException
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

    fun setSearchValue(searchValue: String) {
        _uiState.value = _uiState.value.copy(searchValue = searchValue)
    }

    fun toggleLabel(labelUi: LabelUi) {
        _uiState.value = _uiState.value.copy(
            selectableLabels = _uiState.value.selectableLabels.map {
                if (it.label.name == labelUi.name) {
                    it.copy(isSelected = !it.isSelected)
                } else {
                    it
                }
            },
        )
    }

    fun addDreamDiary() {
        val title = _uiState.value.title
        val content = _uiState.value.content
        val labels = _uiState.value.selectableLabels.map { it.label.name }
        viewModelScope.launch {
            addDreamDiaryUseCase(title, content, labels)
            _event.trySend(DiaryWriteEvent.DiaryAddSuccess)
        }
    }

    fun addLabel() {
        val addLabel = _uiState.value.searchValue
        viewModelScope.launch {
            try {
                addLabelUseCase(addLabel)
                _event.trySend(DiaryWriteEvent.LabelAddSuccess)
            } catch (e: SQLiteConstraintException) {
                Log.d("DiaryWriteViewModel", "addLabel: Duplicate label error - ${e.message}")
                _event.trySend(DiaryWriteEvent.LabelAddFailure(LabelAddFailureReason.DUPLICATE_LABEL))
            } catch (e: IOException) {
                Log.d("DiaryWriteViewModel", "addLabel: Duplicate label error - ${e.message}")
                _event.trySend(DiaryWriteEvent.LabelAddFailure(LabelAddFailureReason.INSUFFICIENT_STORAGE))
            } catch (e: Exception) {
                Log.d("DiaryWriteViewModel", "addLabel: ${e.message} ${e.cause}")
                _event.trySend(DiaryWriteEvent.LabelAddFailure(LabelAddFailureReason.UNKNOWN_ERROR))
            }
        }
    }

    private fun collectLabels() {
        viewModelScope.launch {
            _uiState.flatMapLatest {
                getLabelsUseCase(it.searchValue)
            }.flowWithStarted(
                _uiState.subscriptionCount,
                SharingStarted.WhileSubscribed(5000L),
            ).collect { labels ->
                _uiState.update { prevUiState ->
                    val newSelectableLabels = mutableListOf<SelectableLabel>()
                    for (label in labels) {
                        var newSelectableLabel = SelectableLabel(label.toLabelUi(), false)
                        for (selectableLabel in prevUiState.selectableLabels) {
                            if (label.name == selectableLabel.label.name) {
                                newSelectableLabel = newSelectableLabel.copy(isSelected = selectableLabel.isSelected)
                                break
                            }
                        }
                        newSelectableLabels.add(newSelectableLabel)
                    }
                    prevUiState.copy(selectableLabels = newSelectableLabels)
                }
            }
        }
    }
}
