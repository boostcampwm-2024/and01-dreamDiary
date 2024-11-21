package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.core.data.database.dao.DreamDiaryDao
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.ImageEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.LabelEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.TextEntity
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.boostcamp.dreamteam.dreamdiary.core.model.DiaryContent
import com.boostcamp.dreamteam.dreamdiary.core.model.Label
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.util.UUID
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

    override suspend fun addDreamDiary(
        title: String,
        diaryContents: List<DiaryContent>,
        labels: List<String>,
        sleepStartAt: Instant,
        sleepEndAt: Instant,
    ) {
        val body = makeBody(diaryContents)
        dreamDiaryDao.insertDreamDiary(
            title = title,
            body = body,
            labels = labels,
            sleepStartAt = sleepStartAt,
            sleepEndAt = sleepEndAt,
        )
    }

    override suspend fun updateDreamDiary(
        diaryId: String,
        title: String,
        diaryContents: List<DiaryContent>,
        labels: List<String>,
        sleepStartAt: Instant,
        sleepEndAt: Instant,
    ) {
        dreamDiaryDao.updateDreamDiary(
            diaryId = diaryId,
            title = title,
            sleepStartAt = sleepStartAt,
            sleepEndAt = sleepEndAt,
            body = makeBody(diaryContents = diaryContents),
            labels = labels,
        )
    }

    override fun getDreamDiaries(): Flow<PagingData<Diary>> =
        Pager(
            config = PagingConfig(pageSize = 100),
            pagingSourceFactory = { dreamDiaryDao.getDreamDiaries() },
        ).flow.map { pagingData ->
            pagingData.map {
                it.toDomain(parseBody(it.dreamDiary.body))
            }
        }

    override fun getDreamDiariesByLabel(labels: List<String>): Flow<PagingData<Diary>> =
        Pager(
            config = PagingConfig(pageSize = 100),
            pagingSourceFactory = { dreamDiaryDao.getDreamDiariesByLabels(labels) },
        ).flow.map { pagingData ->
            pagingData.map {
                it.toDomain(parseBody(it.dreamDiary.body))
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

    override suspend fun addDreamDiaryLabel(
        diaryId: String,
        labels: List<String>,
    ) {
        dreamDiaryDao.setLabelsToDreamDiary(diaryId, labels)
    }

    override fun getDreamDiariesBySleepEndInRange(
        start: Instant,
        end: Instant,
    ): Flow<List<Diary>> =
        dreamDiaryDao
            .getDreamDiariesBySleepEndInRange(start, end)
            .map { list -> list.map { it.toDomain(parseBody(it.body)) } }

    override suspend fun getDreamDiary(id: String): Diary {
        val dreamDiaryEntity = dreamDiaryDao.getDreamDiary(id)
        return dreamDiaryEntity.toDomain(parseBody(dreamDiaryEntity.dreamDiary.body))
    }

    private suspend fun makeBody(diaryContents: List<DiaryContent>): String {
        var body = ""
        diaryContents.forEach { diaryContent ->
            val newId = UUID.randomUUID().toString()

            val token = when (diaryContent) {
                is DiaryContent.Text -> {
                    dreamDiaryDao.insertText(
                        TextEntity(
                            id = newId,
                            text = diaryContent.text,
                        ),
                    )
                    "text:$newId:"
                }

                is DiaryContent.Image -> {
                    dreamDiaryDao.insertImage(
                        ImageEntity(
                            id = newId,
                            path = diaryContent.path,
                        ),
                    )
                    "image:$newId:"
                }
            }

            body += token
        }
        return body
    }

    private suspend fun parseBody(body: String): List<DiaryContent> {
        val diaryContents = mutableListOf<DiaryContent>()

        val parsingDiaryContent = body.split(":")
        var index = 0

        while (index < parsingDiaryContent.size) {
            if (parsingDiaryContent[index] == "text") {
                index += 1
                val id = parsingDiaryContent[index]
                val textEntity = dreamDiaryDao.getText(id) ?: continue
                diaryContents.add(
                    DiaryContent.Text(
                        text = textEntity.text,
                    ),
                )
            } else if (parsingDiaryContent[index] == "image") {
                index += 1
                val id = parsingDiaryContent[index]
                val imageEntity = dreamDiaryDao.getImage(id) ?: continue
                diaryContents.add(
                    DiaryContent.Image(
                        path = imageEntity.path,
                    ),
                )
            } else {
                index += 1
                continue
            }
        }
        return diaryContents
    }
}
