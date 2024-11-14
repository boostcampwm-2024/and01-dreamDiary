package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi
import kotlinx.serialization.Serializable

@Serializable
data object DiaryHomeRoute

fun NavController.navigateToDiaryHomeScreen(navOptions: NavOptions) = navigate(route = DiaryHomeRoute, navOptions)

fun NavGraphBuilder.diaryHomeScreen(
    onFabClick: () -> Unit,
    onCommunityClick: () -> Unit,
    onSettingClick: () -> Unit,
    onDiaryItemClick: (DiaryUi) -> Unit,
) {
    composable<DiaryHomeRoute> {

        DiaryHomeScreen(
            onDiaryClick = onDiaryItemClick,
            onNavigateToWriteScreen = {
                onFabClick()
            },
            onNavigateToCommunity = {
                onCommunityClick()
            },
            onNavigateToSetting = {
                onSettingClick()
            }
        )
    }
}
