package com.boostcamp.dreamteam.dreamdiary.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import com.boostcamp.dreamteam.dreamdiary.community.CommunityGraph
import com.boostcamp.dreamteam.dreamdiary.community.communityGraph
import com.boostcamp.dreamteam.dreamdiary.community.navigateToCommunityWrite
import com.boostcamp.dreamteam.dreamdiary.feature.auth.SignInRoute
import com.boostcamp.dreamteam.dreamdiary.feature.auth.signInScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.DiaryGraph
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.diaryGraph
import com.boostcamp.dreamteam.dreamdiary.setting.SettingGraph
import com.boostcamp.dreamteam.dreamdiary.setting.settingGraph
import com.boostcamp.dreamteam.dreamdiary.ui.DreamDiaryAppState

@Composable
fun DreamDiaryNavHost(
    appState: DreamDiaryAppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    val diaryWidgetManger = appState.diaryWidgetManger

    Surface(
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        NavHost(
            navController = navController,
            startDestination = DiaryGraph,
            modifier = modifier,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            signInScreen(
                onSignInSuccess = {
                    val options = NavOptions.Builder()
                        .setPopUpTo(SignInRoute, inclusive = true)
                        .setLaunchSingleTop(true)
                        .build()
                    navController.navigate(DiaryGraph, options)
                },
                onNotSignInClick = {
                    val options = NavOptions.Builder()
                        .setPopUpTo(SignInRoute, inclusive = true)
                        .setLaunchSingleTop(true)
                        .build()
                    navController.navigate(DiaryGraph, options)
                },
            )

            diaryGraph(
                onShareDiary = { navController.navigateToCommunityWrite(it) },
                onCommunityClick = {
                    val options = NavOptions.Builder()
                        .setPopUpTo(DiaryGraph, inclusive = false, saveState = true)
                        .setLaunchSingleTop(true)
                        .setRestoreState(true)
                        .build()
                    navController.navigate(CommunityGraph, options)
                },
                onSettingClick = {
                    val options = NavOptions.Builder()
                        .setPopUpTo(DiaryGraph, inclusive = false, saveState = true)
                        .setLaunchSingleTop(true)
                        .setRestoreState(true)
                        .build()
                    navController.navigate(SettingGraph, options)
                },
                navController = navController,
                onDialogConfirmClick = {
                    val options = NavOptions.Builder()
                        .setPopUpTo(DiaryGraph, inclusive = true)
                        .build()
                    navController.navigate(SignInRoute, options)
                },
                updateDiaryWidget = { context ->
                    diaryWidgetManger.update(context)
                },
            )

            communityGraph(
                navController = navController,
                onDiaryClick = {
                    val options = NavOptions.Builder()
                        .setPopUpTo(CommunityGraph, inclusive = false, saveState = true)
                        .setLaunchSingleTop(true)
                        .setRestoreState(true)
                        .build()
                    navController.navigate(DiaryGraph, options)
                },
                onSettingClick = {
                    val options = NavOptions.Builder()
                        .setPopUpTo(CommunityGraph, inclusive = false, saveState = true)
                        .setLaunchSingleTop(true)
                        .setRestoreState(true)
                        .build()
                    navController.navigate(SettingGraph, options)
                },
                onGoToSignInClick = {
                    val options = NavOptions.Builder()
                        .setPopUpTo(CommunityGraph, inclusive = true)
                        .build()
                    navController.navigate(SignInRoute, options)
                },
            )

            settingGraph(
                navController = navController,
                onDiaryClick = {
                    val options = NavOptions.Builder()
                        .setPopUpTo(SettingGraph, inclusive = false, saveState = true)
                        .setLaunchSingleTop(true)
                        .setRestoreState(true)
                        .build()
                    navController.navigate(DiaryGraph, options)
                },
                onCommunityClick = {
                    val options = NavOptions.Builder()
                        .setPopUpTo(SettingGraph, inclusive = false, saveState = true)
                        .setLaunchSingleTop(true)
                        .setRestoreState(true)
                        .build()
                    navController.navigate(CommunityGraph, options)
                },
                onGoToSignInClick = {
                    val options = NavOptions.Builder()
                        .setPopUpTo(SettingGraph, inclusive = true)
                        .build()
                    navController.navigate(SignInRoute, options)
                },
            )
        }
    }
}
