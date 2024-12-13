package com.altaf.plantanist.ui.auth

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.altaf.plantanist.ui.login.LoginFragment
import com.altaf.plantanist.ui.welcome.WelcomeFragmentOne
import com.altaf.plantanist.ui.welcome.WelcomeFragmentTwo

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WelcomeFragmentOne()
            1 -> WelcomeFragmentTwo()
            2 -> LoginFragment() // Halaman login
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
