package com.pedro.bookexchange.library

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pedro.bookexchange.MainActivity
import com.pedro.bookexchange.common.BookViewState
import com.pedro.bookexchange.database.AppDatabase
import com.pedro.bookexchange.databaseCall
import com.pedro.bookexchange.global.CurrentUser
import com.pedro.bookexchange.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val userService: UserService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val isToSelected = savedStateHandle.toRoute<MainActivity.Tab.Library>().select

    private val _state = MutableStateFlow(LibraryViewState(emptyList(), isToSelected))
    val state = _state.asStateFlow()

    fun getBooks() {
        viewModelScope.launch {
            val currentUser = userService.currentUser.first()
            val books = databaseCall {
                FirebaseDatabase
                    .getInstance()
                    .getReference("Books")
            }.children
                .map { it.getValue(BookViewState::class.java) }
                .filterNotNull()
                .filter { it.userId == currentUser?.id }


            _state.value = LibraryViewState(
                books = books,
                toSelect = isToSelected,
            )
        }
    }
}