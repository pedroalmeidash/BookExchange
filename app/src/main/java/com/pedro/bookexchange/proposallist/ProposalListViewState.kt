package com.pedro.bookexchange.proposallist

import com.pedro.bookexchange.common.BookViewState
import com.pedro.bookexchange.database.User

data class ProposalListViewState(
    val proposals: List<ProposalViewState>,
    val usersList: List<User>,
    val userId: String,
)

data class ProposalViewState(
    val id: String,
    val userBook: BookViewState,
    val proposedBook: BookViewState,
    val state: ProposalState,
)

sealed class ProposalState {
    data object Accepted : ProposalState()
    data object Waiting : ProposalState()
    data object Rejected : ProposalState()
    data object Pending : ProposalState()
}