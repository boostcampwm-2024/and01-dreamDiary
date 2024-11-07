package com.boostcamp.dreamteam.dreamdiary.feature.diary.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.AddDreamDiaryUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.AddLabelUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.diary.models.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.DiaryWriteEvent
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.DiaryWriteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryWriteViewModel @Inject constructor(
    private val addDreamDiaryUseCase: AddDreamDiaryUseCase,
    private val addLabelUseCase: AddLabelUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiaryWriteUiState())
    val uiState: StateFlow<DiaryWriteUiState> = _uiState.asStateFlow()

    private val _event = Channel<DiaryWriteEvent>(64)
    val event = _event.receiveAsFlow()

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
        viewModelScope.launch {
            addDreamDiaryUseCase(title, content)
            _event.trySend(DiaryWriteEvent.AddSuccess)
        }
    }

    fun addLabel() {
        val addLabel = _uiState.value.searchValue
        viewModelScope.launch {
            addLabelUseCase(addLabel)
            _event.trySend(DiaryWriteEvent.AddSuccess)
        }
    }
}
