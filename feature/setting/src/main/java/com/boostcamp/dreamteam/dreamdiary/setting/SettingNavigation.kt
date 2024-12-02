package com.boostcamp.dreamteam.dreamdiary.setting

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.boostcamp.dreamteam.dreamdiary.setting.theme.SettingThemeScreen
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

    @Serializable
    data object SettingThemeRoute
}

fun NavGraphBuilder.settingGraph(
    navController: NavHostController,
    onDiaryClick: () -> Unit,
    onCommunityClick: () -> Unit,
    onGoToSignInClick: () -> Unit,
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
                onNavigateToSettingTheme = {
                    navController.navigate(
                        SettingGraph.SettingThemeRoute,
                        navOptions = navOptions {
                            launchSingleTop = true
                        },
                    )
                },
                onGoToSignInClick = onGoToSignInClick,
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
        composable<SettingGraph.SettingThemeRoute> {
            SettingThemeScreen(
                onBackClick = navController::navigateUp,
            )
        }
    }
}
