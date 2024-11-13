package com.boostcamp.dreamteam.dreamdiary.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation

const val HOME_ROUTE = "home_route"

fun NavController.navigateToHomeScreen(navOptions: NavOptions) = navigate(route = HOME_ROUTE, navOptions)

fun NavGraphBuilder.homeNavGraph(rootNavController: NavHostController) {
    navigation(
        startDestination = BottomNavItem.MY_DREAM.route,
        route = HOME_ROUTE,
    ) {
        composable(BottomNavItem.MY_DREAM.route) {
            HomeScreen(rootNavController = rootNavController)
        }
    }
}
