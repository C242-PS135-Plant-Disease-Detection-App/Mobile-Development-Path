package com.altaf.plantanist

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.altaf.plantanist.data.AuthenticationDatabase
import com.altaf.plantanist.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AuthenticationDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AuthenticationDatabase.getDatabase(this)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_scan, R.id.navigation_history, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Set up a listener to hide/show the bottom navigation and action bar
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_login -> {
                    navView.visibility = View.GONE
                    supportActionBar?.hide()
                }
                else -> {
                    navView.visibility = View.VISIBLE
                    supportActionBar?.show()
                }
            }
        }

        // Check for existing token
        lifecycleScope.launch {
            val token = database.tokenDao().getToken()
            if (token != null) {
                // Navigate to the main screen directly
                navController.navigate(R.id.navigation_scan)
            }
        }
    }
}