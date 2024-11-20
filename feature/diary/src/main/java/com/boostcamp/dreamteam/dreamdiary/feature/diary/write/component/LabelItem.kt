package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.clickable { onLabelClick(LabelUi(label)) },
        headlineContent = { Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        trailingContent = {
            Checkbox(
                checked = isChecked,
                onCheckedChange = null,
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun LabelItemPreview() {
    DreamdiaryTheme {
        LabelItem(
            modifier = Modifier
                .width(200.dp)
                .padding(4.dp),
            label = "악몽",
            isChecked = true,
            onLabelClick = {},
        )
    }
}
