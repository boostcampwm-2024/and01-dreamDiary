package com.boostcamp.dreamteam.dreamdiary.manager

import android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE
import android.content.Context
import android.content.Intent
import com.boostcamp.dreamteam.dreamdiary.feature.widget.DiaryWriteWidgetReceiver
import javax.inject.Singleton

@Singleton
class DiaryWidgetManger {
    fun update(context: Context) {
        val updateWidgetIntent = Intent(context, DiaryWriteWidgetReceiver::class.java).apply {
            setAction(ACTION_APPWIDGET_UPDATE)
        }

        context.sendBroadcast(updateWidgetIntent)
    }
}
