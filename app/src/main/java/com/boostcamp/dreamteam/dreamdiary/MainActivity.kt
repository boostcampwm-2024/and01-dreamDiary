package com.boostcamp.dreamteam.dreamdiary

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boostcamp.dreamteam.dreamdiary.notification.createNotificationChannel
import com.boostcamp.dreamteam.dreamdiary.notification.startTrackingService
import com.boostcamp.dreamteam.dreamdiary.ui.DreamDiaryApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val splashScreen = installSplashScreen()

        val uiState = mutableStateOf(MainUiState())

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    uiState.value = it
                }
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (uiState.value.settingThemeUiState) {
                SettingThemeUiState.Loading -> true
                else -> false
            }
        }

        enableEdgeToEdge()

        createNotificationChannel(this)
        if (sharedPreferences.getBoolean("onTracking", false)) {
            startTrackingService(this)
        }

        setContent {
            val settingTheme = uiState.value.settingThemeUiState
            val darkTheme = when (settingTheme) {
                SettingThemeUiState.System, SettingThemeUiState.Loading -> {
                    isSystemInDarkTheme()
                }

                else -> {
                    settingTheme == SettingThemeUiState.Dark
                }
            }

            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { darkTheme },
                )
                onDispose {}
            }

            DreamDiaryApp(darkTheme)
        }
    }
}

// NIA 발췌
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)
