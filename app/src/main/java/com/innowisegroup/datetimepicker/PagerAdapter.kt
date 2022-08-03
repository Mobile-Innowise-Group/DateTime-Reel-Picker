package com.innowisegroup.datetimepicker

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

open class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val mFragmentList: MutableList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment = mFragmentList[position]

    override fun getCount(): Int = mFragmentList.size

    fun addFragment(fragment: Fragment) = mFragmentList.add(fragment)
}