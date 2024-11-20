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
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component.DiaryContentEditor
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component.DiaryContentEditorParams
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component.DiaryInfoEditorParams
import com.boostcamp.dreamteam.dreamdiary.feature.diary.write.component.DiaryInfosEditor

@Composable
internal fun DiaryEditor(
    diaryInfoEditorParams: DiaryInfoEditorParams,
    diaryContentEditorParams: DiaryContentEditorParams,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
) {
    val scrollState = rememberScrollState()

    Column(modifier = modifier.verticalScroll(scrollState)) {
        DiaryInfosEditor(
            diaryInfoEditorParams = diaryInfoEditorParams,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            readOnly = readOnly,
        )
        Spacer(modifier = Modifier.height(24.dp))

        DiaryContentEditor(
            diaryContentEditorParams = diaryContentEditorParams,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            readOnly = readOnly,
        )
    }
}

internal data class DiaryEditorParams(
    val diaryInfoEditorParams: DiaryInfoEditorParams,
    val diaryContentEditorParams: DiaryContentEditorParams,
)
