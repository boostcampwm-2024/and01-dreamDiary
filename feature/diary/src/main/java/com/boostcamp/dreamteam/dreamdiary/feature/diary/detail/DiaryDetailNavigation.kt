package com.boostcamp.dreamteam.dreamdiary.feature.diary.detail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class DiaryDetailRoute(
    val id: String,
)

fun NavHostController.navigateToDiaryDetailScreen(
    route: DiaryDetailRoute,
    navOptions: NavOptions,
) {
    navigate(route, navOptions)
}

fun NavGraphBuilder.diaryDetailScreen(onBackClick: () -> Unit) {
    composable<DiaryDetailRoute> {
        DiaryDetailScreen(
            onBackClick = onBackClick,
        )
    }
}
