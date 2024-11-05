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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryWriteScreen() {
    val scrollState = rememberScrollState()

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
                        // TODO
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
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.AutoMirrored.Outlined.Label, contentDescription = stringResource(R.string.write_category))
                Text(text = "악몽, 개꿈, 귀신")
            }

            Spacer(modifier = Modifier.height(24.dp))

            BasicTextField(
                value = "",
                onValueChange = { /*TODO*/ },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if ("".isEmpty()) {
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
                value = "",
                onValueChange = { /*TODO*/ },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(300.dp),
                decorationBox = { innerTextField ->
                    if ("".isEmpty()) {
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

@Composable
fun LabelSelectMenu() {
    val items = listOf("악몽 으악", "나쁜 꿈", "개꿈", "Menu item")
    val checkedState = remember { mutableStateListOf(false, false, false, false) }

    Column(
        modifier = Modifier
            .width(200.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        items.forEachIndexed { index, text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = text,
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp,
                )
                Checkbox(
                    checked = checkedState[index],
                    onCheckedChange = { checkedState[index] = it },
                )
            }
            if (index < items.size - 1) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewLabelCheck() {
    LabelSelectMenu()
}

@Composable
@Preview(showBackground = true)
fun PreviewDiaryListScreen() {
    DiaryWriteScreen()
}
