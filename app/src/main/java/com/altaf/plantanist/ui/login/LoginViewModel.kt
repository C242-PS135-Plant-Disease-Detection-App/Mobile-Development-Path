package com.altaf.plantanist.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.AuthCredential
import com.altaf.plantanist.data.Authentication
import com.altaf.plantanist.data.AuthenticationDatabase
import kotlinx.coroutines.launch

class LoginViewModel(private val database: AuthenticationDatabase) : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signInWithGoogle(idToken: String, onComplete: (Boolean) -> Unit) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        // Obtain Firebase ID token
                        it.getIdToken(false).addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {
                                val firebaseIdToken = tokenTask.result?.token
                                firebaseIdToken?.let { token ->
                                    saveToken(token, it.displayName ?: "", it.email ?: "") // Save token, name, and email
                                }
                            }
                        }
                    }
                }
                onComplete(task.isSuccessful)
            }
    }

    private fun saveToken(token: String, name: String, email: String) {
        viewModelScope.launch {
            database.tokenDao().insert(Authentication(token = token, name = name, email = email))
        }
    }
}

class LoginViewModelFactory(private val database: AuthenticationDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}