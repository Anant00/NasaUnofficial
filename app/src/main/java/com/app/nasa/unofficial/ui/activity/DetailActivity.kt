package com.app.nasa.unofficial.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.nasa.unofficial.R
import com.app.nasa.unofficial.databinding.ActivityDetailsBinding
import com.app.nasa.unofficial.utils.IntentUtil
import com.squareup.picasso.Picasso


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        val data: IntentUtil? = intent?.getParcelableExtra("data")
        val position = intent?.getIntExtra("position", 0)
        binding.item = position?.let { data?.imageList?.get(it) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
        Picasso.get().cancelTag("image")
    }

    override fun onStop() {
        super.onStop()
        Picasso.get().cancelTag("image")
    }

    override fun onDestroy() {
        super.onDestroy()
        Picasso.get().cancelTag("image")
    }
}