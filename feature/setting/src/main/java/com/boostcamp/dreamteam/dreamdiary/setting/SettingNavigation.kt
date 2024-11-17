package com.boostcamp.dreamteam.dreamdiary.setting

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object SettingGraph {
    @Serializable
    data object SettingRoute

    // 추후 수정
    @Serializable
    data class SettingDetailRoute(val id: Long)
}

fun NavController.navigateToSettingScreen(navOptions: NavOptions? = null) {
    this.navigate(route = SettingGraph, navOptions)
}

fun NavGraphBuilder.settingGraph(
    onDiaryHomeClick: () -> Unit,
    onCommunityClick: () -> Unit,
) {
    navigation<SettingGraph>(
        startDestination = SettingGraph.SettingRoute,
    ) {
        composable<SettingGraph.SettingRoute> {
            SettingScreen(
                onNavigateToDiary = onDiaryHomeClick,
                onNavigateToCommunity = onCommunityClick,
            )
        }
    }
}
