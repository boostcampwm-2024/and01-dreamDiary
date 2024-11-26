package com.boostcamp.dreamteam.dreamdiary.community.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.boostcamp.dreamteam.dreamdiary.community.R
import com.boostcamp.dreamteam.dreamdiary.community.model.PostUi
import com.boostcamp.dreamteam.dreamdiary.community.model.pagedPostPreview
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavItem
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavigation
import com.boostcamp.dreamteam.dreamdiary.ui.toNavigationItem
import timber.log.Timber

@Composable
fun CommunityListScreen(
    onNavigateToDiary: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onDiaryClick: (diaryId: String) -> Unit,
    viewModel: CommunityListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val diaries = viewModel.posts.collectAsLazyPagingItems()

    CommunityListScreenContent(
        onNavigateToDiary = onNavigateToDiary,
        onNavigateToSetting = onNavigateToSetting,
        diaries = diaries,
        onDiaryClick = { diary -> onDiaryClick(diary.id) },
        onSaveClick = viewModel::addCommunityPost,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CommunityListScreenContent(
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
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            Button(
                modifier = modifier.padding(innerPadding),
                onClick = {
                    onSaveClick()
                    Timber.d(diaries.toString())
                },
            ) {
                Text(text = "저장")
            }
        }

//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding),
//            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//        ) {
//            items(items = diaries, key = { it.id }) { diary ->
//                CommunityDiaryCard(
//                    diary = diary,
//                    onClickMenu = { /* TODO: 메뉴 눌렀을 때 기능 추가하기 */ },
//                    onClickLike = { /* TODO: 좋아요 눌렀을 때 기능 추가하기 */ },
//                    modifier = Modifier
//                        .clickable(onClick = { onDiaryClick(diary) })
//                        .animateItem(),
//                )
//            }
//        }
    }
}

@Preview
@Composable
private fun CommunityListScreenContentPreview() {
    DreamdiaryTheme {
        CommunityListScreenContent(
            onNavigateToDiary = { },
            onNavigateToSetting = { },
            onDiaryClick = { },
            onSaveClick = { },
            diaries = pagedPostPreview.collectAsLazyPagingItems(),
        )
    }
}
