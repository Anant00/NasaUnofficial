package com.app.nasa.unofficial.ui

import android.os.Bundle
import com.app.nasa.unofficial.R
import com.app.nasa.utils.showLog
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var string: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showLog(string)
    }
}
