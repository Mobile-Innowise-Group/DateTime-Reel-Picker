package com.innowisegroup.reelpicker.picker.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

internal class PagerAdapter(
    fragmentActivity: FragmentActivity,
    private val list: List<Fragment>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment = list[position]

    override fun getItemViewType(position: Int): Int =
        when (list[position]) {
            is TimePickerFragment -> 0
            else -> 1
        }
}