package com.pedro.bookexchange

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.pedro.bookexchange.database.User
import com.pedro.bookexchange.service.UserService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun databaseCall(db: () -> DatabaseReference): DataSnapshot = suspendCancellableCoroutine {
    val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            it.resume(snapshot)
        }

        override fun onCancelled(error: DatabaseError) {}
    }

    db().addListenerForSingleValueEvent(listener)
}
