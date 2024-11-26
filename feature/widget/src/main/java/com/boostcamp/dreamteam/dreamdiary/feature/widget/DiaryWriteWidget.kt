package com.boostcamp.dreamteam.dreamdiary.feature.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceComposable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.boostcamp.dreamteam.dreamdiary.feature.widget.DiaryWriteWidget.Companion.HORIZONTAL_RECTANGLE
import com.boostcamp.dreamteam.dreamdiary.feature.widget.constant.DiaryWritePreferencesKey
import com.boostcamp.dreamteam.dreamdiary.feature.widget.model.DiaryUi
import kotlinx.serialization.json.Json
import com.boostcamp.dreamteam.dreamdiary.designsystem.R as DesignSystemR

internal class DiaryWriteWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(
            SMALL_SQUARE,
            HORIZONTAL_RECTANGLE,
        ),
    )

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        provideContent {
            GlanceTheme {
                CreateDiaryWidgetContent(modifier = GlanceModifier.clickable(onClick = actionRunCallback<DiaryWriteAction>()))
            }
        }
    }

    companion object {
        val SMALL_SQUARE = DpSize(57.dp, 102.dp)
        val HORIZONTAL_RECTANGLE = DpSize(130.dp, 102.dp)
    }
}

@Composable
@GlanceComposable
private fun CreateDiaryWidgetContent(modifier: GlanceModifier = GlanceModifier) {
    val size = LocalSize.current

    val encodedDiaries = currentState(key = DiaryWritePreferencesKey.encodedDiaries) ?: ""
    val diaries: List<DiaryUi> = encodedDiaries.takeIf { it.isNotEmpty() }?.let {
        Json.decodeFromString<List<DiaryUi>>(it)
    } ?: emptyList()

    Scaffold(
        modifier = modifier.clickable(
            onClick = actionRunCallback<DiaryWriteAction>(),
        ),
        backgroundColor = GlanceTheme.colors.widgetBackground,
    ) {
        Row(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val iconModifier = GlanceModifier
                .cornerRadius(radius = 32.dp)
                .background(GlanceTheme.colors.primaryContainer)
                .padding(4.dp)

            WidgetIcon(
                diaries = diaries,
                modifier = if (size.width >= HORIZONTAL_RECTANGLE.width) {
                    iconModifier.size(32.dp)
                } else {
                    iconModifier.size(64.dp)
                },
            )
            if (size.width >= HORIZONTAL_RECTANGLE.width) {
                WidgetText(diaries = diaries, modifier = GlanceModifier.defaultWeight().padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
private fun WidgetIcon(
    diaries: List<DiaryUi>,
    modifier: GlanceModifier = GlanceModifier,
) {
    Box(
        modifier = modifier,
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

@Composable
@GlanceComposable
private fun WidgetText(
    diaries: List<DiaryUi>,
    modifier: GlanceModifier = GlanceModifier,
) {
    Column(
        modifier = modifier.padding(start = 8.dp),
        verticalAlignment = Alignment.Vertical.CenterVertically,
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
    ) {
        if (diaries.isEmpty()) {
            Text(
                text = "꿈 일기\n" +
                    "작성해줘\n" +
                    "✏️",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = GlanceTheme.colors.onSurface,
                ),
            )
        } else {
            Text(
                text = "오늘은 이미 작성했어요!👌",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = GlanceTheme.colors.onSurface,
                ),
                maxLines = 2,
            )
            Box(modifier = GlanceModifier.fillMaxWidth().height(4.dp)) {}
            LazyColumn(
                modifier = GlanceModifier.background(GlanceTheme.colors.primaryContainer),
            ) {
                items(count = diaries.size) { index ->
                    val diary = diaries[index]
                    Column(
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = actionRunCallback<DiaryDetailAction>(
                                    parameters = actionParametersOf(DiaryDetailAction.diaryId to diary.id),
                                ),
                            ),
                    ) {
                        Text(
                            text = diary.title,
                            modifier = GlanceModifier.padding(4.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = GlanceTheme.colors.onPrimaryContainer,
                            ),
                            maxLines = 2,
                        )
                    }
                    if (index < diaries.size - 1) {
                        Box(modifier = GlanceModifier.fillMaxWidth().height(1.dp)) {}
                    }
                }
            }
        }
    }
}
