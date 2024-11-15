package com.boostcamp.dreamteam.dreamdiary.feature.diary.write

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component.DiaryWriteScreenBody
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component.DiaryWriteScreenHeader
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.DiaryWriteEvent
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.LabelAddFailureReason
import kotlinx.coroutines.flow.collectLatest
import java.time.ZonedDateTime

@Composable
fun DiaryWriteScreen(
    onBackClick: () -> Unit,
    viewModel: DiaryWriteViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val title = uiState.title
    val content = uiState.content
    val labelFilter = uiState.labelFilter
    val filteredLabels = uiState.filteredLabels
    val selectedLabels = uiState.selectedLabels
    val sleepStartAt = uiState.sleepStartAt
    val sleepEndAt = uiState.sleepEndAt

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest {
            when (it) {
                is DiaryWriteEvent.DiaryAddSuccess -> onBackClick()
                is DiaryWriteEvent.LabelAddSuccess -> {
                    Toast.makeText(context, "라벨 추가 성공", Toast.LENGTH_SHORT).show()
                }

                is DiaryWriteEvent.LabelAddFailure -> {
                    when (it.labelAddFailureReason) {
                        LabelAddFailureReason.DUPLICATE_LABEL -> {
                            Toast.makeText(context, context.getString(R.string.write_duplicate_error), Toast.LENGTH_SHORT).show()
                        }

                        LabelAddFailureReason.INSUFFICIENT_STORAGE -> {
                            Toast.makeText(context, context.getString(R.string.write_insufficient_storage_error), Toast.LENGTH_SHORT).show()
                        }

                        LabelAddFailureReason.UNKNOWN_ERROR -> {
                            Toast.makeText(context, context.getString(R.string.write_unknown_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            DiaryWriteTopBar(
                onBackClick = onBackClick,
                onClickSave = viewModel::addDreamDiary,
            )
        },
    ) { innerPadding ->
        DiaryWriteScreen(
            title = title,
            content = content,
            labelFilter = labelFilter,
            filteredLabels = filteredLabels,
            selectedLabels = selectedLabels,
            onTitleChange = viewModel::setTitle,
            onContentChange = viewModel::setContent,
            onCheckChange = viewModel::toggleLabel,
            onLabelFilterChange = viewModel::setLabelFilter,
            onClickLabelSave = viewModel::addLabel,
            sleepStartAt = sleepStartAt,
            onSleepStartAtChange = viewModel::setSleepStartAt,
            sleepEndAt = sleepEndAt,
            onSleepEndAtChange = viewModel::setSleepEndAt,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun DiaryWriteScreen(
    title: String,
    content: String,
    labelFilter: String,
    filteredLabels: List<LabelUi>,
    selectedLabels: Set<LabelUi>,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onCheckChange: (labelUi: LabelUi) -> Unit,
    onLabelFilterChange: (String) -> Unit,
    onClickLabelSave: () -> Unit,
    sleepStartAt: ZonedDateTime,
    onSleepStartAtChange: (ZonedDateTime) -> Unit,
    sleepEndAt: ZonedDateTime,
    onSleepEndAtChange: (ZonedDateTime) -> Unit,
    modifier: Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        DiaryWriteScreenHeader(
            labelFilter = labelFilter,
            onLabelFilterChange = onLabelFilterChange,
            filteredLabels = filteredLabels,
            selectedLabels = selectedLabels,
            sleepStartAt = sleepStartAt,
            sleepEndAt = sleepEndAt,
            onSleepStartAtChange = onSleepStartAtChange,
            onSleepEndAtChange = onSleepEndAtChange,
            onCheckChange = onCheckChange,
            onClickLabelSave = onClickLabelSave,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        DiaryWriteScreenBody(
            title = title,
            onTitleChange = onTitleChange,
            content = content,
            onContentChange = onContentChange,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DiaryWriteTopBar(
    onBackClick: () -> Unit,
    onClickSave: () -> Unit,
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.write_back),
                )
            }
        },
        actions = {
            IconButton(onClick = { onClickSave() }) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(R.string.write_save),
                )
            }
        },
    )
}

@Composable
@Preview(showBackground = true)
private fun PreviewDiaryListScreen() {
    DreamdiaryTheme {
        DiaryWriteScreen(
            title = "",
            content = "",
            labelFilter = "",
            filteredLabels = listOf(
                LabelUi("악몽"),
                LabelUi("개꿈"),
                LabelUi("귀신"),
            ),
            selectedLabels = setOf(
                LabelUi("악몽"),
                LabelUi("공룡"),
            ),
            onTitleChange = {},
            onContentChange = {},
            onCheckChange = {},
            onClickLabelSave = {},
            onLabelFilterChange = {},
            sleepStartAt = ZonedDateTime.now(),
            onSleepStartAtChange = {},
            sleepEndAt = ZonedDateTime.now(),
            onSleepEndAtChange = {},
            modifier = Modifier,
        )
    }
}
