package com.boostcamp.dreamteam.dreamdiary.setting

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }

    fun nonPasswordSignIn() {
        sharedPreferences.edit().putBoolean("onPass", false).apply()
    }

    fun getUserEmail(): String? {
        return authRepository.getUserEmail()
    }
}
