package com.projectapp.firebasechat.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SignInViewModel @Inject constructor() : ViewModel() {

    private var _mutableAuthStateFlow = MutableStateFlow<AuthState>(AuthState.AuthNone)
    val authStateFlow: StateFlow<AuthState>
        get() = _mutableAuthStateFlow.asStateFlow()

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        _mutableAuthStateFlow.value = AuthState.AuthInProgress
        Firebase.auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                _mutableAuthStateFlow.value = AuthState.AuthSuccess
            } else {
                _mutableAuthStateFlow.value = AuthState.AuthError
            }
        }
    }
}

sealed class AuthState {
    object AuthNone : AuthState()
    object AuthInProgress : AuthState()
    object AuthSuccess : AuthState()
    object AuthError : AuthState()
}