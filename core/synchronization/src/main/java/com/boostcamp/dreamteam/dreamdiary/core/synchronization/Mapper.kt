package com.boostcamp.dreamteam.dreamdiary.core.synchronization

import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryWithLabels
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.SynchronizingDreamDiaryEntity
import com.boostcamp.dreamteam.dreamdiary.core.model.synchronization.SynchronizeDreamDiaryRequest

internal fun SynchronizingDreamDiaryEntity.toRequest(): SynchronizeDreamDiaryRequest {
    return SynchronizeDreamDiaryRequest(
        id = this.id,
        title = this.title,
        content = this.body,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = null,
        sleepStartAt = this.sleepStartAt,
        sleepEndAt = this.sleepEndAt,
        labels = emptyList(),
        diaryContents = emptyList(),
        previousVersion = this.version,
        currentVersion = this.version
    )
}

internal fun DreamDiaryWithLabels.toRequest(): SynchronizeDreamDiaryRequest {
    return SynchronizeDreamDiaryRequest(
        id = dreamDiary.id,
        title = dreamDiary.title,
        content = dreamDiary.body,
        createdAt = dreamDiary.createdAt,
        updatedAt = dreamDiary.updatedAt,
        deletedAt = dreamDiary.deletedAt,
        labels = this.labels.map { it.label },
        sleepStartAt = dreamDiary.sleepStartAt,
        sleepEndAt = dreamDiary.sleepEndAt,
        diaryContents = parseBodyOnlyId(this.dreamDiary.body),
        previousVersion = this.dreamDiary.lastSyncVersion,
        currentVersion = this.dreamDiary.currentVersion,
    )
}

private fun parseBodyOnlyId(body: String): List<SynchronizeDreamDiaryRequest.ContentId> {
    val diaryContents = mutableListOf<SynchronizeDreamDiaryRequest.ContentId>()

    val parsingDiaryContent = body.split(":")
    var index = 0

    while (index < parsingDiaryContent.size) {
        if (parsingDiaryContent[index] == "text") {
            index += 1
            diaryContents.add(
                SynchronizeDreamDiaryRequest.ContentId.Text(
                    id = parsingDiaryContent[index],
                ),
            )
        } else if (parsingDiaryContent[index] == "image") {
            index += 1
            diaryContents.add(
                SynchronizeDreamDiaryRequest.ContentId.Image(
                    id = parsingDiaryContent[index],
                ),
            )
        } else {
            index += 1
            continue
        }
    }
    return diaryContents
}
