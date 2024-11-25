package com.boostcamp.dreamteam.dreamdiary.community.model.vo

import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

data class DisplayableDateTime(
    val value: Instant,
    val formattedDate: String,
    val formattedTime: String,
) {
    companion object {
        val localedFormatter: Map<Locale, DisplayableDateTimeFormatter> = ConcurrentHashMap()
    }
}

internal fun Instant.toDisplayableDateTime(
    zoneId: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.getDefault(),
): DisplayableDateTime {
    val zonedDateTime = this.atZone(zoneId)
    val formatters = DisplayableDateTime.localedFormatter.getOrPut(locale)

    return DisplayableDateTime(
        value = this,
        formattedDate = zonedDateTime.format(formatters.dateFormatter),
        formattedTime = zonedDateTime.format(formatters.timeFormatter),
    )
}

private fun Map<Locale, DisplayableDateTimeFormatter>.getOrPut(locale: Locale): DisplayableDateTimeFormatter =
    this[locale]
        ?: DisplayableDateTimeFormatter.of(locale = locale).also { this.toMutableMap()[locale] = it }
