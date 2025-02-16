package com.pedro.bookexchange.bookproposal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.firebase.database.FirebaseDatabase
import com.pedro.bookexchange.Fragments
import com.pedro.bookexchange.common.BookViewState
import com.pedro.bookexchange.database.Proposal
import com.pedro.bookexchange.database.ProposalBook
import com.pedro.bookexchange.databaseCall
import com.pedro.bookexchange.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BookProposalViewModel @Inject constructor(
    private val userService: UserService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val bookId = savedStateHandle.toRoute<Fragments.BookProposal>().bookId
    private var anotherBookId: String? = null
    val navigateToList = MutableStateFlow(false)

    val state = MutableStateFlow(
        BookProposalViewState(
            emptyList(),
            false,
        )
    )

    init {
        initialLoad()
    }

    fun initialLoad() {
        viewModelScope.launch {
            state.value = BookProposalViewState(
                bookItems = listOf(
                    BookItemViewState.Book(getBook(bookId)),
                    BookItemViewState.AddBook,
                ),
                isButtonEnabled = false,
            )
        }
    }

    private suspend fun getBook(bookId: String): BookViewState {
        return databaseCall {
            FirebaseDatabase
                .getInstance()
                .getReference("Books")
                .child(bookId)
        }.getValue(BookViewState::class.java)!!
    }

    fun addBook(addedBookId: String) {
        anotherBookId = addedBookId
        viewModelScope.launch {
            val book = getBook(addedBookId)
            val initialBook = state.value.bookItems.first()
            state.value = BookProposalViewState(
                bookItems = listOf(
                    initialBook,
                    BookItemViewState.Book(book),
                ),
                isButtonEnabled = true,
            )
        }
    }

    fun onButtonTapped() {
        viewModelScope.launch {
            val owner = getBook(bookId)
            val another = getBook(anotherBookId!!)
            val id = UUID.randomUUID().toString()
            FirebaseDatabase
                .getInstance()
                .getReference("Proposals")
                .child(id)
                .setValue(
                    ProposalBook(
                        proposalId = id,
                        ownerBook = owner,
                        proposedBook = another,
                        status = "pending"
                    )
                ).await()
            navigateToList.value = true
        }
    }
}