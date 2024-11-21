package com.boostcamp.dreamteam.dreamdiary.feature.diary.write

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.component.DiaryContentEditorParams
import com.boostcamp.dreamteam.dreamdiary.feature.diary.component.DiaryEditor
import com.boostcamp.dreamteam.dreamdiary.feature.diary.component.DiaryInfoEditorParams
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryContentUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.filteredLabelsPreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.selectedLabelsPreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.DiaryWriteEvent
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.LabelAddFailureReason
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.io.File
import java.io.FileOutputStream
import java.time.ZonedDateTime
import java.util.UUID

@Composable
fun DiaryWriteScreen(
    onBackClick: () -> Unit,
    onWriteSuccess: (diaryId: String) -> Unit,
    viewModel: DiaryWriteViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(onBackClick, onWriteSuccess) {
        viewModel.event.collectLatest { writeEvent ->
            when (writeEvent) {
                is DiaryWriteEvent.DiaryAddSuccess -> {
                    Toast.makeText(context, "일기 작성 성공", Toast.LENGTH_SHORT).show()
                    onWriteSuccess(writeEvent.diaryId)
                }

                is DiaryWriteEvent.DiaryUpdateSuccess -> {
                    Toast.makeText(context, "일기 수정 성공", Toast.LENGTH_SHORT).show()
                    onWriteSuccess(writeEvent.diaryId)
                }

                is DiaryWriteEvent.LabelAddSuccess -> {
                    Toast.makeText(context, "라벨 추가 성공", Toast.LENGTH_SHORT).show()
                }

                is DiaryWriteEvent.LabelAddFailure -> {
                    when (writeEvent.labelAddFailureReason) {
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

                is DiaryWriteEvent.DiaryUpdateFail -> {
                    Toast.makeText(context, context.getString(R.string.write_edit_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    DiaryWriteScreenContent(
        onBackClick = onBackClick,
        title = uiState.title,
        onTitleChange = viewModel::setTitle,
        labelFilter = uiState.labelFilter,
        filteredLabels = uiState.filteredLabels,
        selectedLabels = uiState.selectedLabels,
        sleepStartAt = uiState.sleepStartAt,
        onSleepStartAtChange = viewModel::setSleepStartAt,
        sleepEndAt = uiState.sleepEndAt,
        onSleepEndAtChange = viewModel::setSleepEndAt,
        diaryContents = uiState.diaryContents,
        onContentImageAdd = viewModel::addContentImage,
        onClickSave = viewModel::addOrUpdateDreamDiary,
        onCheckChange = viewModel::toggleLabel,
        onLabelFilterChange = viewModel::setLabelFilter,
        onClickLabelSave = viewModel::addLabel,
        onContentTextChange = viewModel::setContentText,
        onContentImageDelete = viewModel::deleteContentImage,
        onEditLabel = viewModel::updateLabel,
        onDeleteLabel = viewModel::deleteLabel,
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
    )
}

@Composable
private fun DiaryWriteScreenContent(
    onBackClick: () -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    labelFilter: String,
    filteredLabels: List<LabelUi>,
    selectedLabels: Set<LabelUi>,
    sleepStartAt: ZonedDateTime,
    onSleepStartAtChange: (ZonedDateTime) -> Unit,
    sleepEndAt: ZonedDateTime,
    onSleepEndAtChange: (ZonedDateTime) -> Unit,
    diaryContents: List<DiaryContentUi>,
    onContentImageAdd: (contentIndex: Int, Int, String) -> Unit,
    onClickSave: () -> Unit,
    onCheckChange: (LabelUi) -> Unit,
    onLabelFilterChange: (String) -> Unit,
    onClickLabelSave: () -> Unit,
    onContentTextChange: (Int, String) -> Unit,
    onContentImageDelete: (Int) -> Unit,
    onEditLabel: (labelUi: LabelUi, newValue: String) -> Unit,
    onDeleteLabel: (labelUi: LabelUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    // 현재는 사실상 Text만 포커스 됨
    var currentFocusContent by remember { mutableIntStateOf(0) }
    var currentTextCursorPosition by remember { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val singleImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                coroutineScope.launch(Dispatchers.IO) {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val outputFile = File(context.filesDir, UUID.randomUUID().toString())
                    val outputStream = FileOutputStream(outputFile)

                    inputStream?.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }
                    yield()
                    launch(Dispatchers.Main) {
                        onContentImageAdd(
                            currentFocusContent,
                            currentTextCursorPosition,
                            outputFile.path,
                        )
                    }
                }
            }
        },
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            DiaryWriteTopBar(
                onBackClick = onBackClick,
                onClickSave = onClickSave,
            )
        },
        bottomBar = {
            DiaryWriteBottomBar(
                singleImagePicker = singleImagePicker,
            )
        },
    ) { innerPadding ->
        DiaryEditor(
            diaryInfoEditorParams = DiaryInfoEditorParams(
                labelFilter = labelFilter,
                onLabelFilterChange = onLabelFilterChange,
                filteredLabels = filteredLabels,
                selectedLabels = selectedLabels,
                sleepStartAt = sleepStartAt,
                onSleepStartAtChange = onSleepStartAtChange,
                sleepEndAt = sleepEndAt,
                onSleepEndAtChange = onSleepEndAtChange,
                onCheckChange = onCheckChange,
                onClickLabelSave = onClickLabelSave,
                onEditLabel = onEditLabel,
                onDeleteLabel = onDeleteLabel,
            ),
            diaryContentEditorParams = DiaryContentEditorParams(
                title = title,
                onTitleChange = onTitleChange,
                diaryContents = diaryContents,
                onContentTextChange = onContentTextChange,
                onContentFocusChange = { currentFocusContent = it },
                onContentTextPositionChange = { currentTextCursorPosition = it },
                onContentImageDelete = onContentImageDelete,
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
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
private fun DiaryWriteBottomBar(
    singleImagePicker: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        IconButton(
            onClick = {
                singleImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
        ) {
            Icon(
                imageVector = Icons.Outlined.AddPhotoAlternate,
                contentDescription = stringResource(R.string.write_add_image),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun DiaryWriteScreenPreview() {
    DreamdiaryTheme {
        DiaryWriteScreenContent(
            onBackClick = {},
            title = "",
            onTitleChange = {},
            labelFilter = "",
            filteredLabels = filteredLabelsPreview,
            selectedLabels = selectedLabelsPreview,
            sleepStartAt = ZonedDateTime.now(),
            onSleepStartAtChange = {},
            sleepEndAt = ZonedDateTime.now(),
            onSleepEndAtChange = {},
            diaryContents = listOf(DiaryContentUi.Text("")),
            onContentImageAdd = { _, _, _ -> },
            onClickSave = {},
            onCheckChange = {},
            onLabelFilterChange = {},
            onClickLabelSave = {},
            onContentTextChange = { _, _ -> },
            onContentImageDelete = { },
            onEditLabel = { _, _ -> },
            onDeleteLabel = { },
        )
    }
}
