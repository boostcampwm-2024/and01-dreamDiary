package com.boostcamp.dreamteam.dreamdiary.setting.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme

@Composable
internal fun SettingOption(
    icon: ImageVector,
    text: String,
    helpText: String? = null,
    switchOption: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
            )
            if (helpText != null) {
                Text(
                    text = helpText,
                    style = MaterialTheme.typography.bodySmall
                        .copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                )
            }
        }
        if (switchOption) {
            Switch(
                checked = false,
                onCheckedChange = {/* TODO */ },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingOptionPreview() {
    DreamdiaryTheme {
        SettingOption(
            icon = Icons.Outlined.Alarm,
            text = "기상 알림 시간 설정",
            helpText = "10:00",
            modifier = Modifier.width(400.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingOptionSwitchPreview() {
    DreamdiaryTheme {
        SettingOption(
            icon = Icons.Outlined.Alarm,
            text = "기상 알림 시간 설정",
            helpText = "10:00",
            switchOption = true,
            modifier = Modifier.width(400.dp),
        )
    }
}
