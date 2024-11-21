package com.boostcamp.dreamteam.dreamdiary.setting

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.notification.launchNotificationSetting
import com.boostcamp.dreamteam.dreamdiary.notification.startTrackingService
import com.boostcamp.dreamteam.dreamdiary.notification.stopTrackingService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    private val _onTracking = MutableStateFlow<Boolean>(sharedPreferences.getBoolean("onTracking", false))
    val onTracking = _onTracking.asStateFlow()
    fun signOut() {
        authRepository.firebaseSignOut()
        viewModelScope.launch {
            authRepository.snsSignOut()
        }
    }

    fun nonPasswordSignIn() {
        sharedPreferences.edit().putBoolean("onPass", false).apply()
    }

    fun getUserEmail(): String? {
        return authRepository.getUserEmail()
    }

    fun getSignInProvider(): String? {
        return authRepository.getSignInProvider()
    }

    fun goToLaunchNotificationSetting(activity: Activity) {
        launchNotificationSetting(activity)
    }

    fun startTracking(context: Context) {
        sharedPreferences.edit().putBoolean("onTracking", true).apply()
        _onTracking.value = true
        startTrackingService(context)
    }

    fun stopTracking(context: Context) {
        sharedPreferences.edit().putBoolean("onTracking", false).apply()
        _onTracking.value = false
        stopTrackingService(context)
    }

    fun getTrackingState(): Boolean {
        return sharedPreferences.getBoolean("onTracking", false)
    }
}
