package com.boostcamp.dreamteam.dreamdiary.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme

@Composable
fun DdCard(
    headline: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    overline: @Composable (() -> Unit)? = null,
    underline: @Composable (() -> Unit)? = null,
    tail: (@Composable () -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    content: @Composable (() -> Unit)? = null,
) {
    val decoratedOverlineContent: @Composable () -> Unit = overline?.let {
        {
            ProvideTextStyle(
                value = MaterialTheme.typography.labelMedium,
            ) {
                overline()
            }
        }
    } ?: { }

    val decoratedHeadlineContent: @Composable () -> Unit = {
        ProvideTextStyle(
            value = MaterialTheme.typography.titleLarge,
        ) {
            headline()
        }
    }

    val decoratedUnderLineContent: @Composable () -> Unit = underline?.let {
        {
            ProvideTextStyle(
                value = MaterialTheme.typography.labelMedium,
            ) {
                underline()
            }
        }
    } ?: { }

    val decoratedTailContent: @Composable () -> Unit = tail?.let {
        {
            ProvideTextStyle(
                value = MaterialTheme.typography.labelMedium,
            ) {
                tail()
            }
        }
    } ?: { }

    val decoratedBodyContent: @Composable () -> Unit = content?.let {
        {
            ProvideTextStyle(
                value = MaterialTheme.typography.bodyMedium,
            ) {
                content()
            }
        }
    } ?: { }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors().copy(containerColor = containerColor),
        elevation = CardDefaults.elevatedCardElevation(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            decoratedOverlineContent()
            decoratedHeadlineContent()
            decoratedUnderLineContent()
            Spacer(modifier = Modifier.height(16.dp))

            decoratedBodyContent()
            if (tail != null) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            decoratedTailContent()
        }
    }
}

@Preview
@Composable
private fun DdCardPreviewEmpty() {
    DreamdiaryTheme {
        DdCard(
            headline = { Text(text = "타이틀이지롱") },
            overline = { Text(text = "오버라인이지롱") },
            underline = { Text(text = "언더라인이지롱") },
            content = { Text(text = "바디이지롱") },
            tail = { Text(text = "꼬리이지롱") },
        )
    }
}
