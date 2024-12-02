package com.boostcamp.dreamteam.dreamdiary.feature.diary.component.component

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
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
    onDeleteLabel: (labelUi: LabelUi) -> Unit,
    onClickLabelSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (pendingLabelToDelete, setPendingLabelToDelete) = rememberSaveable { mutableStateOf<LabelUi?>(null) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            modifier = modifier,
        ) {
            if (pendingLabelToDelete != null) {
                LabelDeleteConfirmDialog(
                    label = pendingLabelToDelete,
                    onClickConfirm = {
                        onDeleteLabel(pendingLabelToDelete)
                        setPendingLabelToDelete(null)
                    },
                    onClickCancel = { setPendingLabelToDelete(null) },
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                LabelSearchDialog(
                    labelFilter = labelFilter,
                    onLabelFilterChange = onLabelFilterChange,
                    onClickLabelSave = onClickLabelSave,
                    filteredLabels = filteredLabels,
                    selectedLabels = selectedLabels,
                    onCheckChange = onCheckChange,
                    onDeleteLabel = { setPendingLabelToDelete(it) },
                    onDismissRequest = onDismissRequest,
                )
            }
        }
    }
}

@Composable
private fun LabelDeleteConfirmDialog(
    label: LabelUi,
    onClickConfirm: () -> Unit,
    onClickCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = label.name,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = stringResource(R.string.label_delete_confirm), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TextButton(onClick = onClickCancel) {
                Text("취소하기")
            }
            TextButton(onClick = onClickConfirm) {
                Text("삭제하기", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun LabelSearchDialog(
    labelFilter: String,
    onLabelFilterChange: (String) -> Unit,
    onClickLabelSave: () -> Unit,
    filteredLabels: List<LabelUi>,
    selectedLabels: Set<LabelUi>,
    onCheckChange: (labelUi: LabelUi) -> Unit,
    onDeleteLabel: (labelUi: LabelUi) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
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
                    onDeleteLabel = { onDeleteLabel(filteredLabel) },
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
            onDeleteLabel = { },
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
            onDeleteLabel = { },
        )
    }
}
