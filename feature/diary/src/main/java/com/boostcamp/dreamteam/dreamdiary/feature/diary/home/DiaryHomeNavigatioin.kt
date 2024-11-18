package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.DiaryDetailScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.DiaryWriteScreen
import kotlinx.serialization.Serializable

@Serializable
data object DiaryGraph {
    @Serializable
    data object DiaryHomeRoute

    @Serializable
    data object DiaryWriteRoute

    @Serializable
    data class DiaryDetailRoute(val id: String)
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
                onDiaryClick = {
                },
                onNavigateToWriteScreen = { navController.navigate(DiaryGraph.DiaryWriteRoute) },
                onNavigateToCommunity = onCommunityClick,
                onNavigateToSetting = onSettingClick,
            )
        }

        composable<DiaryGraph.DiaryWriteRoute> {
            DiaryWriteScreen(
                onBackClick = navController::navigateUp,
            )
        }

        composable<DiaryGraph.DiaryDetailRoute> {
            DiaryDetailScreen(
                navController::navigateUp,
            )
        }
    }
}
