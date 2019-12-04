package com.app.nasa.unofficial.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.nasa.unofficial.R
import com.app.nasa.unofficial.api.apimodel.NasaImages
import com.app.nasa.unofficial.databinding.ActivityMainBinding
import com.app.nasa.unofficial.ui.adapters.ImagesAdapter
import com.app.nasa.unofficial.utils.EndlessScroll
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
    private val imagesAdapter by lazy { ImagesAdapter() }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setRecyclerView()
        onLoadMore()
        setViewModel()
    }

    private fun setRecyclerView() {
        binding.recyclerViewImages.setItemViewCacheSize(20)
        binding.recyclerViewImages.adapter = imagesAdapter
    }

    private fun setViewModel() {
        repoViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        repoViewModel.getImageData()?.observe(this, Observer {
            showData(it)
        })
        repoViewModel.loadMoreData.observe(this, Observer {
            showData(it)
        })
    }

    private fun showData(resource: Resource<List<NasaImages>>) {
        binding.resource = resource
        when (resource.status) {
            Status.SUCCESS -> {
                setData(resource.data)
            }
            Status.ERROR -> {
                showLog(resource.message)
            }
            Status.LOADING -> {
                showLog(resource.message)
            }
        }
    }

    private fun onLoadMore() {
        val layoutManager = binding.recyclerViewImages.layoutManager as LinearLayoutManager
        binding.recyclerViewImages.addOnScrollListener(object : EndlessScroll(layoutManager) {
            override fun onLoadMore(current_page: Int) {
                repoViewModel.incrementPage(current_page)
            }
        })
    }

    private fun setData(list: List<NasaImages>?) {
        if (imagesAdapter.currentList.isNullOrEmpty()) {
            imagesAdapter.submitList(list)
            binding.recyclerViewImages.scheduleLayoutAnimation()
        } else {
            val newList = imagesAdapter.currentList.toMutableList()
            list?.let { newList.addAll(it) }
            imagesAdapter.submitList(newList)
        }
    }
}
