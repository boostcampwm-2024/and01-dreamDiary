package com.boostcamp.dreamteam.dreamdiary.feature.diary.models.vos

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

data class DisplayableDateTime(
    val value: ZonedDateTime,
)

internal fun Instant.toDisplayableDateTime(): DisplayableDateTime {
    val zonedDateTime = this.atZone(ZoneId.systemDefault())

    return DisplayableDateTime(
        value = zonedDateTime,
    )
}
