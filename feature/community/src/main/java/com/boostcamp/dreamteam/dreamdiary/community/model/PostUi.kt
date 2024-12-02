package com.boostcamp.dreamteam.dreamdiary.community.model

import androidx.paging.PagingData
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.DisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.toDisplayableDateTime
import com.boostcamp.dreamteam.dreamdiary.core.model.DiaryContent
import com.boostcamp.dreamteam.dreamdiary.core.model.community.CommunityPostList
import kotlinx.coroutines.flow.flowOf
import java.time.Instant
import java.time.ZoneId

data class PostUi(
    val id: String,
    val title: String,
    val images: List<String>,
    val previewText: String,
    val sharedAt: DisplayableDateTime,
    val commentCount: Long,
    val isLiked: Boolean,
    val author: UserUi,
    val likeCount: Long,
)

fun CommunityPostList.toPostUi(): PostUi {
    return PostUi(
        id = this.id,
        title = this.title,
        images = this.diaryContents.filterIsInstance<DiaryContent.Image>().map { it.path },
        previewText = this.diaryContents.filterIsInstance<DiaryContent.Text>().joinToString("\n") { it.text },
        sharedAt = this.createdAt.toDisplayableDateTime(),
        commentCount = this.commentCount,
        isLiked = this.isLiked,
        likeCount = this.likeCount,
        author = UserUi(
            uid = this.uid,
            username = this.author,
            profileImageUrl = this.profileImageUrl,
        ),
    )
}

internal val postUiPreview1 = PostUi(
    id = "1",
    title = "Diary 1",
    previewText = "This is a preview text for Diary 1",
    sharedAt = Instant.now().toDisplayableDateTime(zoneId = ZoneId.systemDefault()),
    commentCount = 10,
    isLiked = false,
    images = emptyList(),
    author = userUiPreview1,
    likeCount = 10,
)

internal val postUiPreview2 = PostUi(
    id = "2",
    title = "Diary 2",
    previewText = "This is a preview text for Diary 2",
    sharedAt = Instant.now().toDisplayableDateTime(zoneId = ZoneId.systemDefault()),
    commentCount = 2147483647,
    isLiked = true,
    images = listOf("https://picsum.photos/200/300"),
    author = userUiPreview2,
    likeCount = 0,
)

private val postUiPreview3 = PostUi(
    id = "3",
    title = "Diary 3",
    previewText = "This is a preview text for Diary 3",
    sharedAt = Instant.now().toDisplayableDateTime(zoneId = ZoneId.systemDefault()),
    commentCount = 0,
    isLiked = false,
    images = listOf("https://picsum.photos/200/300", "https://picsum.photos/300/400"),
    author = userUiPreview3,
    likeCount = 0,
)

internal val diariesUiPreview = listOf(
    postUiPreview1,
    postUiPreview2,
    postUiPreview3,
)

internal val pagedPostPreview = flowOf(PagingData.from(diariesUiPreview))
