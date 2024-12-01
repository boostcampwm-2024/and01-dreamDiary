package com.boostcamp.dreamteam.dreamdiary.community.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.community.R
import com.boostcamp.dreamteam.dreamdiary.community.model.PostUi
import com.boostcamp.dreamteam.dreamdiary.community.model.UserUi
import com.boostcamp.dreamteam.dreamdiary.community.model.postUiPreview1
import com.boostcamp.dreamteam.dreamdiary.community.model.postUiPreview2
import com.boostcamp.dreamteam.dreamdiary.designsystem.component.DdAsyncImage
import com.boostcamp.dreamteam.dreamdiary.designsystem.component.DdCard
import com.boostcamp.dreamteam.dreamdiary.ui.util.conditional

@Composable
internal fun CommunityDiaryCard(
    diary: PostUi,
    onPostClick: ((PostUi) -> Unit)?,
    onClickLike: ((diary: PostUi) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    DdCard(
        headline = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = diary.title,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        modifier = modifier
            .conditional(
                condition = onPostClick != null,
                ifTrue = { clickable { onPostClick?.invoke(diary) } },
            ),
        overline = {
            Column(modifier = Modifier.fillMaxWidth()) {
                AuthorHeader(
                    author = diary.author,
                    modifier = Modifier.fillMaxWidth(),
                    sharedAt = diary.sharedAt.formatted,
                )
                Spacer(modifier = Modifier.height(8.dp))
                diary.images.firstOrNull()?.let { image ->
                    DdAsyncImage(
                        model = image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f),
                    )
                }
            }
        },
        tail = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Comment,
                    contentDescription = stringResource(R.string.community_list_diary_card_comment),
                )
                Spacer(Modifier.width(4.dp))
                ProvideTextStyle(
                    value = MaterialTheme.typography.bodyMedium,
                ) {
                    Text(text = diary.commentCount.toString())
                }
                IconButton(
                    enabled = onClickLike != null,
                    onClick = { onClickLike?.invoke(diary) },
                ) {
                    Icon(
                        imageVector = if (diary.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(R.string.community_list_diary_card_like),
                        tint = if (diary.isLiked) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                    )
                }
            }
        },
    ) {
        Text(
            text = diary.previewText,
            modifier = Modifier,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun AuthorHeader(
    author: UserUi,
    sharedAt: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        DdAsyncImage(
            model = author.profileImageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = author.username,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = sharedAt,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                ),
            )
        }
    }
}

@Preview
@Composable
private fun CommunityDiaryCardPreviewWithoutImage() {
    MaterialTheme {
        CommunityDiaryCard(
            diary = postUiPreview1,
            onPostClick = { },
            onClickLike = { },
        )
    }
}

@Preview
@Composable
private fun CommunityDiaryCardPreviewWithImage() {
    MaterialTheme {
        CommunityDiaryCard(
            diary = postUiPreview2,
            onPostClick = {},
            onClickLike = { },
        )
    }
}
