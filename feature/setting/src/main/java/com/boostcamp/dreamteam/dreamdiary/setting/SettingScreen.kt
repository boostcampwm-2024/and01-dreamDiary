package com.boostcamp.dreamteam.dreamdiary.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
    onNavigateToSettingNotification: () -> Unit,
    onNavigateToSettingBackup: () -> Unit,
    onNavigateToSettingTheme: () -> Unit,
    onGoToSignInClick: () -> Unit,
    modifier: Modifier = Modifier,
    settingViewModel: SettingViewModel = hiltViewModel(),
) {
    val email by settingViewModel.email.collectAsStateWithLifecycle()
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
        onGoToSignInClick = onGoToSignInClick,
        onNavigateToSettingNotification = onNavigateToSettingNotification,
        onNavigateToSettingBackup = onNavigateToSettingBackup,
        onNavigateToSettingTheme = onNavigateToSettingTheme,
        modifier = modifier,
        signInProvider = settingViewModel.getSignInProvider(),
        userEmail = email,
        onSignOut = settingViewModel::signOut,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SettingScreenContent(
    navigationItems: List<NavigationItem>,
    onGoToSignInClick: () -> Unit,
    onNavigateToSettingNotification: () -> Unit,
    onNavigateToSettingBackup: () -> Unit,
    onNavigateToSettingTheme: () -> Unit,
    signInProvider: String?,
    onSignOut: () -> Unit,
    userEmail: String?,
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
            onGoToSignInClick = onGoToSignInClick,
            onNavigateToSettingNotification = onNavigateToSettingNotification,
            onNavigateToSettingBackup = onNavigateToSettingBackup,
            onNavigateToSettingTheme = onNavigateToSettingTheme,
            onSignOut = onSignOut,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun SettingScreenBody(
    signInProvider: String?,
    userEmail: String?,
    onGoToSignInClick: () -> Unit,
    onNavigateToSettingNotification: () -> Unit,
    onNavigateToSettingBackup: () -> Unit,
    onNavigateToSettingTheme: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rememberScrollState = rememberScrollState()
    var showSNSDialog by remember { mutableStateOf(false) }
    var signOutDialog by remember { mutableStateOf(false) }
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
    if (signOutDialog) {
        AlertDialog(
            onDismissRequest = { signOutDialog = false },
            text = { Text(stringResource(R.string.setting_signout_dialog)) },
            confirmButton = {
                TextButton(onClick = {
                    onSignOut()
                    signOutDialog = false
                }) { Text("확인") }
            },
            dismissButton = {
                TextButton(onClick = { signOutDialog = false }) { Text("취소") }
            },
        )
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SettingCategory(text = stringResource(R.string.setting_alarm_setting))
        SettingOption(
            icon = Icons.Outlined.Alarm,
            text = stringResource(R.string.setting_alarm_setting),
            onClick = onNavigateToSettingNotification,
        )
        SettingCategory(text = stringResource(R.string.setting_data_restore))
        SettingOption(
            icon = Icons.Outlined.CloudUpload,
            text = stringResource(R.string.setting_data_restore),
            onClick = onNavigateToSettingBackup,
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
            onClick = {
                onNavigateToSettingTheme()
            },
        )
        SettingOption(
            icon = Icons.Outlined.Lock,
            text = stringResource(R.string.setting_lock_setting),
        )

        if (userEmail == null) {
            SettingOption(
                icon = Icons.AutoMirrored.Outlined.Logout,
                text = stringResource(R.string.setting_login_go),
                onClick = {
                    onGoToSignInClick()
                },
            )
        } else {
            SettingOption(
                icon = Icons.Outlined.AccountBox,
                text = stringResource(R.string.setting_check_account),
                onClick = {
                    showSNSDialog = true
                },
            )
            SettingOption(
                icon = Icons.AutoMirrored.Outlined.Logout,
                text = stringResource(R.string.setting_logout),
                onClick = {
                    signOutDialog = true
                },
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
            onGoToSignInClick = {},
            onNavigateToSettingNotification = {},
            onNavigateToSettingBackup = {},
            onNavigateToSettingTheme = {},
            signInProvider = "Google",
            onSignOut = {},
            userEmail = "someone@example.com",
        )
    }
}
