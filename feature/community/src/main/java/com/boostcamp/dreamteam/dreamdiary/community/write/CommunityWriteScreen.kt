package com.boostcamp.dreamteam.dreamdiary.community.write

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boostcamp.dreamteam.dreamdiary.community.R
import com.boostcamp.dreamteam.dreamdiary.community.write.component.CommunityEditor
import com.boostcamp.dreamteam.dreamdiary.community.write.component.CommunityEditorState
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@Composable
fun CommunityWriteScreen(
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CommunityWriteViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CommunityWriteScreenContent(
        onContentImageAdd = viewModel::addContentImage,
        topAppbarState = CommunityWriteTopAppbarState(
            onClickBack = onClickBack,
            onClickSave = viewModel::writePost,
        ),
        editorState = CommunityEditorState(
            title = state.editorState.title,
            onTitleChange = viewModel::setTitle,
            postContents = state.editorState.contents,
            onContentTextChange = viewModel::setContentText,
            onContentImageDelete = viewModel::deleteContentImage,
        ),
        modifier = modifier,
    )
}

@Composable
private fun CommunityWriteScreenContent(
    onContentImageAdd: (contentIndex: Int, textPosition: Int, imagePath: String) -> Unit,
    topAppbarState: CommunityWriteTopAppbarState,
    editorState: CommunityEditorState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    val (currentFocusContent, setCurrentFocusContent) = remember { mutableIntStateOf(0) }
    val (currentTextCursorPosition, setCurrentTextCursorPosition) = remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    val singleImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION,
                )
                coroutineScope.launch(Dispatchers.IO) {
                    val fileName = UUID.randomUUID().toString()
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val outputFile = File(context.filesDir, fileName)
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
                            fileName,
                        )
                    }
                }
            }
        },
    )

    Scaffold(
        modifier = modifier.imePadding(),
        topBar = {
            CommunityWriteTopAppbar(params = topAppbarState)
        },
        bottomBar = {
            CommunityWriteBottomBar(
                singleImagePicker = singleImagePicker,
            )
        },
    ) { paddingValues ->
        CommunityEditor(
            setCurrentFocusContent = setCurrentFocusContent,
            setCurrentTextCursorPosition = setCurrentTextCursorPosition,
            state = editorState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CommunityWriteTopAppbar(
    params: CommunityWriteTopAppbarState,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.community_write_title)) },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = params.onClickBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.community_write_toppappbar_navigate_back),
                )
            }
        },
        actions = {
            IconButton(
                onClick = params.onClickSave,
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.community_write_toppappbar_action_save),
                )
            }
        },
    )
}

private data class CommunityWriteTopAppbarState(
    val onClickBack: () -> Unit,
    val onClickSave: () -> Unit,
)

@Composable
private fun CommunityWriteBottomBar(
    singleImagePicker: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .navigationBarsPadding(),
    ) {
        IconButton(
            onClick = {
                singleImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
        ) {
            Icon(
                imageVector = Icons.Outlined.AddPhotoAlternate,
                contentDescription = stringResource(R.string.community_write_editor_bottombar_image_add),
            )
        }
    }
}

@Preview
@Composable
private fun CommunityWriteScreenContentPreview() {
    DreamdiaryTheme {
        CommunityWriteScreenContent(
            onContentImageAdd = { _, _, _ -> },
            topAppbarState = CommunityWriteTopAppbarState(
                onClickBack = { },
                onClickSave = { },
            ),
            editorState = CommunityEditorState(
                title = "",
                onTitleChange = { },
                postContents = emptyList(),
                onContentTextChange = { _, _ -> },
                onContentImageDelete = { },
            ),
        )
    }
}
