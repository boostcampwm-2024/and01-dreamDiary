package com.boostcamp.dreamteam.dreamdiary.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.DiaryHomeScreen

@Composable
internal fun HomeScreen() {
    val navController: NavHostController = rememberNavController()

    Scaffold(
        bottomBar = { HomeBottomNavigation(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.MY_DREAM.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.MY_DREAM.route) {
                DiaryHomeScreen(
                    onDiaryClick = {},
                    onFabClick = {},
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
                }
            )
        }
    }
}
