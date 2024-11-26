package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.core.data.database.CommunityRemoteDataSource
import com.boostcamp.dreamteam.dreamdiary.core.model.CommunityDreamPost
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommunityRepository @Inject constructor(
    private val communityRemoteDataSource: CommunityRemoteDataSource,
) {
    /**
     * save community post
     * @return true when success, false when fail
     */
    suspend fun saveCommunityPost(communityDreamPost: CommunityDreamPost): Boolean {
        return communityRemoteDataSource.addCommunityPost(communityDreamPost)
    }

    fun getCommunityPosts(): Flow<PagingData<CommunityDreamPost>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { communityRemoteDataSource.getCommunityPostsPagingSource() },
        ).flow
    }
}
