package com.projectapp.firebasechat.viewmodel

import android.content.res.Resources
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInViewModel(private val resources: Resources) : ViewModel() {
//
//    var auth: FirebaseAuth = Firebase.auth
//
//    fun signInGoogleAccount(task:Task<GoogleSignInAccount>) {
//        try {
//            val account = task.getResult(ApiException::class.java)
//            if (account != null) {
//                firebaseAuthWithGoogle(account.idToken.toString())
//            }
//        } catch (e: ApiException) {
//            Log.d("Mytag", e.message.toString())
//        }
//    }
//
//
//    fun getClient(): GoogleSignInClient {
//        val gso = GoogleSignInOptions
//            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(resources.getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        return GoogleSignIn.getClient(requireContext(), gso)
//        return GoogleSignIn.getClient()
//    }
//
//    fun signInWithGoogle() {
//        val signInClient = getClient()
//        launcher.launch(signInClient.signInIntent)
//    }
//
//    fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
//            if (authResult.isSuccessful) {
//                Log.d("Mytag", "Google sign in is successful")
//            } else {
//                Log.d("Mytag", "Google sign in is ERROR")
//            }
//        }
//    }
}