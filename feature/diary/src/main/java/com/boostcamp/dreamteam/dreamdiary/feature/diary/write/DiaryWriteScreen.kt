package com.boostcamp.dreamteam.dreamdiary.feature.diary.write

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component.LabelSelectionDialog
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.DiaryWriteEvent
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.LabelAddFailureReason
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.model.SelectableLabel
import kotlinx.coroutines.flow.collectLatest
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
internal fun DiaryWriteScreen(
    viewModel: DiaryWriteViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val (title, content, searchValue, selectableLabels, sleepEndAt, sleepStartAt) = uiState

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest {
            when (it) {
                is DiaryWriteEvent.DiaryAddSuccess -> onBackClick()
                is DiaryWriteEvent.LabelAddSuccess -> {
                    Toast.makeText(context, "라벨 추가 성공", Toast.LENGTH_SHORT).show()
                }
                is DiaryWriteEvent.LabelAddFailure -> {
                    when (it.labelAddFailureReason) {
                        LabelAddFailureReason.DUPLICATE_LABEL -> {
                            Toast.makeText(context, context.getString(R.string.write_duplicate_error), Toast.LENGTH_SHORT).show()
                        }

                        LabelAddFailureReason.INSUFFICIENT_STORAGE -> {
                            Toast.makeText(context, context.getString(R.string.write_insufficient_storage_error), Toast.LENGTH_SHORT).show()
                        }

                        LabelAddFailureReason.UNKNOWN_ERROR -> {
                            Toast.makeText(context, context.getString(R.string.write_unknown_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    DiaryWriteScreen(
        title = title,
        content = content,
        searchValue = searchValue,
        selectableLabels = selectableLabels,
        onTitleChange = viewModel::setTitle,
        onContentChange = viewModel::setContent,
        onCheckChange = viewModel::toggleLabel,
        onSearchValueChange = viewModel::setSearchValue,
        onClickLabelSave = viewModel::addLabel,
        onClickSave = viewModel::addDreamDiary,
        onBackClick = onBackClick,
        sleepStartAt = sleepStartAt,
        onSleepStartAtChange = viewModel::setSleepStartAt,
        sleepEndAt = sleepEndAt,
        onSleepEndAtChange = viewModel::setSleepEndAt,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiaryWriteScreen(
    title: String,
    content: String,
    searchValue: String,
    selectableLabels: List<SelectableLabel>,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onCheckChange: (labelUi: LabelUi) -> Unit,
    onSearchValueChange: (String) -> Unit,
    onClickLabelSave: () -> Unit,
    onClickSave: () -> Unit,
    onBackClick: () -> Unit,
    sleepStartAt: ZonedDateTime,
    onSleepStartAtChange: (ZonedDateTime) -> Unit,
    sleepEndAt: ZonedDateTime,
    onSleepEndAtChange: (ZonedDateTime) -> Unit,
) {
    val scrollState = rememberScrollState()
    var isLabelSelectionDialogOpen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.write_back),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onClickSave() }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.write_save),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState),
        ) {
            WriteDateInfo(
                sleepStart = sleepStartAt,
                sleepEnd = sleepEndAt,
                onTimeChanged = { a, b ->
                    onSleepStartAtChange(a)
                    onSleepEndAtChange(b)
                },
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        isLabelSelectionDialogOpen = true
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.AutoMirrored.Outlined.Label, contentDescription = stringResource(R.string.write_category))
                Text(text = "악몽, 개꿈, 귀신")
            }

            Spacer(modifier = Modifier.height(24.dp))

            BasicTextField(
                value = title,
                onValueChange = onTitleChange,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (title.isEmpty()) {
                        Text(
                            text = stringResource(R.string.write_text_title),
                            style = TextStyle(color = MaterialTheme.colorScheme.secondary),
                        )
                    }
                    innerTextField()
                },
            )

            Spacer(modifier = Modifier.height(24.dp))

            BasicTextField(
                value = content,
                onValueChange = onContentChange,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(300.dp),
                decorationBox = { innerTextField ->
                    if (content.isEmpty()) {
                        Text(
                            text = stringResource(R.string.write_text_content),
                            style = TextStyle(color = MaterialTheme.colorScheme.secondary),
                        )
                    }
                    innerTextField()
                },
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
private fun WriteDateInfo(
    sleepStart: ZonedDateTime,
    sleepEnd: ZonedDateTime,
    onTimeChanged: (ZonedDateTime, ZonedDateTime) -> Unit = { a, b -> },
) {
    val truncatedSleepStart = sleepStart.truncatedTo(ChronoUnit.MINUTES)
    val truncatedSleepEnd = sleepEnd.truncatedTo(ChronoUnit.MINUTES)

    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        DateHeader(
            date = truncatedSleepStart.toLocalDate(),
            onConfirm = { millis ->
                val newDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault())
                val diffHour = ChronoUnit.HOURS.between(truncatedSleepStart, truncatedSleepEnd)
                val newSleepStart = truncatedSleepStart.with(newDate.toLocalDate())
                val newSleepEnd = newSleepStart.plusHours(diffHour)
                onTimeChanged(newSleepStart, newSleepEnd)
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
                onTimeChanged(newSleepStart, newSleepEnd)
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

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
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

    if (isDatePickerOpen) {
        val datePickerState = rememberDatePickerState(
            date.atStartOfDay().atZone(ZoneId.of("UTC")).toInstant().toEpochMilli(),
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

    val dateFormatter = remember(locale) {
        val pattern = DateTimeFormatterBuilder
            .getLocalizedDateTimePattern(
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
        Icon(Icons.Filled.DateRange, contentDescription = stringResource(R.string.write_save_calendar))
        Text(text = date.format(dateFormatter))
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewDiaryListScreen() {
    DreamdiaryTheme {
        DiaryWriteScreen(
            title = "",
            content = "",
            searchValue = "",
            selectableLabels = listOf(
                SelectableLabel(LabelUi("악몽"), isSelected = true),
                SelectableLabel(LabelUi("개꿈"), isSelected = false),
                SelectableLabel(LabelUi("귀신"), isSelected = false),
            ),
            onTitleChange = {},
            onContentChange = {},
            onCheckChange = {},
            onClickLabelSave = {},
            onClickSave = {},
            onBackClick = {},
            onSearchValueChange = {},
            sleepStartAt = ZonedDateTime.now(),
            onSleepStartAtChange = {},
            sleepEndAt = ZonedDateTime.now(),
            onSleepEndAtChange = {},
        )
    }
}
