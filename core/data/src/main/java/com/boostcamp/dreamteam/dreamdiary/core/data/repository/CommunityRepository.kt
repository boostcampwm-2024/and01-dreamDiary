package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import androidx.core.net.toUri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.core.data.dto.CommunityPostResponse
import com.boostcamp.dreamteam.dreamdiary.core.data.firebase.FirebaseCommunityPostPagingSource
import com.boostcamp.dreamteam.dreamdiary.core.data.firebase.firestore.model.FirestoreAddCommunityPostRequest
import com.boostcamp.dreamteam.dreamdiary.core.model.CommunityPostDetail
import com.boostcamp.dreamteam.dreamdiary.core.model.DiaryContent
import com.boostcamp.dreamteam.dreamdiary.core.model.community.CommunityPostList
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.File
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class CommunityRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
) {
    private val communityCollection = firebaseFirestore.collection("community")
    private val userCollection = firebaseFirestore.collection("users")
    private val imageStorage = firebaseStorage.reference.child("community").child("images")

    suspend fun saveCommunityPost(
        title: String,
        diaryContents: List<DiaryContent>,
        uid: String,
        name: String,
        profileImageUrl: String,
    ): String {
        val postReference = communityCollection.document()

        val referenceToData = mutableListOf<Pair<DocumentReference, Map<String, Any?>>>()

        var content = ""
        for (diaryContent in diaryContents) {
            when (diaryContent) {
                is DiaryContent.Image -> {
                    val imageReference = postReference.collection("images").document()
                    val imageNameForStorage = UUID.randomUUID().toString()

                    val imageFile = File(diaryContent.path)
                    val imageUri = if (imageFile.exists()) {
                        imageFile.toUri()
                    } else {
                        diaryContent.path.toUri()
                    }

                    imageStorage
                        .child(uid)
                        .child(imageNameForStorage)
                        .putFile(imageUri)
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
            profileImageUrl = profileImageUrl,
            likeCount = 0,
            commentCount = 0,
        )

        firebaseFirestore.runBatch { batch ->
            batch.set(postReference, request)
            for ((reference, data) in referenceToData) {
                batch.set(reference, data)
            }
        }.await()

        return id
    }

    fun getCommunityPosts(uid: String?): Flow<PagingData<CommunityPostList>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { FirebaseCommunityPostPagingSource(communityCollection) },
        ).flow.map { postResponses ->
            postResponses.map { postResponse ->
                val isLiked = if (uid == null) {
                    false
                } else {
                    checkPostLike(postResponse.id, uid)
                }
                val diaryContents =
                    parseListPostContent(
                        content = postResponse.content,
                        postId = postResponse.id,
                        postUID = postResponse.uid,
                    )
                CommunityPostList(
                    id = postResponse.id,
                    author = postResponse.author,
                    profileImageUrl = postResponse.profileImageUrl,
                    uid = postResponse.uid,
                    title = postResponse.title,
                    diaryContents = diaryContents,
                    commentCount = postResponse.commentCount,
                    likeCount = postResponse.likeCount,
                    isLiked = isLiked,
                    createdAt = Instant.ofEpochSecond(postResponse.createdAt.seconds, postResponse.createdAt.nanoseconds.toLong()),
                )
            }
        }
    }

    private suspend fun checkPostLike(
        postId: String,
        userId: String,
    ): Boolean {
        val userLikeReference = userCollection.document(userId).collection("likes").document(postId)
        return try {
            userLikeReference.get().await().exists()
        } catch (e: Exception) {
            Timber.e(e, "Error checking like for post $postId by user $userId")
            throw e
        }
    }

    suspend fun togglePostLike(
        postId: String,
        userId: String,
    ): Boolean {
        val postLikeCollection = communityCollection.document(postId).collection("likes")
        val userLikeReference = postLikeCollection.document(userId)

        val userLikeCollection = userCollection.document(userId).collection("likes")
        val userReference = userLikeCollection.document(postId)

        return try {
            firebaseFirestore.runTransaction { transaction ->
                val snapshot = transaction.get(userLikeReference)
                // 존재하면 마이너스
                if (snapshot.exists()) {
                    transaction.delete(userLikeReference)
                    transaction.delete(userReference)
                    transaction.update(communityCollection.document(postId), "likeCount", FieldValue.increment(-1))
                    false
                } else {
                    val likeData = mapOf(
                        "liked" to true,
                        "likedAt" to FieldValue.serverTimestamp(),
                    )
                    transaction.set(userLikeReference, likeData)
                    transaction.set(userReference, likeData)
                    transaction.update(communityCollection.document(postId), "likeCount", FieldValue.increment(1))
                    true
                }
            }.await()
        } catch (e: Exception) {
            Timber.e(e, "Error toggling like for post $postId by user $userId")
            throw e
        }
    }

    private suspend fun parseListPostContent(
        content: String,
        postId: String,
        postUID: String,
    ): List<DiaryContent> {
        val textReference = communityCollection.document(postId).collection("text")
        val imageReference = communityCollection.document(postId).collection("images")

        val text = textReference.get()
            .await()
            .associate {
                it.id to it.get("text") as String
            }

        val images = imageReference.get()
            .await()
            .associate {
                it.id to it.get("name") as String
            }

        val diaryContents = mutableListOf<DiaryContent>()

        val parsingDiaryContent = content.split(DELIMITER)
        var index = 0
        var imageAdded = false

        while (index < parsingDiaryContent.size) {
            if (parsingDiaryContent[index] == TEXT) {
                index += 1

                val id = parsingDiaryContent[index]
                val textContent = text[id] ?: continue
                diaryContents.add(
                    DiaryContent.Text(
                        text = textContent,
                    ),
                )
            } else if (parsingDiaryContent[index] == IMAGE) {
                index += 1
                if (imageAdded) continue
                imageAdded = true

                val id = parsingDiaryContent[index]
                val imageName = images[id] ?: continue

                diaryContents.add(
                    DiaryContent.Image(
                        path = imageStorage
                            .child(postUID)
                            .child(imageName)
                            .downloadUrl
                            .await()
                            .toString(),
                    ),
                )
            } else {
                index += 1
                continue
            }
        }
        return diaryContents
    }

    private companion object BodyToken {
        const val TEXT = "text"
        const val IMAGE = "image"
        const val DELIMITER = ":"
    }

    suspend fun getCommunityPostById(
        postId: String,
        userId: String,
    ): CommunityPostDetail {
        return try {
            val postReference = communityCollection.document(postId)
            val userReference = userCollection.document(userId).collection("likes").document(postId)
            val documentSnapshot =
                postReference
                    .get()
                    .await()
            val userLikeSnapshot = userReference.get().await()

            if (!documentSnapshot.exists()) {
                throw Exception("Community post with ID $postId not found")
            }

            val communityPostResponse = documentSnapshot.toObject(CommunityPostResponse::class.java)
                ?: throw Exception("Failed to parse CommunityPostResponse for ID $postId")

            Timber.d("communityPostResponse: ${communityPostResponse.content}")
            val contents = parseBody(communityPostResponse.uid, postId, communityPostResponse.content)
            Timber.d("contents: $contents")

            CommunityPostDetail(
                id = communityPostResponse.id,
                author = communityPostResponse.author,
                profileImageUrl = communityPostResponse.profileImageUrl,
                title = communityPostResponse.title,
                content = communityPostResponse.content,
                likeCount = communityPostResponse.likeCount,
                commentCount = communityPostResponse.commentCount,
                postContents = contents,
                createdAt = communityPostResponse.createdAt.seconds,
                isLiked = userLikeSnapshot.exists(),
            )
        } catch (e: Exception) {
            Timber.e(e, "Error fetching community post with ID $postId")
            throw e
        }
    }

    private suspend fun parseBody(
        uid: String,
        postId: String,
        body: String,
    ): List<DiaryContent> {
        val diaryContents = mutableListOf<DiaryContent>()

        val parsingDiaryContent = body.split(DELIMITER)
        var index = 0

        while (index < parsingDiaryContent.size) {
            if (parsingDiaryContent[index] == TEXT) {
                index += 1
                val id = parsingDiaryContent[index]
                getTextContent(postId, id)?.let { diaryContents.add(it) }
            } else if (parsingDiaryContent[index] == IMAGE) {
                index += 1
                val id = parsingDiaryContent[index]
                getImageContent(uid, postId, id)?.let {
                    diaryContents.add(it)
                    Timber.d("image: $it")
                }
            } else {
                index += 1
                continue
            }
        }
        return diaryContents
    }

    private suspend fun getImageContent(
        uid: String,
        postId: String,
        imageId: String,
    ): DiaryContent? {
        return try {
            val imageSnapshot = communityCollection.document(postId)
                .collection("images").document(imageId).get().await()

            val imagePath = imageSnapshot.getString("name")
            if (imagePath != null) {
                Timber.d("imageId: $uid")
                Timber.d("imagePath: $imagePath")
                val path = imageStorage.child(uid).child(imagePath).downloadUrl.await()
                Timber.d(path.toString())

                DiaryContent.Image(path.toString())
            } else {
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching image with ID $imageId for post $postId")
            null
        }
    }

    private suspend fun getTextContent(
        postId: String,
        textId: String,
    ): DiaryContent? {
        return try {
            val textSnapshot = communityCollection.document(postId)
                .collection("text").document(textId).get().await()

            val text = textSnapshot.getString("text")
            if (text != null) {
                DiaryContent.Text(text)
            } else {
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching text with ID $textId for post $postId")
            null
        }
    }
}
