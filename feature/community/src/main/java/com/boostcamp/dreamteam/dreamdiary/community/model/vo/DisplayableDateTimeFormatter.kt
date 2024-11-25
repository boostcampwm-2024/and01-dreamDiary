package com.boostcamp.dreamteam.dreamdiary.community.model.vo

import java.time.chrono.Chronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.Locale

data class DisplayableDateTimeFormatter(
    val dateFormatter: DateTimeFormatter,
    val timeFormatter: DateTimeFormatter,
) {
    companion object {
        fun of(locale: Locale): DisplayableDateTimeFormatter {
            val dateFormatter = DateTimeFormatter.ofPattern(
                DateTimeFormatterBuilder.getLocalizedDateTimePattern(
                    FormatStyle.FULL,
                    null,
                    Chronology.ofLocale(locale),
                    locale,
                ),
            )
            val timeFormatter = DateTimeFormatter.ofPattern(
                DateTimeFormatterBuilder.getLocalizedDateTimePattern(
                    null,
                    FormatStyle.SHORT,
                    Chronology.ofLocale(locale),
                    locale,
                ),
            )

            return DisplayableDateTimeFormatter(dateFormatter, timeFormatter)
        }
    }
}
