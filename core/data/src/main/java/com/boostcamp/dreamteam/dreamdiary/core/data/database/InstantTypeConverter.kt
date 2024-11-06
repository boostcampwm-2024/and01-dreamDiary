package com.boostcamp.dreamteam.dreamdiary.core.data.database

import androidx.room.TypeConverter
import java.time.Instant

internal class InstantTypeConverter {
    @TypeConverter
    fun longToInstant(value: Long?): Instant? = value?.let(Instant::ofEpochMilli)

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? = instant?.toEpochMilli()
}
