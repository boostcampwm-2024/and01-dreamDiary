package com.boostcamp.dreamteam.dreamdiary.community.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.community.R
import com.boostcamp.dreamteam.dreamdiary.community.model.CommentUi
import com.boostcamp.dreamteam.dreamdiary.community.model.commentUiPreview1
import com.boostcamp.dreamteam.dreamdiary.community.model.commentUiPreview2
import com.boostcamp.dreamteam.dreamdiary.designsystem.component.DdAsyncImage
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.ui.util.notImplementedFeature

@Composable
internal fun CommunityDetailComment(
    comment: CommentUi,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.surface,
        LocalTextStyle provides LocalTextStyle.current.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        ListItem(
            headlineContent = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(text = comment.content)
                    Text(
                        text = comment.createdAt.formatted,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            },
            modifier = modifier,
            overlineContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    DdAsyncImage(
                        model = comment.author.profileImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(MaterialTheme.shapes.extraLarge),
                        contentScale = ContentScale.Crop,
                    )
                    Text(
                        text = comment.author.username,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    val context = LocalContext.current
                    IconButton(
                        onClick = { notImplementedFeature(context) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.community_detail_more),
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
        )
    }
}

@Preview
@Composable
private fun CommunityDetailCommentPreview2() {
    DreamdiaryTheme {
        CommunityDetailComment(
            comment = commentUiPreview2,
        )
    }
}
