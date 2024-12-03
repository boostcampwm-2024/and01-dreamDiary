package com.boostcamp.dreamteam.dreamdiary.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.boostcamp.dreamteam.dreamdiary.manager.DiaryWidgetManger
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
    diaryWidgetManger: DiaryWidgetManger = DiaryWidgetManger(),
) = DreamDiaryAppState(
    coroutineScope = coroutineScope,
    navController = navController,
    diaryWidgetManger = diaryWidgetManger,
)

@Stable
class DreamDiaryAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    val diaryWidgetManger: DiaryWidgetManger,
)
