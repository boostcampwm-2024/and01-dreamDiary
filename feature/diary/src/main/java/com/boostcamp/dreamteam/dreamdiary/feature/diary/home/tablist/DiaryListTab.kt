package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.component.DiaryCard
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.PagingIndexKey
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.diaryPreview1
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.diaryPreview2
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun DiaryListTab(
    diaries: LazyPagingItems<DiaryUi>,
    labels: List<LabelUi>,
    labelOptions: Set<LabelUi>,
    onCheckLabel: (LabelUi) -> Unit,
    onDiaryClick: (DiaryUi) -> Unit,
    onDeleteDiary: (DiaryUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        ExpandableChip(
            labels = labels,
            labelOptions = labelOptions,
            onCheckLabel = onCheckLabel,
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
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
                        onDiaryClick = { onDiaryClick(diary) },
                        onDeleteDiary = onDeleteDiary,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandableChip(
    labels: List<LabelUi>,
    labelOptions: Set<LabelUi>,
    onCheckLabel: (LabelUi) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val name = if (labelOptions.isEmpty()) {
        "정렬"
    } else if (labelOptions.size == 1) {
        labelOptions.first().name
    } else {
        "${labelOptions.first().name} 외 ${labelOptions.size - 1}종"
    }
    Box {
        FilterChip(
            selected = expanded,
            onClick = { expanded = !expanded },
            label = {
                Text(text = name)
            },
            modifier = Modifier.padding(8.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Label,
                    contentDescription = stringResource(R.string.home_filter_label),
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(R.string.home_filter_label_list),
                )
            },
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Column {
                labels.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(text = label.name) },
                        onClick = {
                            onCheckLabel(label)
                        },
                        trailingIcon = {
                            Checkbox(
                                checked = label in labelOptions,
                                onCheckedChange = null,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DiaryListTabPreview() {
    DreamdiaryTheme {
        DiaryListTab(
            diaries = pagedDiariesPreview.collectAsLazyPagingItems(),
            labels = listOf(),
            labelOptions = setOf(),
            onCheckLabel = { },
            onDiaryClick = { },
            onDeleteDiary = { },
        )
    }
}

internal val diaryHomeTabListUIStatePreview = listOf(
    diaryPreview1,
    diaryPreview2,
)

internal val pagedDiariesPreview = flowOf(PagingData.from(diaryHomeTabListUIStatePreview))
