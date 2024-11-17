package com.boostcamp.dreamteam.dreamdiary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.boostcamp.dreamteam.dreamdiary.community.communityGraph
import com.boostcamp.dreamteam.dreamdiary.community.navigateToCommunityScreen
import com.boostcamp.dreamteam.dreamdiary.feature.auth.SignInRoute
import com.boostcamp.dreamteam.dreamdiary.feature.auth.signInScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.DiaryDetailRoute
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.diaryDetailScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.navigateToDiaryDetailScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.DiaryHomeRoute
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.diaryHomeScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.navigateToDiaryHomeScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.diaryWriteScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.navigateToDiaryWriteScreen
import com.boostcamp.dreamteam.dreamdiary.setting.navigateToSettingScreen
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
                navController.navigateToCommunityScreen(
                    navOptions = navOptions {
                        launchSingleTop = true
                    },
                )
            },
            onSettingClick = {
                navController.navigateToSettingScreen()
            },
            onDiaryItemClick = { diaryUI ->
                navController.navigateToDiaryDetailScreen(
                    route = DiaryDetailRoute(diaryUI.id),
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

        settingGraph(
            onDiaryHomeClick = {
                navController.navigateToDiaryHomeScreen(
                    navOptions = navOptions {
                        launchSingleTop = true
                    },
                )
            },
            onCommunityClick = {
                navController.navigateToCommunityScreen(
                    navOptions = navOptions {
                        launchSingleTop = true
                    },
                )
            },
        )

        communityGraph(
            onDiaryClick = {
                navController.navigateToDiaryHomeScreen(
                    navOptions = navOptions {
                        popUpTo(DiaryHomeRoute) {
                            saveState = true
                        }
                    }
                )
            },
            onCommunityClick = {
                navController.navigateToSettingScreen()
            },
        )
    }
}
