package com.boostcamp.dreamteam.dreamdiary.feature.widget.model

import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.math.abs

@Serializable
data class DiaryUi(
    val id: String,
    val title: String,
) {
    val key: Long = id.toLongId()
}

internal fun Diary.toDiaryUi(): DiaryUi =
    DiaryUi(
        id = id,
        title = title,
    )

private fun String.toLongId(): Long = abs(UUID.fromString(this).leastSignificantBits)
