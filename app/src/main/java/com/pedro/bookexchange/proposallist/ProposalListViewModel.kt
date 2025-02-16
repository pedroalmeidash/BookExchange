package com.pedro.bookexchange.proposallist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.pedro.bookexchange.common.BookViewState
import com.pedro.bookexchange.database.ProposalBook
import com.pedro.bookexchange.database.User
import com.pedro.bookexchange.databaseCall
import com.pedro.bookexchange.global.CurrentUser
import com.pedro.bookexchange.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProposalListViewModel @Inject constructor(
    private val userService: UserService,
) : ViewModel() {

    val state = MutableStateFlow(
        ProposalListViewState(emptyList(), emptyList(), "")
    )

    init {
        refreshProposalList()
    }

    private fun refreshProposalList() {
        viewModelScope.launch {
            val user = userService.currentUser.first()
            val proposals = getProposals()
            state.value = ProposalListViewState(
                proposals.map {
                    ProposalViewState(
                        id = it.proposalId,
                        userBook = BookViewState(
                            bookId = it.ownerBook.bookId,
                            userName = it.ownerBook.userName,
                            bookTitle = it.ownerBook.bookTitle,
                            bookImage = it.ownerBook.bookImage,
                            bookAuthor = it.ownerBook.bookAuthor,
                            userId = it.ownerBook.userId,
                            availableForProposal = false,
                        ),
                        proposedBook = BookViewState(
                            bookId = it.proposedBook.bookId,
                            userName = it.proposedBook.userName,
                            bookTitle = it.proposedBook.bookTitle,
                            bookImage = it.proposedBook.bookImage,
                            bookAuthor = it.proposedBook.bookAuthor,
                            userId = it.proposedBook.userId,
                            availableForProposal = false,
                        ),
                        state = when (it.status) {
                            "pending" -> {
                                if (it.ownerBook.userId == user?.id) {
                                    ProposalState.Pending
                                } else {
                                    ProposalState.Waiting
                                }
                            }
                            "accepted" -> ProposalState.Accepted
                            else -> ProposalState.Rejected
                        }
                    )
                }.reversed(),
                databaseCall {
                    FirebaseDatabase.getInstance().getReference("Users")
                }.children.mapNotNull { it.getValue(User::class.java) },
                user?.id!!
            )
        }
    }

    fun acceptProposal(proposalId: String) {
        viewModelScope.launch {
            val proposals = getProposals()
            val proposal = proposals.first { it.proposalId == proposalId }
            updateProposal(proposal.copy(status = "accepted"))

            updateBook(proposal.ownerBook)
            updateBook(proposal.proposedBook)

            val books = listOf(proposal.proposedBook.bookId, proposal.ownerBook.bookId)

            proposals.map {
                if (books.contains(it.ownerBook.bookId) || books.contains(it.proposedBook.bookId)) {
                    if ((it.status == "pending" || it.status == "waiting") && it.proposalId != proposalId) {
                        updateProposal(it.copy(status = "rejected"))
                    }
                }
            }

            refreshProposalList()
        }
    }

    fun rejectProposal(proposalId: String) {
        viewModelScope.launch {
            val proposal = getProposals().first { it.proposalId == proposalId }
            updateProposal(proposal.copy(status = "rejected"))
            refreshProposalList()
        }
    }

    private suspend fun getProposals(): List<ProposalBook> {
        val currentUser = userService.currentUser.first()
        return databaseCall {
            FirebaseDatabase
                .getInstance()
                .getReference("Proposals")
        }.children
            .map { it.getValue(ProposalBook::class.java) }
            .filterNotNull()
            .filter { it.ownerBook.userId == currentUser?.id || it.proposedBook.userId == currentUser?.id }
            .reversed()
    }

    private suspend fun updateBook(book: BookViewState) {
        FirebaseDatabase
            .getInstance()
            .getReference("Books")
            .child(book.bookId)
            .setValue(book.copy(availableForProposal = false))
            .await()
    }

    private suspend fun updateProposal(proposalBook: ProposalBook) {
        FirebaseDatabase
            .getInstance()
            .getReference("Proposals")
            .child(proposalBook.proposalId)
            .setValue(proposalBook)
            .await()
    }
}