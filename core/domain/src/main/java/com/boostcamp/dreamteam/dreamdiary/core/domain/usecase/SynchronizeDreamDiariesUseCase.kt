package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.FunctionRepository
import javax.inject.Inject

class SynchronizeDreamDiariesUseCase @Inject constructor(
    private val dreamDiaryRepository: DreamDiaryRepository,
    private val functionRepository: FunctionRepository,
) {
    suspend operator fun invoke() {
        val dreamDiariesNeedSync = dreamDiaryRepository.getDreamDiariesNeedSync()
        val syncResponse = functionRepository.syncDreamDiaries(dreamDiariesNeedSync)
        for (i in syncResponse) {
            dreamDiaryRepository.updateDreamDiaryVersionAndNeedSync(i.diaryId, i.version)
        }
    }
}
