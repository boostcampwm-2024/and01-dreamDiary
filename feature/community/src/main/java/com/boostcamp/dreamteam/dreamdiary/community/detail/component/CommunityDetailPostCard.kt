package com.boostcamp.dreamteam.dreamdiary.community.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.community.R
import com.boostcamp.dreamteam.dreamdiary.community.model.PostDetailUi
import com.boostcamp.dreamteam.dreamdiary.community.model.UserUi
import com.boostcamp.dreamteam.dreamdiary.community.model.postDetailUiPreview
import com.boostcamp.dreamteam.dreamdiary.community.write.component.InputBody
import com.boostcamp.dreamteam.dreamdiary.designsystem.component.DdAsyncImage
import com.boostcamp.dreamteam.dreamdiary.designsystem.component.DdCard
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme

@Composable
internal fun CommunityDetailPostCard(
    post: PostDetailUi,
    onClickLikePost: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DdCard(
        modifier = modifier,
        headline = {
            CardUserHeader(
                user = post.author,
                sharedAt = post.sharedAt.formatted,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        content = {
            InputBody(
                postContents = post.contents,
                readOnly = true,
                onContentTextPositionChange = { },
                onContentTextChange = { _, _ -> },
                onContentFocusChange = { },
                onContentImageDelete = { },
            )
        },
        tail = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = onClickLikePost,
                    modifier = Modifier.size(24.dp),
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (post.isLiked) {
                            stringResource(R.string.community_detail_post_dislike)
                        } else {
                            stringResource(R.string.community_detail_post_like)
                        },
                        tint = if (post.isLiked) MaterialTheme.colorScheme.error else LocalContentColor.current,
                    )
                }
                Text(
                    text = post.likeCount.toString(),
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        },
        shape = RoundedCornerShape(0.dp),
    )
}

@Composable
private fun CardUserHeader(
    user: UserUi,
    sharedAt: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        DdAsyncImage(
            model = user.profileImageUrl,
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
                text = user.username,
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
private fun CommunityDetailPostCardPreview() {
    DreamdiaryTheme {
        CommunityDetailPostCard(
            post = postDetailUiPreview,
            onClickLikePost = { },
        )
    }
}
