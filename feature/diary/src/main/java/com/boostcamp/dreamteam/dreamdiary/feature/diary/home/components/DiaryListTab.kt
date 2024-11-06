package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme

@Composable
internal fun DiaryListTab(
    diaries: List<Diary>,
    modifier: Modifier = Modifier,
    onDiaryClick: (Diary) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(diaries) { diary: Diary ->
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
        DiaryListTab(diaries = diariesPreview)
    }
}

internal val diariesPreview: List<Diary> = listOf(
    diaryPreview1,
    diaryPreview2,
)
