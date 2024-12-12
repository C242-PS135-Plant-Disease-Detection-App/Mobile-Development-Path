package com.altaf.plantanist.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.altaf.plantanist.R
import com.altaf.plantanist.data.AuthenticationDatabase
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private lateinit var database: AuthenticationDatabase
    private lateinit var textLoggedInAs: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        database = AuthenticationDatabase.getDatabase(requireContext())

        textLoggedInAs = view.findViewById(R.id.text_logged_in_as)
        val logoutButton: Button = view.findViewById(R.id.button_logout)

        // Load user data
        lifecycleScope.launch {
            val token = database.tokenDao().getToken()
            token?.let {
                textLoggedInAs.text = "Logged in as:\n${it.email}"
            }
        }

        logoutButton.setOnClickListener {
            lifecycleScope.launch {
                database.tokenDao().deleteToken() // Clear the token
            }
            val navController = findNavController()
            navController.navigate(R.id.navigation_login)
        }

        return view
    }
}