package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import javax.inject.Inject

class AddLabelUseCase @Inject constructor(
    private val diaryRepository: DreamDiaryRepository,
) {
    suspend operator fun invoke(label: String) {
        return diaryRepository.addLabel(label)
    }
}
