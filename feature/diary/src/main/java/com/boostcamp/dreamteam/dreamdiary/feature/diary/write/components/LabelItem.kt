package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun LabelItem(
    modifier: Modifier = Modifier,
    label: String,
    isChecked: Boolean,
    onCheckChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label)
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckChanged,
            modifier = Modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LabelItemPreview() {
    LabelItem(
        modifier = Modifier
            .width(200.dp)
            .padding(4.dp),
        label = "악몽",
        isChecked = true,
        onCheckChanged = {},
    )
}
