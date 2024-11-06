package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun LabelSelectionDialog(
    onDismissRequest: () -> Unit,
    labelList: List<String>, // 어캐하지 -> check 된 것을 같이 넘길까?
    searchValue: String,
    isCheckList: List<Boolean>,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextField(
                        value = searchValue,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("검색") },
                        trailingIcon = {
                            if (searchValue.isNotEmpty()) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Label",
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Label",
                                )
                            }
                        },
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    labelList.forEach {
                        LabelItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                            label = it,
                            onCheckChanged = {},
                        )
                        HorizontalDivider()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text("취소")
                    }
                    TextButton(onClick = { /* TODO: confirm action */ }) {
                        Text("확인")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LabelSelectionDialogPreview() {
    LabelSelectionDialog(
        onDismissRequest = {},
        labelList = listOf("악몽", "개꿈", "귀신"),
        searchValue = "",
        isCheckList = listOf(false, false, false),
        modifier = Modifier.width(200.dp),
    )
}
