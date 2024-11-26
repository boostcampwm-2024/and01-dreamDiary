package com.boostcamp.dreamteam.dreamdiary.community.write.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil3.request.ImageRequest
import com.boostcamp.dreamteam.dreamdiary.community.R
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.PostContentUi
import com.boostcamp.dreamteam.dreamdiary.designsystem.component.DdAsyncImage

internal data class CommunityEditorState(
    val title: String,
    val onTitleChange: (String) -> Unit,
    val postContents: List<PostContentUi>,
    val onContentTextChange: (contentIndex: Int, String) -> Unit,
    val onContentImageDelete: (contentIndex: Int) -> Unit,
    val readOnly: Boolean = false,
)

@Composable
internal fun CommunityEditor(
    state: CommunityEditorState,
    modifier: Modifier = Modifier,
) {
    var currentFocusContent by remember { mutableIntStateOf(0) }
    var currentTextCursorPosition by remember { mutableIntStateOf(0) }

    Column(modifier = modifier) {
        InputTitle(
            title = state.title,
            onTitleChange = state.onTitleChange,
            modifier = Modifier.fillMaxWidth(),
            readOnly = state.readOnly,
        )
        Spacer(modifier = Modifier.height(24.dp))
        InputBody(
            postContents = state.postContents,
            onContentTextPositionChange = { currentTextCursorPosition = it },
            onContentTextChange = state.onContentTextChange,
            onContentFocusChange = { currentFocusContent = it },
            onContentImageDelete = state.onContentImageDelete,
            modifier = Modifier.fillMaxWidth(),
            readOnly = state.readOnly,
        )
    }
}

@Composable
private fun InputTitle(
    title: String,
    onTitleChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
) {
    var isTitleFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = title,
        onValueChange = onTitleChange,
        modifier = modifier.onFocusChanged { isTitleFocused = it.isFocused },
        readOnly = readOnly,
        textStyle = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
        decorationBox = { innerTextField ->
            if (title.isEmpty() && !isTitleFocused) {
                Text(
                    text = stringResource(R.string.community_write_editor_title_placeholder),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                    ),
                )
            } else {
                innerTextField()
            }
        },
    )
}

@Composable
private fun InputBody(
    postContents: List<PostContentUi>,
    onContentTextPositionChange: (textPosition: Int) -> Unit,
    onContentTextChange: (contentIndex: Int, String) -> Unit,
    onContentFocusChange: (contentIndex: Int) -> Unit,
    onContentImageDelete: (contentIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
) {
    LazyColumn(modifier = modifier) {
        items(count = postContents.size) { index ->
            when (val content = postContents[index]) {
                is PostContentUi.Text -> BodyText(
                    textContent = content,
                    onContentTextPositionChange = onContentTextPositionChange,
                    onContentTextChange = { onContentTextChange(index, it) },
                    onFocusChange = { if (it) onContentFocusChange(index) },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = readOnly,
                )

                is PostContentUi.Image -> BodyImage(
                    imageContent = content,
                    onContentImageDelete = { onContentImageDelete(index) },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = readOnly,
                )
            }
        }
    }
}

@Composable
private fun BodyText(
    textContent: PostContentUi.Text,
    onContentTextPositionChange: (textPosition: Int) -> Unit,
    onContentTextChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
) {
    var isFocused by remember { mutableStateOf(false) }
    var textFieldValueState by remember {
        mutableStateOf(TextFieldValue(textContent.text))
    }

    val textFieldValue = textFieldValueState.copy(text = textContent.text)

    BasicTextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValueState = it
            onContentTextPositionChange(it.selection.end)
            onContentTextChange(it.text)
        },
        modifier = modifier.onFocusChanged {
            isFocused = it.isFocused
            onFocusChange(it.isFocused)
        },
        readOnly = readOnly,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
        decorationBox = { innerTextField ->
            if (textFieldValue.text.isEmpty() && !isFocused) {
                Text(
                    text = stringResource(R.string.community_write_editor_body_placeholder),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                    ),
                )
            } else {
                innerTextField()
            }
        },
    )
}

@Composable
private fun BodyImage(
    imageContent: PostContentUi.Image,
    onContentImageDelete: () -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
) {
    Box(modifier = modifier) {
        DdAsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(imageContent.path).build(),
            contentDescription = stringResource(R.string.community_write_editor_image_description),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp),
            error = {
                Icon(
                    imageVector = Icons.Outlined.Error,
                    contentDescription = stringResource(R.string.community_write_editor_image_error),
                )
            },
        )
        if (!readOnly) {
            FilledIconButton(
                onClick = { onContentImageDelete() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                colors = IconButtonDefaults.filledIconButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.8f),
                    contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                ),
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.community_write_editor_image_delete),
                )
            }
        }
    }
}