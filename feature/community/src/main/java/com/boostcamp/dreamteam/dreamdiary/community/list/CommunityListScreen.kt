package com.boostcamp.dreamteam.dreamdiary.community.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boostcamp.dreamteam.dreamdiary.community.R
import com.boostcamp.dreamteam.dreamdiary.community.list.component.CommunityDiaryCard
import com.boostcamp.dreamteam.dreamdiary.community.model.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.community.model.diariesUiPreview
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavItem
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavigation
import com.boostcamp.dreamteam.dreamdiary.ui.toNavigationItem

@Composable
fun CommunityListScreen(
    onNavigateToDiary: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onDiaryClick: (diaryId: String) -> Unit,
    viewModel: CommunityListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CommunityListScreenContent(
        onNavigateToDiary = onNavigateToDiary,
        onNavigateToSetting = onNavigateToSetting,
        diaries = state.diaries,
        onDiaryClick = { diary -> onDiaryClick(diary.id) },
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CommunityListScreenContent(
    onNavigateToDiary: () -> Unit,
    onNavigateToSetting: () -> Unit,
    diaries: List<DiaryUi>,
    onDiaryClick: (DiaryUi) -> Unit,
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(items = diaries, key = { it.id }) { diary ->
                CommunityDiaryCard(
                    diary = diary,
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

@Preview
@Composable
private fun CommunityListScreenContentPreview() {
    DreamdiaryTheme {
        CommunityListScreenContent(
            onNavigateToDiary = { },
            onNavigateToSetting = { },
            onDiaryClick = { },
            diaries = diariesUiPreview,
        )
    }
}
