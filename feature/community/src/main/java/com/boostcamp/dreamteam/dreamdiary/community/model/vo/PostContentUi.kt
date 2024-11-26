package com.boostcamp.dreamteam.dreamdiary.community.model.vo

sealed class PostContentUi {
    data class Text(
        val text: String,
    ) : PostContentUi()

    data class Image(
        val path: String,
    ) : PostContentUi()
}
