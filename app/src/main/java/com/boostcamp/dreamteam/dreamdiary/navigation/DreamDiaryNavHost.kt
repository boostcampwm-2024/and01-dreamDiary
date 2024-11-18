package com.boostcamp.dreamteam.dreamdiary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.boostcamp.dreamteam.dreamdiary.community.CommunityGraph
import com.boostcamp.dreamteam.dreamdiary.community.communityGraph
import com.boostcamp.dreamteam.dreamdiary.feature.auth.SignInRoute
import com.boostcamp.dreamteam.dreamdiary.feature.auth.signInScreen
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.DiaryGraph
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.diaryGraph
import com.boostcamp.dreamteam.dreamdiary.setting.SettingGraph
import com.boostcamp.dreamteam.dreamdiary.setting.settingGraph
import com.boostcamp.dreamteam.dreamdiary.ui.DreamDiaryAppState
import timber.log.Timber

@Composable
fun DreamDiaryNavHost(
    appState: DreamDiaryAppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = SignInRoute,
        modifier = modifier,
    ) {
        signInScreen(
            onSignInSuccess = {
                val options = NavOptions.Builder()
                    .setPopUpTo(SignInRoute, inclusive = true)
                    .setLaunchSingleTop(true)
                    .build()
                navController.navigate(DiaryGraph, options)
            },
            onPassClick = {
                val options = NavOptions.Builder()
                    .setPopUpTo(SignInRoute, inclusive = true)
                    .setLaunchSingleTop(true)
                    .build()
                navController.navigate(DiaryGraph, options)
            },
        )

        diaryGraph(
            onCommunityClick = {
                navController.navigate(CommunityGraph) {
                    Timber.d("${navController.graph.findStartDestination().route}")
                    popUpTo(DiaryGraph) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onSettingClick = {
                navController.navigate(SettingGraph) {
                    Timber.d("${navController.graph.findStartDestination().route}")

                    popUpTo(DiaryGraph) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            navController = navController,
        )

        communityGraph(
            onDiaryClick = {
                navController.navigate(
                    DiaryGraph,
                    navOptions = navOptions {
                        Timber.d("${navController.graph.findStartDestination().route}")

                        popUpTo(CommunityGraph) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    },
                )
            },
            onSettingClick = {
                navController.navigate(
                    SettingGraph,
                    navOptions = navOptions {
                        Timber.d("${navController.graph.findStartDestination().route}")

                        popUpTo(CommunityGraph) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    },
                )
            },
        )

        settingGraph(
            onDiaryClick = {
                navController.navigate(DiaryGraph) {
                    Timber.d("${navController.graph.findStartDestination().route}")

                    popUpTo(SettingGraph) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onCommunityClick = {
                navController.navigate(CommunityGraph) {
                    Timber.d("${navController.graph.findStartDestination().route}")

                    popUpTo(SettingGraph) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onLogoutClick = {
                navController.navigate(SignInRoute) {
                    popUpTo(SettingGraph) {
                        inclusive = true
                    }
                }
            },
        )
//        composable<MainRoute> {
//            MainScreen()
//        }
    }
}
