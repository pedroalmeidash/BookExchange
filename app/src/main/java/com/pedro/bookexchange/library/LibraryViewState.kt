package com.pedro.bookexchange.library

import com.pedro.bookexchange.common.BookViewState

data class LibraryViewState(
    val books: List<BookViewState>,
    val toSelect: Boolean,
)