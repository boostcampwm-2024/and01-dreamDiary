package com.boostcamp.dreamteam.dreamdiary.community

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object CommunityGraph {
    @Serializable
    data object CommunityRoute

    @Serializable
    data class CommunityDetailRoute(val id: Long)
}

fun NavController.navigateToCommunityScreen(navOptions: NavOptions? = null) {
    this.navigate(route = CommunityGraph, navOptions)
}

fun NavGraphBuilder.communityGraph(
    onDiaryClick: () -> Unit,
    onSettingClick: () -> Unit,
) {
    navigation<CommunityGraph>(
        startDestination = CommunityGraph.CommunityRoute,
    ) {
        composable<CommunityGraph.CommunityRoute> {
            CommunityScreen(
                onNavigateToDiary = onDiaryClick,
                onNavigateToSetting = onSettingClick,
            )
        }

//        composable<Route.CommunityGraph.CommunityDetailRoute> { backStackEntry ->
//            val id = backStackEntry.arguments?.getLong("id") ?: 0
//            CommunityDetailScreen(id = id)
//        }
    }
}
