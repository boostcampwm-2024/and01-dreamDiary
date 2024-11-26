package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import timber.log.Timber
import java.time.Instant
import javax.inject.Inject

class GetDreamDiariesForTodayUseCase @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
) {
    suspend operator fun invoke(
        start: Instant,
        end: Instant,
    ): List<Diary> {
        Timber.d("GetDreamDiariesForTodayUseCase")
        return dreamDiaryRepository.getDreamDiariesForToday(
            start = start,
            end = end,
        )
    }
}
