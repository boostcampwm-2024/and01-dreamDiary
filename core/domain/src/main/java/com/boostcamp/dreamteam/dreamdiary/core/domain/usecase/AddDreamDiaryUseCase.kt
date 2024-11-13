package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import java.time.ZonedDateTime
import javax.inject.Inject

class AddDreamDiaryUseCase @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
) {
    suspend operator fun invoke(
        title: String,
        body: String,
        labels: List<String> = listOf(),
        sleepStartAt: ZonedDateTime,
        sleepEndAt: ZonedDateTime,
    ) {
        dreamDiaryRepository.addDreamDiary(title, body, labels, sleepStartAt.toInstant(), sleepEndAt.toInstant())
    }
}
