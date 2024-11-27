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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
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
            author = author,
            likes = 0,
            createdAt = FieldValue.serverTimestamp(),
        )

        commentRef.set(newComment).await()

        return commentRef.id
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
                        .document(postId).collection("comments")
                )
            }
        ).flow.map {
            it.map { commentResponse ->
                Comment(
                    id = commentResponse.id,
                    content = commentResponse.content,
                    author = commentResponse.author,
                    likes = commentResponse.likes,
                    createdAt = commentResponse.createdAt
                )
            }
        }
    }

}
