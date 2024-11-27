@file:OptIn(ExperimentalMaterial3Api::class)

package com.boostcamp.dreamteam.dreamdiary.community.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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

    CommunityDetailScreenContent(
        onClickBack = onClickBack,
        post = state.post,
        onClickLikePost = { post -> viewModel.toggleLikePost(post.id) },
        comments = comments,
        onClickLikeComment = { comment -> viewModel.toggleLikeComment(comment.id) },
    )
}

@Composable
private fun CommunityDetailScreenContent(
    onClickBack: () -> Unit,
    post: PostDetailUi,
    onClickLikePost: (PostDetailUi) -> Unit,
    comments: LazyPagingItems<CommentUi>,
    onClickLikeComment: (CommentUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val refreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CommunityDetailTopAppBar(
                state = CommunityDetailTopAppbarState(
                    scrollBehavior = scrollBehavior,
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
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                // TODO: 새로고침 로직 추가
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = refreshState,
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                item {
                    CommunityDetailPostCard(
                        post = post,
                        onClickLikePost = { onClickLikePost(post) },
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
                            onClickLikeComment = { onClickLikeComment(comment) },
                        )
                    }
                }
            }
        }
    }
}

private data class CommunityDetailTopAppbarState(
    val scrollBehavior: TopAppBarScrollBehavior,
    val onClickBack: () -> Unit,
    val title: String,
)

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
            onClickLikePost = { },
            post = postDetailUiPreview,
            comments = pagingCommentsUiPreview.collectAsLazyPagingItems(),
            onClickLikeComment = { },
        )
    }
}
