package com.boostcamp.dreamteam.dreamdiary

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DreamDiaryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initTimber()
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
