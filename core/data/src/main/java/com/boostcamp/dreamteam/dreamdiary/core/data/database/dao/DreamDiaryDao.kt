package com.boostcamp.dreamteam.dreamdiary.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.LabelEntity

@Dao
interface DreamDiaryDao {
    @Insert
    suspend fun insertDreamDiary(dreamDiaryEntity: DreamDiaryEntity)

    @Insert
    suspend fun insertLabel(labelEntity: LabelEntity)
}
