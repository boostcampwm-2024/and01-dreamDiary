package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R

@Composable
internal fun DiaryWriteScreenBody(
    title: String,
    onTitleChange: (String) -> Unit,
    content: String,
    onContentChange: (String) -> Unit,
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

        BasicTextField(
            value = content,
            onValueChange = onContentChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            decorationBox = { innerTextField ->
                if (content.isEmpty()) {
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

@Preview(showBackground = true)
@Composable
private fun DiaryWriteScreenBodyPreview() {
    DreamdiaryTheme {
        DiaryWriteScreenBody(
            title = "",
            onTitleChange = {},
            content = "",
            onContentChange = {},
        )
    }
}
