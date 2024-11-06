package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.boostcamp.dreamteam.dreamdiary.core.data.database.dao.DreamDiaryDao
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryEntity
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

internal class DefaultDreamDiaryRepository @Inject constructor(
    private val dreamDiaryDao: DreamDiaryDao,
) : DreamDiaryRepository {
    override suspend fun addDreamDiary(
        title: String,
        body: String,
    ) {
        dreamDiaryDao.insertDreamDiary(
            DreamDiaryEntity(
                id = UUID.randomUUID().toString(),
                title = title,
                body = body,
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
            ),
        )
    }
}