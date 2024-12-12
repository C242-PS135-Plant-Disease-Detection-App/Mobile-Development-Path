package com.altaf.plantanist.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.SignInButton
import com.altaf.plantanist.R
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.altaf.plantanist.data.AuthenticationDatabase

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Get the AuthenticationDatabase instance
        val database = AuthenticationDatabase.getDatabase(requireContext())
        
        // Use the factory to create the ViewModel
        val factory = LoginViewModelFactory(database)
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        val signInButton: SignInButton = view.findViewById(R.id.button_sign_in)
        signInButton.setSize(SignInButton.SIZE_WIDE)
        signInButton.setOnClickListener { signIn() }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == -1) { // RESULT_OK
                val data: Intent? = result.data
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }

        return view
    }

    private fun signIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                viewModel.signInWithGoogle(idToken) { success ->
                    if (success) {
                        val navController = findNavController()
                        navController.navigate(R.id.navigation_scan)
                    } else {
                        Toast.makeText(requireContext(), "Sign-in failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: ApiException) {
            Toast.makeText(requireContext(), "Sign-in error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}