package com.boostcamp.dreamteam.dreamdiary

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.boostcamp.dreamteam.dreamdiary.community.CommunityGraph
import com.boostcamp.dreamteam.dreamdiary.community.communityGraph
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.DiaryGraph
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.diaryGraph
import com.boostcamp.dreamteam.dreamdiary.setting.SettingGraph
import com.boostcamp.dreamteam.dreamdiary.setting.settingGraph

// main route 값 정의
const val MainRoute = "main_route"

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = DiaryGraph,
    ) {
        diaryGraph(
            onCommunityClick = {
                navController.navigate(CommunityGraph) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onSettingClick = {
                navController.navigate(SettingGraph) {
                    popUpTo(navController.graph.findStartDestination().route!!) {
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
                        popUpTo(navController.graph.findStartDestination().id) {
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
                        popUpTo(navController.graph.findStartDestination().id) {
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
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onCommunityClick = {
                navController.navigate(CommunityGraph) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
        )
    }
}
