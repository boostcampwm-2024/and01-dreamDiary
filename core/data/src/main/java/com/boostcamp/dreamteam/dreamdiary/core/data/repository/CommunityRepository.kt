package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import android.content.Context
import androidx.core.net.toUri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.core.data.convertToFirebaseData
import com.boostcamp.dreamteam.dreamdiary.core.data.firebase.FirebaseCommunityPostPagingSource
import com.boostcamp.dreamteam.dreamdiary.core.data.firebase.firestore.model.FirestoreAddCommunityPostRequest
import com.boostcamp.dreamteam.dreamdiary.core.model.DiaryContent
import com.boostcamp.dreamteam.dreamdiary.core.model.community.CommunityPostList
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import java.io.File
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class CommunityRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    @ApplicationContext private val context: Context,
) {
    private val communityCollection = firebaseFirestore.collection("community")
    private val imageStorage = firebaseStorage.reference.child("community").child("images")

    suspend fun saveCommunityPost(
        title: String,
        diaryContents: List<DiaryContent>,
        uid: String,
        name: String,
    ): String {
        val postReference = communityCollection.document()

        val referenceToData = mutableListOf<Pair<DocumentReference, Map<String, Any?>>>()

        var content = ""
        for (diaryContent in diaryContents) {
            when (diaryContent) {
                is DiaryContent.Image -> {
                    val imageReference = postReference.collection("images").document()
                    val imageNameForStorage = UUID.randomUUID().toString()

                    imageStorage
                        .child(uid)
                        .child(imageNameForStorage)
                        .putFile(File(context.filesDir, diaryContent.path).toUri())
                        .await()

                    referenceToData.add(
                        imageReference to mapOf(
                            "id" to imageReference.id,
                            "name" to imageNameForStorage,
                        ),
                    )

                    content += "image:${imageReference.id}:"
                }

                is DiaryContent.Text -> {
                    val textReference = postReference.collection("text").document()

                    referenceToData.add(
                        textReference to mapOf(
                            "id" to textReference.id,
                            "text" to diaryContent.text,
                        ),
                    )
                    content += "text:${textReference.id}:"
                }
            }
        }

        val id = postReference.id
        val request = FirestoreAddCommunityPostRequest(
            id = id,
            uid = uid,
            author = name,
            title = title,
            content = content,
            likeCount = 0,
        )

        val requestMap = (Json.encodeToJsonElement(request).jsonObject.convertToFirebaseData() as Map<String, Any>).toMutableMap()
        requestMap["createdAt"] = FieldValue.serverTimestamp()

        firebaseFirestore.runBatch { batch ->
            batch.set(postReference, requestMap)
            for ((reference, data) in referenceToData) {
                batch.set(reference, data)
            }
        }.await()

        return id
    }

    fun getCommunityPosts(): Flow<PagingData<CommunityPostList>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { FirebaseCommunityPostPagingSource(communityCollection) },
        ).flow.map {
            it.map {
                CommunityPostList(
                    id = it.id,
                    author = it.author,
                    title = it.title,
                    createdAt = Instant.ofEpochSecond(it.createdAt.seconds, it.createdAt.nanoseconds.toLong()),
                )
            }
        }
    }
}
