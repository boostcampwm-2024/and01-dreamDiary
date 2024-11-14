package com.boostcamp.dreamteam.dreamdiary.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.navigateToDiaryWriteScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = { HomeBottomNavigation(navController = navController) },
        topBar = {
            when (currentRoute) {
                BottomNavItem.MyDream.route -> {
                    DiaryHomeScreenTopAppBar(
                        onNotificationClick = { /* TODO */ },
                        onSearchClick = { /* TODO */ },
                    )
                }

                BottomNavItem.Community.route -> {
                    // Todo
                    TopAppBar(title = { Text("커뮤니티") })
                }

                BottomNavItem.Setting.route -> {
                    // Todo
                    TopAppBar(title = { Text("설정") })
                }
            }
        },
        floatingActionButton = {
            if (currentRoute == BottomNavItem.MyDream.route) {
                FloatingActionButton(
                    onClick = {
                        navController.navigateToDiaryWriteScreen(
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
        HomeNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DiaryHomeScreenTopAppBar(
    onNotificationClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text("나의 일기") },
        modifier = modifier,
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
    val screens = listOf(
        BottomNavItem.MyDream,
        BottomNavItem.Community,
        BottomNavItem.Setting,
    )

    val navBarStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBarStackEntry?.destination

    BottomAppBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.route,
                        tint = if (currentDestination?.route == screen.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    )
                },
                label = { Text(stringResource(screen.label)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}
