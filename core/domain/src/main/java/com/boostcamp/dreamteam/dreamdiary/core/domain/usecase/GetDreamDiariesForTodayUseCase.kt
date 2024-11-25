package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import timber.log.Timber
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class GetDreamDiariesForTodayUseCase @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
) {
    suspend operator fun invoke(): List<Diary> {
        Timber.d("GetDreamDiariesForTodayUseCase")
        return dreamDiaryRepository.getDreamDiariesForToday(
            start = Instant.now().truncatedTo(ChronoUnit.DAYS),
            end = Instant.now().plusSeconds(24 * 60 * 60 - 1),
        )
    }
}
