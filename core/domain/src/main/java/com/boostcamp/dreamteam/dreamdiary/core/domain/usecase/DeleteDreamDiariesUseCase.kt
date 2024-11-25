package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import timber.log.Timber
import javax.inject.Inject

class DeleteDreamDiariesUseCase @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
) {
    suspend operator fun invoke(diaryId: String) {
        Timber.d("DeleteDreamDiariesUseCase: diaryId = $diaryId")
        dreamDiaryRepository.deleteDreamDiary(diaryId = diaryId)
    }
}
