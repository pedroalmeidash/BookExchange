package com.pedro.bookexchange.profile

import com.pedro.bookexchange.common.BookViewState

class ProfileViewState(
    val userName: String,
    val exchangedBooks: String,
    val booksInLibrary: List<BookViewState>,
    val showOptions: Boolean,
)
