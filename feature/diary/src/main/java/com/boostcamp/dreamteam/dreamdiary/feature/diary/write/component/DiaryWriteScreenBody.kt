package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryContentUi

@Composable
internal fun DiaryWriteScreenBody(
    title: String,
    onTitleChange: (String) -> Unit,
    content: String,
    onContentChange: (String) -> Unit,
    diaryContents: List<DiaryContentUi>,
    onContentFocusChange: (contentIndex: Int) -> Unit,
    onContentTextPositionChange: (textPosition: Int) -> Unit,
    onContentTextChange: (contentIndex: Int, String) -> Unit,
    onContentImageDelete: (DiaryContentUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        BasicTextField(
            value = title,
            onValueChange = onTitleChange,
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                if (title.isEmpty()) {
                    Text(
                        text = stringResource(R.string.write_text_title),
                        style = TextStyle(color = MaterialTheme.colorScheme.secondary),
                    )
                }
                innerTextField()
            },
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column {
            for ((index, diaryContent) in diaryContents.withIndex()) {
                when (diaryContent) {
                    is DiaryContentUi.Image -> AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(diaryContent.path)
                            .build(),
                        contentDescription = stringResource(R.string.write_content_image),
                        modifier = Modifier
                            .fillMaxWidth(),
                    )

                    is DiaryContentUi.Text -> {
                        var textFieldValueState by remember {
                            mutableStateOf(TextFieldValue(diaryContent.text))
                        }

                        val textFieldValue = textFieldValueState.copy(text = diaryContent.text)

                        BasicTextField(
                            value = textFieldValue,
                            onValueChange = {
                                textFieldValueState = it

                                onContentTextPositionChange(it.selection.end)
                                onContentTextChange(index, it.text)
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .onFocusChanged {
                                    if (it.isFocused) {
                                        onContentFocusChange(index)
                                    }
                                },
                            decorationBox = { innerTextField ->
                                if (textFieldValue.text.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.write_text_content),
                                        style = TextStyle(color = MaterialTheme.colorScheme.secondary),
                                    )
                                }
                                innerTextField()
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryWriteScreenBodyPreview() {
    DreamdiaryTheme {
        DiaryWriteScreenBody(
            title = "",
            onTitleChange = {},
            content = "",
            onContentChange = {},
            diaryContents = listOf(DiaryContentUi.Text("안녕\n뉴라인")),
            onContentTextChange = { _, _ -> },
            onContentFocusChange = { },
            onContentTextPositionChange = { },
            onContentImageDelete = { },
        )
    }
}
