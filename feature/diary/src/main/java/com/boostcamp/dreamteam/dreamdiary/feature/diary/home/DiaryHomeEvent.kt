package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

sealed class DiaryHomeEvent {
    sealed class Delete : DiaryHomeEvent() {
        data object Success : Delete()
    }
}
