package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.DiaryCalendarTab
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.DiaryHomeTabCalendarUIState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.diaryHomeTabCalendarUIStatePreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist.DiaryHomeTabListUIState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist.DiaryListTab
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist.diaryHomeTabListUIStatePreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.DiaryWriteRoute
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavItem
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavigation
import com.boostcamp.dreamteam.dreamdiary.ui.NavigationItem

@Composable
fun DiaryHomeScreen(
    onDiaryClick: (DiaryUi) -> Unit,
    navController: NavHostController,
    viewModel: DiaryHomeViewModel = hiltViewModel(),
) {
    val listUIState by viewModel.tabListUIState.collectAsStateWithLifecycle()
    val calendarUIState by viewModel.tabCalendarUiState.collectAsStateWithLifecycle()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val navigationItems = listOf(
        NavigationItem(
            route = HomeBottomNavItem.MyDream.route,
            icon = HomeBottomNavItem.MyDream.icon,
            labelRes = HomeBottomNavItem.MyDream.label,
            isSelected = currentRoute == HomeBottomNavItem.MyDream.route,
            onClick = {
                navController.navigate(HomeBottomNavItem.MyDream.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            },
        ),
        NavigationItem(
            route = HomeBottomNavItem.Community.route,
            icon = HomeBottomNavItem.Community.icon,
            labelRes = HomeBottomNavItem.Community.label,
            isSelected = currentRoute == HomeBottomNavItem.Community.route,
            onClick = {
                navController.navigate(HomeBottomNavItem.Community.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            },
        ),
        NavigationItem(
            route = HomeBottomNavItem.Setting.route,
            icon = HomeBottomNavItem.Setting.icon,
            labelRes = HomeBottomNavItem.Setting.label,
            isSelected = currentRoute == HomeBottomNavItem.Setting.route,
            onClick = {
                navController.navigate(HomeBottomNavItem.Setting.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            },
        ),
    )

    Scaffold(
        topBar = {
            DiaryHomeScreenTopAppBar(
                onMenuClick = { /* 메뉴 클릭 시 동작 */ },
                onNotificationClick = { /* 알림 클릭 시 동작 */ },
                onSearchClick = { /* 검색 클릭 시 동작 */ },
            )
        },
        bottomBar = {
            // TODO: BottomBar
            HomeBottomNavigation(items = navigationItems)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(DiaryWriteRoute.ROUTE)
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = "일기 작성",
                )
            }
        },
    ) { innerPadding ->
        DiaryHomeScreenContent(
            listUIState = listUIState,
            calendarUIState = calendarUIState,
            onDiaryClick = onDiaryClick,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiaryHomeScreenContent(
    listUIState: DiaryHomeTabListUIState,
    calendarUIState: DiaryHomeTabCalendarUIState,
    modifier: Modifier = Modifier,
    onDiaryClick: (DiaryUi) -> Unit = {},
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("일기", "달력")

    Column(
        modifier = modifier.fillMaxSize(),
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
                uiState = listUIState,
                modifier = tabModifier,
                onDiaryClick = onDiaryClick,
            )

            1 -> DiaryCalendarTab(
                modifier = tabModifier,
                state = calendarUIState,
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DiaryHomeScreenTopAppBar(
    onMenuClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text("나의 일기") },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onMenuClick,
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "메뉴 열기",
                )
            }
        },
        actions = {
            IconButton(
                onClick = onNotificationClick,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "알림",
                )
            }
            IconButton(
                onClick = onSearchClick,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "검색",
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
            listUIState = diaryHomeTabListUIStatePreview,
            calendarUIState = diaryHomeTabCalendarUIStatePreview,
        )
    }
}
