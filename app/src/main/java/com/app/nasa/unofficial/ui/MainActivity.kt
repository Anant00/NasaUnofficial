package com.app.nasa.unofficial.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.nasa.unofficial.R
import com.app.nasa.unofficial.api.apimodel.NasaImages
import com.app.nasa.unofficial.repository.NetworkRepo
import com.app.nasa.unofficial.utils.Status
import com.app.nasa.unofficial.utils.showLog
import com.app.nasa.unofficial.viewmodel.MainViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var repoViewModel: MainViewModel
    @Inject
    lateinit var repo: NetworkRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewModel()
    }

    private fun setViewModel() {
        repoViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        repoViewModel.getImageData()?.observe(this, Observer {
            showData(it)
        })
    }

    private fun showData(data: Resource<List<NasaImages>>) {
        when (data.status) {
            Status.SUCCESS -> {
                data.data?.iterator()?.forEach {
                    it.title?.let { imageTitle -> showLog(imageTitle) }
                }
            }
            Status.ERROR -> {
                showLog(data.message!!)
            }
            Status.LOADING -> {
                showLog("Loading")
            }
        }
    }
}
