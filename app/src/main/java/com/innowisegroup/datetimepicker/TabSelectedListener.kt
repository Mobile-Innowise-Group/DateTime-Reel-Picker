package com.innowisegroup.datetimepicker

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

interface TabSelectedListener : OnTabSelectedListener {

    override fun onTabSelected(tab: TabLayout.Tab) {}

    override fun onTabUnselected(tab: TabLayout.Tab) {}

    override fun onTabReselected(tab: TabLayout.Tab) {}
}