package com.boostcamp.dreamteam.dreamdiary.core.data.database

import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseDataSource @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getAllDiaries(): List<Diary> {
        val diaries = mutableListOf<Diary>()
        return suspendCoroutine { continuation ->
            db
                .collection("diary")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Timber.d("${document.id} => ${document.data}")
                        val diary = document.toObject(Diary::class.java)
                        diaries.add(diary)
                    }
                    continuation.resume(diaries)
                }.addOnFailureListener { exception ->
                    Timber.w(exception, "Error getting documents: ")
                    continuation.resume(emptyList())
                }
        }
    }

    fun setDiary(diary: Diary) {
        db
            .collection("diary")
            .document(diary.id.toString())
            .set(diary)
            .addOnSuccessListener {
                Timber.d("${diary.id} => $diary")
            }.addOnFailureListener { exception ->
                Timber.w(exception, "Error setting documents: ")
            }
    }
}
