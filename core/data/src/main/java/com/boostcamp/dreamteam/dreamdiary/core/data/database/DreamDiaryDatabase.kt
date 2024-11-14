package com.boostcamp.dreamteam.dreamdiary.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.boostcamp.dreamteam.dreamdiary.core.data.database.dao.DreamDiaryDao
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryLabelEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.LabelEntity

@Database(entities = [DreamDiaryEntity::class, LabelEntity::class, DreamDiaryLabelEntity::class], version = 1)
@TypeConverters(value = [InstantTypeConverter::class])
internal abstract class DreamDiaryDatabase : RoomDatabase() {
    abstract fun dreamDiaryDao(): DreamDiaryDao
}
