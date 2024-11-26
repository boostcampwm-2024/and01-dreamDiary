package com.boostcamp.dreamteam.dreamdiary.community.write

import com.boostcamp.dreamteam.dreamdiary.community.model.vo.PostContentUi

data class CommunityWriteScreenState(
    val editorState: EditorState = EditorState(),
    val isLoading: Boolean = true,
)

data class EditorState(
    val title: String = "",
    val contents: List<PostContentUi> = listOf(PostContentUi.Text("")),
)
