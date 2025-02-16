package com.pedro.bookexchange.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val bookId: String,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "book_title") val bookTitle: String,
    @ColumnInfo(name = "book_image") val bookImage: String,
    @ColumnInfo(name = "book_author") val bookAuthor: String,
    @ColumnInfo(name = "available_for_proposal") val availableForProposal: Boolean,
)