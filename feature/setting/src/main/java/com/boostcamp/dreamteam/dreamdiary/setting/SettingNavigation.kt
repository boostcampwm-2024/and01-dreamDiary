package com.boostcamp.dreamteam.dreamdiary.setting

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SettingRoute

fun NavController.navigateToSettingScreen(navOptions: NavOptions? = null) {
    this.navigate(route = SettingRoute, navOptions)
}

fun NavGraphBuilder.settingScreen(
    onHomeClick: () -> Unit,
    onCommunityClick: () -> Unit,
) {
    composable<SettingRoute> {
        SettingScreen(
            onNavigateToWriteScreen = onHomeClick,
            onNavigateToCommunity = onCommunityClick,
        )
    }
}
