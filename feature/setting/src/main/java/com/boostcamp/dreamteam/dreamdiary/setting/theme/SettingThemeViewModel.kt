package com.boostcamp.dreamteam.dreamdiary.setting.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.SettingThemeRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.setting.SettingTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingThemeViewModel @Inject constructor(
    private val settingThemeRepository: SettingThemeRepository,
) : ViewModel() {
    val settingTheme = settingThemeRepository
        .getTheme()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingTheme.SYSTEM)

    fun setSettingTheme(newSettingTheme: SettingTheme) {
        viewModelScope.launch {
            settingThemeRepository.setTheme(newSettingTheme)
        }
    }
}
