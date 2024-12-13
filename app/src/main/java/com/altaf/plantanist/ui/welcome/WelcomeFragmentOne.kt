package com.altaf.plantanist.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.altaf.plantanist.R
import com.altaf.plantanist.ui.auth.ViewPagerAdapter

class WelcomeFragmentOne : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome_one, container, false)
    }
}
