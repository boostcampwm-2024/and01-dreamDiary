package com.boostcamp.dreamteam.dreamdiary.community.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boostcamp.dreamteam.dreamdiary.community.R
import com.boostcamp.dreamteam.dreamdiary.community.model.PostDetailUi
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme

@Composable
fun CommunityDetailScreen(
    onClickBack: () -> Unit,
    viewModel: CommunityDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CommunityDetailScreenContent(
        onClickBack = onClickBack,
        post = state.post,
    )
}

@Composable
private fun CommunityDetailScreenContent(
    onClickBack: () -> Unit,
    post: PostDetailUi,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CommunityDetailTopAppBar(
                state = CommunityDetailTopAppbarState(
                    onClickBack = onClickBack,
                    title = post.title,
                ),
            )
        },
        bottomBar = {
            // TODO
            NewCommentBottomBar(
                state = CommunityDetailBottomBarState(
                    inputComment = "",
                    onInputCommentChange = { },
                    onSubmitComment = { },
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {}
    }
}

private data class CommunityDetailTopAppbarState(
    val onClickBack: () -> Unit,
    val title: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommunityDetailTopAppBar(
    state: CommunityDetailTopAppbarState,
    modifier: Modifier = Modifier,
) {
    MediumTopAppBar(
        title = { Text(text = state.title) },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = state.onClickBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.community_detail_toppappbar_navigate_back),
                )
            }
        },
    )
}

private data class CommunityDetailBottomBarState(
    val inputComment: String,
    val onInputCommentChange: (String) -> Unit,
    val onSubmitComment: () -> Unit,
)

@Composable
private fun NewCommentBottomBar(
    state: CommunityDetailBottomBarState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = BottomAppBarDefaults.containerColor),
    ) {
        OutlinedTextField(
            value = state.inputComment,
            onValueChange = state.onInputCommentChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.community_detail_bottombar_input_comment_hint),
                    color = LocalContentColor.current.copy(alpha = 0.6f),
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.AddComment,
                    contentDescription = stringResource(R.string.community_detail_bottombar_input_comment_icon),
                    tint = LocalContentColor.current.copy(alpha = 0.6f),
                )
            },
            trailingIcon = if (state.inputComment.isNotEmpty()) {
                {
                    IconButton(onClick = state.onSubmitComment) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Send,
                            contentDescription = stringResource(R.string.community_detail_bottombar_input_comment_submit),
                        )
                    }
                }
            } else {
                null
            },
        )
    }
}

@Preview
@Composable
private fun CommunityDetailScreenContentPreview() {
    DreamdiaryTheme {
        CommunityDetailScreenContent(
            onClickBack = { },
            post = PostDetailUi.EMPTY,
        )
    }
}
