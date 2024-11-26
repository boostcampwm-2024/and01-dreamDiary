package com.boostcamp.dreamteam.dreamdiary.community.list

import com.boostcamp.dreamteam.dreamdiary.community.model.PostUi

data class CommunityListUiState(
    val diaries: List<PostUi> = emptyList(),
)
