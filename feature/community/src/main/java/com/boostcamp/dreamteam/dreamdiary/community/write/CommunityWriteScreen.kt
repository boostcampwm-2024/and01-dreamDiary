package com.boostcamp.dreamteam.dreamdiary.community.write

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.boostcamp.dreamteam.dreamdiary.community.R
import com.boostcamp.dreamteam.dreamdiary.community.write.component.CommunityEditor
import com.boostcamp.dreamteam.dreamdiary.community.write.component.CommunityEditorState
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme

@Composable
fun CommunityWriteScreen(
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CommunityWriteViewModel = hiltViewModel(),
) {
    CommunityWriteScreenContent(
        topAppbarParams = CommunityWriteTopAppbarState(
            onClickBack = onClickBack,
            onClickSave = viewModel::onClickSave,
        ),
        editorState = CommunityEditorState(
            title = "",
            onTitleChange = { },
            postContents = emptyList(),
            onContentTextPositionChange = { },
            onContentTextChange = { _, _ -> },
            onContentFocusChange = { },
            onContentImageDelete = { },
        ),
        modifier = modifier,
    )
}

@Composable
private fun CommunityWriteScreenContent(
    topAppbarParams: CommunityWriteTopAppbarState,
    editorState: CommunityEditorState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CommunityWriteTopAppbar(params = topAppbarParams)
        },
    ) { paddingValues ->
        CommunityEditor(
            state = editorState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CommunityWriteTopAppbar(
    params: CommunityWriteTopAppbarState,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.community_write_title)) },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = params.onClickBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.community_write_toppappbar_navigate_back),
                )
            }
        },
        actions = {
            IconButton(
                onClick = params.onClickSave,
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.community_write_toppappbar_action_save),
                )
            }
        },
    )
}

private data class CommunityWriteTopAppbarState(
    val onClickBack: () -> Unit,
    val onClickSave: () -> Unit,
)

@Preview
@Composable
private fun CommunityWriteScreenContentPreview() {
    DreamdiaryTheme {
        CommunityWriteScreenContent(
            topAppbarParams = CommunityWriteTopAppbarState(
                onClickBack = {},
                onClickSave = {},
            ),
            editorState = CommunityEditorState(
                title = "",
                onTitleChange = { },
                postContents = emptyList(),
                onContentTextPositionChange = { },
                onContentTextChange = { _, _ -> },
                onContentFocusChange = { },
                onContentImageDelete = { },
            ),
        )
    }
}
