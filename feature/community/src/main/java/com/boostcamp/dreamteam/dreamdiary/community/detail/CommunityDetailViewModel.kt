package com.boostcamp.dreamteam.dreamdiary.community.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.community.model.CommentUi
import com.boostcamp.dreamteam.dreamdiary.community.model.toUIState
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.AddCommentUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.GetCommentUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.GetCommunityPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCommunityPostUseCase: GetCommunityPostUseCase,
    private val getCommentsUseCase: GetCommentUseCase,
    private val addCommentUseCase: AddCommentUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CommunityDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<CommunityDetailEvent>(64)
    val event = _event.receiveAsFlow()

    private val postId: String? = savedStateHandle.get<String>("id")

    init {
        getPostDetail(postId)
    }

    fun toggleLikePost(postId: String) {
        // TODO: 포스트 좋아요 로직 추가
    }

    // TODO: 댓글 삭제 로직 추가

    fun toggleLikeComment(commentId: String) {
        // TODO: 댓글 좋아요 로직 추가
    }

    private fun getPostDetail(postId: String?) {
        if (postId != null) {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true)
                try {
                    val communityDetail = getCommunityPostUseCase(postId)
                    _uiState.value = _uiState.value.copy(post = communityDetail.toUIState(), isLoading = false)
                } catch (e: Exception) {
                    Timber.e(e)
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    val comments: Flow<PagingData<CommentUi>> = postId?.let { id ->
        getCommentsUseCase(id)
            .map { pagingData -> pagingData.map { it.toUIState() } }
            .cachedIn(viewModelScope)
    } ?: flowOf(PagingData.empty())

    fun changeCommentContent(content: String) {
        _uiState.update {
            it.copy(commentContent = content)
        }
    }

    fun addComment() {
        val commentAddLoading = _uiState.value.commentAddLoading
        val commentContent = _uiState.value.commentContent
        if (commentAddLoading) return
        if (postId != null && commentContent.isNotBlank()) {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(commentAddLoading = true)
                }
                try {
                    addCommentUseCase(postId, commentContent)
                    changeCommentContent("")
                    _event.trySend(CommunityDetailEvent.CommentAdd.Success)
                } catch (e: Exception) {
                    Timber.e(e)
                } finally {
                    _uiState.update {
                        it.copy(commentAddLoading = false)
                    }
                }
            }
        }
    }
}
