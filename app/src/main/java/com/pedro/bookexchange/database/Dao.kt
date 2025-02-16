package com.pedro.bookexchange.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pedro.bookexchange.global.CurrentUser

@Dao
interface Dao {
    @Query("SELECT * FROM books WHERE user_name IN (:userName)")
    suspend fun getBooksByUser(userName: String): List<Book>

    @Insert
    suspend fun insertAll(vararg books: Book)

    @Query("SELECT * FROM books WHERE book_title LIKE '%' || :title || '%' AND user_name != :currentUser AND available_for_proposal = 1")
    suspend fun searchBooks(
        title: String,
        currentUser: String = CurrentUser.currentUser.name,
    ): List<Book>

    @Update
    suspend fun updateBook(book: Book)

    @Query("UPDATE books SET available_for_proposal = :availableForProposal WHERE bookId = :bookId")
    suspend fun updateBookAvailableForProposal(bookId: String, availableForProposal: Boolean)

    @Query("SELECT * FROM books WHERE bookId == :bookId")
    suspend fun getBook(bookId: String): Book

}