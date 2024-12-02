package com.boostcamp.dreamteam.dreamdiary.community.list

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.boostcamp.dreamteam.dreamdiary.community.R
import com.boostcamp.dreamteam.dreamdiary.community.list.component.CommunityDiaryCard
import com.boostcamp.dreamteam.dreamdiary.community.model.PostUi
import com.boostcamp.dreamteam.dreamdiary.community.model.pagedPostPreview
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavItem
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavigation
import com.boostcamp.dreamteam.dreamdiary.ui.PagingIndexKey
import com.boostcamp.dreamteam.dreamdiary.ui.toNavigationItem

@Composable
fun CommunityListScreen(
    onClickFab: () -> Unit,
    onNavigateToDiary: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onDiaryClick: (diaryId: String) -> Unit,
    goToSignInClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CommunityListViewModel = hiltViewModel(),
) {
    val posts = viewModel.posts.collectAsLazyPagingItems()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is CommunityListEvent.LikePost.Success -> {}
                is CommunityListEvent.LikePost.Failure -> {
                    Toast.makeText(context, "좋아요 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val contentModifier = Modifier
    if (viewModel.notSignIn()) {
        NotSignInCommunityContent(
            onNavigateToDiary = onNavigateToDiary,
            onNavigateToSetting = onNavigateToSetting,
            posts = posts,
            goToSignInClick = goToSignInClick,
            modifier = contentModifier,
        )
    } else {
        CommunityListScreenContent(
            onClickFab = onClickFab,
            onNavigateToDiary = onNavigateToDiary,
            onNavigateToSetting = onNavigateToSetting,
            posts = posts,
            onPostClick = { diary -> onDiaryClick(diary.id) },
            onPostLikeClick = viewModel::togglePostLike,
            modifier = contentModifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotSignInCommunityContent(
    onNavigateToDiary: () -> Unit,
    onNavigateToSetting: () -> Unit,
    posts: LazyPagingItems<PostUi>,
    goToSignInClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigationItems = listOf(
        HomeBottomNavItem.MyDream.toNavigationItem(
            onClick = onNavigateToDiary,
        ),
        HomeBottomNavItem.Community.toNavigationItem(
            onClick = { /* no-op */ },
            isSelected = true,
        ),
        HomeBottomNavItem.Setting.toNavigationItem(
            onClick = onNavigateToSetting,
        ),
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.community_title)) },
            )
        },
        bottomBar = {
            HomeBottomNavigation(items = navigationItems)
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                userScrollEnabled = false,
            ) {
                items(
                    count = posts.itemCount,
                    key = { posts.peek(it)?.id ?: PagingIndexKey(it) },
                ) { diaryIndex ->
                    val diary = posts[diaryIndex]
                    if (diary != null) {
                        CommunityDiaryCard(
                            post = diary,
                            onPostClick = null,
                            onClickLike = null,
                            modifier = Modifier.alpha(1.0f - diaryIndex * 0.3f),
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .shadow(
                        elevation = 8.dp,
                        shape = MaterialTheme.shapes.medium,
                    )
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.medium,
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ProvideTextStyle(
                    value = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    ),
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(stringResource(R.string.community_list_not_signin))
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedButton(
                        modifier = Modifier,
                        onClick = goToSignInClick,
                        shape = MaterialTheme.shapes.small,
                    ) {
                        Text(stringResource(R.string.community_list_go_to_signin))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CommunityListScreenContent(
    onClickFab: () -> Unit,
    onNavigateToDiary: () -> Unit,
    onNavigateToSetting: () -> Unit,
    posts: LazyPagingItems<PostUi>,
    onPostClick: (PostUi) -> Unit,
    onPostLikeClick: (PostUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigationItems = listOf(
        HomeBottomNavItem.MyDream.toNavigationItem(
            onClick = onNavigateToDiary,
        ),
        HomeBottomNavItem.Community.toNavigationItem(
            onClick = { /* no-op */ },
            isSelected = true,
        ),
        HomeBottomNavItem.Setting.toNavigationItem(
            onClick = onNavigateToSetting,
        ),
    )

    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val bottomAppBarScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.community_title)) },
                scrollBehavior = topAppBarScrollBehavior,
            )
        },
        bottomBar = {
            HomeBottomNavigation(
                items = navigationItems,
                scrollBehavior = bottomAppBarScrollBehavior,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onClickFab) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.community_list_fab_add_diary_description),
                )
            }
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = posts.loadState.refresh is LoadState.Loading,
            onRefresh = { posts.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                items(
                    count = posts.itemCount,
                    key = { posts.peek(it)?.id ?: PagingIndexKey(it) },
                ) { diaryIndex ->
                    val diary = posts[diaryIndex]
                    if (diary != null) {
                        CommunityDiaryCard(
                            post = diary,
                            onPostClick = onPostClick,
                            onClickLike = { onPostLikeClick(diary) },
                            modifier = Modifier.animateItem(),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommunityListScreenContentPreview() {
    DreamdiaryTheme {
        CommunityListScreenContent(
            onClickFab = { },
            onNavigateToDiary = { },
            onNavigateToSetting = { },
            posts = pagedPostPreview.collectAsLazyPagingItems(),
            onPostClick = { },
            onPostLikeClick = { },
        )
    }
}
