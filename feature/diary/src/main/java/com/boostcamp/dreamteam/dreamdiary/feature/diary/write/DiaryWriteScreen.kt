package com.boostcamp.dreamteam.dreamdiary.feature.diary.write

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryWriteScreen() {

    val scrollState = rememberScrollState()

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "") },
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Save"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.clickable {
                            /*TODO*/
                        }
                    ) {
                        Icon(Icons.Filled.DateRange, contentDescription = "날짜 선택")
                        Text(text = "2024년 10월 28일 월요일")
                    }

                    Row(
                        modifier = Modifier.clickable {
                            /*TODO*/
                        }
                    ) {
                        Icon(Icons.Outlined.Timer, contentDescription = "시간 선택")
                        Text(text = "23:00 ~ 9:00")
                    }
                }

                Box(modifier = Modifier.padding(vertical = 4.dp))

                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row {
                        Icon(Icons.AutoMirrored.Outlined.Label, contentDescription = "카테고리")
                        Text(text = "악몽, 개꿈, 귀신")
                    }
                }

                Box(modifier = Modifier.padding(vertical = 12.dp))

                BasicTextField(
                    value = "",
                    onValueChange = { /*TODO*/ },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if ("".isEmpty()) {
                            Text(
                                text = "제목을 입력하세요",
                                style = TextStyle(color = Color.Gray)
                            )
                        }
                        innerTextField()
                    }
                )

                Box(modifier = Modifier.padding(vertical = 12.dp))

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
                                text = "내용을 입력하세요",
                                style = TextStyle(color = Color.Gray)
                            )
                        }
                        innerTextField()
                    }
                )
            }
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
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        items.forEachIndexed { index, text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp
                )
                Checkbox(
                    checked = checkedState[index],
                    onCheckedChange = { checkedState[index] = it }
                )
            }
            if (index < items.size - 1) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )
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
