package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDreamDiariesByTitleUseCase @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
) {
    operator fun invoke(query: String): Flow<PagingData<Diary>> {
        return dreamDiaryRepository.getDreamDiariesByTitle(query)
    }
}
