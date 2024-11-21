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

        /* TODO: 업데이트 성공시 이벤트
        data class LabelUpdateSuccess(
            val labelId: String,
        ) : Label()
         */

        data object UpdateFailure : Label()

        /* TODO: 삭제 성공시 이벤트
        data object LabelDeleteSuccess : Label()
         */

        data object DeleteFailure : Label()
    }
}

enum class LabelAddFailureReason {
    DUPLICATE_LABEL,
    INSUFFICIENT_STORAGE,
    UNKNOWN_ERROR,
}
