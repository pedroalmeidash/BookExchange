package com.pedro.bookexchange.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.pedro.bookexchange.common.BookViewState

@Entity(tableName = "proposals")
data class Proposal(
    @PrimaryKey val proposalId: String,
    @ColumnInfo("book_owner") val bookOwner: String,
    @ColumnInfo("book_proposed") val bookProposed: String,
    @ColumnInfo("status") val status: String,
)

data class ProposalBook(
    val proposalId: String,
    val ownerBook: BookViewState,
    val proposedBook: BookViewState,
    val status: String,
) {
    constructor() : this("", BookViewState("", "", "", "", "", "", false), BookViewState("", "", "", "", "", "", false), "")
}