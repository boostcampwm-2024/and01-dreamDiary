package com.boostcamp.dreamteam.dreamdiary.community.write

import com.boostcamp.dreamteam.dreamdiary.community.model.vo.PostContentUi
import com.boostcamp.dreamteam.dreamdiary.community.model.vo.toPostContentUi
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary

data class CommunityWriteScreenState(
    val editorState: EditorState = EditorState(),
    val isLoading: Boolean = true,
)

data class EditorState(
    val title: String = "",
    val contents: List<PostContentUi> = listOf(PostContentUi.Text("")),
)

internal fun Diary.toEditorState(): EditorState =
    EditorState(
        title = title,
        contents = diaryContents.map { it.toPostContentUi() },
    )
