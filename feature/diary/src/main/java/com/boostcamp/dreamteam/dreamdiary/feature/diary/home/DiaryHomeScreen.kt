package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.checkSelfPermission
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
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavItem
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavigation
import com.boostcamp.dreamteam.dreamdiary.ui.NavigationItem
import java.time.YearMonth

@Composable
fun DiaryHomeScreen(
    onDiaryClick: (DiaryUi) -> Unit,
    onDiaryEdit: (DiaryUi) -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToSetting: () -> Unit,
    viewModel: DiaryHomeViewModel = hiltViewModel(),
    onNavigateToWriteScreen: () -> Unit,
) {
    val diaries = viewModel.dreamDiaries.collectAsLazyPagingItems()
    val calendarUIState by viewModel.tabCalendarUiState.collectAsStateWithLifecycle()
    val labels by viewModel.dreamLabels.collectAsStateWithLifecycle()
    val labelOptions by viewModel.labelOptions.collectAsStateWithLifecycle()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (checkSelfPermission(LocalContext.current, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(LocalContext.current as Activity, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
        }
    }

    sendLocalNotification(LocalContext.current, "test", "hi")
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
        onDeleteDiary = { /* TODO: diary 삭제 기능 구현 */ },
        onDiaryClick = onDiaryClick,
        onDiaryEdit = onDiaryEdit,
    )
}

fun sendLocalNotification(
    context: Context,
    title: String,
    message: String,
) {
    val channelId = "local_notification_channel"
    val notificationId = 1001
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelName = "Local Notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = "Channel for local notifications"
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()
    if (checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }
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
    modifier: Modifier = Modifier,
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
        modifier = modifier,
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
                    labels = labels,
                    labelOptions = labelOptions,
                    onCheckLabel = onCheckLabel,
                    onDiaryClick = onDiaryClick,
                    onDiaryEdit = onDiaryEdit,
                    onDeleteDiary = onDeleteDiary,
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
            labels = listOf(),
            labelOptions = setOf(),
            onCheckLabel = {},
            calendarUIState = diaryHomeTabCalendarUIStatePreview,
            onCalendarYearMothChange = { },
            onNavigateToWriteScreen = {},
            onNavigateToCommunity = {},
            onNavigateToSetting = {},
            onDiaryClick = {},
            onDeleteDiary = {},
            onSearchClick = {},
            onNotificationClick = {},
            onDiaryEdit = {},
        )
    }
}
