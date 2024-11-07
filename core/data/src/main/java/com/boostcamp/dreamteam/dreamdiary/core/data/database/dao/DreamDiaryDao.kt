package com.boostcamp.dreamteam.dreamdiary.core.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.DreamDiaryEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.LabelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DreamDiaryDao {
    @Insert
    suspend fun insertDreamDiary(dreamDiaryEntity: DreamDiaryEntity)

    @Insert
    suspend fun insertLabel(labelEntity: LabelEntity)

    @Query("select * from label where :search is null or label like :search")
    fun getLabels(search: String?): Flow<List<LabelEntity>>

    @Query("select * from diary order by updatedAt desc")
    fun getDreamDiaries(): PagingSource<Int, DreamDiaryEntity>
}
