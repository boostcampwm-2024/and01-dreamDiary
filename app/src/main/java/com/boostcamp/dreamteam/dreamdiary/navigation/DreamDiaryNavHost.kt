package com.boostcamp.dreamteam.dreamdiary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.boostcamp.dreamteam.dreamdiary.feature.auth.SignInRoute
import com.boostcamp.dreamteam.dreamdiary.feature.auth.signInScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.DiaryHomeRoute
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.DiaryHomeScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.navigateToDiaryHomeScreen
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
//                navController.navigateToDiaryHomeScreen(
//                    navOptions = navOptions {
//                        launchSingleTop = true
//                    },
//                )
            },
            onNotSignInClick = {
//                navController.navigateToHomeScreen(
//                    navOptions = navOptions {
//                        launchSingleTop = true
//                    },
//                )
                navController.navigateToDiaryHomeScreen(
                    navOptions = navOptions {
                        popUpTo(SignInRoute) { inclusive = true }
                    },
                )
            },
        )

        composable<DiaryHomeRoute> {
            DiaryHomeScreen(
                onDiaryClick = {},
                navController = navController,
            )
        }
    }
}
