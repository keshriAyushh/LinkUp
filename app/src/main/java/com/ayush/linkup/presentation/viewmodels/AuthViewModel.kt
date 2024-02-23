package com.ayush.linkup.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.linkup.data.model.User
import com.ayush.linkup.data.repository.AuthRepository
import com.ayush.linkup.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signInState = MutableStateFlow<State<Boolean>>(State.None)
    val signInState get() = _signInState

    private val _signUpState = MutableStateFlow<State<Boolean>>(State.None)
    val signUpState get() = _signUpState

    var isUserSignedIn: Boolean = false


    private val _forgotPasssState = MutableStateFlow<State<Boolean>>(State.None)
    val forgotPassState get() = _forgotPasssState


    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authRepository.signIn(email, password)
                .collect {
                    signInState.value = it
                }
        }
    }

    fun signUp(user: User) {
        viewModelScope.launch {
            authRepository.signUp(user)
                .collect {
                    signUpState.value = it
                }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            authRepository.forgotPassword(email).collect {
                forgotPassState.value = it
            }
        }
    }

    fun isUserSignedIn() {
        isUserSignedIn = authRepository.isUserSignedIn()
    }

    fun signOut() {
        authRepository.signOut()
    }

    init {
        isUserSignedIn()
    }
}