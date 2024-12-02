package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import javax.inject.Inject

class DeleteLabelUseCase @Inject constructor(
    private val labelRepository: DreamDiaryRepository,
) {
    suspend operator fun invoke(label: String) {
        labelRepository.deleteLabel(label)
    }
}
