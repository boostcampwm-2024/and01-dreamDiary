package com.boostcamp.dreamteam.dreamdiary.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.DiaryHomeScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.DiaryWriteRoute
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.DiaryWriteScreen

const val HOME_ROUTE = "home_route"

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    modifier: Modifier,
) {
    NavHost(
        navController = navController,
        route = HOME_ROUTE,
        startDestination = BottomNavItem.MyDream.route,
        modifier = modifier,
    ) {
        composable(route = BottomNavItem.MyDream.route) {
            DiaryHomeScreen(
                onDiaryClick = { diaryUi -> },
            )
        }
        composable(route = BottomNavItem.Community.route) {
            // CommunityScreen()
        }
        composable(route = BottomNavItem.Setting.route) {
            // SettingScreen()
        }

        composable<DiaryWriteRoute> {
            DiaryWriteScreen {
                navController.navigateUp()
            }
        }

        // 여기에 다른 composable
    }
}
