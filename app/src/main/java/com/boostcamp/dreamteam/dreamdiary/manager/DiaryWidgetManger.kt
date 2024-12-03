package com.boostcamp.dreamteam.dreamdiary.manager

import android.content.Context
import android.content.Intent
import com.boostcamp.dreamteam.dreamdiary.feature.widget.DiaryWriteWidgetReceiver
import javax.inject.Singleton

@Singleton
class DiaryWidgetManger {
    fun update(context: Context) {
        val updateWidgetIntent = Intent(context, DiaryWriteWidgetReceiver::class.java).apply {
            setAction(DiaryWriteWidgetReceiver.ACTION_APPWIDGET_MANUAL_UPDATE)
        }

        context.sendBroadcast(updateWidgetIntent)
    }
}
