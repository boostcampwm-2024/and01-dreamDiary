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
data object DiaryHomeGraph {
    @Serializable
    data object DiaryHomeRoute

    @Serializable
    data object DiaryWriteRoute

    @Serializable
    data class DiaryDetailRoute(val id: Long)
}

fun NavController.navigateToDiaryHomeScreen(navOptions: NavOptions) = navigate(route = DiaryHomeGraph, navOptions)

fun NavGraphBuilder.diaryHomeGraph(
    onCommunityClick: () -> Unit,
    onSettingClick: () -> Unit,
    navController: NavController,
) {
    navigation<DiaryHomeGraph>(
        startDestination = DiaryHomeGraph.DiaryHomeRoute,
    ) {
        composable<DiaryHomeGraph.DiaryHomeRoute> {
            DiaryHomeScreen(
                onDiaryClick = {},
                onNavigateToWriteScreen = { navController.navigate(DiaryHomeGraph.DiaryWriteRoute) },
                onNavigateToCommunity = onCommunityClick,
                onNavigateToSetting = onSettingClick,
            )
        }

        composable<DiaryHomeGraph.DiaryWriteRoute> {
            DiaryWriteScreen(
                onBackClick = navController::navigateUp,
            )
        }

        composable<DiaryHomeGraph.DiaryDetailRoute> {
            // todo : 무엇을 넘겨야하는 지 알기
            DiaryDetailScreen(
                navController::navigateUp,
            )
        }

    }
}
