package com.boostcamp.dreamteam.dreamdiary.feature.diary.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryContentUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.LabelUi
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component.DiaryContentEditor
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component.DiaryInfosEditor
import java.time.ZonedDateTime

@Composable
internal fun DiaryEditor(
    title: String,
    onTitleChange: (String) -> Unit,
    labelFilter: String,
    filteredLabels: List<LabelUi>,
    selectedLabels: Set<LabelUi>,
    onCheckChange: (LabelUi) -> Unit,
    onLabelFilterChange: (String) -> Unit,
    onClickLabelSave: () -> Unit,
    sleepStartAt: ZonedDateTime,
    onSleepStartAtChange: (ZonedDateTime) -> Unit,
    sleepEndAt: ZonedDateTime,
    onSleepEndAtChange: (ZonedDateTime) -> Unit,
    diaryContents: List<DiaryContentUi>,
    onContentTextChange: (contentIndex: Int, String) -> Unit,
    onCurrentFocusContentChange: (Int) -> Unit,
    onContentTextPositionChange: (Int) -> Unit,
    onContentImageDelete: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(modifier = modifier.verticalScroll(scrollState)) {
        DiaryInfosEditor(
            labelFilter = labelFilter,
            onLabelFilterChange = onLabelFilterChange,
            filteredLabels = filteredLabels,
            selectedLabels = selectedLabels,
            sleepStartAt = sleepStartAt,
            sleepEndAt = sleepEndAt,
            onSleepStartAtChange = onSleepStartAtChange,
            onSleepEndAtChange = onSleepEndAtChange,
            onCheckChange = onCheckChange,
            onClickLabelSave = onClickLabelSave,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))

        DiaryContentEditor(
            title = title,
            onTitleChange = onTitleChange,
            diaryContents = diaryContents,
            onContentTextChange = onContentTextChange,
            onContentFocusChange = { onCurrentFocusContentChange(it) },
            onContentTextPositionChange = { onContentTextPositionChange(it) },
            onContentImageDelete = onContentImageDelete,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        )
    }
}

internal data class DiaryEditorParams(
    val diaryInfoEditorParams: DiaryInfoEditorParams,
    val diaryContentEditorParams: DiaryContentEditorParams,
)

internal data class DiaryInfoEditorParams(
    val labelFilter: String,
    val onLabelFilterChange: (String) -> Unit,
    val filteredLabels: List<LabelUi>,
    val selectedLabels: Set<LabelUi>,
    val sleepStartAt: ZonedDateTime,
    val onSleepStartAtChange: (ZonedDateTime) -> Unit,
    val sleepEndAt: ZonedDateTime,
    val onSleepEndAtChange: (ZonedDateTime) -> Unit,
    val onCheckChange: (LabelUi) -> Unit,
    val onClickLabelSave: () -> Unit,
)

internal data class DiaryContentEditorParams(
    val title: String,
    val onTitleChange: (String) -> Unit,
    val diaryContents: List<DiaryContentUi>,
    val onContentTextChange: (Int, String) -> Unit,
    val onContentFocusChange: (Int) -> Unit,
    val onContentTextPositionChange: (Int) -> Unit,
    val onContentImageDelete: (Int) -> Unit,
)
