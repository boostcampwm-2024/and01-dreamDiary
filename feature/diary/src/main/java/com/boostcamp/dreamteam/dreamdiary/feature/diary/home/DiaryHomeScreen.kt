package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBarDefaults
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySort
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortOrder
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortType
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.DiaryCalendarTab
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.DiaryHomeTabCalendarUIState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.diaryHomeTabCalendarUIStatePreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist.DiaryListTab
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist.pagedDiariesPreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.widget.util.updateWidget
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavItem
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavigation
import com.boostcamp.dreamteam.dreamdiary.ui.component.GoToSignInDialog
import com.boostcamp.dreamteam.dreamdiary.ui.toNavigationItem
import java.time.YearMonth

@Composable
fun DiaryHomeScreen(
    onDiaryClick: (DiaryUi) -> Unit,
    onDiaryEdit: (DiaryUi) -> Unit,
    onShareDiary: (diaryId: String) -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onDialogConfirmClick: () -> Unit,
    viewModel: DiaryHomeViewModel = hiltViewModel(),
    onNavigateToWriteScreen: () -> Unit,
) {
    val diaries = viewModel.dreamDiaries.collectAsLazyPagingItems()
    val calendarUIState by viewModel.tabCalendarUiState.collectAsStateWithLifecycle()
    val labels by viewModel.dreamLabels.collectAsStateWithLifecycle()
    val labelOptions by viewModel.labelOptions.collectAsStateWithLifecycle()
    val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is DiaryHomeEvent.Delete.Success -> {
                    updateWidget(context)
                }
            }
        }
    }
    GoToSignInDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = {
            onDialogConfirmClick()
            showDialog = false
        },
    )
    DiaryHomeScreenContent(
        diaries = diaries,
        labels = labels,
        labelOptions = labelOptions,
        onCheckLabel = viewModel::toggleLabel,
        calendarUIState = calendarUIState,
        onCalendarYearMothChange = viewModel::setCalendarYearMonth,
        onNavigateToWriteScreen = onNavigateToWriteScreen,
        onNavigateToCommunity = onNavigateToCommunity,
        onNavigateToSetting = onNavigateToSetting,
        onDiaryClick = onDiaryClick,
        onDiaryEdit = onDiaryEdit,
        onDeleteDiary = { viewModel.deleteDiary(it.id) },
        onShareDiary = {
            if (email == null) {
                showDialog = true
            } else {
                onShareDiary(it.id)
            }
        },
        onChangeSort = viewModel::setSort,
        sortOption = sortOption,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiaryHomeScreenContent(
    diaries: LazyPagingItems<DiaryUi>,
    labels: List<LabelUi>,
    labelOptions: Set<LabelUi>,
    onCheckLabel: (LabelUi) -> Unit,
    calendarUIState: DiaryHomeTabCalendarUIState,
    onCalendarYearMothChange: (YearMonth) -> Unit,
    onNavigateToWriteScreen: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onDiaryClick: (DiaryUi) -> Unit,
    onDiaryEdit: (DiaryUi) -> Unit,
    onDeleteDiary: (DiaryUi) -> Unit,
    onShareDiary: (DiaryUi) -> Unit,
    sortOption: DiarySort,
    onChangeSort: (DiarySort) -> Unit,
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
) {
    val (currentTabIndex, setCurrentTabIndex) = rememberSaveable { mutableIntStateOf(0) }

    val navigationItems = listOf(
        HomeBottomNavItem.MyDream.toNavigationItem(
            onClick = { /* no-op */ },
            isSelected = true,
        ),
        HomeBottomNavItem.Community.toNavigationItem(
            onClick = onNavigateToCommunity,
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
            DiaryHomeScreenTopAppBar(
                onNotificationClick = { /* 알림 클릭 시 동작 */ },
                onSearchClick = { /* 검색 클릭 시 동작 */ },
                scrollBehavior = topAppBarScrollBehavior,
                currentTabIndex = currentTabIndex,
                onClickTab = setCurrentTabIndex,
            )
        },
        bottomBar = {
            HomeBottomNavigation(
                items = navigationItems,
                scrollBehavior = bottomAppBarScrollBehavior,
            )
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
            val tabModifier = Modifier.fillMaxSize()
            when (currentTabIndex) {
                0 -> DiaryListTab(
                    diaries = diaries,
                    labels = labels,
                    labelOptions = labelOptions,
                    onCheckLabel = onCheckLabel,
                    onDiaryClick = onDiaryClick,
                    onDiaryEdit = onDiaryEdit,
                    onDeleteDiary = onDeleteDiary,
                    onShareDiary = onShareDiary,
                    sortOption = sortOption,
                    onChangeSort = onChangeSort,
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
    scrollBehavior: TopAppBarScrollBehavior,
    currentTabIndex: Int,
    onClickTab: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf(stringResource(R.string.home_tab_dream), stringResource(R.string.home_tab_calendar))

    Column(modifier = modifier) {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(R.string.home_my_dream)) },
            actions = {
                IconButton(onClick = onNotificationClick) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = stringResource(R.string.home_alarm_description),
                    )
                }
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = stringResource(R.string.home_search_description),
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )
        PrimaryTabRow(
            selectedTabIndex = currentTabIndex,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(
                        selectedTabIndex = currentTabIndex,
                    ),
                )
            },
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = currentTabIndex == index,
                    onClick = { onClickTab(index) },
                    text = { Text(title) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun DiaryHomeScreenContentPreview() {
    DreamdiaryTheme {
        DiaryHomeScreenContent(
            diaries = pagedDiariesPreview.collectAsLazyPagingItems(),
            labels = listOf(),
            labelOptions = setOf(),
            onCheckLabel = {},
            calendarUIState = diaryHomeTabCalendarUIStatePreview,
            onCalendarYearMothChange = { },
            onNavigateToWriteScreen = {},
            onNavigateToCommunity = {},
            onNavigateToSetting = {},
            onDiaryClick = {},
            onDiaryEdit = {},
            onDeleteDiary = {},
            onShareDiary = {},
            onChangeSort = {},
            onSearchClick = {},
            onNotificationClick = {},
            sortOption = DiarySort(
                DiarySortType.UPDATED,
                DiarySortOrder.DESC,
            ),
        )
    }
}
