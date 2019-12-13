package com.app.nasa.unofficial.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.nasa.unofficial.api.apimodel.NasaImages
import com.app.nasa.unofficial.databinding.FragmentPictureofthedayBinding
import com.app.nasa.unofficial.ui.adapters.ImagesAdapter
import com.app.nasa.unofficial.utils.EndlessScroll
import com.app.nasa.unofficial.utils.Resource
import com.app.nasa.unofficial.utils.Status
import com.app.nasa.unofficial.utils.showLog
import com.app.nasa.unofficial.viewmodel.MainViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class PictureOfTheDayFragment : DaggerFragment() {
    private lateinit var binding: FragmentPictureofthedayBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var repoViewModel: MainViewModel
    private val imagesAdapter by lazy { ImagesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPictureofthedayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        onLoadMore()
        setViewModel()
    }

    private fun setRecyclerView() {
        binding.recyclerViewImages.setItemViewCacheSize(100)
        binding.recyclerViewImages.setHasFixedSize(true)
        binding.recyclerViewImages.adapter = imagesAdapter
    }

    private fun setViewModel() {
        repoViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        repoViewModel.getImageData()?.observe(viewLifecycleOwner, Observer {
            binding.resource = it
            showData(it)
        })
        repoViewModel.loadMoreData.observe(viewLifecycleOwner, Observer {
            binding.loadingMore = true
            showData(it)
        })
    }

    private fun showData(resource: Resource<List<NasaImages>>?) {
        when (resource?.status) {
            Status.SUCCESS -> {
                setData(resource.data)
                binding.loadingMore = false
            }
            Status.ERROR -> {
                showLog(resource.message)
                binding.loadingMore = false
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
