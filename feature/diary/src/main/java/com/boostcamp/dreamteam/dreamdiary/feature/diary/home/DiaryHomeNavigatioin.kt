package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.DiaryDetailScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.DiaryWriteScreen
import kotlinx.serialization.Serializable

@Serializable
data object DiaryGraph {
    @Serializable
    data object DiaryHomeRoute

    @Serializable
    data class DiaryWriteRoute(
        val diaryId: String? = null,
    )

    @Serializable
    data class DiaryDetailRoute(
        val id: String,
    )
}

fun NavGraphBuilder.diaryGraph(
    onCommunityClick: () -> Unit,
    onSettingClick: () -> Unit,
    navController: NavController,
) {
    navigation<DiaryGraph>(
        startDestination = DiaryGraph.DiaryHomeRoute,
    ) {
        composable<DiaryGraph.DiaryHomeRoute> {
            DiaryHomeScreen(
                onDiaryClick = { diaryUI ->
                    navController.navigate(
                        route = DiaryGraph.DiaryDetailRoute(diaryUI.id),
                        navOptions = navOptions {
                            launchSingleTop = true
                        },
                    )
                },
                onDiaryEdit = { navController.navigateToWriteScreen(diaryId = it.id) },
                onNavigateToCommunity = onCommunityClick,
                onNavigateToSetting = onSettingClick,
                onNavigateToWriteScreen = { navController.navigate(DiaryGraph.DiaryWriteRoute()) },
            )
        }

        composable<DiaryGraph.DiaryWriteRoute>(
            deepLinks = listOf(
                navDeepLink<DiaryGraph.DiaryWriteRoute>(
                    basePath = "dreamdiary://diary/write",
                ),
            ),
        ) {
            DiaryWriteScreen(
                onBackClick = navController::navigateUp,
            )
        }

        composable<DiaryGraph.DiaryDetailRoute> {
            DiaryDetailScreen(
                onBackClick = navController::navigateUp,
                onEditDiary = { diaryId -> navController.navigateToWriteScreen(diaryId = diaryId) },
            )
        }
    }
}

private fun NavController.navigateToWriteScreen(
    diaryId: String? = null,
    navOptions: NavOptions? = null,
) {
    navigate(route = DiaryGraph.DiaryWriteRoute(diaryId), navOptions = navOptions)
}
