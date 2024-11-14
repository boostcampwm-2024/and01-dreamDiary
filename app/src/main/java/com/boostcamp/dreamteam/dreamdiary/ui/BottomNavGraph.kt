package com.boostcamp.dreamteam.dreamdiary.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.DiaryHomeScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.MyDream.route,
    ) {
        composable(BottomNavItem.MyDream.route) {
            DiaryHomeScreen(
                onDiaryClick = { },
            )
        }
        composable(BottomNavItem.Setting.route) {
//            SettingScreen()
        }
        composable(BottomNavItem.Community.route) {
//            CommunityScreen()
        }
    }
}
