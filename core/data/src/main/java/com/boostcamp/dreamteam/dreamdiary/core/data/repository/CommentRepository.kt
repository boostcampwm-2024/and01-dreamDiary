package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.core.data.dto.CommentRequest
import com.boostcamp.dreamteam.dreamdiary.core.data.firebase.FirebaseCommentPagingSource
import com.boostcamp.dreamteam.dreamdiary.core.model.Author
import com.boostcamp.dreamteam.dreamdiary.core.model.Comment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseFunctions: FirebaseFunctions,
) {
    suspend fun saveComment(
        postId: String,
        content: String,
        author: Author,
    ): String {
        val commentRef = firebaseFirestore.collection("community")
            .document(postId).collection("comments").document()

        val newComment = CommentRequest(
            id = commentRef.id,
            content = content,
            author = author.userName,
            uid = author.id,
            profileImageUrl = author.profileImageUrl,
            likeCount = 0,
            createdAt = FieldValue.serverTimestamp(),
        )

        // comment 증가
        val postRef = firebaseFirestore.collection("community").document(postId)

        firebaseFirestore.runBatch { batch ->
            batch.set(commentRef, newComment)
            batch.update(postRef, "commentCount", FieldValue.increment(1))
        }.await()

        commentRef.set(newComment).await()

        return commentRef.id
    }

    suspend fun deleteComment(
        postId: String,
        commentId: String,
    ) {
        firebaseFirestore.collection("community")
            .document(postId).collection("comments").document(commentId).delete().await()
    }

    fun getComments(postId: String): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                FirebaseCommentPagingSource(
                    commentReference = firebaseFirestore.collection("community")
                        .document(postId).collection("comments"),
                )
            },
        ).flow.map {
            it.map { commentResponse ->
                Comment(
                    id = commentResponse.id,
                    content = commentResponse.content,
                    uid = commentResponse.uid,
                    author = commentResponse.author,
                    profileImageUrl = commentResponse.profileImageUrl,
                    likeCount = commentResponse.likeCount,
                    createdAt = commentResponse.createdAt.seconds,
                )
            }
        }
    }

    // community post에서 uid 값을 가져온 뒤 uid에 해당하는 user 정보를 가져오기
    suspend fun sendCommentNotification(
        uid: String,
        title: String,
        content: String,
    ) {
        Timber.d("uid $uid")
        Timber.d("uid $title")

        val userSnapshot = firebaseFirestore.collection("users").document(uid).get().await()
        val data = userSnapshot.data ?: throw Exception("User not found")
        val fcmToken = data["fcmToken"] ?: throw Exception("FCM token not found")
        val response = firebaseFunctions.getHttpsCallable("sendNotification")
            .call(mapOf("token" to fcmToken, "title" to title, "body" to "새로운 댓글이 달렸어요: $content"))
            .await()
            .data

        Timber.d("Notification response: $response")
    }
}
