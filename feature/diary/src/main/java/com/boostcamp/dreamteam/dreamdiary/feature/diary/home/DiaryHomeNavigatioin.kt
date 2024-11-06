package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import kotlinx.serialization.Serializable

@Serializable
data object DiaryHomeRoute

fun NavController.navigateToDiaryHomeScreen(navOptions: NavOptions) = navigate(route = DiaryHomeRoute, navOptions)

fun NavGraphBuilder.diaryHomeScreen(
    onDiaryClick: (Diary) -> Unit,
    onFabClick: () -> Unit,
) {
    composable<DiaryHomeRoute> {
        DiaryHomeScreen(
            onDiaryClick = onDiaryClick,
            onFabClick = onFabClick,
        )
    }
}