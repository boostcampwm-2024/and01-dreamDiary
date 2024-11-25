package com.boostcamp.dreamteam.dreamdiary.setting

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object SettingGraph {
    @Serializable
    data object SettingRoute

    @Serializable
    data object SettingBackupRoute

    @Serializable
    data class SettingDetailRoute(val id: Long)
}

fun NavGraphBuilder.settingGraph(
    onDiaryClick: () -> Unit,
    onCommunityClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    navigation<SettingGraph>(
        startDestination = SettingGraph.SettingRoute,
    ) {
        composable<SettingGraph.SettingRoute> {
            SettingScreen(
                onNavigateToDiary = onDiaryClick,
                onNavigateToCommunity = onCommunityClick,
                onLogoutClick = onLogoutClick,
            )
        }
    }
}
