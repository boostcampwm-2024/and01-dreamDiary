package com.boostcamp.dreamteam.dreamdiary.core.data.database

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.boostcamp.dreamteam.dreamdiary.core.model.Comment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class CommentDataSource @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    fun getCommentsForPostPagingSource(postId: String): PagingSource<Query, Comment> {
        return object : PagingSource<Query, Comment>() {
            override suspend fun load(params: LoadParams<Query>): LoadResult<Query, Comment> {
                return try {
                    val query = params.key ?: db.collection("community")
                        .document(postId)
                        .collection("comments")
                        .orderBy("createdAt", Query.Direction.DESCENDING)
                        .limit(params.loadSize.toLong())

                    val querySnapshot = query.get().await()
                    val documents = querySnapshot.documents
                    val comments = documents.map { it.toObject(Comment::class.java)!! }

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

            override fun getRefreshKey(state: PagingState<Query, Comment>): Query? {
                return null
            }
        }
    }
}
