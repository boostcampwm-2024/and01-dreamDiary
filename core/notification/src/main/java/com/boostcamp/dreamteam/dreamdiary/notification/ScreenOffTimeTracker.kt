package com.boostcamp.dreamteam.dreamdiary.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import timber.log.Timber

class ScreenOffTimeTracker(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
) {
    private var isScreenOff: Boolean = false
    private val screenOffDuration = 5_000L

    private val screenStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent,
        ) {
            when (intent.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    sharedPreferences.edit().putLong("screenOffTime", System.currentTimeMillis()).apply()
                    isScreenOff = true
                }
                Intent.ACTION_SCREEN_ON -> {
                    isScreenOff = false
                    if (System.currentTimeMillis() - sharedPreferences.getLong("screenOffTime", 0) >= screenOffDuration) {
                        sendScreenOffNotification()
                    }
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

    private fun sendScreenOffNotification() {
        val notification = NotificationCompat.Builder(context, "aaaaa")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(context.getString(R.string.notification_screen_off_title))
            .setContentText(context.getString(R.string.notification_screen_off_text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        try {
            NotificationManagerCompat.from(context).notify(12, notification)
        } catch (e: SecurityException) {
            Timber.d(e)
        }
    }
}
