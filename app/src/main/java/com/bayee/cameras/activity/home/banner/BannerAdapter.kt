package com.bayee.cameras.activity.home.banner

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bayee.cameras.adapter.BaseFragmentPagerAdapter

class BannerAdapter(context: Context, fragmentManager: FragmentManager) :
    BaseFragmentPagerAdapter<Int>(context, fragmentManager) {

    /**
     * 获取当前位置的数据
     *
     * @param position
     * @return
     */
    override fun getItem(position: Int): Fragment {
        return BannerFragment.newInstance(getData(position))
    }

}