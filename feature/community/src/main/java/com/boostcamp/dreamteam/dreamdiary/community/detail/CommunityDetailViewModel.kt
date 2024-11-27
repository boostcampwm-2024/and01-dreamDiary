package com.boostcamp.dreamteam.dreamdiary.community.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.community.model.CommentUi
import com.boostcamp.dreamteam.dreamdiary.community.model.toUIState
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.GetCommunityPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCommunityPostUseCase: GetCommunityPostUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CommunityDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val id: String? = savedStateHandle.get<String>("id")

    // TODO: 댓글 가져오는 로직 추가
    val comments: Flow<PagingData<CommentUi>> = flowOf(PagingData.empty())

    init {
        getPostDetail(id)
    }

    fun toggleLikePost(postId: String) {
        // TODO: 포스트 좋아요 로직 추가
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
}
