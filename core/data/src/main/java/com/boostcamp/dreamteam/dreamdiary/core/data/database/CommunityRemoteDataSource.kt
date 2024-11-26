package com.boostcamp.dreamteam.dreamdiary.core.data.database

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.boostcamp.dreamteam.dreamdiary.core.model.CommunityDreamPost
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CommunityRemoteDataSource @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    suspend fun addCommunityPost(communityDreamPost: CommunityDreamPost): Boolean {
        return suspendCoroutine { continuation ->
            db.collection("community")
                .add(communityDreamPost)
                .addOnSuccessListener { documentReference ->
                    Timber.d("DocumentSnapshot added with ID: ${documentReference.id}")
                    val updatedPost = communityDreamPost.copy(id = documentReference.id)
                    documentReference.set(updatedPost)
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    Timber.w(exception, "Error adding document")
                    continuation.resume(false)
                }
        }
    }

    fun getCommunityPostsPagingSource(): PagingSource<Query, CommunityDreamPost> {
        return object : PagingSource<Query, CommunityDreamPost>() {
            override suspend fun load(params: LoadParams<Query>): LoadResult<Query, CommunityDreamPost> {
                return try {
                    val query = params.key ?: db.collection("community")
                        .orderBy("createdAt", Query.Direction.DESCENDING)
                        .limit(params.loadSize.toLong())

                    val querySnapshot = query.get().await()
                    val documents = querySnapshot.documents
                    val posts = documents.map { it.toObject(CommunityDreamPost::class.java)!! }

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

            override fun getRefreshKey(state: PagingState<Query, CommunityDreamPost>): Query? {
                return null
            }
        }
    }
}
