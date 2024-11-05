package com.boostcamp.dreamteam.dreamdiary.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.navigation.DreamDiaryNavHost

@Composable
internal fun DreamDiaryApp(
    appState: DreamDiaryAppState = rememberAppState(),
    modifier: Modifier = Modifier,
) {
    DreamdiaryTheme {
        DreamDiaryNavHost(appState = appState)
    }
}
