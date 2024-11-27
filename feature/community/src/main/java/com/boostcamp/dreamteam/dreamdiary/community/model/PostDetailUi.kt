package com.boostcamp.dreamteam.dreamdiary.community.model

import com.boostcamp.dreamteam.dreamdiary.community.model.vo.DisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.PostContentUi
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.toDisplayableDateTime
import java.time.Instant

data class PostDetailUi(
    val id: String,
    val title: String,
    val contents: List<PostContentUi>,
    val author: UserUi,
    val sharedAt: DisplayableDateTime,
) {
    companion object {
        val EMPTY = PostDetailUi(
            id = "",
            title = "",
            contents = emptyList(),
            author = UserUi.EMPTY,
            sharedAt = DisplayableDateTime.EMPTY,
        )
    }
}
