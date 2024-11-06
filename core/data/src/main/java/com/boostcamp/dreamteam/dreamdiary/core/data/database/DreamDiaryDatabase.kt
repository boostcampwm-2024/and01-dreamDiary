package com.boostcamp.dreamteam.dreamdiary.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.boostcamp.dreamteam.dreamdiary.core.data.database.dao.DreamDiaryDao
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryEntity

@Database(entities = [DreamDiaryEntity::class], version = 1)
@TypeConverters(value = [InstantTypeConverter::class])
internal abstract class DreamDiaryDatabase : RoomDatabase() {
    abstract fun dreamDiaryDao(): DreamDiaryDao
}