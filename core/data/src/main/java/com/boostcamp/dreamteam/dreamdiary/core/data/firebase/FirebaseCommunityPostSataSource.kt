package com.boostcamp.dreamteam.dreamdiary.core.data.firebase

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.boostcamp.dreamteam.dreamdiary.core.data.firebase.firestore.model.FirestoreGetCommunityPostResponse
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseCommunityPostSataSource(
    private val communityCollection: CollectionReference,
) : PagingSource<Query, FirestoreGetCommunityPostResponse>() {
    override suspend fun load(params: LoadParams<Query>): LoadResult<Query, FirestoreGetCommunityPostResponse> {
        return try {
            val query = params.key ?: communityCollection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(params.loadSize.toLong())

            val querySnapshot = query.get().await()
            val documents = querySnapshot.documents
            val posts = mutableListOf<FirestoreGetCommunityPostResponse>()
            for (document in documents) {
                try {
                    val communityPost = document.toObject(FirestoreGetCommunityPostResponse::class.java)
                    if (communityPost != null) {
                        posts.add(communityPost)
                    }
                } catch (e: Exception) {
                    Timber.e(e, "community post 변환 에러")
                }
            }

            val nextQuery = if (documents.isNotEmpty()) {
                query.startAfter(documents.last())
            } else {
                null
            }

            LoadResult.Page(
                data = posts,
                prevKey = null,
                nextKey = nextQuery,
            )
        } catch (e: Exception) {
            Timber.e(e, "community post 로딩 에러")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Query, FirestoreGetCommunityPostResponse>): Query? {
        return null
    }
}
