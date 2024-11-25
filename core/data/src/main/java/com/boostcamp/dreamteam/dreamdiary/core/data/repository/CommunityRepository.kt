package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.boostcamp.dreamteam.dreamdiary.core.data.database.CommunityRemoteDataSource
import com.boostcamp.dreamteam.dreamdiary.core.model.CommunityDreamPost
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
}
