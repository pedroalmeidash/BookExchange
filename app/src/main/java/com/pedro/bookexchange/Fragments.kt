package com.pedro.bookexchange

import kotlinx.serialization.Serializable

@Serializable
sealed class Fragments {
    @Serializable
    data object AddBook : Fragments()
    @Serializable
    data class Book(val bookId: String) : Fragments()
    @Serializable
    data class BookProposal(val bookId: String) : Fragments()
    @Serializable
    data object SignIn : Fragments()
    @Serializable
    data object SignUp : Fragments()
    @Serializable
    data object Splash : Fragments()
}