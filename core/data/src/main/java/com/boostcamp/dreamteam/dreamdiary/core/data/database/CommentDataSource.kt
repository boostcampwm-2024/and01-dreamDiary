package com.boostcamp.dreamteam.dreamdiary.core.data.database

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.boostcamp.dreamteam.dreamdiary.core.data.dto.CommentRequest
import com.boostcamp.dreamteam.dreamdiary.core.data.dto.CommentResponse
import com.boostcamp.dreamteam.dreamdiary.core.data.dto.toDomain
import com.boostcamp.dreamteam.dreamdiary.core.model.Author
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CommentDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun addComment(
        postId: String,
        request: CommentRequest,
    ): Boolean {
        return suspendCoroutine { continuation ->
            // test author / todo author 를 어떻게 넣을지 생각
            val newComment = request.toDomain(Author("author", "https://picsum.photos/200/300"))
            db.collection("community")
                .document(postId)
                .collection("comments")
                .add(request)
                .addOnSuccessListener { documentReference ->
                    Timber.d("DocumentSnapshot added with ID: ${documentReference.id}")
                    val updatedComment = newComment.copy(id = documentReference.id)
                    documentReference.set(updatedComment)
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    Timber.w(exception, "Error adding document")
                    continuation.resumeWithException(exception)
                }
        }
    }

    fun getCommentsForPostPagingSource(postId: String): PagingSource<Query, CommentResponse> {
        return object : PagingSource<Query, CommentResponse>() {
            override suspend fun load(params: LoadParams<Query>): LoadResult<Query, CommentResponse> {
                return try {
                    val query = params.key ?: db.collection("community")
                        .document(postId)
                        .collection("comments")
                        .orderBy("createdAt", Query.Direction.DESCENDING)
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
    }
}
