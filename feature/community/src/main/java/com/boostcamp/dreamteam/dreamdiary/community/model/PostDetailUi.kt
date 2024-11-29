package com.boostcamp.dreamteam.dreamdiary.community.model

import com.boostcamp.dreamteam.dreamdiary.community.model.vo.DisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.PostContentUi
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.toDisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.toPostContentUi
import com.boostcamp.dreamteam.dreamdiary.core.model.CommunityPostDetail
import timber.log.Timber
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

fun CommunityPostDetail.toUIState(): PostDetailUi =
    PostDetailUi(
        id = this.id,
        title = this.title,
        contents = this.postContents.map { content ->
            Timber.d("content: $content")
            content.toPostContentUi()
        },
        author = UserUi(
            uid = this.author,
            username = this.author,
            profileImageUrl = this.profileImageUrl,
        ),
        sharedAt = Instant.ofEpochSecond(this.createdAt).toDisplayableDateTime(),
        isLiked = false,
    )

internal val postDetailUiPreview = PostDetailUi(
    id = "1",
    title = "제목",
    contents = listOf(PostContentUi.Text("내용")),
    author = userUiPreview1,
    sharedAt = Instant.now().toDisplayableDateTime(),
    isLiked = false,
)
