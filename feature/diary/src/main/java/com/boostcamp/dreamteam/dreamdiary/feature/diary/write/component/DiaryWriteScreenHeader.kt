package com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.SelectableLabel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.chrono.Chronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

@Composable
internal fun DiaryWriteScreenHeader(
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    selectableLabels: List<SelectableLabel>,
    sleepStartAt: ZonedDateTime,
    sleepEndAt: ZonedDateTime,
    onSleepStartAtChange: (ZonedDateTime) -> Unit,
    onSleepEndAtChange: (ZonedDateTime) -> Unit,
    onCheckChange: (labelUi: LabelUi) -> Unit,
    onClickLabelSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isLabelSelectionDialogOpen by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        WriteDateInfo(
            sleepStart = sleepStartAt,
            sleepEnd = sleepEndAt,
            onTimeChange = { sleepStartAt, sleepEndAt ->
                onSleepStartAtChange(sleepStartAt)
                onSleepEndAtChange(sleepEndAt)
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .clickable {
                    isLabelSelectionDialogOpen = true
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.Label,
                contentDescription = stringResource(R.string.write_category),
            )
            Text(
                text = if (selectableLabels.isEmpty()) {
                    stringResource(R.string.write_no_label)
                } else {
                    selectableLabels.filter { it.isSelected }.joinToString { it.label.name }
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
            )
        }

        if (isLabelSelectionDialogOpen) {
            LabelSelectionDialog(
                onDismissRequest = { isLabelSelectionDialogOpen = false },
                searchValue = searchValue,
                onSearchValueChange = onSearchValueChange,
                selectableLabels = selectableLabels,
                onCheckChange = onCheckChange,
                onClickLabelSave = onClickLabelSave,
                modifier = Modifier.width(400.dp),
            )
        }
    }
}

@Composable
internal fun WriteDateInfo(
    sleepStart: ZonedDateTime,
    sleepEnd: ZonedDateTime,
    onTimeChange: (ZonedDateTime, ZonedDateTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    val truncatedSleepStart = sleepStart.truncatedTo(ChronoUnit.MINUTES)
    val truncatedSleepEnd = sleepEnd.truncatedTo(ChronoUnit.MINUTES)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        DateHeader(
            date = truncatedSleepStart.toLocalDate(),
            onConfirm = { millis ->
                val newDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault())
                val diffHour = ChronoUnit.HOURS.between(truncatedSleepStart, truncatedSleepEnd)
                val newSleepStart = truncatedSleepStart.with(newDate.toLocalDate())
                val newSleepEnd = newSleepStart.plusHours(diffHour)
                onTimeChange(newSleepStart, newSleepEnd)
            },
        )

        TimeHeader(
            truncatedSleepStart.toLocalTime(),
            truncatedSleepEnd.toLocalTime(),
            onConfirmStartTime = { a, b ->
                val newSleepStart = truncatedSleepStart.with(a)
                var newSleepEnd = truncatedSleepStart.with(b)
                if (newSleepEnd <= newSleepStart) {
                    newSleepEnd = newSleepEnd.plusDays(1)
                }
                onTimeChange(newSleepStart, newSleepEnd)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeHeader(
    startTime: LocalTime,
    endTime: LocalTime,
    onConfirmStartTime: (LocalTime, LocalTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isTimePickerOpen by rememberSaveable { mutableStateOf(false) }
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isTimePickerOpen) {
            val (inithour, initminute) = if (tabIndex == 0) {
                startTime.hour to startTime.minute
            } else {
                endTime.hour to endTime.minute
            }
            val timePickerState = rememberTimePickerState(
                initialHour = inithour,
                initialMinute = initminute,
                is24Hour = true,
            )
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    Button(
                        onClick = {
                            val newTime = LocalTime.of(
                                timePickerState.hour,
                                timePickerState.minute,
                            )
                            if (tabIndex == 0) {
                                onConfirmStartTime(newTime, endTime)
                            } else {
                                onConfirmStartTime(startTime, newTime)
                            }
                            isTimePickerOpen = false
                        },
                    ) {
                        Text("확인")
                    }
                },
                text = { TimePicker(state = timePickerState) },
            )
        }

        Icon(Icons.Outlined.Timer, contentDescription = stringResource(R.string.write_select_time))

        Text(
            modifier = Modifier.clickable {
                tabIndex = 0
                isTimePickerOpen = true
            },
            text = startTime.truncatedTo(ChronoUnit.MINUTES).format(timeFormatter),
        )
        Text(text = "~")
        Text(
            modifier = Modifier.clickable {
                tabIndex = 1
                isTimePickerOpen = true
            },
            text = endTime.truncatedTo(ChronoUnit.MINUTES).format(timeFormatter),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateHeader(
    date: LocalDate,
    onConfirm: (Long) -> Unit,
    modifier: Modifier = Modifier,
    locale: java.util.Locale = Locale.current.platformLocale,
) {
    var isDatePickerOpen by rememberSaveable { mutableStateOf(false) }

    val dateFormatter = remember(locale) {
        val pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            FormatStyle.MEDIUM,
            null,
            Chronology.ofLocale(locale),
            locale,
        )
        DateTimeFormatter.ofPattern(pattern)
    }

    Row(
        modifier = modifier.clickable {
            isDatePickerOpen = true
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isDatePickerOpen) {
            val datePickerState = rememberDatePickerState(
                date
                    .atStartOfDay()
                    .atZone(ZoneId.of("UTC"))
                    .toInstant()
                    .toEpochMilli(),
            )
            DatePickerDialog(
                onDismissRequest = {},
                confirmButton = {
                    Button(
                        onClick = {
                            val millis = datePickerState.selectedDateMillis ?: return@Button
                            onConfirm(millis)
                            isDatePickerOpen = false
                        },
                    ) { Text("확인") }
                },
            ) {
                DatePicker(datePickerState)
            }
        }

        Icon(
            Icons.Filled.DateRange,
            contentDescription = stringResource(R.string.write_save_calendar),
        )
        Text(text = date.format(dateFormatter))
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryWriteScreenHeaderPreview() {
    DreamdiaryTheme {
        DiaryWriteScreenHeader(
            searchValue = "",
            onSearchValueChange = { },
            selectableLabels = emptyList(),
            sleepStartAt = ZonedDateTime.now(),
            sleepEndAt = ZonedDateTime.now(),
            onSleepStartAtChange = { },
            onSleepEndAtChange = { },
            onCheckChange = { },
            onClickLabelSave = { },
        )
    }
}
