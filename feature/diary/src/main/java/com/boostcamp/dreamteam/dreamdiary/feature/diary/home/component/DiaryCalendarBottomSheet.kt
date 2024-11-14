package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.diaryPreview1
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.diaryPreview2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DiaryCalendarBottomSheet(
    diariesOfDay: List<DiaryUi>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    initialSheetValue: SheetValue = SheetValue.Hidden,
) {
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = initialSheetValue,
        skipHiddenState = false,
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = bottomSheetState
    ) {
        when {
            diariesOfDay.isEmpty() -> {
                EmptyBottomSheet(modifier = Modifier.fillMaxSize())
            }

            else -> {
                ListBottomSheet(diariesOfDay = diariesOfDay, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun EmptyBottomSheet(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Outlined.NightsStay,
                    contentDescription = "Empty Diary",
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.calendar_no_diary),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
private fun ListBottomSheet(diariesOfDay: List<DiaryUi>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(diariesOfDay) { diary: DiaryUi ->
            ListItem(
                headlineContent = {
                    Text(text = diary.title)
                },
                overlineContent = {
                    Text(text = diary.sortKey.formatted)
                },
                supportingContent = {
                    Text(text = diary.content, maxLines = 2, overflow = TextOverflow.Ellipsis)
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ),
            )
            Spacer(modifier = Modifier.height(1.dp))
        }
    }
}

@ExperimentalMaterial3Api
@PreviewLightDark
@Composable
private fun DiaryCalendarBottomSheetPreviewList() {
    DreamdiaryTheme {
        DiaryCalendarBottomSheet(
            diariesOfDay = listOf(diaryPreview1, diaryPreview2),
            onDismissRequest = { /* no-op */ },
            initialSheetValue = SheetValue.Expanded,
        )
    }
}

@ExperimentalMaterial3Api
@PreviewLightDark
@Composable
private fun DiaryCalendarBottomSheetPreviewEmpty() {
    DreamdiaryTheme {
        DiaryCalendarBottomSheet(
            diariesOfDay = emptyList(),
            onDismissRequest = { /* no-op */ },
            initialSheetValue = SheetValue.Expanded,
        )
    }
}
