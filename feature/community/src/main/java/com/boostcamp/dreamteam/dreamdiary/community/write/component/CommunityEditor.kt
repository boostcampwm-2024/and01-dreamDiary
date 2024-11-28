package com.boostcamp.dreamteam.dreamdiary.community.write.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.request.ImageRequest
import com.boostcamp.dreamteam.dreamdiary.community.R
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.PostContentUi
import com.boostcamp.dreamteam.dreamdiary.designsystem.component.DdAsyncImage
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.ui.util.conditional
import java.io.File

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
    setCurrentFocusContent: (Int) -> Unit,
    setCurrentTextCursorPosition: (Int) -> Unit,
    state: CommunityEditorState,
    modifier: Modifier = Modifier,
) {
    val firstTextFieldFocusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {
        InputTitle(
            title = state.title,
            onTitleChange = state.onTitleChange,
            onNext = { firstTextFieldFocusRequester.requestFocus() },
            modifier = Modifier.fillMaxWidth(),
            readOnly = state.readOnly,
        )
        Spacer(modifier = Modifier.height(24.dp))
        InputBody(
            postContents = state.postContents,
            onContentTextPositionChange = setCurrentFocusContent,
            onContentTextChange = state.onContentTextChange,
            onContentFocusChange = setCurrentTextCursorPosition,
            onContentImageDelete = state.onContentImageDelete,
            modifier = Modifier.fillMaxWidth(),
            firstTextFieldFocusRequester = firstTextFieldFocusRequester,
            readOnly = state.readOnly,
        )
    }
}

@Composable
private fun InputTitle(
    title: String,
    onTitleChange: (String) -> Unit,
    onNext: (KeyboardActionScope.() -> Unit),
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
) {
    BasicTextField(
        value = title,
        onValueChange = onTitleChange,
        modifier = modifier,
        readOnly = readOnly,
        textStyle = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
        ),
        keyboardActions = KeyboardActions(
            onNext = onNext,
        ),
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
        decorationBox = { innerTextField ->
            if (title.isEmpty()) {
                Text(
                    text = stringResource(R.string.community_write_editor_title_placeholder),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                    ),
                )
            }
            innerTextField()
        },
    )
}

@Composable
internal fun InputBody(
    postContents: List<PostContentUi>,
    onContentTextPositionChange: (textPosition: Int) -> Unit,
    onContentTextChange: (contentIndex: Int, String) -> Unit,
    onContentFocusChange: (contentIndex: Int) -> Unit,
    onContentImageDelete: (contentIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
    firstTextFieldFocusRequester: FocusRequester = remember { FocusRequester() },
    readOnly: Boolean = false,
) {
    val firstTextFieldIndex = remember { derivedStateOf { postContents.indexOfFirst { it is PostContentUi.Text } } }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        postContents.forEachIndexed { index, content ->
            when (content) {
                is PostContentUi.Text -> BodyText(
                    textContent = content,
                    onContentTextPositionChange = onContentTextPositionChange,
                    onContentTextChange = { onContentTextChange(index, it) },
                    onFocusChange = { if (it) onContentFocusChange(index) },
                    modifier = Modifier.fillMaxWidth(),
                    focusRequester = if (index == firstTextFieldIndex.value) firstTextFieldFocusRequester else null,
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
    focusRequester: FocusRequester? = null,
    readOnly: Boolean = false,
) {
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
        modifier = modifier
            .conditional(
                condition = focusRequester != null,
                ifTrue = {
                    if (focusRequester == null) {
                        this
                    } else {
                        focusRequester(focusRequester)
                    }
                },
            )
            .onFocusChanged { onFocusChange(it.isFocused) },
        readOnly = readOnly,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
        decorationBox = { innerTextField ->
            if (textFieldValue.text.isEmpty()) {
                Text(
                    text = stringResource(R.string.community_write_editor_body_placeholder),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                    ),
                )
            }
            innerTextField()
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
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(imageContent.path)
                .build(),
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

@Preview(showBackground = true)
@Composable
private fun CommunityEditorPreview() {
    DreamdiaryTheme {
        CommunityEditor(
            setCurrentFocusContent = { },
            setCurrentTextCursorPosition = { },
            state = CommunityEditorState(
                title = "제목",
                onTitleChange = {},
                postContents = listOf(
                    PostContentUi.Text("내용"),
                    PostContentUi.Image("https://example.com/image.jpg"),
                ),
                onContentTextChange = { _, _ -> },
                onContentImageDelete = { },
            ),
        )
    }
}
