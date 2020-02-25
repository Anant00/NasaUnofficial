package com.app.nasa.unofficial.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.app.nasa.unofficial.R
import com.app.nasa.unofficial.databinding.ActivityMainBinding
import com.app.nasa.unofficial.events.EventBus
import com.app.nasa.unofficial.events.MainActivityClickEvents
import com.app.nasa.unofficial.events.PODFragmentClickEvents
import com.app.nasa.unofficial.notifications.DailyImageUpdateWork
import com.app.nasa.unofficial.repository.networkbound.NetworkRepo
import com.app.nasa.unofficial.ui.adapters.MARS_FRAGMENT_INDEX
import com.app.nasa.unofficial.ui.adapters.POD_FRAGMENT_INDEX
import com.app.nasa.unofficial.ui.adapters.ViewPagerAdapter
import com.app.nasa.unofficial.utils.showLog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val compositeDisposable by lazy { CompositeDisposable() }
    private var syncDataRequest: OneTimeWorkRequest? = null
    @Inject
    lateinit var networkRepo: NetworkRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        tabClickListener()
        listenMainActivityEvents()
        syncDataRequest = OneTimeWorkRequestBuilder<DailyImageUpdateWork>().build()
        syncDataRequest?.let {
            WorkManager.getInstance(this).enqueue(it)
        }
        sendNotification()
    }

    private fun listenMainActivityEvents() {
        compositeDisposable.add(
            EventBus
                .listen(MainActivityClickEvents::class.java)
                .subscribe(::clickEvent)
        )
    }

    private fun sendNotification() {
        syncDataRequest?.id?.let {
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(it)
                .observe(this, Observer { workInfo ->
                    when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            showLog("success and data is ${workInfo.outputData.getString("image")}")
                        }
                        else -> {
                            showLog(
                                "Work state is ${workInfo.state} and msg is ${workInfo.outputData.getString(
                                    "error"
                                )}"
                            )
                        }
                    }
                })
        }
    }

    private fun clickEvent(event: MainActivityClickEvents) {
        when (event) {
            is MainActivityClickEvents.UpdateBadge -> {
                val badge = binding.tabs?.getTabAt(0)?.orCreateBadge
                badge?.isVisible = true
            }
        }
    }

    private fun tabClickListener() {
        binding.tabs?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                EventBus.publish(PODFragmentClickEvents.OnTabClicked(tab?.parent))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                EventBus.publish(PODFragmentClickEvents.OnTabClicked(tab?.parent))
            }

        })
    }

    private fun getTabTitle(position: Int) = when (position) {
        POD_FRAGMENT_INDEX -> "POD"
        MARS_FRAGMENT_INDEX -> "MARS"
        else -> ""
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
    }
}
