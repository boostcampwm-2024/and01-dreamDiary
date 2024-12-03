package com.boostcamp.dreamteam.dreamdiary.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.valentinilk.shimmer.shimmer

@Composable
fun DdCard(
    headline: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    overline: @Composable (() -> Unit)? = null,
    underline: @Composable (() -> Unit)? = null,
    tail: (@Composable () -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    shape: Shape = CardDefaults.shape,
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
        shape = shape,
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

@Composable
fun ShimmerCard(
    modifier: Modifier = Modifier,
    showAuthor: Boolean = false,
    showImage: Boolean = false,
) {
    val density = LocalDensity.current

    var contentSize by remember { mutableStateOf(DpSize.Zero) }
    val boxModifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHighest)

    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.extraLarge,
            ).background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .shimmer()
            .padding(16.dp)
            .onSizeChanged {
                contentSize = with(density) {
                    it
                        .toSize()
                        .toDpSize()
                }
            },
    ) {
        if (showAuthor) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                )
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = boxModifier
                        .height(20.dp)
                        .width(contentSize.width * 0.6f),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (showImage) {
            Box(
                modifier = boxModifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Box(
            modifier = boxModifier
                .height(20.dp)
                .width(contentSize.width * 0.8f),
        )
        Box(
            modifier = boxModifier
                .height(20.dp)
                .widthIn(contentSize.width * 0.7f),
        )
        Box(
            modifier = boxModifier
                .height(20.dp)
                .widthIn(contentSize.width * 0.5f),
        )
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = boxModifier
                .height(32.dp)
                .fillMaxWidth(),
        )
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

@Preview
@Composable
private fun DdCardPreviewWithShimmer() {
    DreamdiaryTheme {
        ShimmerCard(
            showAuthor = true,
            showImage = true,
        )
    }
}
