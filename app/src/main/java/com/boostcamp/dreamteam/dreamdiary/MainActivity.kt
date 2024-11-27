package com.boostcamp.dreamteam.dreamdiary

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.boostcamp.dreamteam.dreamdiary.notification.createNotificationChannel
import com.boostcamp.dreamteam.dreamdiary.notification.startTrackingService
import com.boostcamp.dreamteam.dreamdiary.ui.DreamDiaryApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        createNotificationChannel(this)
        if (sharedPreferences.getBoolean("onTracking", false)) {
            startTrackingService(this)
        }

        setContent {
            DreamDiaryApp()
        }
    }
}
