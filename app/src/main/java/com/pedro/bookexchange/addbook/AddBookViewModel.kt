package com.pedro.bookexchange.addbook

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.database.FirebaseDatabase
import com.pedro.bookexchange.common.BookViewState
import com.pedro.bookexchange.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val userService: UserService,
) : ViewModel() {

    val state = MutableStateFlow(false)

    private fun uploadImage(
        uri: Uri,
        onImageUploaded: (String) -> Unit,
    ) {
        MediaManager.get().upload(uri).unsigned("png123").callback(object : UploadCallback {
            override fun onStart(requestId: String) {
                Log.d("Cloudinary Quickstart", "Upload start")
            }

            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                Log.d("Cloudinary Quickstart", "Upload progress")
            }

            override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                Log.d("Cloudinary Quickstart", "Upload success")
                val url = resultData["secure_url"] as String?
                url?.let { onImageUploaded(it) }
            }

            override fun onError(requestId: String, error: ErrorInfo) {
                Log.d("Cloudinary Quickstart", "Upload failed")
            }

            override fun onReschedule(requestId: String, error: ErrorInfo) {
            }
        }).dispatch()
    }

    fun onBookAdded(
        title: String,
        author: String,
        imageUri: Uri,
        availableForProposal: Boolean,
    ) {
        uploadImage(imageUri) {
            viewModelScope.launch {
                val currentUser = userService.getUser()
                val bookId = UUID.randomUUID().toString()
                FirebaseDatabase
                    .getInstance()
                    .getReference("Books")
                    .child(bookId)
                    .setValue(
                        BookViewState(
                            userName = currentUser?.name ?: "",
                            bookTitle = title,
                            bookAuthor = author,
                            bookImage = it,
                            bookId = bookId,
                            userId = currentUser?.id ?: "",
                            availableForProposal = availableForProposal,
                        )
                    )
                    .await()
                state.value = true
            }
        }
    }
}