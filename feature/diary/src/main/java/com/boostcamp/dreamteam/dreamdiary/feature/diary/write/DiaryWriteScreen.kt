package com.boostcamp.dreamteam.dreamdiary.feature.diary.write

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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

    val title = uiState.title
    val content = uiState.content
    val labelFilter = uiState.labelFilter
    val filteredLabels = uiState.filteredLabels
    val selectedLabels = uiState.selectedLabels
    val sleepStartAt = uiState.sleepStartAt
    val sleepEndAt = uiState.sleepEndAt
    val diaryContents = uiState.diaryContents

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
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding(),
            diaryContents = diaryContents,
            onContentTextChange = viewModel::setContentText,
            onContentImageAdd = viewModel::addContentImage,
            onContentImageDelete = { },
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
    onContentImageAdd: (contentIndex: Int, Int, String) -> Unit,
    onContentImageDelete: (DiaryContentUi) -> Unit,
    modifier: Modifier,
) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current

    // 현재는 사실상 Text만 포커스 됨
    var currentFocusContent by remember { mutableIntStateOf(0) }
    var currentTextCursorPosition by remember { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()

    val singleImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri == null) return@rememberLauncherForActivityResult
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
                    onContentImageAdd(currentFocusContent, currentTextCursorPosition, outputFile.path)
                }
            }
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
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
                onContentFocusChange = { currentFocusContent = it },
                onContentTextPositionChange = { currentTextCursorPosition = it },
                onContentImageDelete = { },
                modifier = Modifier
                    .padding(horizontal = 16.dp),
            )
        }

        DiaryWriteBottomBar(
            singleImagePicker = singleImagePicker,
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        IconButton(
            onClick = {
                singleImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
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
            onContentImageAdd = { _, _, _ -> },
            onContentImageDelete = { },
            modifier = Modifier,
        )
    }
}
