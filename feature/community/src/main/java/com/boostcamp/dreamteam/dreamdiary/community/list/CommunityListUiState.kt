package com.boostcamp.dreamteam.dreamdiary.community.list

import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.community.model.PostUi
import kotlinx.coroutines.flow.Flow

data class CommunityListUiState(
    val posts: Flow<PagingData<PostUi>>,
    val isUserSignedIn: Boolean,
)
