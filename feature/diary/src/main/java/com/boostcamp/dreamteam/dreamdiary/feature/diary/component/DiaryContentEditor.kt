package com.boostcamp.dreamteam.dreamdiary.feature.diary.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.request.ImageRequest
import com.boostcamp.dreamteam.dreamdiary.designsystem.component.DdAsyncImage
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryContentUi

@Composable
internal fun DiaryContentEditor(
    diaryContentEditorParams: DiaryContentEditorParams,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
) {
    Column(modifier = modifier) {
        InputTitle(
            title = diaryContentEditorParams.title,
            onTitleChange = diaryContentEditorParams.onTitleChange,
            modifier = Modifier.fillMaxWidth(),
            readOnly = readOnly,
        )

        Spacer(modifier = Modifier.height(24.dp))

        InputBody(
            diaryContents = diaryContentEditorParams.diaryContents,
            onContentTextPositionChange = diaryContentEditorParams.onContentTextPositionChange,
            onContentTextChange = diaryContentEditorParams.onContentTextChange,
            onContentFocusChange = diaryContentEditorParams.onContentFocusChange,
            onContentImageDelete = diaryContentEditorParams.onContentImageDelete,
            readOnly = readOnly,
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
    BasicTextField(
        value = title,
        onValueChange = onTitleChange,
        modifier = modifier,
        textStyle = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
        decorationBox = { innerTextField ->
            if (title.isEmpty()) {
                Text(
                    text = stringResource(R.string.write_text_title),
                    style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.secondary),
                )
            }
            innerTextField()
        },
    )
}

@Composable
private fun InputBody(
    diaryContents: List<DiaryContentUi>,
    onContentTextPositionChange: (textPosition: Int) -> Unit,
    onContentTextChange: (contentIndex: Int, String) -> Unit,
    onContentFocusChange: (contentIndex: Int) -> Unit,
    onContentImageDelete: (contentIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
) {
    Column(modifier = modifier) {
        diaryContents.forEachIndexed { index, diaryContent ->
            when (diaryContent) {
                is DiaryContentUi.Image -> BodyImage(
                    diaryContent = diaryContent,
                    onContentImageDelete = { onContentImageDelete(index) },
                    modifier = Modifier.fillMaxWidth(),
                )

                is DiaryContentUi.Text -> BodyText(
                    diaryContent = diaryContent,
                    onContentTextPositionChange = onContentTextPositionChange,
                    onContentTextChange = { onContentTextChange(index, it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { if (it.isFocused) onContentFocusChange(index) },
                )
            }
        }
    }
}

@Composable
private fun BodyImage(
    diaryContent: DiaryContentUi.Image,
    onContentImageDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        DdAsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(diaryContent.path).build(),
            contentDescription = stringResource(R.string.write_content_image),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp),
            error = {
                Icon(
                    imageVector = Icons.Outlined.Error,
                    contentDescription = stringResource(R.string.write_content_image),
                )
            },
        )
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
                contentDescription = stringResource(R.string.write_content_image_delete),
            )
        }
    }
}

@Composable
private fun BodyText(
    diaryContent: DiaryContentUi.Text,
    onContentTextPositionChange: (textPosition: Int) -> Unit,
    onContentTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var textFieldValueState by remember {
        mutableStateOf(TextFieldValue(diaryContent.text))
    }

    val textFieldValue = textFieldValueState.copy(text = diaryContent.text)

    BasicTextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValueState = it

            onContentTextPositionChange(it.selection.end)
            onContentTextChange(it.text)
        },
        modifier = modifier,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
        decorationBox = { innerTextField ->
            if (textFieldValue.text.isEmpty()) {
                Text(
                    text = stringResource(R.string.write_text_content),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.secondary,
                    ),
                )
            }
            innerTextField()
        },
    )
}

internal data class DiaryContentEditorParams(
    val title: String,
    val onTitleChange: (String) -> Unit,
    val diaryContents: List<DiaryContentUi>,
    val onContentTextChange: (Int, String) -> Unit,
    val onContentFocusChange: (Int) -> Unit,
    val onContentTextPositionChange: (Int) -> Unit,
    val onContentImageDelete: (Int) -> Unit,
)

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DiaryWriteScreenBodyPreviewEmpty() {
    DreamdiaryTheme {
        DiaryContentEditor(
            diaryContentEditorParams = DiaryContentEditorParams(
                title = "",
                onTitleChange = {},
                diaryContents = emptyList(),
                onContentTextChange = { _, _ -> },
                onContentFocusChange = {},
                onContentTextPositionChange = {},
                onContentImageDelete = {},
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DiaryWriteScreenBodyPreviewFilled() {
    DreamdiaryTheme {
        DiaryContentEditor(
            diaryContentEditorParams = DiaryContentEditorParams(
                title = "Title",
                onTitleChange = {},
                diaryContents = listOf(
                    DiaryContentUi.Text("Text"),
                    DiaryContentUi.Image("https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"),
                ),
                onContentTextChange = { _, _ -> },
                onContentFocusChange = {},
                onContentTextPositionChange = {},
                onContentImageDelete = {},
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}
