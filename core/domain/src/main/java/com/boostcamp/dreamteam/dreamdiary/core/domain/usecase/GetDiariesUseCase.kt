package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import javax.inject.Inject

class GetDiariesUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
) {
    suspend operator fun invoke(): List<Diary> {
        return diaryRepository.getAllDiaryFromFireBase()
    }
}
