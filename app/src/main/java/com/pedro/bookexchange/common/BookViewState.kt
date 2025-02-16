package com.pedro.bookexchange.common

data class BookViewState(
    val bookId: String,
    val userId: String,
    val userName: String,
    val bookTitle: String,
    val bookImage: String,
    val bookAuthor: String,
    val availableForProposal: Boolean,
) {
    constructor(): this("", "", "", "", "", "", false)
}