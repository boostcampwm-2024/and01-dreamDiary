package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.DiaryContent
import java.time.ZonedDateTime
import javax.inject.Inject

class UpdateDreamDiaryWithContentsUseCase @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
) {
    suspend operator fun invoke(
        diaryId: String,
        title: String,
        diaryContents: List<DiaryContent>,
        labels: List<String> = listOf(),
        sleepStartAt: ZonedDateTime,
        sleepEndAt: ZonedDateTime,
    ) {
        dreamDiaryRepository.updateDreamDiary(
            diaryId = diaryId,
            title = title,
            diaryContents = diaryContents,
            labels = labels,
            sleepStartAt = sleepStartAt.toInstant(),
            sleepEndAt = sleepEndAt.toInstant(),
        )
    }
}
