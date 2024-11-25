package com.boostcamp.dreamteam.dreamdiary.core.data.database

import com.boostcamp.dreamteam.dreamdiary.core.model.CommunityDreamPost
import com.google.firebase.firestore.FirebaseFirestore
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
                .addOnSuccessListener {
                    Timber.d("DocumentSnapshot added with ID: ${it.id}")
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    Timber.w(exception, "Error adding document")
                    continuation.resume(false)
                }
        }
    }
}
