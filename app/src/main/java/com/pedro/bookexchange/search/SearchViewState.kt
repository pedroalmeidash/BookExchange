package com.pedro.bookexchange.search

import com.pedro.bookexchange.common.BookViewState

data class SearchViewState(
    val results: List<BookViewState>
)