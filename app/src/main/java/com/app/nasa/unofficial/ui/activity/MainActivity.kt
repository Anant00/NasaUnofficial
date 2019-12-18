package com.app.nasa.unofficial.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.nasa.unofficial.R
import com.app.nasa.unofficial.databinding.ActivityMainBinding
import com.app.nasa.unofficial.ui.adapters.MARS_FRAGMENT_INDEX
import com.app.nasa.unofficial.ui.adapters.POD_FRAGMENT_INDEX
import com.app.nasa.unofficial.ui.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()
    }

    private fun getTabTitle(position: Int) = when (position) {
        POD_FRAGMENT_INDEX -> "POD"
        MARS_FRAGMENT_INDEX -> "MARS"
        else -> ""
    }
}
