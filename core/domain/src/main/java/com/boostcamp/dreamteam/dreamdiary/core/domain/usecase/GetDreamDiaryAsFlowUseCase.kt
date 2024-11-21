package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDreamDiaryAsFlowUseCase @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
) {
    operator fun invoke(id: String): Flow<Diary> = dreamDiaryRepository.getDreamDiaryAsFlow(id)
}
