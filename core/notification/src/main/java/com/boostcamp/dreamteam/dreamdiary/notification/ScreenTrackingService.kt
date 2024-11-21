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
import javax.inject.Inject

@AndroidEntryPoint
class ScreenTrackingService : Service() {
    @Inject
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var screenOffTracker: ScreenOffTimeTracker
    private val handler = Handler(Looper.getMainLooper())
    private val checkInterval = 10_000L
    private val screenOffDuration = 20_000L

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
        screenOffTracker = ScreenOffTimeTracker(this, sharedPreferences)
        screenOffTracker.startTracking()

        val notification = NotificationCompat.Builder(this, "aaaaa")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("화면 상태 추적 중")
            .setContentText("화면 꺼짐/켜짐 시간을 기록하고 있습니다.")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        startForeground(1, notification)

        handler.post(checkScreenOffRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
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
