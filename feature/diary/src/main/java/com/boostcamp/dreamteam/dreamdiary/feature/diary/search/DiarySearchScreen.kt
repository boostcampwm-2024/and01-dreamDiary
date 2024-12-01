package com.boostcamp.dreamteam.dreamdiary.feature.diary.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme

@Composable
fun DiarySearchScreen(
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewmodel: DiarySearchViewModel = hiltViewModel()
) {
    DiarySearchScreenContent()
}

@Composable
private fun DiarySearchScreenContent(modifier: Modifier = Modifier) {

}

@Preview
@Composable
private fun DiarySearchScreenContentPreview() {
    DreamdiaryTheme {
        DiarySearchScreenContent()
    }
}
