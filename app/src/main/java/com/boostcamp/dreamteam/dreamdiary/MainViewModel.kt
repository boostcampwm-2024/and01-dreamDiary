package com.boostcamp.dreamteam.dreamdiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.SettingThemeRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.setting.SettingTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingThemeRepository: SettingThemeRepository,
) : ViewModel() {

    // TODO flowWithStarted로 변경
    val uiState = settingThemeRepository.getTheme()
        .map {
            when (it) {
                SettingTheme.SYSTEM -> MainUiState(SettingThemeUiState.System)
                SettingTheme.LIGHT -> MainUiState(SettingThemeUiState.Light)
                SettingTheme.DARK -> MainUiState(SettingThemeUiState.Dark)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, MainUiState())
}
