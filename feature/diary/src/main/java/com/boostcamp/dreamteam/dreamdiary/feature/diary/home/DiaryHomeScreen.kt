package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.DiaryCalendarTab
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.DiaryHomeTabCalendarUIState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.diaryHomeTabCalendarUIStatePreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist.DiaryListTab
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist.pagedDiariesPreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavItem
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavigation
import com.boostcamp.dreamteam.dreamdiary.ui.NavigationItem
import java.time.YearMonth

@Composable
fun DiaryHomeScreen(
    onDiaryClick: (DiaryUi) -> Unit,
    onNavigateToWriteScreen: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToSetting: () -> Unit,
    viewModel: DiaryHomeViewModel = hiltViewModel(),
) {
    val diaries = viewModel.dreamDiaries.collectAsLazyPagingItems()
    val calendarUIState by viewModel.tabCalendarUiState.collectAsStateWithLifecycle()

    DiaryHomeScreenContent(
        diaries = diaries,
        calendarUIState = calendarUIState,
        onDiaryClick = onDiaryClick,
        onCalendarYearMothChange = viewModel::setCalendarYearMonth,
        onNavigateToWriteScreen = onNavigateToWriteScreen,
        onNavigateToCommunity = onNavigateToCommunity,
        onNavigateToSetting = onNavigateToSetting,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiaryHomeScreenContent(
    diaries: LazyPagingItems<DiaryUi>,
    calendarUIState: DiaryHomeTabCalendarUIState,
    onCalendarYearMothChange: (YearMonth) -> Unit,
    onNavigateToWriteScreen: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToSetting: () -> Unit,
    modifier: Modifier = Modifier,
    onDiaryClick: (DiaryUi) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf(stringResource(R.string.home_tab_dream), stringResource(R.string.home_tab_calendar))

    val navigationItems = listOf(
        NavigationItem(
            icon = HomeBottomNavItem.MyDream.icon,
            labelRes = HomeBottomNavItem.MyDream.label,
            isSelected = true,
            onClick = { /* Nothing */ },
        ),
        NavigationItem(
            icon = HomeBottomNavItem.Community.icon,
            labelRes = HomeBottomNavItem.Community.label,
            isSelected = false,
            onClick = onNavigateToCommunity,
        ),
        NavigationItem(
            icon = HomeBottomNavItem.Setting.icon,
            labelRes = HomeBottomNavItem.Setting.label,
            isSelected = false,
            onClick = onNavigateToSetting,
        ),
    )

    Scaffold(
        topBar = {
            DiaryHomeScreenTopAppBar(
                onNotificationClick = { /* 알림 클릭 시 동작 */ },
                onSearchClick = { /* 검색 클릭 시 동작 */ },
            )
        },
        bottomBar = {
            HomeBottomNavigation(items = navigationItems)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToWriteScreen,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = stringResource(R.string.home_diary_write),
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            selectedTabIndex = selectedTabIndex,
                        ),
                    )
                },
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) },
                    )
                }
            }

            val tabModifier = Modifier.fillMaxSize()
            when (selectedTabIndex) {
                0 -> DiaryListTab(
                    diaries = diaries,
                    onDiaryClick = onDiaryClick,
                    modifier = tabModifier,
                )

                1 -> DiaryCalendarTab(
                    onDiaryClick = onDiaryClick,
                    onYearMothChange = onCalendarYearMothChange,
                    modifier = tabModifier,
                    state = calendarUIState,
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DiaryHomeScreenTopAppBar(
    onNotificationClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.home_my_dream)) },
        modifier = modifier,
        actions = {
            IconButton(
                onClick = onNotificationClick,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = stringResource(R.string.home_alarm_description),
                )
            }
            IconButton(
                onClick = onSearchClick,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = stringResource(R.string.home_search_description),
                )
            }
        },
    )
}

@Preview
@Composable
private fun DiaryHomeScreenContentPreview() {
    DreamdiaryTheme {
        DiaryHomeScreenContent(
            diaries = pagedDiariesPreview.collectAsLazyPagingItems(),
            calendarUIState = diaryHomeTabCalendarUIStatePreview,
            onCalendarYearMothChange = { },
            onNavigateToWriteScreen = {},
            onNavigateToCommunity = {},
            onNavigateToSetting = {},
            onDiaryClick = {},
            onSearchClick = {},
            onNotificationClick = {},
        )
    }
}
