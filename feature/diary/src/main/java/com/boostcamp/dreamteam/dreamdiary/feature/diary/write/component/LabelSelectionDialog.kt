package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
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
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.SelectableLabel

@Composable
internal fun LabelSelectionDialog(
    onDismissRequest: () -> Unit,
    searchValue: String,
    selectableLabels: List<SelectableLabel>,
    onSearchValueChange: (String) -> Unit,
    onCheckChange: (labelUi: LabelUi) -> Unit,
    onClickLabelSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            modifier = modifier,
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextField(
                        value = searchValue,
                        onValueChange = { onSearchValueChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("검색") },
                        placeholder = { Text("검색 및 추가") },
                        trailingIcon = {
                            if (searchValue.isNotEmpty()) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Label",
                                    modifier = Modifier.clickable {
                                        onClickLabelSave()
                                    },
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Label",
                                )
                            }
                        },
                        singleLine = true,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                ) {
                    selectableLabels.forEachIndexed { index, selectableLabel ->
                        LabelItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                            label = selectableLabel.label.name,
                            isChecked = selectableLabels[index].isSelected,
                            onLabelClick = onCheckChange,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TextButton(onClick = {
                        onDismissRequest()
                        onSearchValueChange("")
                    }) {
                        Text("취소")
                    }
                    TextButton(onClick = { onDismissRequest() }) {
                        Text("확인")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LabelSelectionDialogPreview() {
    DreamdiaryTheme {
        LabelSelectionDialog(
            onDismissRequest = {},
            searchValue = "",
            onSearchValueChange = {},
            onCheckChange = {},
            selectableLabels = listOf(
                SelectableLabel(LabelUi("악몽"), isSelected = true),
                SelectableLabel(LabelUi("개꿈"), isSelected = false),
                SelectableLabel(LabelUi("귀신"), isSelected = false),
            ),
            onClickLabelSave = {},
            modifier = Modifier.width(400.dp),
        )
    }
}