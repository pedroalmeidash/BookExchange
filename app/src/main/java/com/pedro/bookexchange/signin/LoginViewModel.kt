package com.pedro.bookexchange.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.bookexchange.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userService: UserService,
) : ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun signIn() {
        viewModelScope.launch(
            CoroutineExceptionHandler { coroutineContext, throwable -> },
        ) {
            userService.signIn(email.value, password.value)
        }
    }
}