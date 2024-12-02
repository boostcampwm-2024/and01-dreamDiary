package com.boostcamp.dreamteam.dreamdiary.setting

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.boostcamp.dreamteam.dreamdiary.notification.launchNotificationSetting
import com.boostcamp.dreamteam.dreamdiary.notification.startTrackingService
import com.boostcamp.dreamteam.dreamdiary.notification.stopTrackingService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingNotificationViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {
    private val _onTracking = MutableStateFlow<Boolean>(sharedPreferences.getBoolean("onTracking", false))
    val onTracking = _onTracking.asStateFlow()

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
