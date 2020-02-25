package com.app.nasa.unofficial.events

import com.google.android.material.tabs.TabLayout

sealed class PODFragmentClickEvents {
    data class OnTabClicked(val badge: TabLayout?) : PODFragmentClickEvents()
    data class OnRecyclerViewItemClick(val position: Int) : PODFragmentClickEvents()
}