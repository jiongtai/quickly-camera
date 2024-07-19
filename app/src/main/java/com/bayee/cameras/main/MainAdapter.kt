package com.bayee.cameras.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bayee.cameras.activity.home.HomeFragment
import com.bayee.cameras.me.MeFragment
import com.bayee.cameras.photograph.PhotoGraphFragment
import com.bayee.cameras.record.RecordFragment

/**
 * 首页界面adapter
 */
class MainAdapter(fragmentActivity: FragmentActivity, private val count: Int) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> PhotoGraphFragment.newInstance()
            2 -> RecordFragment.newInstance()
            3 -> MeFragment.newInstance()
            else -> HomeFragment.newInstance()
        }
    }
}