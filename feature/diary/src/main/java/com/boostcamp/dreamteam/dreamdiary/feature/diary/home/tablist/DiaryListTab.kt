package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySort
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortOrder
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortOrder.ASC
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortOrder.DESC
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortType
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortType.CREATED
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortType.SLEEP
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.DiarySortType.UPDATED
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
    onDiaryEdit: (DiaryUi) -> Unit,
    onDeleteDiary: (DiaryUi) -> Unit,
    sortOption: DiarySort,
    onChangeSort: (DiarySort) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ExpandableChip(
                labels = labels,
                labelOptions = labelOptions,
                onCheckLabel = onCheckLabel,
            )
            SortButton(
                sortOption = sortOption,
                onChangeSort = onChangeSort,
            )
        }

        val contentModifier = Modifier.fillMaxSize()
        if (diaries.itemCount == 0) {
            EmptyDiaryListTabContent(modifier = contentModifier)
        } else {
            DiaryListTabContent(
                diaries = diaries,
                onDiaryClick = onDiaryClick,
                onDiaryEdit = onDiaryEdit,
                onDeleteDiary = onDeleteDiary,
                modifier = contentModifier,
            )
        }
    }
}

@Composable
private fun EmptyDiaryListTabContent(modifier: Modifier = Modifier) {
    ProvideTextStyle(
        value = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        ),
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = stringResource(R.string.home_tab_list_empty_diary_list),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Magenta),
            )
        }
    }
}

@Composable
private fun DiaryListTabContent(
    diaries: LazyPagingItems<DiaryUi>,
    onDiaryClick: (DiaryUi) -> Unit,
    onDiaryEdit: (DiaryUi) -> Unit,
    onDeleteDiary: (DiaryUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            count = diaries.itemCount,
            key = { diaries.peek(it)?.id ?: PagingIndexKey(it) },
        ) { diaryIndex ->
            val diary = diaries[diaryIndex]
            if (diary != null) {
                DiaryCard(
                    diary = diary,
                    onDiaryClick = { onDiaryClick(diary) },
                    onDiaryEdit = { onDiaryEdit(diary) },
                    onDeleteDiary = onDeleteDiary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(),
                )
            }
        }
    }
}

@Composable
private fun ExpandableChip(
    labels: List<LabelUi>,
    labelOptions: Set<LabelUi>,
    onCheckLabel: (LabelUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val name = if (labelOptions.isEmpty()) {
        stringResource(R.string.home_filter_label_name)
    } else if (labelOptions.size == 1) {
        labelOptions.first().name
    } else {
        stringResource(R.string.home_filter_label_name_multiple_selection, labelOptions.first().name, labelOptions.size - 1)
    }
    Box(modifier = modifier) {
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
            if (labels.isEmpty()) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.home_filter_label_empty)) },
                    onClick = { },
                )
            } else {
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
private fun DiaryListTabPreviewEmpty() {
    DreamdiaryTheme {
        Surface {
            DiaryListTab(
                diaries = flowOf(PagingData.empty<DiaryUi>()).collectAsLazyPagingItems(),
                labels = listOf(),
                labelOptions = setOf(),
                onCheckLabel = { },
                onDiaryClick = { },
                onDiaryEdit = { },
                onDeleteDiary = { },
                onChangeSort = { },
                sortOption = DiarySort(UPDATED, DESC),
            )
        }
    }
}

@Composable
private fun SortButton(
    sortOption: DiarySort,
    onChangeSort: (DiarySort) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (isMenuExpanded, setIsMenuExpanded) = remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        TextButton(
            onClick = { setIsMenuExpanded(true) },
            modifier = Modifier.padding(8.dp),
        ) {
            val sortTextId by remember(sortOption.type) {
                mutableIntStateOf(
                    when (sortOption.type) {
                        CREATED -> R.string.home_list_filter_sort_createdAt
                        UPDATED -> R.string.home_list_filter_sort_updatedAt
                        SLEEP -> R.string.home_list_filter_sort_sleepStartAt
                    },
                )
            }
            val sortIcon by remember(sortOption.order) { mutableStateOf(sortOption.order.toIcon()) }
            Icon(
                imageVector = Icons.AutoMirrored.Default.Sort,
                contentDescription = stringResource(R.string.home_list_filter_sort_descending),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                AnimatedContent(
                    targetState = sortTextId,
                    transitionSpec = {
                        slideInVertically { it } + fadeIn() togetherWith
                            slideOutVertically { -it } + fadeOut()
                    },
                    label = "",
                ) { targetSortTextId ->
                    Text(
                        text = stringResource(targetSortTextId),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                AnimatedContent(
                    targetState = sortIcon,
                    label = "",
                ) { targetSortIcon ->
                    Icon(
                        imageVector = targetSortIcon,
                        contentDescription = stringResource(R.string.home_list_filter_sort_descending),
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
        SortDropDownMenu(
            isMenuExpanded = isMenuExpanded,
            setIsMenuExpanded = setIsMenuExpanded,
            onClickSort = onChangeSort,
            sortOption = sortOption,
        )
    }
}

@Composable
private fun SortDropDownMenu(
    isMenuExpanded: Boolean,
    setIsMenuExpanded: (Boolean) -> Unit,
    onClickSort: (DiarySort) -> Unit,
    sortOption: DiarySort,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = isMenuExpanded,
        onDismissRequest = { setIsMenuExpanded(false) },
        modifier = modifier,
    ) {
        val items = listOf(
            SortItem(
                name = stringResource(R.string.home_list_filter_sort_createdAt),
                sortType = CREATED,
                sortOrder = if (sortOption.type == CREATED && sortOption.order == DESC) ASC else DESC,
            ),
            SortItem(
                name = stringResource(R.string.home_list_filter_sort_updatedAt),
                sortType = UPDATED,
                sortOrder = if (sortOption.type == UPDATED && sortOption.order == DESC) ASC else DESC,
            ),
            SortItem(
                name = stringResource(R.string.home_list_filter_sort_sleepStartAt),
                sortType = SLEEP,
                sortOrder = if (sortOption.type == SLEEP && sortOption.order == DESC) ASC else DESC,
            ),
        )

        items.forEach { item ->
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = item.name)
                        Icon(
                            imageVector = item.sortOrder.toIcon(),
                            contentDescription = stringResource(R.string.home_list_filter_sort_descending),
                        )
                    }
                },
                onClick = {
                    onClickSort(item.sort)
                    setIsMenuExpanded(false)
                },
            )
        }
    }
}

data class SortItem(
    val name: String,
    val sortOrder: DiarySortOrder,
    val sortType: DiarySortType,
) {
    val sort = DiarySort(sortType, sortOrder)
}

private fun DiarySortOrder.toIcon() =
    if (this == ASC) {
        Icons.Default.ArrowUpward
    } else {
        Icons.Default.ArrowDownward
    }

@Preview
@Composable
private fun DiaryListTabPreview() {
    DreamdiaryTheme {
        Surface {
            DiaryListTab(
                diaries = pagedDiariesPreview.collectAsLazyPagingItems(),
                labels = listOf(),
                labelOptions = setOf(),
                onCheckLabel = { },
                onDiaryClick = { },
                onDiaryEdit = { },
                onDeleteDiary = { },
                onChangeSort = { },
                sortOption = DiarySort(
                    UPDATED,
                    DESC,
                ),
            )
        }
    }
}

internal val diaryHomeTabListUIStatePreview = listOf(
    diaryPreview1,
    diaryPreview2,
)

internal val pagedDiariesPreview = flowOf(PagingData.from(diaryHomeTabListUIStatePreview))
