package com.boostcamp.dreamteam.dreamdiary.feature.widget

import android.content.Context
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceComposable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import com.boostcamp.dreamteam.dreamdiary.feature.widget.constant.DiaryWritePreferencesKey
import com.boostcamp.dreamteam.dreamdiary.feature.widget.model.DiaryUi
import kotlinx.serialization.json.Json
import com.boostcamp.dreamteam.dreamdiary.designsystem.R as DesignSystemR

internal class DiaryWriteWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        Modifier.aspectRatio(1f)
        provideContent {
            GlanceTheme {
                CreateDiaryWidgetContent(modifier = GlanceModifier.clickable(onClick = actionRunCallback<DiaryWriteAction>()))
            }
        }
    }
}

@Composable
@GlanceComposable
private fun CreateDiaryWidgetContent(modifier: GlanceModifier = GlanceModifier) {
    val encodedDiaries = currentState(key = DiaryWritePreferencesKey.encodedDiaries) ?: ""
    val diaries: List<DiaryUi> = encodedDiaries.takeIf { it.isNotEmpty() }?.let {
        Json.decodeFromString<List<DiaryUi>>(it)
    } ?: emptyList()

    Scaffold(
        modifier = modifier.clickable(
            onClick = if (diaries.isEmpty()) {
                actionRunCallback<DiaryWriteAction>()
            } else {
                actionRunCallback<DiaryDetailAction>(
                    parameters = actionParametersOf(DiaryDetailAction.diaryId to diaries.last().id),
                )
            },
        ),
        backgroundColor = GlanceTheme.colors.widgetBackground,
    ) {
        Row(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = GlanceModifier
                    .cornerRadius(radius = 32.dp)
                    .size(64.dp)
                    .background(GlanceTheme.colors.primaryContainer)
                    .padding(4.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (diaries.isEmpty()) {
                    Image(
                        provider = ImageProvider(DesignSystemR.drawable.sun),
                        contentDescription = "일기 작성하러 가기",
                        modifier = GlanceModifier.fillMaxSize(),
                    )
                } else {
                    Image(
                        provider = ImageProvider(DesignSystemR.mipmap.ic_launcher),
                        contentDescription = "꿈 일기 작성",
                        modifier = GlanceModifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}
