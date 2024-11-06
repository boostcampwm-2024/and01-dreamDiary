package com.boostcamp.dreamteam.dreamdiary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.DiaryHomeRoute
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.diaryHomeScreen
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
        startDestination = DiaryHomeRoute,
        modifier = modifier,
    ) {
        diaryHomeScreen(
            onDiaryClick = {
            },
            onFabClick = {
                navController.navigateToDiaryWriteScreen(
                    navOptions = navOptions {
                        launchSingleTop = true
                    },
                )
            },
        )

        diaryWriteScreen(
            onBackClick = navController::navigateUp,
        )
    }
}
