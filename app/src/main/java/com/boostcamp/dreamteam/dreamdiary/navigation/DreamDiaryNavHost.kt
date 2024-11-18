package com.boostcamp.dreamteam.dreamdiary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.boostcamp.dreamteam.dreamdiary.MainRoute
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
                val options = NavOptions.Builder()
                    .setPopUpTo(SignInRoute, inclusive = true)
                    .setLaunchSingleTop(true)
                    .build()
                navController.navigate(MainRoute, options)
            },
            onPassClick = {
                val options = NavOptions.Builder()
                    .setPopUpTo(SignInRoute, inclusive = true)
                    .setLaunchSingleTop(true)
                    .build()
                navController.navigate(MainRoute, options)
            },
        )

        composable(MainRoute) {
            MainScreen()
        }
    }
}
