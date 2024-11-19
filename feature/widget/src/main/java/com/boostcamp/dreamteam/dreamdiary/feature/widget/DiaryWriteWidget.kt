package com.boostcamp.dreamteam.dreamdiary.feature.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.ColorFilter
import androidx.glance.GlanceComposable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize

internal class DiaryWriteWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        provideContent {
            GlanceTheme {
                CreateDiaryWidgetContent(
                    modifier = GlanceModifier.clickable(onClick = actionRunCallback<DiaryWriteAction>()),
                )
            }
        }
    }
}

@Composable
@GlanceComposable
private fun CreateDiaryWidgetContent(modifier: GlanceModifier = GlanceModifier) {
    Scaffold(
        modifier = modifier,
        backgroundColor = GlanceTheme.colors.widgetBackground,
    ) {
        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // TODO: 앱 아이콘으로 변경
            Image(
                provider = ImageProvider(resId = R.drawable.ic_android),
                contentDescription = "꿈 일기 작성",
                modifier = GlanceModifier.fillMaxSize(),
                colorFilter = ColorFilter.tint(GlanceTheme.colors.primary),
            )
        }
    }
}
