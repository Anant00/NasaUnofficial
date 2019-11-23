package com.app.nasa.unofficial.ui

import android.os.Bundle
import android.view.View.GONE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.nasa.unofficial.R
import com.app.nasa.unofficial.api.apimodel.NasaImages
import com.app.nasa.unofficial.repository.NetworkRepo
import com.app.nasa.unofficial.repository.Resource
import com.app.nasa.unofficial.ui.adapters.ImagesAdapter
import com.app.nasa.unofficial.utils.Status
import com.app.nasa.unofficial.utils.showLog
import com.app.nasa.unofficial.viewmodel.MainViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var repoViewModel: MainViewModel
    @Inject
    lateinit var repo: NetworkRepo
    private val imagesAdapter by lazy { ImagesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewModel()
    }

    private fun setViewModel() {
        recyclerViewImages.adapter = imagesAdapter
        recyclerViewImages.setItemViewCacheSize(20)
        repoViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        repoViewModel.getImageData()?.observe(this, Observer {
            showData(it)
        })
    }

    private fun showData(data: Resource<List<NasaImages>>) {
        when (data.status) {
            Status.SUCCESS -> {
                with(data.data) {
                    imagesAdapter.submitList(this)
                    progressBar.visibility = GONE
                }
            }
            Status.ERROR -> {
                showLog(data.message!!)
                progressBar.visibility = GONE
            }
            Status.LOADING -> {
                showLog("Loading")
            }
        }
    }
}
