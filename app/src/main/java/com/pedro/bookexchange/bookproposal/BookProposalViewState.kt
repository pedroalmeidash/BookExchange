package com.pedro.bookexchange.bookproposal

import com.pedro.bookexchange.common.BookViewState

data class BookProposalViewState(
    val bookItems: List<BookItemViewState>,
    val isButtonEnabled: Boolean,
)

sealed class BookItemViewState {
    data class Book(val viewState: BookViewState) : BookItemViewState()
    data object AddBook : BookItemViewState()
}
