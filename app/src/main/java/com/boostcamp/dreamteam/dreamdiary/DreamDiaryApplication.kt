package com.boostcamp.dreamteam.dreamdiary

import android.app.Application
import android.content.SharedPreferences
import com.boostcamp.dreamteam.dreamdiary.core.synchronization.SynchronizationWorker
import com.boostcamp.dreamteam.dreamdiary.notification.createNotificationChannel
import com.boostcamp.dreamteam.dreamdiary.notification.startTrackingService
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class DreamDiaryApplication : Application() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        initTimber()
        createNotificationChannel(this)
        if (sharedPreferences.getBoolean("onTracking", false)) {
            startTrackingService(this)
        }
        SynchronizationWorker.initWorker(this)
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(
                object : Timber.DebugTree() {
                    override fun createStackElementTag(element: StackTraceElement): String =
                        "TB(${element.fileName}:${element.lineNumber})#${element.methodName}"
                },
            )
        }
    }
}
