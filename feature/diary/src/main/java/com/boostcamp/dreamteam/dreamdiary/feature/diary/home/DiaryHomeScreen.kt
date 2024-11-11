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
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tabcalendar.DiaryCalendarTab
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist.DiaryListTab
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist.diariesPreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.models.DiaryUi

@Composable
fun DiaryHomeScreen(
    onDiaryClick: (DiaryUi) -> Unit,
    onFabClick: () -> Unit,
    viewModel: DiaryHomeViewModel = hiltViewModel(),
) {
    val tabListUIState by viewModel.tabListUIState.collectAsStateWithLifecycle()
    val diaries = tabListUIState.diaries
    DiaryHomeScreenContent(
        diaries = diaries,
        onMenuClick = { /*TODO*/ },
        onSearchClick = { /*TODO*/ },
        onNotificationClick = { /*TODO*/ },
        onDiaryClick = onDiaryClick,
        onFabClick = onFabClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiaryHomeScreenContent(
    diaries: List<DiaryUi>,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onDiaryClick: (DiaryUi) -> Unit = {},
    onFabClick: () -> Unit = {},
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("일기", "달력")

    Scaffold(
        modifier = modifier,
        topBar = {
            DiaryHomeScreenTopAppBar(
                onMenuClick = onMenuClick,
                onNotificationClick = onNotificationClick,
                onSearchClick = onSearchClick,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = "일기 작성",
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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

            when (selectedTabIndex) {
                0 -> DiaryListTab(
                    diaries = diaries,
                    modifier = Modifier.fillMaxSize(),
                    onDiaryClick = onDiaryClick,
                )

                1 -> DiaryCalendarTab()
            }
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
            diaries = diariesPreview,
        )
    }
}
