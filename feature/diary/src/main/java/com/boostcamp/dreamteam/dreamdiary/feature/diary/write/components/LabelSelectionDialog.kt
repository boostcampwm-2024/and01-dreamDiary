package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LabelSelectionDialog(
    modifier: Modifier = Modifier,
    labelList: List<String> = listOf("악몽", "개꿈", "귀신"),
) {
    AlertDialog(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("검색") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Label",
                        )
                    },
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // label list
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
        },
        onDismissRequest = { /*TODO*/ },
        confirmButton = {
            TextButton(
                onClick = { /*TODO*/ },
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { /*TODO*/ },
            ) {
                Text("취소")
            }
        },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun LabelSelectionDialogPreview() {
    LabelSelectionDialog(
        modifier = Modifier,
        labelList = listOf("악몽", "개꿈", "귀신"),
    )
}
