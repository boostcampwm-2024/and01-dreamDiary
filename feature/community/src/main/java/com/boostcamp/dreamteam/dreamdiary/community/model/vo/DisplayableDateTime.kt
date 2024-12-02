package com.boostcamp.dreamteam.dreamdiary.community.model.vo

import java.time.Instant
import java.time.ZoneId
import java.util.Locale

data class DisplayableDateTime(
    val value: Instant,
    val formatted: String,
) {
    companion object {
        val EMPTY: DisplayableDateTime = DisplayableDateTime(
            value = Instant.EPOCH,
            formatted = "",
        )
        val localedFormatter: Map<Locale, DisplayableDateTimeFormatter> = emptyMap()
    }
}

internal fun Instant.toDisplayableDateTime(
    zoneId: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.getDefault(),
): DisplayableDateTime {
    val zonedDateTime = this.atZone(zoneId)
    val formatter = DisplayableDateTime.localedFormatter.getOrPut(locale)

    return DisplayableDateTime(
        value = this,
        formatted = formatter.format(zonedDateTime),
    )
}

private fun Map<Locale, DisplayableDateTimeFormatter>.getOrPut(locale: Locale): DisplayableDateTimeFormatter =
    this[locale]
        ?: DisplayableDateTimeFormatter(locale = locale).also { this.toMutableMap()[locale] = it }
