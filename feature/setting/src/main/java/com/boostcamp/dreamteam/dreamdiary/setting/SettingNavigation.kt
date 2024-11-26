package com.boostcamp.dreamteam.dreamdiary.setting

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object SettingGraph {
    @Serializable
    data object SettingRoute

    @Serializable
    data object SettingNotificationRoute

    @Serializable
    data object SettingBackupRoute

    @Serializable
    data class SettingDetailRoute(val id: Long)
}

fun NavGraphBuilder.settingGraph(
    navController: NavHostController,
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
                onNavigateToSettingNotification = {
                    navController.navigate(
                        SettingGraph.SettingNotificationRoute,
                        navOptions = navOptions {
                            launchSingleTop = true
                        },
                    )
                },
                onNavigateToSettingBackup = {
                    navController.navigate(
                        SettingGraph.SettingBackupRoute,
                        navOptions = navOptions {
                            launchSingleTop = true
                        },
                    )
                },
                onLogoutClick = onLogoutClick,
            )
        }
        composable<SettingGraph.SettingNotificationRoute> {
            SettingNotificationScreen(
                onBackClick = navController::navigateUp,
            )
        }
        composable<SettingGraph.SettingBackupRoute> {
            SettingBackupScreen(
                onBackClick = navController::navigateUp,
            )
        }
    }
}
