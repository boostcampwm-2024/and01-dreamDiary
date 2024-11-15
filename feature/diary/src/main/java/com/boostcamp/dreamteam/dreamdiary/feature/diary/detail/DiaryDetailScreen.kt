package com.boostcamp.dreamteam.dreamdiary.feature.diary.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.filteredLabelsPreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component.DiaryWriteScreenHeader
import java.time.ZonedDateTime

@Composable
fun DiaryDetailScreen(
    onBackClick: () -> Unit,
    viewModel: DiaryDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val diaryUiState = uiState.diaryUIState
    val loading = uiState.loading
    if (!loading) {
        DiaryDetailScreen(
            title = diaryUiState.title,
            content = diaryUiState.content,
            sleepStartAt = diaryUiState.sleepStartAt,
            sleepEndAt = diaryUiState.sleepEndAt,
            labels = diaryUiState.labels,
            onBackClick = onBackClick,
        )
    }
}

@Composable
internal fun DiaryDetailScreen(
    title: String,
    content: String,
    sleepStartAt: ZonedDateTime,
    sleepEndAt: ZonedDateTime,
    labels: List<LabelUi>,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            DiaryDetailScreenTopAppBar(
                onBackClick = onBackClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )

            DiaryWriteScreenHeader(
                labelFilter = "",
                onLabelFilterChange = { },
                filteredLabels = emptyList(),
                selectedLabels = labels.toSet(),
                sleepStartAt = sleepStartAt,
                sleepEndAt = sleepEndAt,
                onSleepStartAtChange = { },
                onSleepEndAtChange = { },
                onCheckChange = { },
                onClickLabelSave = { },
                modifier = Modifier.padding(vertical = 16.dp),
                readOnly = true,
            )

            DiaryDetailContent(
                content = content,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DiaryDetailScreenTopAppBar(onBackClick: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "",
                )
            }
        },
        title = { },
    )
}

@Composable
internal fun DiaryDetailContent(
    content: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(vertical = 16.dp),
    ) {
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun DiaryHomeScreenContentPreview() {
    DreamdiaryTheme {
        DiaryDetailScreen(
            title = "투명 드래곤 크앙!",
            content = "Body text for whatever you’d like to say. Add main takeaway points, quotes, anecdotes, or even a very very short story.",
            sleepStartAt = ZonedDateTime.now(),
            sleepEndAt = ZonedDateTime.now(),
            labels = filteredLabelsPreview,
            onBackClick = {},
        )
    }
}
