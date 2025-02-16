package com.pedro.bookexchange.service

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.snapshots
import com.pedro.bookexchange.database.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserService @Inject constructor() {

    val currentUser: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser?.let {
                User(id = it.uid)
            }
            this.trySend(user)
        }

        Firebase.auth.addAuthStateListener(listener)
        awaitClose { Firebase.auth.removeAuthStateListener(listener) }
    }

    suspend fun getUser(): User? {
        val userId = currentUser.first()?.id ?: return null
        return FirebaseDatabase
            .getInstance()
            .getReference("Users")
            .child(userId)
            .snapshots.first().getValue(User::class.java)
    }

    fun hasUser(): Boolean {
        return Firebase.auth.currentUser != null
    }

    suspend fun signIn(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signUp(email: String, name: String, password: String) {
        val user = Firebase.auth.createUserWithEmailAndPassword(email, password).await().user
        user?.let {
            FirebaseDatabase
                .getInstance()
                .getReference("Users")
                .child(it.uid)
                .setValue(
                    User(
                        id = it.uid,
                        name = name,
                        email = email
                    )
                )
        }
    }

    suspend fun signOut() {
        Firebase.auth.signOut()
    }
}