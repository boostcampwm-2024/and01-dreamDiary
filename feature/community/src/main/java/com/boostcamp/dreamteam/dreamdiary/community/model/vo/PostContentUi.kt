package com.boostcamp.dreamteam.dreamdiary.community.model.vo

sealed class PostContentUi(
    open val id: String,
) {
    data class Text(
        override val id: String,
        val text: String,
    ) : PostContentUi(id)

    data class Image(
        override val id: String,
        val path: String,
    ) : PostContentUi(id)
}
