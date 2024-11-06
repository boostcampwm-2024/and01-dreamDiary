package com.boostcamp.dreamteam.dreamdiary.core.datastore

import android.util.Log
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseDataSource @Inject constructor() {
        private val db = FirebaseFirestore.getInstance()

        suspend fun getAllDiaries(): List<Diary> {
            val diaries = mutableListOf<Diary>()
            return suspendCoroutine { continuation ->
                db.collection("diary")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d("FirestoreData", "${document.id} => ${document.data}")
                            val diary = document.toObject(Diary::class.java)
                            diaries.add(diary)
                            continuation.resume(diaries)
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("FirestoreData", "Error getting documents: ", exception)
                        continuation.resume(emptyList())
                    }
            }
        }

        fun setDiary(diary: Diary) {
            db.collection("diary")
                .document(diary.id.toString())
                .set(diary)
                .addOnSuccessListener {
                    Log.d("FirestoreData", "${diary.id} => $diary")
                }
                .addOnFailureListener { exception ->
                    Log.w("FirestoreData", "Error setting documents: ", exception)
                }
        }
    }
