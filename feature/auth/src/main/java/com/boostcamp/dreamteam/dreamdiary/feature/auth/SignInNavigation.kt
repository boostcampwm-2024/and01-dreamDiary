package com.boostcamp.dreamteam.dreamdiary.feature.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SignInRoute

fun NavController.navigateToLoginScreen(navOptions: NavOptions) = navigate(route = SignInRoute, navOptions)

fun NavGraphBuilder.signInScreen(
    onSignInSuccess: () -> Unit,
    onPassClick: () -> Unit,
) {
    composable<SignInRoute> {
        SignInScreen(onSignInSuccess, onPassClick)
    }
}
