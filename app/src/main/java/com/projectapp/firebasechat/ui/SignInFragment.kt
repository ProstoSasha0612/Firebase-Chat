package com.projectapp.firebasechat.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.projectapp.firebasechat.App
import com.projectapp.firebasechat.R
import com.projectapp.firebasechat.appComponent
import com.projectapp.firebasechat.databinding.FragmentSignInBinding
import com.projectapp.firebasechat.di.SignInFragmentComponent
import javax.inject.Inject

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = requireNotNull(_binding)
    lateinit var signInFragmentComponent: SignInFragmentComponent
        private set

    @Inject
    lateinit var signInClient: GoogleSignInClient

    @Inject
    lateinit var auth: FirebaseAuth



    private val launcher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account.idToken.toString())
                    openChatFragment()
                }
            } catch (e: ApiException) {
                Log.d("Mytag", e.message.toString())
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInFragmentComponent =
            requireContext().appComponent.signInFragmentComponentFactory()
                .create(requireActivity() as MainActivity)
        signInFragmentComponent.inject(this)

        binding.btnSignIn.setOnClickListener {
            signInWithGoogle(signInClient)
        }

    }

    private fun signInWithGoogle(signInClient: GoogleSignInClient) {
        launcher.launch(signInClient.signInIntent)
//        openChatFragment()
    }

    //    private val launcher: ActivityResultLauncher<Intent> =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//                if (account != null) {
//                    firebaseAuthWithGoogle(account.idToken.toString())
//                    openChatFragment()
//                }
//            } catch (e: ApiException) {
//                Log.d("Mytag", e.message.toString())
//            }
//        }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                Log.d("Mytag", "Google sign in is successful")
            } else {
                Log.d("Mytag", "Google sign in is ERROR")
            }
        }
    }

    private fun openChatFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ChatFragment.newInstance())
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignInFragment()
    }
}