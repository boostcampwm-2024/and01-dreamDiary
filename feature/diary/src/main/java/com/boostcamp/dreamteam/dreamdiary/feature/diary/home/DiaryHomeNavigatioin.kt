package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.DiaryDetailScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.search.DiarySearchScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.DiaryWriteScreen
import kotlinx.serialization.Serializable

@Serializable
data object DiaryGraph {
    @Serializable
    data object DiaryHomeRoute

    @Serializable
    data class DiaryWriteRoute(
        val diaryId: String? = null,
        val sleepTime: String? = null,
    )

    @Serializable
    data class DiaryDetailRoute(
        val id: String,
    )

    @Serializable
    data object DiarySearchRoute
}

fun NavGraphBuilder.diaryGraph(
    onCommunityClick: () -> Unit,
    onSettingClick: () -> Unit,
    onShareDiary: (diaryId: String) -> Unit,
    navController: NavController,
    onDialogConfirmClick: () -> Unit,
    updateDiaryWidget: (Context) -> Unit,
) {
    navigation<DiaryGraph>(
        startDestination = DiaryGraph.DiaryHomeRoute,
    ) {
        composable<DiaryGraph.DiaryHomeRoute> {
            DiaryHomeScreen(
                onDiaryClick = { diaryUI ->
                    navController.navigateToDetailScreen(
                        diaryId = diaryUI.id,
                        navOptions = navOptions {
                            launchSingleTop = true
                        },
                    )
                },
                onDiaryEdit = { navController.navigateToWriteScreen(diaryId = it.id) },
                onShareDiary = onShareDiary,
                onNavigateToCommunity = onCommunityClick,
                onNavigateToSetting = onSettingClick,
                onNavigateToWriteScreen = { navController.navigate(DiaryGraph.DiaryWriteRoute()) },
                onDialogConfirmClick = onDialogConfirmClick,
                onClickSearch = {
                    navController.navigateToSearchScreen(
                        navOptions = navOptions {
                            launchSingleTop = true
                        },
                    )
                },
                updateDiaryWidget = updateDiaryWidget,
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
                onWriteSuccess = { diaryId ->
                    navController.navigateToDetailScreen(
                        diaryId = diaryId,
                        navOptions = navOptions {
                            popUpTo<DiaryGraph.DiaryWriteRoute> {
                                inclusive = true
                            }
                            launchSingleTop = true
                        },
                    )
                },
                updateDiaryWidget = updateDiaryWidget,
            )
        }

        composable<DiaryGraph.DiaryDetailRoute>(
            deepLinks = listOf(
                navDeepLink<DiaryGraph.DiaryDetailRoute>(
                    basePath = "dreamdiary://diary/detail",
                ),
            ),
        ) {
            DiaryDetailScreen(
                onBackClick = navController::navigateUp,
                onEditDiary = { diaryId -> navController.navigateToWriteScreen(diaryId = diaryId) },
                onShareDiary = onShareDiary,
                onDialogConfirmClick = onDialogConfirmClick,
                updateDiaryWidget = updateDiaryWidget,
            )
        }

        composable<DiaryGraph.DiarySearchRoute> {
            DiarySearchScreen(
                onClickBack = navController::navigateUp,
                onClickDiary = { diaryId ->
                    navController.navigateToDetailScreen(
                        diaryId = diaryId,
                        navOptions = navOptions {
                            launchSingleTop = true
                        },
                    )
                },
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

private fun NavController.navigateToDetailScreen(
    diaryId: String,
    navOptions: NavOptions? = null,
) {
    navigate(route = DiaryGraph.DiaryDetailRoute(diaryId), navOptions = navOptions)
}

private fun NavController.navigateToSearchScreen(navOptions: NavOptions? = null) {
    navigate(route = DiaryGraph.DiarySearchRoute, navOptions = navOptions)
}
