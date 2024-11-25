package com.boostcamp.dreamteam.dreamdiary.core.model.synchronization

data class SyncVersionResponse(
    val needSyncDiaries: List<NeedSyncDiary>,
    val serverOnlyDiaries: List<NeedSyncDiary>
) {
    data class NeedSyncDiary(
        val diaryId: String,
    )
}
