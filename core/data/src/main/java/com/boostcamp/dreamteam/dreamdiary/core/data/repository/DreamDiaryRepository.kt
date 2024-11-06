package com.boostcamp.dreamteam.dreamdiary.core.data.repository

interface DreamDiaryRepository {
    suspend fun addDreamDiary(
        title: String,
        body: String,
    )
}
