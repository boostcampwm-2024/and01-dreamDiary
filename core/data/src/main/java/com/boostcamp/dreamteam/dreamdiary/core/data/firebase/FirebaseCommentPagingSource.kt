package com.boostcamp.dreamteam.dreamdiary.core.data.firebase

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.boostcamp.dreamteam.dreamdiary.core.data.dto.CommentResponse
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseCommentPagingSource(
    private val commentReference: CollectionReference,
) : PagingSource<Query, CommentResponse>() {
    override suspend fun load(params: LoadParams<Query>): LoadResult<Query, CommentResponse> {
        return try {
            val query = params.key ?: commentReference
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(params.loadSize.toLong())

            val querySnapshot = query.get().await()
            val documents = querySnapshot.documents
            val comments = documents.map { it.toObject(CommentResponse::class.java)!! }

            val nextQuery = if (documents.isNotEmpty()) {
                query.startAfter(documents.last())
            } else {
                null
            }

            LoadResult.Page(
                data = comments,
                prevKey = null,
                nextKey = nextQuery,
            )
        } catch (e: Exception) {
            Timber.e(e, "Error loading comments")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Query, CommentResponse>): Query? {
        return null
    }
}
