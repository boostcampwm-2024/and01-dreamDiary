package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import javax.inject.Inject

class GetDreamDiariesInRangeByUseCase @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
) {
    operator fun invoke(
        filterType: GetDiariesFilterType,
        start: Instant,
        end: Instant,
    ): Flow<List<Diary>> =
        when (filterType) {
            GetDiariesFilterType.SLEEP_END_AT -> {
                dreamDiaryRepository.getDreamDiariesBySleepEndInRange(start = start, end = end)
            }
        }
}

enum class GetDiariesFilterType {
    SLEEP_END_AT,
}
