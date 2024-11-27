package com.boostcamp.dreamteam.dreamdiary.core.data.database

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.boostcamp.dreamteam.dreamdiary.core.data.dto.CommunityPostRequest
import com.boostcamp.dreamteam.dreamdiary.core.data.dto.CommunityPostResponse
import com.boostcamp.dreamteam.dreamdiary.core.data.dto.toDomain
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CommunityRemoteDataSource @Inject constructor(
    private val db: FirebaseFirestore,
) {
    suspend fun addCommunityPost(request: CommunityPostRequest): Boolean {
        return suspendCoroutine { continuation ->
            val newPost = request.toDomain()
            db.collection("community")
                .add(newPost)
                .addOnSuccessListener { documentReference ->
                    Timber.d("DocumentSnapshot added with ID: ${documentReference.id}")
                    val updatedPost = newPost.copy(id = documentReference.id)
                    documentReference.set(updatedPost)
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    Timber.w(exception, "Error adding document")
                    continuation.resumeWithException(exception)
                }
        }
    }

    fun getCommunityPostsPagingSource(): PagingSource<Query, CommunityPostResponse> {
        return object : PagingSource<Query, CommunityPostResponse>() {
            override suspend fun load(params: LoadParams<Query>): LoadResult<Query, CommunityPostResponse> {
                return try {
                    val query = params.key ?: db.collection("community")
                        .orderBy("createdAt", Query.Direction.DESCENDING)
                        .limit(params.loadSize.toLong())

                    val querySnapshot = query.get().await()
                    val documents = querySnapshot.documents
                    val posts = documents.map { it.toObject(CommunityPostResponse::class.java)!! }

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
                    Timber.e(e, "Error loading community posts")
                    LoadResult.Error(e)
                }
            }

            override fun getRefreshKey(state: PagingState<Query, CommunityPostResponse>): Query? {
                return null
            }
        }
    }
}
