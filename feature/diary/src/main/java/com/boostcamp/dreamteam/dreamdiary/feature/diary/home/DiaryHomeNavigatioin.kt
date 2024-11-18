package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
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
    data class DiaryDetailRoute(val id: Long)
}

fun NavController.navigateToDiaryHomeScreen(navOptions: NavOptions) = navigate(route = DiaryGraph, navOptions)

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
                onDiaryClick = {},
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
            // todo : 무엇을 넘겨야하는 지 알기
            DiaryDetailScreen(
                navController::navigateUp,
            )
        }

    }
}
