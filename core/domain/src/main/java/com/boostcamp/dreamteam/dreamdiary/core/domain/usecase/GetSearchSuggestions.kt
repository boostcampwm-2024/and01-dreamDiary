package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import javax.inject.Inject

class GetSearchSuggestions @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
) {
    suspend operator fun invoke(query: String): List<String> {
        return dreamDiaryRepository.getSearchSuggestion(query)
    }
}
