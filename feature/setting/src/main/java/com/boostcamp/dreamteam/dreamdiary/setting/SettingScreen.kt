package com.boostcamp.dreamteam.dreamdiary.setting

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.BedtimeOff
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavItem
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavigation
import com.boostcamp.dreamteam.dreamdiary.ui.NavigationItem
import com.boostcamp.dreamteam.dreamdiary.ui.toNavigationItem

@Composable
internal fun SettingScreen(
    onNavigateToDiary: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    settingViewModel: SettingViewModel = hiltViewModel(),
) {
    val onTracking by settingViewModel.onTracking.collectAsStateWithLifecycle()
    val navigationItems = listOf(
        HomeBottomNavItem.MyDream.toNavigationItem(
            onClick = onNavigateToDiary,
        ),
        HomeBottomNavItem.Community.toNavigationItem(
            onClick = onNavigateToCommunity,
        ),
        HomeBottomNavItem.Setting.toNavigationItem(
            onClick = { /* no-op */ },
            isSelected = true,
        ),
    )

    SettingScreenContent(
        navigationItems = navigationItems,
        onLogoutClick = onLogoutClick,
        modifier = modifier,
        signInProvider = settingViewModel.getSignInProvider(),
        userEmail = settingViewModel.getUserEmail(),
        onTracking = onTracking,
        onSignOut = settingViewModel::signOut,
        onNonPasswordSignIn = settingViewModel::nonPasswordSignIn,
        goToLaunchNotificationSetting = settingViewModel::goToLaunchNotificationSetting,
        startTracking = settingViewModel::startTracking,
        stopTracking = settingViewModel::stopTracking,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SettingScreenContent(
    navigationItems: List<NavigationItem>,
    onLogoutClick: () -> Unit,
    signInProvider: String?,
    onSignOut: () -> Unit,
    onNonPasswordSignIn: () -> Unit,
    goToLaunchNotificationSetting: (Activity) -> Unit,
    startTracking: (Context) -> Unit,
    stopTracking: (Context) -> Unit,
    userEmail: String?,
    onTracking: Boolean,
    modifier: Modifier = Modifier,
) {
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
        SettingScreenBody(
            signInProvider = signInProvider,
            userEmail = userEmail,
            onTracking = onTracking,
            onNonPasswordSignIn = onNonPasswordSignIn,
            goToLaunchNotificationSetting = goToLaunchNotificationSetting,
            startTracking = startTracking,
            stopTracking = stopTracking,
            onLogoutClick = onLogoutClick,
            onSignOut = onSignOut,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun SettingScreenBody(
    signInProvider: String?,
    userEmail: String?,
    onTracking: Boolean,
    onNonPasswordSignIn: () -> Unit,
    goToLaunchNotificationSetting: (Activity) -> Unit,
    startTracking: (Context) -> Unit,
    stopTracking: (Context) -> Unit,
    onLogoutClick: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rememberScrollState = rememberScrollState()
    var showSNSDialog by remember { mutableStateOf(false) }
    if (showSNSDialog) {
        AlertDialog(
            onDismissRequest = { showSNSDialog = false },
            title = { Text("$signInProvider") },
            text = { Text("$userEmail") },
            confirmButton = {
                TextButton(onClick = { showSNSDialog = false }) { Text("확인") }
            },
        )
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(rememberScrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val activity = LocalContext.current as Activity
        val context = LocalContext.current

        SettingCategory(text = stringResource(R.string.setting_alarm_setting))
        SettingOption(
            icon = Icons.Outlined.Alarm,
            text = stringResource(R.string.setting_schedule_alarm),
            modifier = Modifier.clickable(onClick = {
                goToLaunchNotificationSetting(activity)
            }),
        )
        if (!onTracking) {
            SettingOption(
                icon = Icons.Default.Bedtime,
                text = stringResource(R.string.setting_notification_diary_on),
                modifier = Modifier.clickable(onClick = {
                    startTracking(context)
                }),
            )
        } else {
            SettingOption(
                icon = Icons.Default.BedtimeOff,
                text = stringResource(R.string.setting_notification_diary_off),
                modifier = Modifier.clickable(onClick = {
                    stopTracking(context)
                }),
            )
        }
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

        if (userEmail == null) {
            SettingOption(
                icon = Icons.AutoMirrored.Outlined.Logout,
                text = stringResource(R.string.setting_login_go),
                modifier = Modifier.clickable(onClick = {
                    onNonPasswordSignIn()
                    onLogoutClick()
                }),
            )
        } else {
            SettingOption(
                icon = Icons.Outlined.AccountBox,
                text = stringResource(R.string.setting_check_account),
                modifier = Modifier.clickable(onClick = {
                    showSNSDialog = true
                }),
            )
            SettingOption(
                icon = Icons.AutoMirrored.Outlined.Logout,
                text = stringResource(R.string.setting_logout),
                modifier = Modifier.clickable(onClick = {
                    onSignOut()
                    onLogoutClick()
                }),
            )
        }

        SettingOption(
            icon = Icons.Outlined.Window,
            text = stringResource(R.string.setting_withdraw),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    val navigationItems = listOf(
        HomeBottomNavItem.MyDream.toNavigationItem(
            onClick = {},
        ),
        HomeBottomNavItem.Community.toNavigationItem(
            onClick = {},
        ),
        HomeBottomNavItem.Setting.toNavigationItem(
            onClick = { /* no-op */ },
            isSelected = true,
        ),
    )
    DreamdiaryTheme {
        SettingScreenContent(
            navigationItems = navigationItems,
            onLogoutClick = {},
            signInProvider = "Google",
            onSignOut = {},
            onNonPasswordSignIn = {},
            userEmail = "someone@example.com",
            onTracking = false,
            goToLaunchNotificationSetting = {},
            startTracking = {},
            stopTracking = {},
        )
    }
}
