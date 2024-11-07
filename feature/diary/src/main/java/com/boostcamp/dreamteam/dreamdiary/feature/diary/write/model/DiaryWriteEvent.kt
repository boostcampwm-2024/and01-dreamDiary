package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model

sealed class DiaryWriteEvent {
    data object DiaryAddSuccess : DiaryWriteEvent()

    data object LabelAddSuccess : DiaryWriteEvent()

    data class LabelAddFailure(val labelAddFailureReason: LabelAddFailureReason) : DiaryWriteEvent()
}

enum class LabelAddFailureReason {
    DUPLICATE_LABEL,
    INSUFFICIENT_STORAGE,
    UNKNOWN_ERROR,
}
