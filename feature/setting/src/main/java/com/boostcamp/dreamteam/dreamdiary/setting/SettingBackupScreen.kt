package com.boostcamp.dreamteam.dreamdiary.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable

internal fun SettingBackupScreen(
    onBackClick: () -> Unit
) {
    val rememberScrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.setting_backup_title))
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(8.dp)
                .verticalScroll(rememberScrollState)
        ) {
            SettingCategory(text = "데이터 백업")
            SettingOption(
                icon = Icons.Outlined.Alarm,
                text = "지금 내보내기",
            )
            SettingOption(
                icon = Icons.Outlined.Alarm,
                text = "실시간 동기화",
            )
            SettingOption(
                icon = Icons.Outlined.Alarm,
                text = "백업 정보",
            )
            SettingOption(
                icon = Icons.Outlined.Alarm,
                text = "백업 알림",
            )
        }
    }
}

@Composable
@Preview
private fun SettingBackupScreenPreview() {
    DreamdiaryTheme() {
        SettingBackupScreen(onBackClick = {})
    }
}
