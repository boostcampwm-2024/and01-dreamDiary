package com.boostcamp.dreamteam.dreamdiary.feature.diary.component.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.filteredLabelsPreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.selectedLabelsPreview

@Composable
internal fun LabelSelectionDialog(
    onDismissRequest: () -> Unit,
    labelFilter: String,
    filteredLabels: List<LabelUi>,
    selectedLabels: Set<LabelUi>,
    onLabelFilterChange: (String) -> Unit,
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
                TextField(
                    value = labelFilter,
                    onValueChange = { onLabelFilterChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.label_search)) },
                    placeholder = { Text(stringResource(R.string.label_search_or_add)) },
                    trailingIcon = {
                        if (labelFilter.isNotEmpty()) {
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
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onClickLabelSave() }),
                    singleLine = true,
                )

                Column(
                    modifier = Modifier
                        .height(224.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    filteredLabels.forEach { filteredLabel ->
                        LabelItem(
                            modifier = Modifier.fillMaxWidth(),
                            label = filteredLabel.name,
                            isChecked = filteredLabel in selectedLabels,
                            onLabelClick = onCheckChange,
                        )
                    }
                }
                HorizontalDivider()
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TextButton(
                        onClick = {
                            onDismissRequest()
                            onLabelFilterChange("")
                        },
                    ) {
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
private fun LabelSelectionDialogPreviewEmpty() {
    DreamdiaryTheme {
        LabelSelectionDialog(
            onDismissRequest = {},
            labelFilter = "",
            onLabelFilterChange = {},
            onCheckChange = {},
            filteredLabels = emptyList(),
            selectedLabels = emptySet(),
            onClickLabelSave = {},
            modifier = Modifier.width(400.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LabelSelectionDialogPreviewWithItems() {
    DreamdiaryTheme {
        LabelSelectionDialog(
            onDismissRequest = {},
            labelFilter = "",
            onLabelFilterChange = {},
            onCheckChange = {},
            filteredLabels = filteredLabelsPreview,
            selectedLabels = selectedLabelsPreview,
            onClickLabelSave = {},
            modifier = Modifier.width(400.dp),
        )
    }
}