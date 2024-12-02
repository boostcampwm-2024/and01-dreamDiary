package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.di.SettingThemeDataStore
import com.boostcamp.dreamteam.dreamdiary.core.model.setting.SettingTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingThemeRepository @Inject constructor(
    @SettingThemeDataStore private val dataStore: DataStore<Preferences>,
) {
    private val key = stringPreferencesKey("selected_setting_theme")

    fun getTheme(): Flow<SettingTheme> {
        return dataStore.data.map {
            val theme = it[key] ?: SettingTheme.SYSTEM.name
            SettingTheme.valueOf(theme)
        }
    }

    suspend fun setTheme(theme: SettingTheme) {
        dataStore.updateData {
            it.toMutablePreferences().apply {
                this[key] = theme.name
            }
        }
    }
}
