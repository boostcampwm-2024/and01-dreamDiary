package com.boostcamp.dreamteam.dreamdiary.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
) = DreamDiaryAppState(
    coroutineScope = coroutineScope,
    navController = navController,
)

@Stable
class DreamDiaryAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
)
