package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.Label
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLabelsUseCase @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
) {
    operator fun invoke(): Flow<List<Label>> {
        return dreamDiaryRepository.getLabels()
    }
}
