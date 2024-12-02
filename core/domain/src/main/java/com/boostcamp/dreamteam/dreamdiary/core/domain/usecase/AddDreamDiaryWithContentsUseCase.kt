package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.DiaryContent
import java.time.ZonedDateTime
import javax.inject.Inject

class AddDreamDiaryWithContentsUseCase @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
) {
    suspend operator fun invoke(
        title: String,
        diaryContents: List<DiaryContent>,
        labels: List<String> = listOf(),
        sleepStartAt: ZonedDateTime,
        sleepEndAt: ZonedDateTime,
    ): String =
        dreamDiaryRepository.addDreamDiary(
            title = title,
            diaryContents = diaryContents,
            labels = labels,
            sleepStartAt = sleepStartAt.toInstant(),
            sleepEndAt = sleepEndAt.toInstant(),
        )
}
