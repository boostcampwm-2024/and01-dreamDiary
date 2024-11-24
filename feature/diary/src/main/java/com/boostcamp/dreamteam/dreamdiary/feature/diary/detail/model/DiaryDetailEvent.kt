package com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.model

sealed class DiaryDetailEvent {
    sealed class DeleteDiary : DiaryDetailEvent() {
        data object Success : DeleteDiary()

        data object Failure : DeleteDiary()
    }

    class LoadDiary {
        data object Success : DiaryDetailEvent()

        data object Failure : DiaryDetailEvent()
    }
}
