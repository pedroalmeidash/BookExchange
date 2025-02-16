package com.pedro.bookexchange.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.pedro.bookexchange.common.BookViewState
import com.pedro.bookexchange.database.AppDatabase
import com.pedro.bookexchange.databaseCall
import com.pedro.bookexchange.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userService: UserService,
) : ViewModel() {

    val searchMutableFlow = MutableStateFlow("")
    val viewState = MutableStateFlow(
        SearchViewState(emptyList())
    )

    init {
        viewModelScope.launch {
            val currentUser = userService.currentUser.first()
            val books = databaseCall {
                FirebaseDatabase
                    .getInstance()
                    .getReference("Books")
            }.children
                .map { it.getValue(BookViewState::class.java) }
                .filterNotNull()
                .filter { it.userId != currentUser?.id }
            searchMutableFlow
                .debounce(300L)
                .map { search ->
                    books.filter { it.bookTitle.lowercase().contains(search.lowercase()) && it.availableForProposal }
                }.collect {
                    viewState.value = SearchViewState(
                        it.map {
                            BookViewState(
                                bookId = it.bookId,
                                userName = it.userName,
                                bookTitle = it.bookTitle,
                                bookImage = it.bookImage,
                                bookAuthor = it.bookAuthor,
                                userId = it.userId,
                                availableForProposal = it.availableForProposal,
                            )
                        }
                    )
                }
        }
    }

    fun searchBook(title: String) {
        searchMutableFlow.value = title
    }
}