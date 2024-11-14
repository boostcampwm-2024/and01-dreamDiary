package com.boostcamp.dreamteam.dreamdiary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.boostcamp.dreamteam.dreamdiary.feature.auth.SignInRoute
import com.boostcamp.dreamteam.dreamdiary.feature.auth.signInScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.navigateToDiaryHomeScreen
import com.boostcamp.dreamteam.dreamdiary.ui.DreamDiaryAppState
import com.boostcamp.dreamteam.dreamdiary.ui.HOME_ROUTE
import com.boostcamp.dreamteam.dreamdiary.ui.HomeScreen

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
            onNotSignInClick = {
//                navController.navigateToHomeScreen(
//                    navOptions = navOptions {
//                        launchSingleTop = true
//                    },
//                )
                navController.navigate(
                    route = HOME_ROUTE,
                    navOptions = navOptions {
                        popUpTo(SignInRoute) { inclusive = true }
                    },
                )
            },
        )

//        homeNavGraph(navController)
        composable(route = HOME_ROUTE) {
            HomeScreen()
        }
//        diaryWriteScreen(
//            onBackClick = {
//                navController.popBackStack()
//            },
//        )
    }
}
