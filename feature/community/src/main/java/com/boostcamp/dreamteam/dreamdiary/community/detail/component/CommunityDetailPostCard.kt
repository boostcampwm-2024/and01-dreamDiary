package com.boostcamp.dreamteam.dreamdiary.community.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    onClickLike: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DdCard(
        modifier = modifier,
        headline = {
            CardUserHeader(
                user = post.author,
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
            IconButton(onClick = onClickLike) {
                Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "좋아요")
            }
        },
    )
}

@Composable
private fun CardUserHeader(
    user: UserUi,
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
        Text(
            text = user.username,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Preview
@Composable
private fun CommunityDetailPostCardPreview() {
    DreamdiaryTheme {
        CommunityDetailPostCard(
            post = postDetailUiPreview,
            onClickLike = { },
        )
    }
}
