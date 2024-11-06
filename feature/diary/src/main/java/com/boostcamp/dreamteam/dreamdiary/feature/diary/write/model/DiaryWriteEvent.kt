package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model

sealed class DiaryWriteEvent {
    data object AddSuccess : DiaryWriteEvent()
}
