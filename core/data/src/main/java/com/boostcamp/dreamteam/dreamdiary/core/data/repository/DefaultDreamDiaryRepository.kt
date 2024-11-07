package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.core.data.database.dao.DreamDiaryDao
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryEntity
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.LabelEntity
import com.boostcamp.dreamteam.dreamdiary.core.model.Label
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

    override fun getDreamDiaries(): Flow<PagingData<Diary>> {
        return Pager(
            config = PagingConfig(pageSize = 100),
            pagingSourceFactory = { dreamDiaryDao.getDreamDiaries() },
        ).flow.map { pagingData ->
            pagingData.map {
                it.toDomain()
            }
        }
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
}
