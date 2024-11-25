package com.boostcamp.dreamteam.dreamdiary.feature.widget

import android.appwidget.AppWidgetManager
import android.content.Context
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
import javax.inject.Inject

@AndroidEntryPoint
internal class WidgetReceiver : GlanceAppWidgetReceiver() {
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

    private fun collectData(context: Context) {
        goAsync {
            GlanceAppWidgetManager(context).getGlanceIds(DiaryWriteWidget::class.java).firstOrNull()?.let { glanceId ->
                getDreamDiariesForTodayUseCase().let { diaries ->
                    Timber.d("Diaries: $diaries")
                    updateAppWidgetState(
                        context = context,
                        definition = PreferencesGlanceStateDefinition,
                        glanceId = glanceId,
                    ) { preferences ->
                        preferences.toMutablePreferences().apply {
                            this[DiaryWritePreferencesKey.encodedDiaries] = Json.encodeToString(diaries.map { it.toDiaryUi() })
                        }
                    }
                }

                glanceAppWidget.update(context, glanceId)
            }
        }
    }
}
