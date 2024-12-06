package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Sync
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.boostcamp.dreamteam.dreamdiary.core.synchronization.SyncWorkState
import com.boostcamp.dreamteam.dreamdiary.core.synchronization.SynchronizationWorker
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.model.SyncStateUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.DiaryCalendarTab
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.DiaryHomeTabCalendarUIState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.diaryHomeTabCalendarUIStatePreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist.DiaryListTab
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist.pagedDiariesPreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavItem
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavigation
import com.boostcamp.dreamteam.dreamdiary.ui.component.GoToSignInDialog
import com.boostcamp.dreamteam.dreamdiary.ui.toNavigationItem
import kotlinx.coroutines.flow.map
import java.time.YearMonth

@Composable
fun DiaryHomeScreen(
    onDiaryClick: (DiaryUi) -> Unit,
    onDiaryEdit: (DiaryUi) -> Unit,
    onShareDiary: (diaryId: String) -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onDialogConfirmClick: () -> Unit,
    onClickSearch: () -> Unit,
    updateDiaryWidget: (Context) -> Unit,
    viewModel: DiaryHomeViewModel = hiltViewModel(),
    onNavigateToWriteScreen: () -> Unit,
) {
    val context = LocalContext.current

    val diaries = viewModel.dreamDiaries.collectAsLazyPagingItems()
    val calendarUIState by viewModel.tabCalendarUiState.collectAsStateWithLifecycle()
    val labels by viewModel.dreamLabels.collectAsStateWithLifecycle()
    val labelOptions by viewModel.labelOptions.collectAsStateWithLifecycle()
    val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    val syncState by remember(context) {
        SynchronizationWorker.getWorkInfo(context).map {
            when (it) {
                SyncWorkState.RUNNING -> {
                    SyncStateUi.RUNNING
                }

                SyncWorkState.IDLE -> {
                    SyncStateUi.IDLE
                }
            }
        }
    }.collectAsStateWithLifecycle(SyncStateUi.IDLE)

    LaunchedEffect(updateDiaryWidget) {
        viewModel.event.collect {
            when (it) {
                is DiaryHomeEvent.Delete.Success -> {
                    updateDiaryWidget(context)
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
        onSearchClick = onClickSearch,
        syncState = syncState,
        onSyncClick = { SynchronizationWorker.runSynchronizationWorker(context) },
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
    syncState: SyncStateUi,
    onSyncClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit,
) {
    val tabs = listOf(stringResource(R.string.home_tab_dream), stringResource(R.string.home_tab_calendar))

    val (currentTabIndex, setCurrentTabIndex) = rememberSaveable { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { tabs.size }
    LaunchedEffect(currentTabIndex) {
        pagerState.animateScrollToPage(currentTabIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        setCurrentTabIndex(pagerState.currentPage)
    }

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
                onSyncClick = onSyncClick,
                onSearchClick = onSearchClick,
                scrollBehavior = topAppBarScrollBehavior,
                tabs = tabs,
                currentTabIndex = currentTabIndex,
                onClickTab = setCurrentTabIndex,
                syncState = syncState,
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
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) { index ->
            val tabModifier = Modifier.fillMaxSize()
            when (index) {
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
    syncState: SyncStateUi,
    onSyncClick: () -> Unit,
    onSearchClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    tabs: List<String>,
    currentTabIndex: Int,
    onClickTab: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent,
            ),
            title = { Text(stringResource(R.string.home_my_dream)) },
            actions = {
                DiarySyncIconButton(
                    syncState = syncState,
                    onSyncClick = onSyncClick,
                )
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

@Composable
private fun DiarySyncIconButton(
    syncState: SyncStateUi,
    onSyncClick: () -> Unit,
) {
    val rotationTarget = remember(syncState) {
        if (syncState == SyncStateUi.RUNNING) 360f else 0f
    }

    val currentRotation by animateFloatAsState(
        targetValue = rotationTarget,
        animationSpec = if (syncState == SyncStateUi.RUNNING) {
            infiniteRepeatable(
                animation = tween(durationMillis = 500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            )
        } else {
            tween(durationMillis = 500, easing = LinearEasing)
        },
        label = "SyncButtonRotation",
    )

    IconButton(
        onClick = onSyncClick,
    ) {
        Icon(
            modifier = Modifier.graphicsLayer {
                rotationZ = 360f - currentRotation
            },
            imageVector = Icons.Outlined.Sync,
            contentDescription = stringResource(R.string.home_alarm_description),
        )
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
            sortOption = DiarySort(
                DiarySortType.UPDATED,
                DiarySortOrder.DESC,
            ),
            syncState = SyncStateUi.IDLE,
            onSyncClick = {},
        )
    }
}
