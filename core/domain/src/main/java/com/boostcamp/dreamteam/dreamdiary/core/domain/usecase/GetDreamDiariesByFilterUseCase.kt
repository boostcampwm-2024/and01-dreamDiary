package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDreamDiariesByFilterUseCase @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
) {
    operator fun invoke(labels: List<String>): Flow<PagingData<Diary>> {
        if (labels.isEmpty()) {
            return dreamDiaryRepository.getDreamDiaries()
        } else {
            return dreamDiaryRepository.getDreamDiariesByLabel(labels)
        }
    }
}
