package com.app.nasa.unofficial.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.nasa.unofficial.R
import com.app.nasa.unofficial.api.apimodel.NasaImages
import com.app.nasa.unofficial.databinding.ActivityMainBinding
import com.app.nasa.unofficial.repository.networkbound.NetworkRepo
import com.app.nasa.unofficial.ui.adapters.ImagesAdapter
import com.app.nasa.unofficial.utils.Resource
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
    private val imagesAdapter by lazy { ImagesAdapter() }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setViewModel()
    }

    private fun setViewModel() {
        binding.recyclerViewImages.setItemViewCacheSize(20)
        repoViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        repoViewModel.getImageData()?.observe(this, Observer {
            showData(it)
        })
    }

    private fun showData(resource: Resource<List<NasaImages>>) {
        binding.resource = resource
        when (resource.status) {
            Status.SUCCESS -> {
                imagesAdapter.submitList(resource.data)
                binding.recyclerViewImages.adapter = imagesAdapter
                binding.recyclerViewImages.scheduleLayoutAnimation()
            }
            Status.ERROR -> {
                showLog(resource.message)
            }
            Status.LOADING -> {
                showLog("Loading")
            }
        }
    }
}
