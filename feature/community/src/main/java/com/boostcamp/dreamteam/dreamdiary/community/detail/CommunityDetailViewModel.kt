package com.boostcamp.dreamteam.dreamdiary.community.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.community.model.CommentUi
import com.boostcamp.dreamteam.dreamdiary.community.model.toUIState
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.AddCommentUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.DeleteCommentUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.GetCommentUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.GetCommunityPostUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.SendCommentNotificationUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.TogglePostLikeUseCase
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
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val togglePostLikeUseCase: TogglePostLikeUseCase,
    private val sendCommentNotificationUseCase: SendCommentNotificationUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CommunityDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<CommunityDetailEvent>(64)
    val event = _event.receiveAsFlow()

    private val postId: String? = savedStateHandle.get<String>("id")

    private val _uId = MutableStateFlow<String?>(authRepository.getUserUID())
    val uId = _uId.asStateFlow()

    init {
        getPostDetail(postId)
    }

    fun togglePostLike() {
        if (postId != null) {
            viewModelScope.launch {
                try {
                    togglePostLikeUseCase(postId)
                    getPostDetail(postId)
                    _event.trySend(CommunityDetailEvent.LikePost.Success)
                } catch (e: Exception) {
                    _event.trySend(CommunityDetailEvent.LikePost.Fail)
                    Timber.e(e)
                }
            }
        }
    }

    // TODO: 댓글 삭제 로직 추가
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
            .map { pagingData -> pagingData.map { it.toUIState(uId.value) } }
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
                    // 알림 보내기
                    val uid = _uiState.value.post.author.uid
                    val title = _uiState.value.post.title
                    sendCommentNotificationUseCase(
                        uid = uid,
                        title = title,
                        content = commentContent,
                    )
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

    fun deleteComment(commentId: String) {
        if (postId != null) {
            viewModelScope.launch {
                try {
                    deleteCommentUseCase(postId, commentId)
                    _event.trySend(CommunityDetailEvent.CommentDelete.Success)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }
}
