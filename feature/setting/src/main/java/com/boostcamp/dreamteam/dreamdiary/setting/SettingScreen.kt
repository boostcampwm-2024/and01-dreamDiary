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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingScreen(
    modifier: Modifier = Modifier,
) {
    val rememberScrollState = rememberScrollState()
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "설정")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(8.dp)
                .verticalScroll(rememberScrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SettingCategory(text = "알림 설정")
            SettingOption(icon = Icons.Outlined.Alarm, text = "일정 알림")
            SettingOption(icon = Icons.AutoMirrored.Outlined.Comment, text = "댓글 알림")

            SettingCategory(text = "데이터 백업 및 복원")
            SettingOption(icon = Icons.Outlined.CloudUpload, text = "데이터 백업 및 복원")
            SettingOption(icon = Icons.Outlined.ResetTv, text = "초기화")

            SettingCategory(text = "커뮤니케이션")
            SettingOption(icon = Icons.Outlined.NoAccounts, text = "차단한 사용자")
            SettingOption(icon = Icons.Outlined.PeopleOutline, text = "구독중")
            SettingOption(icon = Icons.Outlined.Image, text = "꿈 사진으로 보기")

            SettingCategory(text = "정보")
            SettingOption(icon = Icons.Outlined.DarkMode, text = "다크모드 설정")
            SettingOption(icon = Icons.Outlined.Lock, text = "잠금설정")
            SettingOption(icon = Icons.Outlined.AccountBox, text = "로그인된 SNS 계정 확인")
            SettingOption(icon = Icons.AutoMirrored.Outlined.Logout, text = "로그아웃")
            SettingOption(icon = Icons.Outlined.Window, text = "탈퇴")

        }
    }
}

@Composable
private fun SettingCategory(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun SettingOption(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = text, style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    DreamdiaryTheme {
        SettingScreen()
    }
}
