package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.navigateToDiaryWriteScreen
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavItem
import kotlinx.serialization.Serializable

@Serializable
data object DiaryHomeRoute

fun NavController.navigateToDiaryHomeScreen(navOptions: NavOptions) = navigate(route = DiaryHomeRoute, navOptions)

fun NavGraphBuilder.diaryHomeScreen(
    navController: NavHostController,
    onDiaryClick: (DiaryUi) -> Unit,
) {
    composable<DiaryHomeRoute> {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route ?: ""

        DiaryHomeScreen(
            onDiaryClick = onDiaryClick,
            onNavigateToWriteScreen = {
                navController.navigateToDiaryWriteScreen()
            },
            onNavigateToCommunity = {
                navController.navigate(HomeBottomNavItem.Community.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            },
            onNavigateToSetting = {
                navController.navigate(HomeBottomNavItem.Setting.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            },
            currentRoute = currentRoute,
        )
    }
}
