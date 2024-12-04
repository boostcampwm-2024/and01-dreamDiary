package com.boostcamp.dreamteam.dreamdiary.community.detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.community.R
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme

@Composable
internal fun CommunityDetailMenuButton(
    isVisible: Boolean,
    onVisibleChange: (Boolean) -> Unit,
    onDeletePost: () -> Unit,
    onPostEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        IconButton(
            onClick = { onVisibleChange(true) },
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.community_detail_more_vert),
                modifier = Modifier.height(16.dp),
            )
        }
        DropdownMenu(
            expanded = isVisible,
            onDismissRequest = { onVisibleChange(false) },
        ) {
            DropdownMenuItem(
                text = { Text(text = "편집") },
                onClick = {
                    onPostEdit()
                    onVisibleChange(false)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "편집",
                    )
                },
            )
            DropdownMenuItem(
                text = { Text(text = "삭제") },
                onClick = {
                    onDeletePost()
                    onVisibleChange(false)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "삭제",
                    )
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryEditDropDownMenuPreview() {
    DreamdiaryTheme {
        CommunityDetailMenuButton(
            isVisible = true,
            onVisibleChange = { },
            onDeletePost = { },
            onPostEdit = { },
        )
    }
}
