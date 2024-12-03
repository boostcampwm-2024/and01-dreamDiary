package com.boostcamp.dreamteam.dreamdiary.community.detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.PostAdd
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
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme

@Composable
internal fun CommentsMenuButton(
    isVisible: Boolean,
    onVisibleChange: (Boolean) -> Unit,
    onDeleteDiary: () -> Unit,
    onDiaryEdit: () -> Unit,
    onShareDiary: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        IconButton(
            onClick = { onVisibleChange(true) },
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.home_list_card_menu),
                modifier = Modifier.height(16.dp),
            )
        }
        DropdownMenu(
            expanded = isVisible,
            onDismissRequest = { onVisibleChange(false) },
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.home_list_card_menu_edit)) },
                onClick = {
                    onDiaryEdit()
                    onVisibleChange(false)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = stringResource(R.string.home_list_edit_icon),
                    )
                },
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.home_list_card_menu_delete)) },
                onClick = {
                    onDeleteDiary()
                    onVisibleChange(false)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.home_list_delete_icon),
                    )
                },
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.home_list_card_menu_share)) },
                onClick = {
                    onShareDiary()
                    onVisibleChange(false)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.PostAdd,
                        contentDescription = stringResource(R.string.home_list_post_icon),
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
        CommentsMenuButton(
            isVisible = true,
            onVisibleChange = { },
            onDeleteDiary = { },
            onDiaryEdit = { },
            onShareDiary = { },
        )
    }
}
