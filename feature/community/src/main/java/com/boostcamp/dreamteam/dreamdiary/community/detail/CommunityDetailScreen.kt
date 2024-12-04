@file:OptIn(ExperimentalMaterial3Api::class)

package com.boostcamp.dreamteam.dreamdiary.community.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.boostcamp.dreamteam.dreamdiary.community.R
import com.boostcamp.dreamteam.dreamdiary.community.detail.component.CommunityDetailComment
import com.boostcamp.dreamteam.dreamdiary.community.detail.component.CommunityDetailPostCard
import com.boostcamp.dreamteam.dreamdiary.community.model.CommentUi
import com.boostcamp.dreamteam.dreamdiary.community.model.PostDetailUi
import com.boostcamp.dreamteam.dreamdiary.community.model.pagingCommentsUiPreview
import com.boostcamp.dreamteam.dreamdiary.community.model.postDetailUiPreview
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.ui.PagingIndexKey

@Composable
fun CommunityDetailScreen(
    onClickBack: () -> Unit,
    viewModel: CommunityDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val comments = viewModel.comments.collectAsLazyPagingItems()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is CommunityDetailEvent.CommentAdd.Success -> {
                    comments.refresh()
                    focusManager.clearFocus()
                }
                is CommunityDetailEvent.CommentDelete.Success -> {
                    comments.refresh()
                    focusManager.clearFocus()
                }
                is CommunityDetailEvent.LikePost.Success -> {}
                is CommunityDetailEvent.LikePost.Fail -> {
                    Toast.makeText(
                        context,
                        "좋아요 실패",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    CommunityDetailScreenContent(
        onClickBack = onClickBack,
        post = state.post,
        onClickLikePost = viewModel::togglePostLike,
        comments = comments,
        commentContent = state.commentContent,
        onChangeCommentContent = viewModel::changeCommentContent,
        onSubmitComment = {
            viewModel.addComment()
        },
        onDeleteComment = viewModel::deleteComment,
    )
}

@Composable
private fun CommunityDetailScreenContent(
    onClickBack: () -> Unit,
    post: PostDetailUi,
    onClickLikePost: () -> Unit,
    comments: LazyPagingItems<CommentUi>,
    commentContent: String,
    onSubmitComment: () -> Unit,
    onChangeCommentContent: (String) -> Unit,
    onDeleteComment: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            CommunityDetailTopAppBar(
                state = CommunityDetailTopAppbarState(
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                    onClickBack = onClickBack,
                    title = post.title,
                ),
            )
        },
        bottomBar = {
            NewCommentBottomBar(
                state = CommunityDetailBottomBarState(
                    inputComment = commentContent,
                    onInputCommentChange = onChangeCommentContent,
                    onSubmitComment = onSubmitComment,
                ),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            item {
                CommunityDetailPostCard(
                    post = post,
                    onClickLikePost = { onClickLikePost() },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            items(
                count = comments.itemCount,
                key = { comments.peek(it)?.id ?: PagingIndexKey(it) },
            ) { index ->
                val comment = comments[index]
                if (comment != null) {
                    CommunityDetailComment(
                        comment = comment,
                        onDeleteComment = onDeleteComment,
                    )
                }
            }
        }
    }
}

private data class CommunityDetailTopAppbarState(
    val topAppBarScrollBehavior: TopAppBarScrollBehavior,
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
        scrollBehavior = state.topAppBarScrollBehavior,
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
    var shouldShowSendButton by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
            .navigationBarsPadding(),
    ) {
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            BasicTextField(
                value = state.inputComment,
                onValueChange = {
                    state.onInputCommentChange(it)
                },
                modifier = Modifier
                    .weight(1f)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            shouldShowSendButton = true
                        }
                    }
                    .padding(8.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
                decorationBox = { innerTextField ->
                    if (state.inputComment.isEmpty()) {
                        Text(
                            text = stringResource(R.string.community_detail_bottombar_input_comment_hint),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                            ),
                        )
                    }
                    innerTextField()
                },
            )
            if (shouldShowSendButton) {
                IconButton(
                    enabled = state.inputComment.isNotBlank(),
                    onClick = state.onSubmitComment,
                    modifier = Modifier.size(36.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Send,
                        contentDescription = stringResource(R.string.community_detail_bottombar_input_comment_submit),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommunityDetailScreenContentPreview() {
    DreamdiaryTheme {
        CommunityDetailScreenContent(
            onClickBack = { },
            onClickLikePost = { },
            onSubmitComment = { },
            post = postDetailUiPreview,
            commentContent = "",
            onChangeCommentContent = { },
            onDeleteComment = { },
            comments = pagingCommentsUiPreview.collectAsLazyPagingItems(),
        )
    }
}
