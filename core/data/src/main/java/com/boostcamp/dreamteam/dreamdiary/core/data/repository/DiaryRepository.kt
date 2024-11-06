package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.boostcamp.dreamteam.dreamdiary.core.data.database.FirebaseDataSource
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import javax.inject.Inject

class DiaryRepository @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
) {
    suspend fun getAllDiaryFromFireBase(): List<Diary> {
        return firebaseDataSource.getAllDiaries()
    }

    fun setDiaryToFireBase(diary: Diary) {
        firebaseDataSource.setDiary(diary)
    }
}
