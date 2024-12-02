package com.boostcamp.dreamteam.dreamdiary.community.model.vo

import java.time.ZonedDateTime
import java.time.chrono.Chronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.Locale

data class DisplayableDateTimeFormatter(
    private val locale: Locale,
) {
    private val defaultPattern: String by lazy {
        DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            FormatStyle.FULL,
            FormatStyle.SHORT,
            Chronology.ofLocale(locale),
            locale,
        )
    }

    /**
     * 년도 부분 제거
     *
     *
     * [출처](https://stackoverflow.com/a/46428741)
     */
    private val sameYearFormatter: DateTimeFormatter by lazy {
        DateTimeFormatter.ofPattern(
            defaultPattern.replace("((' de ')|[^dM]*)y[^dM]*".toRegex(), ""),
        )
    }

    private val differentYearFormatter: DateTimeFormatter by lazy {
        DateTimeFormatter.ofPattern(defaultPattern)
    }

    fun format(
        target: ZonedDateTime,
        now: ZonedDateTime = ZonedDateTime.now(),
    ): String {
        val diff = now.toEpochSecond() - target.toEpochSecond()

        return when {
            diff < 60 -> "방금 전"
            diff < 3600 -> "${diff / 60}분 전"
            diff < 86400 -> "${diff / 3600}시간 전"
            diff < 604800 -> "${diff / 86400}일 전"
            target.year == now.year -> sameYearFormatter.format(target)
            else -> differentYearFormatter.format(target)
        }
    }
}
