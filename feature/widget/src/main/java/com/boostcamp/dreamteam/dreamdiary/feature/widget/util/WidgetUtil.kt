package com.boostcamp.dreamteam.dreamdiary.feature.widget.util

import android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE
import android.content.Context
import android.content.Intent
import com.boostcamp.dreamteam.dreamdiary.feature.widget.DiaryWriteWidgetReceiver

fun updateWidget(context: Context) {
    context.sendBroadcast(
        Intent(context, DiaryWriteWidgetReceiver::class.java).apply {
            setAction(ACTION_APPWIDGET_UPDATE)
        },
    )
}
