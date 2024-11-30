package com.boostcamp.dreamteam.dreamdiary.community.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.community.model.toUIState
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.AddCommentUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.GetCommentUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.GetCommunityPostUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.TogglePostLikeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCommunityPostUseCase: GetCommunityPostUseCase,
    private val getCommentsUseCase: GetCommentUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val togglePostLikeUseCase: TogglePostLikeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CommunityDetailUiState())
    val uiState = _uiState.asStateFlow()
    private val _commentContent = MutableStateFlow("")
    val commentContent = _commentContent.asStateFlow()

    private val postId: String? = savedStateHandle.get<String>("id")

    // TODO: 댓글 가져오는 로직 추가
//    val comments: Flow<PagingData<CommentUi>> = flowOf(PagingData.empty())

    init {
        getPostDetail(postId)
    }

    fun togglePostLike() {
        if (postId != null) {
            viewModelScope.launch {
                togglePostLikeUseCase(postId)
            }
        }
    }

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

    val comments = getCommentsUseCase(postId!!)
        .map { pagingData ->
            pagingData.map { it.toUIState() }
        }
        .cachedIn(viewModelScope)

    fun changeCommentContent(content: String) {
        _commentContent.value = content
    }

    fun addComment() {
        if (postId != null) {
            viewModelScope.launch {
                try {
                    addCommentUseCase(postId, commentContent.value)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }
}
