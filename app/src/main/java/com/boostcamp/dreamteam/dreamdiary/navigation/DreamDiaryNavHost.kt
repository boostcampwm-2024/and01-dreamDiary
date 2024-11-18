package com.boostcamp.dreamteam.dreamdiary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.boostcamp.dreamteam.dreamdiary.MainScreen
import com.boostcamp.dreamteam.dreamdiary.feature.auth.SignInRoute
import com.boostcamp.dreamteam.dreamdiary.feature.auth.signInScreen
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
                navController.navigate("main_route") {
                    popUpTo(SignInRoute) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onPassClick = {
                navController.navigate("main_route") {
                    popUpTo(SignInRoute) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
        )

        composable("main_route") {
            MainScreen()
        }
    }
}
