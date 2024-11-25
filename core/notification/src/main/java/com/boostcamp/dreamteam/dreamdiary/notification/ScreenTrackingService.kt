package com.boostcamp.dreamteam.dreamdiary.notification

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ScreenTrackingService : Service() {
    private lateinit var screenOffTracker: ScreenOffTimeTracker
    private val handler = Handler(Looper.getMainLooper())
    private val checkInterval = 10_000L
    private val screenOffDuration = 20_000L

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val checkScreenOffRunnable = object : Runnable {
        override fun run() {
            if (screenOffTracker.isScreenOffFor(screenOffDuration)) {
                sendScreenOffNotification()
            }
            handler.postDelayed(this, checkInterval)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("알람 서비스 활성화")
        screenOffTracker = ScreenOffTimeTracker(this, sharedPreferences)
        screenOffTracker.startTracking()

        val notification = NotificationCompat.Builder(this, "aaaaa")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(getString(R.string.notification_startforeground_title))
            .setContentText(getString(R.string.notification_startforeground_text))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        startForeground(1, notification)

        handler.post(checkScreenOffRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("알람 서비스 비활성화")
        handler.removeCallbacks(checkScreenOffRunnable)
        screenOffTracker.stopTracking()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun sendScreenOffNotification() {
        val notification = NotificationCompat.Builder(this, "aaaaa")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("지금 잠에서 깨지 않으면 우린 춤을 출거에요")
            .setContentText("야레야레 못말리는 아가씨")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(2, notification)
    }
}
