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
    val isLiked: Boolean,
) {
    companion object {
        val EMPTY = PostDetailUi(
            id = "",
            title = "",
            contents = emptyList(),
            author = UserUi.EMPTY,
            sharedAt = DisplayableDateTime.EMPTY,
            isLiked = false,
        )
    }
}

internal val postDetailUiPreview = PostDetailUi(
    id = "1",
    title = "제목",
    contents = listOf(PostContentUi.Text("내용")),
    author = userUiPreview1,
    sharedAt = Instant.now().toDisplayableDateTime(),
    isLiked = false,
)
