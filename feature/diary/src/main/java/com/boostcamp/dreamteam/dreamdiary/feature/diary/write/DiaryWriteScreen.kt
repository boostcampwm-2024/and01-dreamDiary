package com.boostcamp.dreamteam.dreamdiary.feature.diary.write

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryContentUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.filteredLabelsPreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.selectedLabelsPreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component.DiaryWriteScreenBody
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component.DiaryWriteScreenHeader
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
    viewModel: DiaryWriteViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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

    DiaryWriteScreenContent(
        onBackClick = onBackClick,
        viewModel = viewModel,
        title = uiState.title,
        content = uiState.content,
        labelFilter = uiState.labelFilter,
        filteredLabels = uiState.filteredLabels,
        selectedLabels = uiState.selectedLabels,
        sleepStartAt = uiState.sleepStartAt,
        sleepEndAt = uiState.sleepEndAt,
        diaryContents = uiState.diaryContents,
        modifier = Modifier.imePadding(),
    )
}

@Composable
private fun DiaryWriteScreenContent(
    onBackClick: () -> Unit,
    viewModel: DiaryWriteViewModel,
    title: String,
    content: String,
    labelFilter: String,
    filteredLabels: List<LabelUi>,
    selectedLabels: Set<LabelUi>,
    sleepStartAt: ZonedDateTime,
    sleepEndAt: ZonedDateTime,
    diaryContents: List<DiaryContentUi>,
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
                        viewModel.addContentImage(
                            contentIndex = currentFocusContent,
                            textPosition = currentTextCursorPosition,
                            imagePath = outputFile.path,
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
                onClickSave = viewModel::addDreamDiary,
            )
        },
        bottomBar = {
            DiaryWriteBottomBar(
                singleImagePicker = singleImagePicker,
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            diaryContents = diaryContents,
            onContentTextChange = viewModel::setContentText,
            onCurrentFocusContentChange = { currentFocusContent = it },
            onContentTextPositionChange = { currentTextCursorPosition = it },
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
    diaryContents: List<DiaryContentUi>,
    onContentTextChange: (contentIndex: Int, String) -> Unit,
    onCurrentFocusContentChange: (Int) -> Unit,
    onContentTextPositionChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(modifier = modifier.verticalScroll(scrollState)) {
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        DiaryWriteScreenBody(
            title = title,
            onTitleChange = onTitleChange,
            content = content,
            onContentChange = onContentChange,
            diaryContents = diaryContents,
            onContentTextChange = onContentTextChange,
            onContentFocusChange = { onCurrentFocusContentChange(it) },
            onContentTextPositionChange = { onContentTextPositionChange(it) },
            onContentImageDelete = { /* TODO */ },
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
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
private fun PreviewDiaryListScreen() {
    DreamdiaryTheme {
        DiaryWriteScreen(
            title = "",
            content = "",
            labelFilter = "",
            filteredLabels = filteredLabelsPreview,
            selectedLabels = selectedLabelsPreview,
            onTitleChange = {},
            onContentChange = {},
            onCheckChange = {},
            onClickLabelSave = {},
            onLabelFilterChange = {},
            sleepStartAt = ZonedDateTime.now(),
            onSleepStartAtChange = {},
            sleepEndAt = ZonedDateTime.now(),
            onSleepEndAtChange = {},
            diaryContents = emptyList(),
            onContentTextChange = { _, _ -> },
            onContentTextPositionChange = { },
            onCurrentFocusContentChange = { },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
