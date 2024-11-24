package com.boostcamp.dreamteam.dreamdiary.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.Sync
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
import com.boostcamp.dreamteam.dreamdiary.setting.component.SettingCategory
import com.boostcamp.dreamteam.dreamdiary.setting.component.SettingOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingBackupScreen(onBackClick: () -> Unit) {
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
                            contentDescription = stringResource(R.string.setting_back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(8.dp)
                .verticalScroll(rememberScrollState),
        ) {
            SettingCategory(text = stringResource(R.string.setting_backup_data_backup))
            SettingOption(
                icon = Icons.Outlined.CloudUpload,
                text = stringResource(R.string.setting_backup_now_backup),
            )
            SettingOption(
                icon = Icons.Outlined.Sync,
                text = stringResource(R.string.setting_backup_realtime_sync),
            )
            SettingOption(
                icon = Icons.Outlined.Info,
                text = stringResource(R.string.setting_backup_info),
            )
            SettingOption(
                icon = Icons.Outlined.Alarm,
                text = stringResource(R.string.setting_backup_alarm),
            )
            SettingCategory(text = stringResource(R.string.setting_backup_restore))
            SettingOption(
                icon = Icons.Outlined.CloudDownload,
                text = stringResource(R.string.setting_backup_now_get),
            )
            SettingCategory(stringResource(R.string.setting_backup_reset))
            SettingOption(
                icon = Icons.Outlined.Restore,
                text = stringResource(R.string.setting_backup_reset),
            )
        }
    }
}

@Composable
@Preview
private fun SettingBackupScreenPreview() {
    DreamdiaryTheme {
        SettingBackupScreen(onBackClick = {})
    }
}
