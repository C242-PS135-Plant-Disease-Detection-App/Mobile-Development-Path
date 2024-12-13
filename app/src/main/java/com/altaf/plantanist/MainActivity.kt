package com.altaf.plantanist
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.altaf.plantanist.data.AuthenticationDatabase
import com.altaf.plantanist.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.content.ContextCompat
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.altaf.plantanist.R

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

                R.id.navigation_detail -> {
                    navView.visibility = View.GONE
                    supportActionBar?.show()
                }

                R.id.welcomePagerFragment -> {
                    navView.visibility = View.GONE
                    supportActionBar?.hide()
                }

                else -> {
                    navView.visibility = View.VISIBLE
                    supportActionBar?.show()
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                if (navController.currentDestination?.id == R.id.navigation_scan) {
                    finish() // Exit the app if on ScanFragment
                } else {
                    navController.navigateUp() // Navigate back for other fragments
                }
            }
        })

        // Check for existing token
        lifecycleScope.launch {
            val token = database.tokenDao().getToken()
            if (token != null) {
                // Navigate to the main screen directly
                navController.navigate(R.id.navigation_scan)
            }
        }

        // Setel warna hijau untuk app bar
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.green3)))

        // Menambahkan TextView untuk judul dan memposisikannya di tengah
        val titleTextView = TextView(this)
        titleTextView.text = supportActionBar?.title
        titleTextView.setTextColor(Color.WHITE) // Set warna teks menjadi putih
        titleTextView.gravity = Gravity.CENTER // Set posisi teks ke tengah

        supportActionBar?.customView = titleTextView
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
