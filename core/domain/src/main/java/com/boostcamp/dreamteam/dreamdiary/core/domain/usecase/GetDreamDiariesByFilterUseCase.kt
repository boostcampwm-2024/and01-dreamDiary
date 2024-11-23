package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortOrder.DESC
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortType.CREATED
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDreamDiariesByFilterUseCase @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
) {
    operator fun invoke(
        labels: List<String> = emptyList(),
        sort: DiarySort = DiarySort(CREATED, DESC),
    ): Flow<PagingData<Diary>> =
        if (labels.isEmpty()) {
            dreamDiaryRepository.getDreamDiaries()
        } else {
            dreamDiaryRepository.getDreamDiariesByLabel(labels)
        }
}

data class DiarySort(
    val type: DiarySortType,
    val order: DiarySortOrder,
)

enum class DiarySortType {
    CREATED,
    UPDATED,
    SLEEP,
}

enum class DiarySortOrder {
    ASC,
    DESC,
}
