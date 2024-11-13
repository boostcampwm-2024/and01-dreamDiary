package com.boostcamp.dreamteam.dreamdiary.ui

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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.DiaryHomeScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.navigateToDiaryWriteScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(rootNavController: NavHostController) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = { HomeBottomNavigation(navController) },
        topBar = {
            when (currentRoute) {
                BottomNavItem.MY_DREAM.route -> {
                    DiaryHomeScreenTopAppBar(
                        onMenuClick = { /* TODO */ },
                        onNotificationClick = { /* TODO */ },
                        onSearchClick = { /* TODO */ },
                    )
                }

                BottomNavItem.COMMUNITY.route -> {
                    // Todo
                    TopAppBar(title = { Text("커뮤니티") })
                }

                BottomNavItem.SETTINGS.route -> {
                    // Todo
                    TopAppBar(title = { Text("설정") })
                }
            }
        },
        floatingActionButton = {
            if (currentRoute == BottomNavItem.MY_DREAM.route) {
                FloatingActionButton(
                    onClick = {
                        rootNavController.navigateToDiaryWriteScreen(
                            navOptions = navOptions {
                                launchSingleTop = true
                            },
                        )
                    },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Create,
                        contentDescription = "일기 작성",
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.MY_DREAM.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(BottomNavItem.MY_DREAM.route) {
                DiaryHomeScreen(
                    onDiaryClick = { diaryUi ->
                    },
                )
            }
            composable(BottomNavItem.COMMUNITY.route) {
//                CommunityScreen()
            }
            composable(BottomNavItem.SETTINGS.route) {
//                SettingsScreen()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DiaryHomeScreenTopAppBar(
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

@Composable
private fun HomeBottomNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val currentDestination by navController.currentBackStackEntryAsState()
    NavigationBar {
        BottomNavItem.entries.forEach { item ->
            val selected = currentDestination?.destination?.route == item.route
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(stringResource(item.label)) },
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                },
            )
        }
    }
}
