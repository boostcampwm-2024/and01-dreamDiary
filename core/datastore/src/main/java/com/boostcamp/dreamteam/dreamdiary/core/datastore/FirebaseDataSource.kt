package com.boostcamp.dreamteam.dreamdiary.core.datastore

import android.util.Log
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class FirebaseDataSource {

    private val db = FirebaseFirestore.getInstance()

    fun getAllDiary(): List<Diary> {
        val diaries = mutableListOf<Diary>()
        db.collection("diary")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("FirestoreData", "${document.id} => ${document.data}")
                    val diary = document.toObject(Diary::class.java)
                    diaries.add(diary)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreData", "Error getting documents: ", exception)
            }
        return diaries
    }

    fun setDiary(diary: Diary) {
        db.collection("diary")
            .document(diary.id.toString())
            .set(diary)
            .addOnSuccessListener {
                Log.d("FirestoreData", "${diary.id} => ${diary}")
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreData", "Error setting documents: ", exception)
            }
    }
}
