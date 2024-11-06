package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import javax.inject.Inject

class AddDreamDiaryUseCase @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
) {
    suspend operator fun invoke(
        title: String,
        body: String,
    ) {
        dreamDiaryRepository.addDreamDiary(title, body)
    }
}
