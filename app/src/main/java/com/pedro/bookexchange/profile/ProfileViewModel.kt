package com.pedro.bookexchange.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.snapshots
import com.pedro.bookexchange.MainActivity
import com.pedro.bookexchange.common.BookViewState
import com.pedro.bookexchange.database.ProposalBook
import com.pedro.bookexchange.database.User
import com.pedro.bookexchange.databaseCall
import com.pedro.bookexchange.proposallist.ProposalViewState
import com.pedro.bookexchange.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val service: UserService,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val userId = savedStateHandle.toRoute<MainActivity.Tab.Profile>().userId
    private val isCurrentUser = savedStateHandle.toRoute<MainActivity.Tab.Profile>().isCurrentUser

    val state = MutableStateFlow(
        ProfileViewState(
            userName = "",
            showOptions = false,
            booksInLibrary = emptyList(),
            exchangedBooks = "",
        ),
    )

    init {
        viewModelScope.launch {
            val userId = if (isCurrentUser) {
                service.currentUser.first()!!.id
            } else {
                userId
            }
            val user = getUser(userId)
            val books = getBooks(userId)
            val proposals = getProposals(userId)
            state.value = ProfileViewState(
                userName = user.name,
                booksInLibrary = books,
                exchangedBooks = if (proposals.isEmpty()) {
                    "Esse usuário nunca realizou troca"
                } else {
                    "O usuário já realizou ${proposals.size} troca" + if (proposals.size > 1) {
                        "s"
                    } else ""
                },
                showOptions = isCurrentUser || userId == service.currentUser.first()!!.id,
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            service.signOut()
        }
    }

    private suspend fun getProposals(userId: String): List<ProposalBook> {
        return databaseCall {
            FirebaseDatabase
                .getInstance()
                .getReference("Proposals")
        }.children
            .map { it.getValue(ProposalBook::class.java) }
            .filterNotNull()
            .filter { it.ownerBook.userId == userId || it.proposedBook.userId == userId }
            .filter { it.status == "accepted" }
    }

    private suspend fun getUser(userId: String): User {
        return FirebaseDatabase
            .getInstance()
            .getReference("Users")
            .child(userId)
            .snapshots
            .first()
            .getValue(User::class.java)!!
    }

    private suspend fun getBooks(userId: String): List<BookViewState> {
        return databaseCall {
            FirebaseDatabase
                .getInstance()
                .getReference("Books")
        }.children
            .map { it.getValue(BookViewState::class.java) }
            .filterNotNull()
            .filter { it.userId == userId }
    }
}