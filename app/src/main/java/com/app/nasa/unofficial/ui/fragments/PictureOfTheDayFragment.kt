package com.app.nasa.unofficial.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.app.nasa.unofficial.api.apimodel.NasaImages
import com.app.nasa.unofficial.databinding.FragmentPictureofthedayBinding
import com.app.nasa.unofficial.ui.activity.DetailActivity
import com.app.nasa.unofficial.ui.adapters.ImagesAdapter
import com.app.nasa.unofficial.utils.*
import com.app.nasa.unofficial.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class PictureOfTheDayFragment : DaggerFragment(), OnRecyclerViewItemClick {
    private lateinit var binding: FragmentPictureofthedayBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var mainViewModel: MainViewModel
    private val imagesAdapter: ImagesAdapter by lazy { ImagesAdapter(this) }

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
        binding.recyclerViewImages.apply {
            setItemViewCacheSize(100)
            setHasFixedSize(true)
            adapter = imagesAdapter
        }
    }

    private fun setViewModel() {
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        mainViewModel.loadInitialData.observe(viewLifecycleOwner, Observer {
            binding.resource = it
            showData(it)
        })
        mainViewModel.loadMoreData.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS || it.status == Status.ERROR) {
                binding.loadingMore = false
            }
        })
    }

    private fun showData(resource: Resource<List<NasaImages>>?) {
        when (resource?.status) {

            Status.SUCCESS -> {
                CoroutineScope(IO).launch {
                    imagesAdapter.submitList(resource.data)
                }
                binding.loadingMore = false
            }

            Status.ERROR -> {
                showLog(resource.message)
                binding.loadingMore = false
            }

            Status.LOADING -> {
                if (!resource.data.isNullOrEmpty()) {
                    binding.resource = Resource.success(resource)
                    CoroutineScope(IO).launch {
                        imagesAdapter.submitList(resource.data)
                    }
                }
            }
        }

    }

    private fun onLoadMore() {
        val layoutManager = binding.recyclerViewImages.layoutManager as StaggeredGridLayoutManager
        binding.recyclerViewImages.addOnScrollListener(object : EndlessScroll(layoutManager) {
            override fun onLoadMore(current_page: Int) {
                binding.loadingMore = true
                mainViewModel.incrementPage(current_page)
            }
        })
    }

    override fun onItemClick(position: Int) {
        context?.let {
            startActivity(
                Intent(it, DetailActivity::class.java)
                    .putExtra("data", IntentUtil(imagesAdapter.currentList))
                    .putExtra("position", position)
            )
            activity?.overridePendingTransition(0, 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Picasso.get().cancelTag("image")
    }
}
