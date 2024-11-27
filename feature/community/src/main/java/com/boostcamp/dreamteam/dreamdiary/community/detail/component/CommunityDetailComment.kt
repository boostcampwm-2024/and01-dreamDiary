package com.boostcamp.dreamteam.dreamdiary.community.detail.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.community.R
import com.boostcamp.dreamteam.dreamdiary.community.model.CommentUi
import com.boostcamp.dreamteam.dreamdiary.community.model.commentUiPreview1
import com.boostcamp.dreamteam.dreamdiary.community.model.commentUiPreview2
import com.boostcamp.dreamteam.dreamdiary.designsystem.component.DdAsyncImage
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme

@Composable
internal fun CommunityDetailComment(
    comment: CommentUi,
    onClickLikeComment: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.surface,
        LocalTextStyle provides LocalTextStyle.current.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        ListItem(
            headlineContent = { Text(text = comment.contents) },
            modifier = modifier,
            overlineContent = {
                Text(
                    text = comment.author.username,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            leadingContent = {
                DdAsyncImage(
                    model = comment.author.profileImageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(MaterialTheme.shapes.extraLarge),
                    contentScale = ContentScale.Crop,
                )
            },
            trailingContent = {
                IconButton(
                    onClick = onClickLikeComment,
                ) {
                    if (comment.isLiked) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = stringResource(R.string.community_detail_comment_dislike),
                            tint = MaterialTheme.colorScheme.error,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = stringResource(R.string.community_detail_comment_like),
                        )
                    }
                }
            },
        )
    }
}

@Preview
@Composable
private fun CommunityDetailCommentPreview1() {
    DreamdiaryTheme {
        CommunityDetailComment(
            comment = commentUiPreview1,
            onClickLikeComment = { },
        )
    }
}

@Preview
@Composable
private fun CommunityDetailCommentPreview2() {
    DreamdiaryTheme {
        CommunityDetailComment(
            comment = commentUiPreview2,
            onClickLikeComment = { },
        )
    }
}
