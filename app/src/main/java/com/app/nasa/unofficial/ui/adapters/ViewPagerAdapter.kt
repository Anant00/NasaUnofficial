package com.app.nasa.unofficial.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.nasa.unofficial.ui.fragments.MarsFragment
import com.app.nasa.unofficial.ui.fragments.PictureOfTheDayFragment

const val POD_FRAGMENT_INDEX = 0
const val MARS_FRAGMENT_INDEX = 1

open class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        POD_FRAGMENT_INDEX to { PictureOfTheDayFragment() },
        MARS_FRAGMENT_INDEX to { MarsFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}
