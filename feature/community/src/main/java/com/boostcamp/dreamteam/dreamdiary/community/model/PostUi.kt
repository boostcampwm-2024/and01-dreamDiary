package com.boostcamp.dreamteam.dreamdiary.community.model

import com.boostcamp.dreamteam.dreamdiary.community.model.vo.DisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.toDisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.core.model.CommunityDreamPost
import java.time.Instant
import java.time.ZoneId

data class PostUi(
    val id: String,
    val thumbnail: String? = null,
    val title: String,
    val previewText: String,
    val sharedAt: DisplayableDateTime,
    val commentCount: Long,
    val isLiked: Boolean,
    val author: UserUi,
)

fun CommunityDreamPost.toPostUi(): PostUi {
    return PostUi(
        id = this.id,
        thumbnail = null,
        title = this.title,
        previewText = this.content.take(50),
        sharedAt = DisplayableDateTime(this.createdAt, ":)", ":)"),
        commentCount = this.comments.size.toLong(),
        isLiked = this.likes > 0,
        author = UserUi(
            id = this.author,
            username = this.author,
            /* Todo */
            profileImageUrl = "https://picsum.photos/200/300",
        )
    )
}

internal val postUiPreview1 = PostUi(
    id = "1",
    title = "Diary 1",
    previewText = "This is a preview text for Diary 1",
    sharedAt = Instant.now().toDisplayableDateTime(zoneId = ZoneId.systemDefault()),
    commentCount = 10,
    isLiked = false,
    author = userUiPreview1,
)

internal val postUiPreview2 = PostUi(
    id = "2",
    thumbnail = "https://picsum.photos/200/300",
    title = "Diary 2",
    previewText = "This is a preview text for Diary 2",
    sharedAt = Instant.now().toDisplayableDateTime(zoneId = ZoneId.systemDefault()),
    commentCount = 2147483647,
    isLiked = true,
    author = userUiPreview2,
)

private val postUiPreview3 = PostUi(
    id = "3",
    title = "Diary 3",
    previewText = "This is a preview text for Diary 3",
    sharedAt = Instant.now().toDisplayableDateTime(zoneId = ZoneId.systemDefault()),
    commentCount = 0,
    isLiked = false,
    author = userUiPreview3,
)

internal val diariesUiPreview = listOf(
    postUiPreview1,
    postUiPreview2,
    postUiPreview3,
)
