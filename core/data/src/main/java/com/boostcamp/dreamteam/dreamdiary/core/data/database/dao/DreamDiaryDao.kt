package com.boostcamp.dreamteam.dreamdiary.core.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryEntity

@Dao
interface DreamDiaryDao {
    @Insert
    suspend fun insertDreamDiary(dreamDiaryEntity: DreamDiaryEntity)

    @Query("select * from diary order by updatedAt desc")
    fun getDreamDiaries(): PagingSource<Int, DreamDiaryEntity>
}
