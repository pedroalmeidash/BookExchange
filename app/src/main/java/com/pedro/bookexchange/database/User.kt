package com.pedro.bookexchange.database

data class User(
    val id: String,
    val name: String = "",
    val email: String = "",
) {
    constructor() : this("", "", "")
}