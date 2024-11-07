package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model

sealed class DiaryWriteEvent {
    data object DiaryAddSuccess : DiaryWriteEvent()
    data object LabelAddSuccess : DiaryWriteEvent()
    data object LabelAddFailure : DiaryWriteEvent()
}
