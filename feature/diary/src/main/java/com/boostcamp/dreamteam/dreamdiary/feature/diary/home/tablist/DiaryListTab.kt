package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.components.DiaryCard
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.diariesPreview

@Composable
internal fun DiaryListTab(
    onDiaryClick: (DiaryUi) -> Unit,
    modifier: Modifier = Modifier,
    uiState: DiaryHomeTabListUIState = DiaryHomeTabListUIState(),
) {
    val (diaries) = uiState

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(diaries) { diary ->
            DiaryCard(
                diary = diary,
                modifier = Modifier.fillMaxWidth(),
                onDiaryClick = { onDiaryClick(diary) },
            )
        }
    }
}

@Preview
@Composable
private fun DiaryListTabPreview() {
    DreamdiaryTheme {
        DiaryListTab(
            onDiaryClick = { },
            uiState = diaryHomeTabListUIStatePreview,
        )
    }
}

internal val diaryHomeTabListUIStatePreview = DiaryHomeTabListUIState(
    diaries = diariesPreview,
)
