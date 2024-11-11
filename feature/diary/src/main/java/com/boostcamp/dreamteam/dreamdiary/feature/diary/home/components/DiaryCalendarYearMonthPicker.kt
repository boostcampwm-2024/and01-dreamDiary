package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import androidx.compose.ui.text.intl.Locale as ComposeLocale
import java.util.Locale as JavaLocale

@Composable
internal fun YearMonthPicker(
    currentYearMonth: YearMonth,
    onConfirmClick: (YearMonth) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showingYear by remember { mutableIntStateOf(currentYearMonth.year) }
    var selectedYearMonth by remember { mutableStateOf(currentYearMonth) }

    Dialog(onDismissRequest = { onConfirmClick(selectedYearMonth) }) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                YearMonthPickerHeader(
                    year = showingYear,
                    onBackClick = { showingYear-- },
                    onNextClick = { showingYear++ },
                    modifier = Modifier,
                )
                Spacer(modifier = Modifier.height(16.dp))

                YearMonthPickerBody(
                    showingYear = showingYear,
                    selectedYearMonth = selectedYearMonth,
                    modifier = Modifier,
                    onYearMonthClick = { selectedYearMonth = it },
                )
                Spacer(modifier = Modifier.height(24.dp))

                YearMonthPickerActions(
                    onConfirmClick = { onConfirmClick(selectedYearMonth) },
                    onCancelClick = onCancelClick,
                    modifier = Modifier.align(Alignment.End),
                )
            }
        }
    }
}

@Composable
private fun YearMonthPickerActions(
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
    ) {
        TextButton(onClick = onCancelClick) {
            Text(text = stringResource(R.string.calendar_cancel))
        }
        TextButton(onClick = onConfirmClick) {
            Text(text = stringResource(R.string.calendar_ok))
        }
    }
}

@Composable
private fun YearMonthPickerHeader(
    year: Int,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    currentYearMonth: YearMonth = YearMonth.now(),
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos,
                contentDescription = stringResource(R.string.calendar_previous_month),
            )
        }
        Text(
            text = year.toString(),
            style = MaterialTheme.typography.titleLarge,
        )
        IconButton(
            onClick = onNextClick,
            enabled = !YearMonth.of(year, Month.DECEMBER).isAfter(currentYearMonth),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                contentDescription = stringResource(R.string.calendar_move_next_month),
            )
        }
    }
}

@Composable
private fun YearMonthPickerBody(
    showingYear: Int,
    selectedYearMonth: YearMonth,
    onYearMonthClick: (YearMonth) -> Unit,
    modifier: Modifier = Modifier,
    locale: JavaLocale = ComposeLocale.current.platformLocale,
    currentYearMonth: YearMonth = YearMonth.now(),
) {
    val yearMonths = Month.entries.map { YearMonth.of(showingYear, it) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
    ) {
        items(yearMonths) { yearMonth ->
            val enabled = !yearMonth.isAfter(currentYearMonth)

            TextButton(
                onClick = { onYearMonthClick(yearMonth) },
                enabled = enabled,
            ) {
                Text(
                    text = yearMonth.month.getDisplayName(TextStyle.SHORT, locale),
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = when {
                        yearMonth == selectedYearMonth -> MaterialTheme.colorScheme.primary
                        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Preview
@Composable
private fun YearMonthPickerPreview() {
    DreamdiaryTheme {
        YearMonthPicker(
            currentYearMonth = YearMonth.now(),
            onConfirmClick = { },
            onCancelClick = { },
        )
    }
}
