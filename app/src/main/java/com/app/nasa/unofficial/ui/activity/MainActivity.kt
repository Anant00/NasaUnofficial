package com.app.nasa.unofficial.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.app.nasa.unofficial.R
import com.app.nasa.unofficial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            val navController =
                Navigation.findNavController(this@MainActivity, R.id.mainNavigationFragment)
            bottomNavigation.setupWithNavController(navController)
        }
    }
}
