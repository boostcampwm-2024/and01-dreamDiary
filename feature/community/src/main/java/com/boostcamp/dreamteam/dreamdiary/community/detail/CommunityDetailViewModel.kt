package com.boostcamp.dreamteam.dreamdiary.community.detail

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.community.model.CommentUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(CommunityDetailUiState())
    val uiState = _uiState.asStateFlow()

    val comments: Flow<PagingData<CommentUi>> = TODO("댓글 가져오는 로직 추가")

    fun onLikeClick(postId: String) {
        // TODO: 포스트 좋아요 로직 추가
    }

    fun onLikeComment(commentId: String) {
        // TODO: 댓글 좋아요 로직 추가
    }
}