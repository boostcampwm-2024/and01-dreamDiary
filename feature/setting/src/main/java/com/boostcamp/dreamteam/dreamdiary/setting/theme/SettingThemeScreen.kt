package com.boostcamp.dreamteam.dreamdiary.setting.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boostcamp.dreamteam.dreamdiary.core.model.setting.SettingTheme
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.setting.R

@Composable
fun SettingThemeScreen(
    settingThemeViewModel: SettingThemeViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val settingTheme by settingThemeViewModel.settingTheme.collectAsStateWithLifecycle()
    SettingThemeScreenContent(
        onBackClick = onBackClick,
        settingTheme = settingTheme,
        onSettingThemeChange = settingThemeViewModel::setSettingTheme,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingThemeScreenContent(
    onBackClick: () -> Unit,
    settingTheme: SettingTheme,
    onSettingThemeChange: (SettingTheme) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.setting_theme_title))
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
    ) {
        Column(
            Modifier
                .padding(it)
                .selectableGroup(),
        ) {
            SettingThemeRadio(
                text = stringResource(R.string.setting_theme_system),
                selected = settingTheme == SettingTheme.SYSTEM,
                onClick = { onSettingThemeChange(SettingTheme.SYSTEM) },
            )
            SettingThemeRadio(
                text = stringResource(R.string.setting_theme_light),
                selected = settingTheme == SettingTheme.LIGHT,
                onClick = { onSettingThemeChange(SettingTheme.LIGHT) },
            )
            SettingThemeRadio(
                text = stringResource(R.string.setting_theme_dark),
                selected = settingTheme == SettingTheme.DARK,
                onClick = { onSettingThemeChange(SettingTheme.DARK) },
            )
        }
    }
}

@Composable
fun SettingThemeRadio(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@Preview
@Composable
private fun SettingThemeScreenContentPreview() {
    DreamdiaryTheme {
        SettingThemeScreenContent(
            onBackClick = {},
            settingTheme = SettingTheme.SYSTEM,
            onSettingThemeChange = {},
        )
    }
}
