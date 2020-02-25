package com.app.nasa.unofficial.ui.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.nasa.unofficial.api.apimodel.NasaImages
import com.app.nasa.unofficial.databinding.FragmentPictureofthedayBinding
import com.app.nasa.unofficial.events.EventBus
import com.app.nasa.unofficial.events.MainActivityClickEvents
import com.app.nasa.unofficial.events.PODFragmentClickEvents
import com.app.nasa.unofficial.ui.activity.DetailActivity
import com.app.nasa.unofficial.ui.adapters.ImagesAdapter
import com.app.nasa.unofficial.utils.*
import com.app.nasa.unofficial.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class PictureOfTheDayFragment : DaggerFragment(), OnItemClick {
    private val clickEventsDisposable: CompositeDisposable? = CompositeDisposable()
    private lateinit var binding: FragmentPictureofthedayBinding
    private var onOpenScroll = true
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
        listenToClickEvents()
    }

    private fun setRecyclerView() {
        binding.recyclerViewImages.apply {
            recycledViewPool.setMaxRecycledViews(0, 100)
            setItemViewCacheSize(100)
            setHasFixedSize(true)
            adapter = imagesAdapter
        }
    }

    private fun setViewModel() {
        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
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

            Status.SUCCESS -> onSuccess(resource)

            Status.ERROR -> binding.loadingMore = false

            Status.LOADING -> onLoading(resource)
        }
    }

    private fun onLoading(resource: Resource<List<NasaImages>>) {
        if (!resource.data.isNullOrEmpty()) {
            binding.resource = Resource.success(resource)
            CoroutineScope(IO).launch {
                imagesAdapter.submitList(resource.data)
            }
            scrollRecViewToLastUserPosition()
            EventBus.publish(MainActivityClickEvents.UpdateBadge)
        }
    }

    private fun onSuccess(resource: Resource<List<NasaImages>>?) {
        CoroutineScope(IO).launch {
            imagesAdapter.submitList(resource?.data)
        }
        binding.loadingMore = false
        scrollRecViewToLastUserPosition()
    }

    private fun scrollRecViewToLastUserPosition() {
        val position = restorePosition()
        if (onOpenScroll && position != null) {
            binding.recyclerViewImages.scrollToPosition(position)
            onOpenScroll = false
        }
    }

    private fun onLoadMore() {
        val layoutManager = binding.recyclerViewImages.layoutManager as LinearLayoutManager
        binding.recyclerViewImages.addOnScrollListener(object : EndlessScroll(layoutManager) {
            override fun onLoadMore(current_page: Int) {
                binding.loadingMore = true
                mainViewModel.incrementPage(current_page)
            }
        })
    }

    private fun listenToClickEvents() {
        clickEventsDisposable?.add(
            EventBus
                .listen(PODFragmentClickEvents::class.java)
                .subscribe { event ->
                    when (event) {
                        is PODFragmentClickEvents.OnTabClicked -> {
                            binding.recyclerViewImages.scrollToPosition(0)
                            event.badge?.getTabAt(0)?.removeBadge()
                        }

                        is PODFragmentClickEvents.OnRecyclerViewItemClick -> {
                            navigateToDetailsActivity(event.position)
                        }
                    }
                })
    }

    private fun navigateToDetailsActivity(position: Int) {
        context?.let {
            startActivity(
                Intent(it, DetailActivity::class.java)
                    .putExtra("data", IntentUtil(imagesAdapter.currentList))
                    .putExtra("position", position)
            )
            activity?.overridePendingTransition(0, 0)
        }
    }

    private fun saveRecycleViewScrolledPosition() {
        val editor: SharedPreferences.Editor? =
            context?.getSharedPreferences("scroll", MODE_PRIVATE)?.edit()
        val layoutManager = binding.recyclerViewImages.layoutManager as LinearLayoutManager
        editor?.putInt("pos", layoutManager.findFirstVisibleItemPosition())
        editor?.apply()
    }

    private fun restorePosition(): Int? {
        val prefs: SharedPreferences? = context?.getSharedPreferences("scroll", MODE_PRIVATE)
        return prefs?.getInt("pos", 0)
    }

    override fun onResume() {
        super.onResume()
        restorePosition()
    }

    override fun onStop() {
        super.onStop()
        saveRecycleViewScrolledPosition()
    }

    override fun onDestroy() {
        super.onDestroy()
        saveRecycleViewScrolledPosition()
        Picasso.get().cancelTag("image")
        clickEventsDisposable?.let {
            if (!it.isDisposed) it.dispose()
        }
    }

    override fun onItemClick(position: Int) {

    }
}
