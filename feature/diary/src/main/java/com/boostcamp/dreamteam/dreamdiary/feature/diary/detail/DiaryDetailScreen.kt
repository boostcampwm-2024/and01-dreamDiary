package com.boostcamp.dreamteam.dreamdiary.feature.diary.detail

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.component.DiaryInfoEditorParams
import com.boostcamp.dreamteam.dreamdiary.feature.diary.component.DiaryInfosEditor
import com.boostcamp.dreamteam.dreamdiary.feature.diary.component.DiaryMenuButton
import com.boostcamp.dreamteam.dreamdiary.feature.diary.detail.model.DiaryDetailEvent
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryContentUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.filteredLabelsPreview
import com.boostcamp.dreamteam.dreamdiary.ui.component.GoToSignInDialog
import timber.log.Timber
import java.io.File
import java.time.ZonedDateTime

@Composable
fun DiaryDetailScreen(
    onBackClick: () -> Unit,
    onEditDiary: (diaryId: String) -> Unit,
    onShareDiary: (diaryId: String) -> Unit,
    onDialogConfirmClick: () -> Unit,
    updateDiaryWidget: (Context) -> Unit,
    viewModel: DiaryDetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val email by viewModel.email.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(onBackClick, updateDiaryWidget) {
        viewModel.event.collect { event ->
            Timber.d("DiaryDetailScreen event: $event")
            when (event) {
                is DiaryDetailEvent.DeleteDiary.Success -> {
                    updateDiaryWidget(context)
                    Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show()
                }

                is DiaryDetailEvent.DeleteDiary.Failure -> { // no-op
                }

                is DiaryDetailEvent.LoadDiary.Failure -> {
                    onBackClick()
                }

                is DiaryDetailEvent.LoadDiary.Success -> { // no-op
                }
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val diaryUiState = uiState.diaryUIState
    val loading = uiState.loading
    if (!loading) {
        GoToSignInDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            onConfirm = {
                onDialogConfirmClick()
                showDialog = false
            },
        )
        DiaryDetailScreen(
            title = diaryUiState.title,
            sleepStartAt = diaryUiState.sleepStartAt,
            sleepEndAt = diaryUiState.sleepEndAt,
            labels = diaryUiState.labels,
            diaryContents = diaryUiState.diaryContents,
            onBackClick = onBackClick,
            onEditDiary = { onEditDiary(diaryUiState.id) },
            onDeleteDiary = { viewModel.deleteDiary() },
            onShareDiary = {
                if (email == null) {
                    showDialog = true
                } else {
                    onShareDiary(diaryUiState.id)
                }
            },
        )
    }
}

@Composable
internal fun DiaryDetailScreen(
    title: String,
    sleepStartAt: ZonedDateTime,
    sleepEndAt: ZonedDateTime,
    labels: List<LabelUi>,
    diaryContents: List<DiaryContentUi>,
    onBackClick: () -> Unit,
    onEditDiary: () -> Unit,
    onDeleteDiary: () -> Unit,
    onShareDiary: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            DiaryDetailScreenTopAppBar(
                onBackClick = onBackClick,
                onDiaryEdit = onEditDiary,
                onDeleteDiary = onDeleteDiary,
                onShareDiary = onShareDiary,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )

            DiaryInfosEditor(
                diaryInfoEditorParams = DiaryInfoEditorParams(
                    labelFilter = "",
                    onLabelFilterChange = { },
                    filteredLabels = labels,
                    selectedLabels = labels.toSet(),
                    sleepStartAt = sleepStartAt,
                    onSleepStartAtChange = { },
                    sleepEndAt = sleepEndAt,
                    onSleepEndAtChange = { },
                    onCheckChange = { },
                    onClickLabelSave = { },
                    onDeleteLabel = { },
                ),
                modifier = Modifier.padding(vertical = 16.dp),
                readOnly = true,
            )

            DiaryDetailContent(
                diaryContents = diaryContents,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DiaryDetailScreenTopAppBar(
    onBackClick: () -> Unit,
    onDeleteDiary: () -> Unit,
    onDiaryEdit: () -> Unit,
    onShareDiary: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuVisible by remember { mutableStateOf(false) }
    TopAppBar(
        title = { },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.detail_back),
                )
            }
        },
        actions = {
            DiaryMenuButton(
                isVisible = isMenuVisible,
                onVisibleChange = { isMenuVisible = it },
                onDeleteDiary = onDeleteDiary,
                onDiaryEdit = onDiaryEdit,
                onShareDiary = onShareDiary,
            )
        },
    )
}

@Composable
internal fun DiaryDetailContent(
    diaryContents: List<DiaryContentUi>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(vertical = 16.dp),
    ) {
        for (diaryContent in diaryContents) {
            when (diaryContent) {
                is DiaryContentUi.Text -> {
                    Text(
                        text = diaryContent.text,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                is DiaryContentUi.Image -> {
                    val context = LocalContext.current
                    AsyncImage(
                        model = ImageRequest
                            .Builder(context)
                            .data(File(context.filesDir, diaryContent.path).path)
                            .build(),
                        contentDescription = "aaaa",
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DiaryHomeScreenContentPreview() {
    DreamdiaryTheme {
        DiaryDetailScreen(
            title = "투명 드래곤 크앙!",
            sleepStartAt = ZonedDateTime.now(),
            sleepEndAt = ZonedDateTime.now(),
            labels = filteredLabelsPreview,
            diaryContents = listOf(
                DiaryContentUi.Text(
                    "Body text for whatever you’d like to say. Add main takeaway points, quotes, anecdotes, or even a very very short story.",
                ),
            ),
            onBackClick = {},
            onEditDiary = {},
            onShareDiary = {},
            onDeleteDiary = {},
        )
    }
}
