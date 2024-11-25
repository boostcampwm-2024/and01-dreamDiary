package com.boostcamp.dreamteam.dreamdiary.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.boostcamp.dreamteam.dreamdiary.core.data.database.dao.DreamDiaryDao
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryLabelEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.ImageEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.LabelEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.SynchronizingDreamDiaryEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.TextEntity

@Database(
    entities = [
        DreamDiaryEntity::class,
        LabelEntity::class,
        DreamDiaryLabelEntity::class,
        TextEntity::class,
        ImageEntity::class,
        SynchronizingDreamDiaryEntity::class,
    ],
    version = 1,
)
@TypeConverters(value = [InstantTypeConverter::class])
internal abstract class DreamDiaryDatabase : RoomDatabase() {
    abstract fun dreamDiaryDao(): DreamDiaryDao
}
