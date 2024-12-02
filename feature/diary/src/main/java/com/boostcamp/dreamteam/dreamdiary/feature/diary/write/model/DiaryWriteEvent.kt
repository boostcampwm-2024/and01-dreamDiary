package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model

sealed class DiaryWriteEvent {
    data class DiaryAddSuccess(
        val diaryId: String,
    ) : DiaryWriteEvent()

    data class DiaryUpdateSuccess(
        val diaryId: String,
    ) : DiaryWriteEvent()

    data object DiaryUpdateFail : DiaryWriteEvent()

    sealed class Label : DiaryWriteEvent() {
        data object AddSuccess : Label()

        data class AddFailure(
            val labelAddFailureReason: LabelAddFailureReason,
        ) : Label()

        data object DeleteFailure : Label()
    }
}

enum class LabelAddFailureReason {
    DUPLICATE_LABEL,
    INSUFFICIENT_STORAGE,
    UNKNOWN_ERROR,
}
