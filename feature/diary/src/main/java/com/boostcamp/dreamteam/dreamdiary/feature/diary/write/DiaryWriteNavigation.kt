package com.boostcamp.dreamteam.dreamdiary.feature.diary.write

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object DiaryWriteRoute {
    const val ROUTE = "diary_write"
}

fun NavController.navigateToDiaryWriteScreen(navOptions: NavOptions? = null) {
    this.navigate(DiaryWriteRoute, navOptions)
}

fun NavGraphBuilder.diaryWriteScreen(onBackClick: () -> Unit) {
    composable<DiaryWriteRoute> {
        DiaryWriteScreen(
            onBackClick = onBackClick,
        )
    }
}
