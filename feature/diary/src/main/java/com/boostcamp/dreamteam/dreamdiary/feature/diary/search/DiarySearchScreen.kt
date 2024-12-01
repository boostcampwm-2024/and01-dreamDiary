package com.boostcamp.dreamteam.dreamdiary.feature.diary.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.component.DiaryCard
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.tablist.pagedDiariesPreview
import com.boostcamp.dreamteam.dreamdiary.feature.diary.model.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.ui.PagingIndexKey

@Composable
fun DiarySearchScreen(
    onClickBack: () -> Unit,
    onClickDiary: (diaryId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewmodel: DiarySearchViewModel = hiltViewModel(),
) {
    val searchQuery = viewmodel.searchQuery.collectAsStateWithLifecycle()
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    val searchResults = viewmodel.searchResults.collectAsLazyPagingItems()

    DiarySearchScreenContent(
        onClickBack = onClickBack,
        onClickDiary = onClickDiary,
        searchQuery = searchQuery,
        onSearchQueryChange = viewmodel::updateSearchQuery,
        searchSuggestions = uiState.searchSuggestions,
        searchResults = searchResults,
        modifier = modifier,
    )
}

@Composable
private fun DiarySearchScreenContent(
    onClickBack: () -> Unit,
    onClickDiary: (diaryId: String) -> Unit,
    searchQuery: State<String>,
    onSearchQueryChange: (String) -> Unit,
    searchSuggestions: List<String>,
    searchResults: LazyPagingItems<DiaryUi>,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent),
            ) {
                SearchTopBar(
                    SearchTopBarState(
                        onClickBack = onClickBack,
                        searchQuery = searchQuery.value,
                        onSearchQueryChange = onSearchQueryChange,
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        searchSuggestions = searchSuggestions,
                    ),
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        },
        modifier = modifier,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 8.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (searchResults.itemCount == 0) {
                item {
                    Text(
                        text = stringResource(R.string.search_no_search_result),
                        style = MaterialTheme.typography.displaySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            items(
                count = searchResults.itemCount,
                key = { searchResults.peek(it)?.id ?: PagingIndexKey(it) },
            ) { index ->
                searchResults[index]?.let { diary ->
                    DiaryCard(
                        diary = diary,
                        onDiaryClick = { onClickDiary(diary.id) },
                        onDiaryEdit = { },
                        onDeleteDiary = { },
                        onShareDiary = { },
                        modifier = Modifier
                            .animateItem()
                            .fillMaxWidth(),
                        isMenuButtonVisible = false,
                    )
                }
            }
        }
    }
}

private data class SearchTopBarState(
    val onClickBack: () -> Unit,
    val searchQuery: String,
    val onSearchQueryChange: (String) -> Unit,
    val expanded: Boolean,
    val onExpandedChange: (Boolean) -> Unit,
    val searchSuggestions: List<String>,
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SearchTopBar(
    state: SearchTopBarState,
    modifier: Modifier = Modifier,
) {
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = state.searchQuery,
                onQueryChange = { state.onSearchQueryChange(it) },
                onSearch = { state.onExpandedChange(false) },
                expanded = state.expanded,
                onExpandedChange = { state.onExpandedChange(it) },
                placeholder = { Text(stringResource(R.string.search_input_placeholder)) },
                leadingIcon = {
                    AnimatedContent(state.expanded) { expanded ->
                        if (expanded) {
                            IconButton(
                                onClick = state.onClickBack,
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.search_back),
                                )
                            }
                        } else {
                            Icon(
                                Icons.Outlined.Search,
                                contentDescription = stringResource(R.string.search_search),
                            )
                        }
                    }
                },
            )
        },
        expanded = state.expanded,
        onExpandedChange = { state.onExpandedChange(it) },
        modifier = modifier,
    ) {
        state.searchSuggestions.forEach { value ->
            ListItem(
                headlineContent = { Text(value) },
                leadingContent = { Icon(Icons.Outlined.Search, contentDescription = null) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier
                    .clickable {
                        state.onSearchQueryChange(value)
                        state.onExpandedChange(false)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
            )
        }
    }
}

@Preview
@Composable
private fun DiarySearchScreenContentPreview() {
    DreamdiaryTheme {
        DiarySearchScreenContent(
            onClickBack = {},
            searchQuery = remember { mutableStateOf("") },
            onSearchQueryChange = {},
            searchSuggestions = listOf("검색어 추천1", "검색어 추천2", "검색어 추천3"),
            searchResults = pagedDiariesPreview.collectAsLazyPagingItems(),
            onClickDiary = { },
        )
    }
}
