package com.boostcamp.dreamteam.dreamdiary.community.detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    Surface(modifier = modifier) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                DdAsyncImage(
                    model = comment.author.profileImageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(MaterialTheme.shapes.extraLarge),
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(end = 12.dp),
                ) {
                    Text(
                        text = comment.author.username,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        ),
                        maxLines = 1,
                    )
                    Text(
                        text = comment.content,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = LocalTextStyle.current.fontSize * 1.5f,
                    )
                }
            }
            val context = LocalContext.current
            IconButton(
                onClick = { notImplementedFeature(context) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .scale(0.7f),
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                )
            }
        }
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
