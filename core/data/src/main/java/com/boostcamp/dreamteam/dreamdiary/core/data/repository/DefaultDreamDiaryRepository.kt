package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.boostcamp.dreamteam.dreamdiary.core.data.database.dao.DreamDiaryDao
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.LabelEntity
import com.boostcamp.dreamteam.dreamdiary.core.model.Label
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

internal class DefaultDreamDiaryRepository @Inject constructor(
    private val dreamDiaryDao: DreamDiaryDao,
) : DreamDiaryRepository {
    override suspend fun addDreamDiary(
        title: String,
        body: String,
        labels: List<String>,
        sleepStartAt: Instant,
        sleepEndAt: Instant,
    ) {
        dreamDiaryDao.insertDreamDiary(
            title = title,
            body = body,
            labels = labels,
            sleepStartAt = sleepStartAt,
            sleepEndAt = sleepEndAt,
        )
    }

    override suspend fun addLabel(label: String) {
        dreamDiaryDao.insertLabel(
            LabelEntity(
                label = label,
            ),
        )
    }

    override fun getLabels(search: String): Flow<List<Label>> {
        val formattedSearch: String? = if (search.isBlank()) {
            null
        } else {
            "%$search%"
        }
        return dreamDiaryDao.getLabels(formattedSearch).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addDreamDiaryLabel(
        diaryId: String,
        labels: List<String>,
    ) {
        dreamDiaryDao.setLabelsToDreamDiary(diaryId, labels)
    }
}
