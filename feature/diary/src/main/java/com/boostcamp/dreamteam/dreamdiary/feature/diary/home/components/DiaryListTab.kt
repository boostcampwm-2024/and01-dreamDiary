package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.models.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.models.PagingIndexKey
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun DiaryListTab(
    diaries: LazyPagingItems<DiaryUi>,
    modifier: Modifier = Modifier,
    onDiaryClick: (DiaryUi) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(count = diaries.itemCount, key = {
            val item = diaries.peek(it)
            if (item == null) {
                PagingIndexKey(it)
            } else {
                item.id
            }
        }) { diaryIndex ->
            val diary = diaries[diaryIndex]
            if (diary != null) {
                DiaryCard(
                    diary = diary,
                    modifier = Modifier.fillMaxWidth(),
                    onDiaryClick = { onDiaryClick(diary) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun DiaryListTabPreview() {
    DreamdiaryTheme {
        DiaryListTab(diaries = pagedDiariesPreview.collectAsLazyPagingItems())
    }
}

internal val diariesPreview = listOf(
    diaryPreview1,
    diaryPreview2,
)

internal val pagedDiariesPreview = flowOf(PagingData.from(diariesPreview))
