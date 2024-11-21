package com.boostcamp.dreamteam.dreamdiary.feature.diary.component.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi

@Composable
internal fun LabelItem(
    label: String,
    isChecked: Boolean,
    onLabelClick: (LabelUi) -> Unit,
    onDeleteLabel: () -> Unit,
    onEditLabel: (newValue: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isEditMode by remember { mutableStateOf(false) }

    if (isEditMode) {
        var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = label, selection = TextRange(label.length))) }
        ListItem(
            modifier = modifier,
            headlineContent = {
                val focusRequester = remember { FocusRequester() }
                LaunchedEffect(Unit) { focusRequester.requestFocus() }

                BasicTextField(
                    value = textFieldValueState,
                    onValueChange = { textFieldValueState = it },
                    modifier = Modifier
                        .clickable { isEditMode = false }
                        .focusRequester(focusRequester),
                )
            },
            trailingContent = {
                IconButton(
                    onClick = {
                        onEditLabel(textFieldValueState.text)
                        isEditMode = false
                    },
                    content = { Icon(imageVector = Icons.Outlined.Check, contentDescription = "수정 완료") },
                )
            },
        )
    } else {
        ListItem(
            modifier = modifier.clickable { onLabelClick(LabelUi(label)) },
            leadingContent = {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = null,
                )
            },
            headlineContent = {
                Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis)
            },
            trailingContent = {
                Row {
                    IconButton(
                        onClick = { isEditMode = true },
                        content = { Icon(imageVector = Icons.Outlined.Edit, contentDescription = "수정") },
                    )
                    IconButton(
                        onClick = onDeleteLabel,
                        content = { Icon(imageVector = Icons.Outlined.Delete, contentDescription = "삭제") },
                    )
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LabelItemPreview() {
    DreamdiaryTheme {
        LabelItem(
            modifier = Modifier
                .width(300.dp)
                .padding(4.dp),
            label = "악몽",
            isChecked = true,
            onLabelClick = {},
            onDeleteLabel = {},
            onEditLabel = {},
        )
    }
}
