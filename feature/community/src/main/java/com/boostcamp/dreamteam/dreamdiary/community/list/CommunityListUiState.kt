package com.boostcamp.dreamteam.dreamdiary.community.list

import com.boostcamp.dreamteam.dreamdiary.community.model.DiaryUi

data class CommunityListUiState(
    val diaries: List<DiaryUi> = emptyList(),
)
