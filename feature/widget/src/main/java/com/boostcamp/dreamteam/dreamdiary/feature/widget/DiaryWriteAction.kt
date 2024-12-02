package com.boostcamp.dreamteam.dreamdiary.feature.widget

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import androidx.core.net.toUri
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback

class DiaryWriteAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        val intent: Intent = Intent(ACTION_VIEW, "dreamdiary://diary/write".toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        context.startActivity(intent)
    }
}
