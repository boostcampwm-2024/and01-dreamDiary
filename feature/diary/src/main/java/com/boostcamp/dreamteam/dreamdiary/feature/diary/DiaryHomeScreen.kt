package com.boostcamp.dreamteam.dreamdiary.feature.diary

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
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
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.boostcamp.dreamteam.dreamdiary.feature.diary.components.DiaryListTab
import com.boostcamp.dreamteam.dreamdiary.feature.diary.components.diariesPreview

@Composable
fun DiaryHomeScreen() {
    DiaryHomeScreenContent(
        diaries = emptyList(), // FIXME: viewmodel에서 다이어리 불러오기
        onMenuClick = { /*TODO*/ },
        onSearchClick = { /*TODO*/ },
        onNotificationClick = { /*TODO*/ },
        onDiaryClick = { /*TODO: 상세 화면으로 이동*/ },
        onFabClick = { /*TODO: 새 일기 작성*/ },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiaryHomeScreenContent(
    diaries: List<Diary>,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onDiaryClick: (Diary) -> Unit = {},
    onFabClick: () -> Unit = {},
) {
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
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        val tabs = listOf("일기", "달력")
        Column(modifier = Modifier.padding(innerPadding)) {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                indicator = { TabRowDefaults.SecondaryIndicator() },
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
                0 -> DiaryListTab(diaries = diaries, onDiaryClick = onDiaryClick)
                1 -> TODO("Not yet implemented")
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DiaryHomeScreenTopAppBar(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSearchClick: () -> Unit,
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
    // TODO: 테마 적용
    MaterialTheme {
        DiaryHomeScreenContent(
            diaries = diariesPreview,
        )
    }
}
