package com.boostcamp.dreamteam.dreamdiary.feature.diary.models.vos

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

internal data class DisplayableDateTime(
    val value: ZonedDateTime,
    val formatted: String,
)

internal fun Instant.toDisplayableDateTime(): DisplayableDateTime {
    val zonedDateTime = this.atZone(ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ISO_DATE

    return DisplayableDateTime(
        value = zonedDateTime,
        formatted = zonedDateTime.format(formatter),
    )
}
