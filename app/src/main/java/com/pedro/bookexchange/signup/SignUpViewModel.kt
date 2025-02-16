package com.pedro.bookexchange.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.bookexchange.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userService: UserService,
) : ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val name = MutableStateFlow("")

    fun signIn() {
        GlobalScope.launch(
            CoroutineExceptionHandler { coroutineContext, throwable -> },
        ) {
            userService.signUp(email.value, name.value, password.value)
        }
    }
}