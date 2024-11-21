package com.boostcamp.dreamteam.dreamdiary.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences

class ScreenOffTimeTracker(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
    ) {
    private var isScreenOff: Boolean = false

    private val screenStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    sharedPreferences.edit().putLong("screenOffTime", System.currentTimeMillis()).apply()
                    isScreenOff = true
                }
                Intent.ACTION_SCREEN_ON -> {
                    isScreenOff = false
                }
            }
        }
    }

    fun startTracking() {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
        }
        context.registerReceiver(screenStateReceiver, filter)
    }

    fun stopTracking() {
        context.unregisterReceiver(screenStateReceiver)
    }

    fun isScreenOffFor(duration: Long): Boolean {
        return isScreenOff && (System.currentTimeMillis() - sharedPreferences.getLong("screenOffTime", 0) >= duration)
    }
}

