package com.boostcamp.dreamteam.dreamdiary.community.detail

import com.boostcamp.dreamteam.dreamdiary.community.model.PostDetailUi

data class CommunityDetailUiState(
    val post: PostDetailUi = PostDetailUi.EMPTY,
    val isLoading: Boolean = true,
    val commentContent: String = "",
)
