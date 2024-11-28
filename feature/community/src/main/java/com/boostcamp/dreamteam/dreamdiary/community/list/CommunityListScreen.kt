package com.boostcamp.dreamteam.dreamdiary.community.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun CommunityListScreen(
    onClickFab: () -> Unit,
    onNavigateToDiary: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onDiaryClick: (diaryId: String) -> Unit,
    goToSignInClick: () -> Unit,
    viewModel: CommunityListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val diaries = viewModel.posts.collectAsLazyPagingItems()

    val modifier = Modifier
    if (viewModel.notSignIn()) {
        NotSignInCommunityContent(
            onNavigateToDiary = onNavigateToDiary,
            onNavigateToSetting = onNavigateToSetting,
            diaries = diaries,
            goToSignInClick = {
                goToSignInClick()
            },
            modifier = modifier,
        )
    } else {
        CommunityListScreenContent(
            onClickFab = onClickFab,
            onNavigateToDiary = onNavigateToDiary,
            onNavigateToSetting = onNavigateToSetting,
            diaries = diaries,
            onDiaryClick = { diary -> onDiaryClick(diary.id) },
            onSaveClick = viewModel::addCommunityPost,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotSignInCommunityContent(
    onNavigateToDiary: () -> Unit,
    onNavigateToSetting: () -> Unit,
    diaries: LazyPagingItems<PostUi>,
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
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                items(
                    count = diaries.itemCount,
                    key = { diaries.peek(it)?.id ?: PagingIndexKey(it) },
                ) { diaryIndex ->
                    val diary = diaries[diaryIndex]
                    if (diary != null) {
                        CommunityDiaryCard(
                            diary = diary,
                            onPostClick = { /* Todo: 게시글 클릭 시 동작 추가하기 */ },
                            onClickMenu = { /* TODO: 메뉴 눌렀을 때 기능 추가하기 */ },
                            onClickLike = { /* TODO: 좋아요 눌렀을 때 기능 추가하기 */ },
                            modifier = Modifier
                                .alpha(1.0f - diaryIndex * 0.3f),
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(30.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp),
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(stringResource(R.string.community_list_not_signin))
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedButton(
                    modifier = modifier,
                    onClick = goToSignInClick
                    ,
                    shape = MaterialTheme.shapes.small,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.community_list_go_to_signin))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
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
    diaries: LazyPagingItems<PostUi>,
    onDiaryClick: (PostUi) -> Unit,
    onSaveClick: () -> Unit,
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

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val refreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.community_title)) },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            HomeBottomNavigation(items = navigationItems)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClickFab,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.community_list_fab_add_diary_description),
                )
            }
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                coroutineScope.launch {
                    isRefreshing = true
                    delay(1.seconds)
                    // TODO: 새로고침 로직 추가
                    isRefreshing = false
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = refreshState,
        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                items(
                    count = diaries.itemCount,
                    key = { diaries.peek(it)?.id ?: PagingIndexKey(it) },
                ) { diaryIndex ->
                    val diary = diaries[diaryIndex]
                    if (diary != null) {
                        CommunityDiaryCard(
                            diary = diary,
                            onPostClick = { /* Todo: 게시글 클릭 시 동작 추가하기 */ },
                            onClickMenu = { /* TODO: 메뉴 눌렀을 때 기능 추가하기 */ },
                            onClickLike = { /* TODO: 좋아요 눌렀을 때 기능 추가하기 */ },
                            modifier = Modifier
                                .clickable(onClick = { onDiaryClick(diary) })
                                .animateItem(),
                        )
                    }
                }
            }
        }

//        Button(
//            modifier = modifier.padding(innerPadding),
//            onClick = {
//                onSaveClick()
//                Timber.d(diaries.toString())
//            },
//        ) {
//            Text(text = "저장")
//        }
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
            onDiaryClick = { },
            onSaveClick = { },
            diaries = pagedPostPreview.collectAsLazyPagingItems(),
        )
    }
}
