package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortOrder.ASC
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortOrder.DESC
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortType.CREATED
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortType.SLEEP
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortType.UPDATED
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.model.DiarySort as DataDiarySort
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.model.DiarySortOrder as DataDiarySortOrder
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.model.DiarySortType as DataDiarySortType

class GetDreamDiariesByFilterUseCase @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
) {
    operator fun invoke(
        labels: List<String> = emptyList(),
        sort: DiarySort = DiarySort(CREATED, DESC),
    ): Flow<PagingData<Diary>> {
        Timber.d("GetDreamDiariesByFilterUseCase: labels=$labels, sort=$sort")
        return if (labels.isEmpty()) {
            dreamDiaryRepository.getDreamDiariesOrderBy(sort = sort.toDataDiarySort())
        } else {
            dreamDiaryRepository.getDreamDiariesByLabel(labels)
        }
    }
}

data class DiarySort(
    val type: DiarySortType,
    val order: DiarySortOrder,
)

fun DiarySort.toDataDiarySort(): DataDiarySort =
    DataDiarySort(
        type = type.toDataDiarySortType(),
        order = order.toDataDiarySortOrder(),
    )

private fun DiarySortType.toDataDiarySortType(): DataDiarySortType =
    when (this) {
        CREATED -> DataDiarySortType.CREATED
        UPDATED -> DataDiarySortType.UPDATED
        SLEEP -> DataDiarySortType.SLEEP
    }

private fun DiarySortOrder.toDataDiarySortOrder(): DataDiarySortOrder =
    when (this) {
        DESC -> DataDiarySortOrder.DESC
        ASC -> DataDiarySortOrder.ASC
    }

enum class DiarySortType {
    CREATED,
    UPDATED,
    SLEEP,
}

enum class DiarySortOrder {
    ASC,
    DESC,
}
