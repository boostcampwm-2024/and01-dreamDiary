package com.boostcamp.dreamteam.dreamdiary.community.write

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.community.CommunityGraph
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.PostContentUi
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.toDomain
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDreamDiaryUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.AddCommunityPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityWriteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDreamDiaryUseCase: GetDreamDiaryUseCase,
    private val addCommunityPostUseCase: AddCommunityPostUseCase,
) : ViewModel() {
    private val id: String? = savedStateHandle[CommunityGraph.CommunityWriteRoute::diaryId.name]

    private val _uiState = MutableStateFlow(CommunityWriteScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        initState()
    }

    fun writePost() {
        viewModelScope.launch {
            setIsLoading(true)
            addCommunityPostUseCase(
                title = uiState.value.editorState.title,
                diaryContents = uiState.value.editorState.contents.map { it.toDomain() },
            )
            setIsLoading(false)
        }
    }

    fun setContentText(
        contentIndex: Int,
        text: String,
    ) {
        _uiState.update { state ->
            state.copy(
                editorState = state.editorState.copy(
                    contents = state.editorState.contents.mapIndexed { index, content ->
                        if (index == contentIndex) {
                            PostContentUi.Text(text = text)
                        } else {
                            content
                        }
                    },
                ),
            )
        }
    }

    fun addContentImage(
        contentIndex: Int,
        textPosition: Int,
        imagePath: String,
    ) {
        _uiState.update { state ->
            val diaryContents = state.editorState.contents.toMutableList()

            val safeContentIndex = minOf(contentIndex, diaryContents.size - 1)
            val currentContent = diaryContents[safeContentIndex]
            val newContents = mutableListOf<PostContentUi>()
            if (currentContent is PostContentUi.Text) {
                val endIndex = minOf(textPosition, currentContent.text.length)
                val prevText = currentContent.text.substring(0, endIndex)
                if (prevText.isNotEmpty()) {
                    newContents.add(PostContentUi.Text(prevText))
                }
                newContents.add(PostContentUi.Image(imagePath))
                newContents.add(PostContentUi.Text(currentContent.text.substring(endIndex)))
                diaryContents.removeAt(safeContentIndex)
                diaryContents.addAll(safeContentIndex, newContents)
            } else {
                diaryContents.add(safeContentIndex + 1, PostContentUi.Image(imagePath))
            }

            state.copy(
                editorState = state.editorState.copy(contents = diaryContents),
            )
        }
    }

    fun deleteContentImage(contentIndex: Int) {
        _uiState.update { state ->
            val diaryContents = state.editorState.contents.toMutableList()

            val safeContentIndex = minOf(contentIndex, diaryContents.size - 1)
            val currentContent = diaryContents[safeContentIndex]
            if (currentContent is PostContentUi.Image) {
                diaryContents.removeAt(safeContentIndex)

                if (0 < safeContentIndex && safeContentIndex < diaryContents.size) {
                    val prev = diaryContents[safeContentIndex - 1]
                    val next = diaryContents[safeContentIndex]
                    if (prev is PostContentUi.Text && next is PostContentUi.Text) {
                        diaryContents.removeAt(safeContentIndex)
                        diaryContents.removeAt(safeContentIndex - 1)
                        diaryContents.add(
                            safeContentIndex - 1,
                            PostContentUi.Text(text = prev.text + "\n" + next.text),
                        )
                    }
                }
            }

            state.copy(
                editorState = state.editorState.copy(contents = diaryContents),
            )
        }
    }

    fun setTitle(newTitle: String) {
        _uiState.update { state ->
            state.copy(editorState = state.editorState.copy(title = newTitle))
        }
    }

    private fun initState() {
        if (id != null) {
            viewModelScope.launch {
                getDreamDiaryUseCase(id).let { diary ->
                    _uiState.update { state ->
                        state.copy(
                            editorState = diary.toEditorState(),
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }

    private fun setIsLoading(isLoading: Boolean) {
        _uiState.update { state ->
            state.copy(isLoading = isLoading)
        }
    }
}
