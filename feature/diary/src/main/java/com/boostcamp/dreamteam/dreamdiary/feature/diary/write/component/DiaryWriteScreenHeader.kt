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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.filteredLabelsPreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.selectedLabelsPreview
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
import java.util.Locale

@Composable
internal fun DiaryWriteScreenHeader(
    labelFilter: String,
    onLabelFilterChange: (String) -> Unit,
    filteredLabels: List<LabelUi>,
    selectedLabels: Set<LabelUi>,
    sleepStartAt: ZonedDateTime,
    sleepEndAt: ZonedDateTime,
    onSleepStartAtChange: (ZonedDateTime) -> Unit,
    onSleepEndAtChange: (ZonedDateTime) -> Unit,
    onCheckChange: (labelUi: LabelUi) -> Unit,
    onClickLabelSave: () -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
) {
    var isLabelSelectionDialogOpen by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        WriteDateInfo(
            sleepStartAt = sleepStartAt,
            sleepEndAt = sleepEndAt,
            onTimeChange = { sleepStartAt, sleepEndAt ->
                onSleepStartAtChange(sleepStartAt)
                onSleepEndAtChange(sleepEndAt)
            },
            readOnly = readOnly,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .clickable(enabled = !readOnly) {
                    isLabelSelectionDialogOpen = true
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.Label,
                contentDescription = stringResource(R.string.write_category),
            )
            Text(
                text = if (selectedLabels.isEmpty()) {
                    stringResource(R.string.write_no_label)
                } else {
                    selectedLabels.joinToString { it.name }
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
            )
        }

        if (isLabelSelectionDialogOpen) {
            LabelSelectionDialog(
                onDismissRequest = { isLabelSelectionDialogOpen = false },
                labelFilter = labelFilter,
                onLabelFilterChange = onLabelFilterChange,
                filteredLabels = filteredLabels,
                selectedLabels = selectedLabels,
                onCheckChange = onCheckChange,
                onClickLabelSave = onClickLabelSave,
                modifier = Modifier.width(400.dp),
            )
        }
    }
}

@Composable
internal fun WriteDateInfo(
    sleepStartAt: ZonedDateTime,
    sleepEndAt: ZonedDateTime,
    onTimeChange: (ZonedDateTime, ZonedDateTime) -> Unit,
    readOnly: Boolean,
    modifier: Modifier = Modifier,
) {
    val truncatedSleepStart = sleepStartAt.truncatedTo(ChronoUnit.MINUTES)
    val truncatedSleepEnd = sleepEndAt.truncatedTo(ChronoUnit.MINUTES)

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
            readOnly = readOnly,
        )

        TimeHeader(
            truncatedSleepStart.toLocalTime(),
            truncatedSleepEnd.toLocalTime(),
            onConfirmStartTime = { startTime, endTime ->
                val newSleepStart = truncatedSleepStart.with(startTime)
                var newSleepEnd = truncatedSleepStart.with(endTime)
                if (newSleepEnd <= newSleepStart) {
                    newSleepEnd = newSleepEnd.plusDays(1)
                }
                onTimeChange(newSleepStart, newSleepEnd)
            },
            readOnly = readOnly,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeHeader(
    startTime: LocalTime,
    endTime: LocalTime,
    onConfirmStartTime: (LocalTime, LocalTime) -> Unit,
    readOnly: Boolean,
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
            val (initialHour, initialMinute) = if (tabIndex == 0) {
                startTime.hour to startTime.minute
            } else {
                endTime.hour to endTime.minute
            }
            val timePickerState = rememberTimePickerState(
                initialHour = initialHour,
                initialMinute = initialMinute,
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
                        Text(stringResource(R.string.write_time_ok))
                    }
                },
                text = { TimePicker(state = timePickerState) },
            )
        }

        Icon(Icons.Outlined.Timer, contentDescription = stringResource(R.string.write_select_time))

        Text(
            modifier = Modifier.clickable(enabled = !readOnly) {
                tabIndex = 0
                isTimePickerOpen = true
            },
            text = startTime.truncatedTo(ChronoUnit.MINUTES).format(timeFormatter),
        )
        Text(text = "~")
        Text(
            modifier = Modifier.clickable(enabled = !readOnly) {
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
    readOnly: Boolean,
    locale: Locale = Locale.getDefault(),
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
        modifier = modifier.clickable(enabled = !readOnly) {
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
                    ) { Text(stringResource(R.string.write_date_ok)) }
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
            labelFilter = "",
            onLabelFilterChange = { },
            filteredLabels = filteredLabelsPreview,
            selectedLabels = selectedLabelsPreview,
            sleepStartAt = ZonedDateTime.now(),
            sleepEndAt = ZonedDateTime.now(),
            onSleepStartAtChange = { },
            onSleepEndAtChange = { },
            onCheckChange = { },
            onClickLabelSave = { },
        )
    }
}
