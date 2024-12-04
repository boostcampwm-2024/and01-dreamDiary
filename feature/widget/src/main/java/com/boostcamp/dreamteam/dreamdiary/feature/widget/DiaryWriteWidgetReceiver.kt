package com.boostcamp.dreamteam.dreamdiary.feature.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED
import android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.GetDreamDiariesForTodayUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.widget.constant.DiaryWritePreferencesKey
import com.boostcamp.dreamteam.dreamdiary.feature.widget.model.toDiaryUi
import com.boostcamp.dreamteam.dreamdiary.ui.util.goAsync
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@AndroidEntryPoint
class DiaryWriteWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = DiaryWriteWidget()

    @Inject
    lateinit var getDreamDiariesForTodayUseCase: GetDreamDiariesForTodayUseCase

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        super.onUpdate(context = context, appWidgetManager = appWidgetManager, appWidgetIds = appWidgetIds)
        collectData(context = context)
    }

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        super.onReceive(context = context, intent = intent)
        if (intent.action == ACTION_APPWIDGET_MANUAL_UPDATE ||
            intent.action == ACTION_APPWIDGET_OPTIONS_CHANGED
        ) {
            collectData(context = context)
        }
    }

    private fun collectData(context: Context) {
        Timber.d("updateWidget")
        goAsync {
            val start = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).toInstant()
            val end = start.plusSeconds(24 * 60 * 60 - 1)
            getDreamDiariesForTodayUseCase(
                start = start,
                end = end,
            ).let { diaries ->
                GlanceAppWidgetManager(context).getGlanceIds(DiaryWriteWidget::class.java).forEach { glanceId ->
                    updateAppWidgetState(
                        context = context,
                        definition = PreferencesGlanceStateDefinition,
                        glanceId = glanceId,
                    ) { preferences ->
                        preferences.toMutablePreferences().apply {
                            this[DiaryWritePreferencesKey.encodedDiaries] = Json.encodeToString(diaries.map { it.toDiaryUi() })
                        }
                    }

                    glanceAppWidget.update(context, glanceId)
                }
            }
        }
    }

    companion object {
        const val ACTION_APPWIDGET_MANUAL_UPDATE =
            "com.boostcamp.dreamteam.dreamdiary.feature.widget.ACTION_APPWIDGET_MANUAL_UPDATE"
    }
}
