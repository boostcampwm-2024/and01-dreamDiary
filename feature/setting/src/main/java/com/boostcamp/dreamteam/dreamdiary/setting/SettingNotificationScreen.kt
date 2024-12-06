package com.boostcamp.dreamteam.dreamdiary.setting

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.BedtimeOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.setting.component.SettingCategory
import com.boostcamp.dreamteam.dreamdiary.setting.component.SettingOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingNotificationScreen(
    onBackClick: () -> Unit,
    viewModel: SettingNotificationViewModel = hiltViewModel(),
) {
    val rememberScrollState = rememberScrollState()
    val onTracking by viewModel.onTracking.collectAsStateWithLifecycle()
    val activity = LocalContext.current as Activity
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.setting_alarm_setting))
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
            SettingCategory(text = stringResource(R.string.setting_notification_authority))
            SettingOption(
                icon = Icons.Default.Accessibility,
                text = stringResource(R.string.setting_notification_authority),
                onClick = { viewModel.goToLaunchNotificationSetting(activity) },
            )
            SettingCategory(text = stringResource(R.string.setting_notification_wakeup))
            SettingOption(
                icon = if (onTracking) Icons.Default.BedtimeOff else Icons.Default.Bedtime,
                text = if (onTracking) {
                    stringResource(
                        R.string.setting_notification_diary_off,
                    )
                } else {
                    stringResource(R.string.setting_notification_diary_on)
                },
                onClick = { if (onTracking) viewModel.stopTracking(context) else viewModel.startTracking(context) },
                switchOption = true,
                checked = onTracking,
            )
            SettingCategory(text = stringResource(R.string.setting_comment_alarm))
            SettingOption(
                icon = Icons.AutoMirrored.Outlined.Comment,
                text = stringResource(R.string.setting_comment_alarm),
            )
        }
    }
}

@Composable
@Preview
private fun SettingNotificationScreenPreview() {
    DreamdiaryTheme {
        SettingNotificationScreen(onBackClick = {})
    }
}
