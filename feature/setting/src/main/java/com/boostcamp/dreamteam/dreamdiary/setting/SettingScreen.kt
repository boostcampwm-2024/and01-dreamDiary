package com.boostcamp.dreamteam.dreamdiary.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.NoAccounts
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.ResetTv
import androidx.compose.material.icons.outlined.Window
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavItem
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavigation
import com.boostcamp.dreamteam.dreamdiary.ui.NavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingScreen(
    modifier: Modifier = Modifier,
    onNavigateToWriteScreen: () -> Unit,
    onNavigateToCommunity: () -> Unit,
) {
    val rememberScrollState = rememberScrollState()

    val navigationItems = listOf(
        NavigationItem(
            icon = HomeBottomNavItem.MyDream.icon,
            labelRes = HomeBottomNavItem.MyDream.label,
            isSelected = false,
            onClick = onNavigateToWriteScreen,
        ),
        NavigationItem(
            icon = HomeBottomNavItem.Community.icon,
            labelRes = HomeBottomNavItem.Community.label,
            isSelected = false,
            onClick = onNavigateToCommunity,
        ),
        NavigationItem(
            icon = HomeBottomNavItem.Setting.icon,
            labelRes = HomeBottomNavItem.Setting.label,
            isSelected = true,
            onClick = {},
        ),
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.setting_title))
                },
            )
        },
        bottomBar = {
            HomeBottomNavigation(items = navigationItems)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(8.dp)
                .verticalScroll(rememberScrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SettingCategory(text = stringResource(R.string.setting_alarm_setting))
            SettingOption(
                icon = Icons.Outlined.Alarm,
                text = stringResource(R.string.setting_schedule_alarm),
            )
            SettingOption(
                icon = Icons.AutoMirrored.Outlined.Comment,
                text = stringResource(R.string.setting_comment_alarm),
            )

            SettingCategory(text = stringResource(R.string.setting_data_restore))
            SettingOption(
                icon = Icons.Outlined.CloudUpload,
                text = stringResource(R.string.setting_data_restore),
            )
            SettingOption(
                icon = Icons.Outlined.ResetTv,
                text = stringResource(R.string.setting_reset),
            )

            SettingCategory(text = stringResource(R.string.setting_communication))
            SettingOption(
                icon = Icons.Outlined.NoAccounts,
                text = stringResource(R.string.setting_block),
            )
            SettingOption(
                icon = Icons.Outlined.PeopleOutline,
                text = stringResource(R.string.setting_subscribe),
            )
            SettingOption(
                icon = Icons.Outlined.Image,
                text = stringResource(R.string.setting_picture),
            )

            SettingCategory(text = stringResource(R.string.setting_information))
            SettingOption(
                icon = Icons.Outlined.DarkMode,
                text = stringResource(R.string.setting_darkmode),
            )
            SettingOption(
                icon = Icons.Outlined.Lock,
                text = stringResource(R.string.setting_lock_setting),
            )
            SettingOption(
                icon = Icons.Outlined.AccountBox,
                text = stringResource(R.string.setting_check_account),
            )
            SettingOption(
                icon = Icons.AutoMirrored.Outlined.Logout,
                text = stringResource(R.string.setting_logout),
            )
            SettingOption(
                icon = Icons.Outlined.Window,
                text = stringResource(R.string.setting_withdraw),
            )
        }
    }
}

@Composable
private fun SettingCategory(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun SettingOption(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp),
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    DreamdiaryTheme {
        SettingScreen(
            onNavigateToWriteScreen = {},
            onNavigateToCommunity = {},
        )
    }
}
