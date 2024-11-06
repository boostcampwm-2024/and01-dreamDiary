package com.boostcamp.dreamteam.dreamdiary.feature.diary.write

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.components.LabelSelectionDialog

@Composable
fun DiaryWriteScreen(viewModel: DiaryWriteViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val (title, content, searchValue, labels, selectedLabels) = uiState

    DiaryWriteScreen(
        title = title,
        content = content,
        searchValue = searchValue,
        onTitleChange = viewModel::setTitle,
        onContentChange = viewModel::setContent,
        onSearchValueChange = viewModel::setSearchValue,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryWriteScreen(
    title: String,
    content: String,
    searchValue: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSearchValueChange: (String) -> Unit,
) {
    val scrollState = rememberScrollState()
    var isLabelSelectionDialogOpen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.write_back),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.write_save),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(
                    modifier = Modifier.clickable {

                    },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Filled.DateRange, contentDescription = stringResource(R.string.write_save_calendar))
                    Text(text = "2024년 10월 28일 월요일")
                }

                Row(
                    modifier = Modifier.clickable {
                        // TODO
                    },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Timer, contentDescription = stringResource(R.string.write_select_time))
                    Text(text = "23:00 ~ 9:00")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        isLabelSelectionDialogOpen = true
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.AutoMirrored.Outlined.Label, contentDescription = stringResource(R.string.write_category))
                Text(text = "악몽, 개꿈, 귀신")
            }

            Spacer(modifier = Modifier.height(24.dp))

            BasicTextField(
                value = title,
                onValueChange = onTitleChange,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
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
                    .padding(horizontal = 16.dp)
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
        if (isLabelSelectionDialogOpen) {
            LabelSelectionDialog(
                labelList = listOf("악몽", "개꿈", "귀신"),
                onDismissRequest = { isLabelSelectionDialogOpen = false },
                searchValue = searchValue,
                searchValueChange = onSearchValueChange,
                selectedLabels = listOf(true, false, false),
                modifier = Modifier.width(400.dp),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewDiaryListScreen() {
    DiaryWriteScreen(
        title = "",
        content = "",
        searchValue = "",
        onTitleChange = {},
        onContentChange = {},
        onSearchValueChange = {},
    )
}
