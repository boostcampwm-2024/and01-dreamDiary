package com.boostcamp.dreamteam.dreamdiary.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation

// data object?
const val HOME_ROUTE = "home_route"

fun NavController.navigateToHomeScreen(navOptions: NavOptions) = navigate(route = HOME_ROUTE, navOptions)

fun NavGraphBuilder.homeNavGraph() {
    navigation(
        startDestination = BottomNavItem.MY_DREAM.route,
        route = HOME_ROUTE
    ) {
        composable(BottomNavItem.MY_DREAM.route) {
            HomeScreen()
        }
        composable(BottomNavItem.COMMUNITY.route) {
            HomeScreen()
//            CommunityScreen()
        }
        composable(BottomNavItem.SETTINGS.route) {
            HomeScreen()
//            SettingsScreen()
        }
    }
}
