package com.pedro.bookexchange.book

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pedro.bookexchange.Fragments
import com.pedro.bookexchange.common.BookViewState
import com.pedro.bookexchange.database.AppDatabase
import com.pedro.bookexchange.databaseCall
import com.pedro.bookexchange.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Optional
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val userService: UserService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val book = savedStateHandle.toRoute<Fragments.Book>()
    val state = MutableStateFlow(Optional.empty<Pair<Boolean, BookViewState>>())
    val navigateBack = MutableStateFlow(false)

    fun getBook() {
        viewModelScope.launch {
            val book = databaseCall {
                FirebaseDatabase
                    .getInstance()
                    .getReference("Books")
                    .child(book.bookId)
            }.getValue(BookViewState::class.java)
            val user = userService.getUser()
            state.value = Optional.ofNullable(Pair(user?.id == book?.userId, book!!))
        }
    }

    fun updateBook(availableForProposal: Boolean) {
        viewModelScope.launch {
            databaseCall {
                FirebaseDatabase
                    .getInstance()
                    .getReference("Books")
                    .child(book.bookId)
            }.getValue(BookViewState::class.java)?.let {
                FirebaseDatabase
                    .getInstance()
                    .getReference("Books")
                    .child(book.bookId)
                    .setValue(it.copy(availableForProposal = availableForProposal))
            }
            navigateBack.value = true
        }
    }
}