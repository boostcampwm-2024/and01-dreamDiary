package com.boostcamp.dreamteam.dreamdiary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import com.boostcamp.dreamteam.dreamdiary.community.CommunityGraph
import com.boostcamp.dreamteam.dreamdiary.community.communityGraph
import com.boostcamp.dreamteam.dreamdiary.feature.auth.SignInRoute
import com.boostcamp.dreamteam.dreamdiary.feature.auth.signInScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.DiaryGraph
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.diaryGraph
import com.boostcamp.dreamteam.dreamdiary.setting.SettingGraph
import com.boostcamp.dreamteam.dreamdiary.setting.settingGraph
import com.boostcamp.dreamteam.dreamdiary.ui.DreamDiaryAppState

@Composable
fun DreamDiaryNavHost(
    appState: DreamDiaryAppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = SignInRoute,
        modifier = modifier,
    ) {
        signInScreen(
            onSignInSuccess = {
                val options = NavOptions.Builder()
                    .setPopUpTo(SignInRoute, inclusive = true)
                    .setLaunchSingleTop(true)
                    .build()
                navController.navigate(DiaryGraph, options)
            },
            onPassClick = {
                val options = NavOptions.Builder()
                    .setPopUpTo(SignInRoute, inclusive = true)
                    .setLaunchSingleTop(true)
                    .build()
                navController.navigate(DiaryGraph, options)
            },
        )

        diaryGraph(
            onCommunityClick = {
                val options = NavOptions.Builder()
                    .setPopUpTo(DiaryGraph, inclusive = false, saveState = true)
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .build()
                navController.navigate(CommunityGraph, options)
            },
            onSettingClick = {
                val options = NavOptions.Builder()
                    .setPopUpTo(DiaryGraph, inclusive = false, saveState = true)
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .build()
                navController.navigate(SettingGraph, options)
            },
            navController = navController,
        )

        communityGraph(
            onDiaryClick = {
                val options = NavOptions.Builder()
                    .setPopUpTo(CommunityGraph, inclusive = false, saveState = true)
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .build()
                navController.navigate(DiaryGraph, options)
            },
            onSettingClick = {
                val options = NavOptions.Builder()
                    .setPopUpTo(CommunityGraph, inclusive = false, saveState = true)
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .build()
                navController.navigate(SettingGraph, options)
            },
        )

        settingGraph(
            onDiaryClick = {
                val options = NavOptions.Builder()
                    .setPopUpTo(SettingGraph, inclusive = false, saveState = true)
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .build()
                navController.navigate(DiaryGraph, options)
            },
            onCommunityClick = {
                val options = NavOptions.Builder()
                    .setPopUpTo(SettingGraph, inclusive = false, saveState = true)
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .build()
                navController.navigate(CommunityGraph, options)
            },
            onLogoutClick = {
                val options = NavOptions.Builder()
                    .setPopUpTo(SettingGraph, inclusive = true)
                    .build()
                navController.navigate(SignInRoute, options)
            },
        )
//        composable<MainRoute> {
//            MainScreen()
//        }
    }
}
