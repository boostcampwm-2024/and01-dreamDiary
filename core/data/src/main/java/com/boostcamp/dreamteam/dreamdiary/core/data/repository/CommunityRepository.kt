package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.core.data.database.CommunityRemoteDataSource
import com.boostcamp.dreamteam.dreamdiary.core.data.dto.CommunityPostRequest
import com.boostcamp.dreamteam.dreamdiary.core.data.dto.toDomain
import com.boostcamp.dreamteam.dreamdiary.core.model.CommunityDreamPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommunityRepository @Inject constructor(
    private val communityRemoteDataSource: CommunityRemoteDataSource,
) {
    /**
     * save community post
     * @return true when success, false when fail
     */
    suspend fun saveCommunityPost(request: CommunityPostRequest): Boolean {
        return communityRemoteDataSource.addCommunityPost(request)
    }

    suspend fun getCommunityPost(postId: String): CommunityDreamPost {
        return communityRemoteDataSource.getCommunityPostById(postId).toDomain()
    }

    fun getCommunityPosts(): Flow<PagingData<CommunityDreamPost>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { communityRemoteDataSource.getCommunityPostsPagingSource() },
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }
}
