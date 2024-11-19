package com.boostcamp.dreamteam.dreamdiary.core.model

sealed class NewDiaryContent {
    class Text(val text: String) : NewDiaryContent()

    class Image(val path: String) : NewDiaryContent()
}
