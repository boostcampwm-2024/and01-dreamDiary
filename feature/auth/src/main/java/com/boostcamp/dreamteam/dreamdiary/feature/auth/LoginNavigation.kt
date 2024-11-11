package com.boostcamp.dreamteam.dreamdiary.feature.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute

fun NavController.navigateToLoginScreen(navOptions: NavOptions) = navigate(route = LoginRoute, navOptions)

fun NavGraphBuilder.loginScreen(navigateToDiaryHomeScreen: () -> Unit) {
    composable<LoginRoute> {
        LoginScreen(navigateToDiaryHomeScreen)
    }
}
