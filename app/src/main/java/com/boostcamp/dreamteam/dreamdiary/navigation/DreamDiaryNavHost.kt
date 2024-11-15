package com.boostcamp.dreamteam.dreamdiary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.boostcamp.dreamteam.dreamdiary.feature.auth.SignInRoute
import com.boostcamp.dreamteam.dreamdiary.feature.auth.signInScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.DiaryDetailNavigation
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.diaryDetailScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.navigateToDiaryDetailScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.diaryHomeScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.navigateToDiaryHomeScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.diaryWriteScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.navigateToDiaryWriteScreen
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
                navController.navigateToDiaryHomeScreen(
                    navOptions = navOptions {
                        launchSingleTop = true
                    },
                )
            },
            onPassClick = {
                navController.navigateToDiaryHomeScreen(
                    navOptions = navOptions {
                        launchSingleTop = true
                    },
                )
            },
        )

        diaryHomeScreen(
            onFabClick = {
                navController.navigateToDiaryWriteScreen()
            },
            onCommunityClick = {
//                navController.navigateToCommunityScreen()
            },
            onSettingClick = {
//                navController.navigateToSettingScreen()
            },
            onDiaryItemClick = { diaryUI ->
                navController.navigateToDiaryDetailScreen(
                    route = DiaryDetailNavigation(diaryUI.id),
                    navOptions = navOptions {
                        launchSingleTop = true
                    },
                )
            },
        )

        diaryWriteScreen(
            onBackClick = navController::navigateUp,
        )

        diaryDetailScreen(
            onBackClick = navController::navigateUp,
        )
    }
}
