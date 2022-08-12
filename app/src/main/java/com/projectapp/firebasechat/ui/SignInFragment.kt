package com.projectapp.firebasechat.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.projectapp.firebasechat.R
import com.projectapp.firebasechat.appComponent
import com.projectapp.firebasechat.databinding.FragmentSignInBinding
import com.projectapp.firebasechat.di.SignInFragmentComponent
import com.projectapp.firebasechat.viewmodel.AuthState
import com.projectapp.firebasechat.viewmodel.SignInViewModel
import javax.inject.Inject

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: SignInViewModel by lazy { ViewModelProvider(this)[SignInViewModel::class.java] }
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
                    viewModel.firebaseAuthWithGoogle(account.idToken.toString())
                }
            } catch (e: ApiException) {
                Log.d("Mytag EXCEPTION", e.message.toString())
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

        initFlowListeners()

    }

    private fun signInWithGoogle(signInClient: GoogleSignInClient) {
        launcher.launch(signInClient.signInIntent)
    }

    private fun initFlowListeners() {
        lifecycleScope.launchWhenStarted {
            viewModel.authStateFlow.collect { authState ->
                when (authState) {
                    is AuthState.AuthInProgress -> {
                        binding.btnSignIn.isEnabled = false
                        binding.progressBar.isVisible = true
                    }
                    is AuthState.AuthSuccess -> {
                        binding.btnSignIn.isEnabled = true
                        binding.progressBar.isVisible = false
                        Log.d("Mytag", "Google sign in is successful")
                        openChatFragment()
                    }
                    is AuthState.AuthError -> {
                        binding.btnSignIn.isEnabled = true
                        binding.progressBar.isVisible = false
                        Snackbar.make(binding.root, "Sign in error", Snackbar.LENGTH_SHORT).show()
                        Log.d("Mytag", "Google sign in is ERROR")
                    }
                    is AuthState.AuthNone -> {
                        binding.btnSignIn.isEnabled = true
                        binding.progressBar.isVisible = false
                    }
                }
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