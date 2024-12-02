package com.boostcamp.dreamteam.dreamdiary

data class MainUiState(
    val settingThemeUiState: SettingThemeUiState = SettingThemeUiState.Loading,
)

sealed class SettingThemeUiState {
    data object Loading : SettingThemeUiState()

    data object System : SettingThemeUiState()

    data object Light : SettingThemeUiState()

    data object Dark : SettingThemeUiState()
}
