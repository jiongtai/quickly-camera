package com.bayee.cameras.adapter

import androidx.viewpager2.widget.ViewPager2
import com.bayee.cameras.util.DataUtil
import com.google.android.material.tabs.TabLayout

class TabLayoutViewPager2Mediator(
    private val indicator: TabLayout,
    private val pager: ViewPager2,
    private val config: ((indicator: TabLayout, pager: ViewPager2) -> Unit)? = null
) {
    init {
        val adapterCount: Int = DataUtil.categories.size
        for (i in 0 until adapterCount) {
            indicator.addTab(indicator.newTab().setText(DataUtil.categories.get(i).title), false)
        }

        indicator.selectTab(indicator.getTabAt(0))
    }

    /**
     * 关联BottomNavigationView和ViewPager2的选择关系
     */
    fun attach() {
        config?.invoke(indicator, pager)
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicator.selectTab(indicator.getTabAt(position))
            }
        })

        indicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val index = tab.position
                if (pager.currentItem != index) {
                    pager.setCurrentItem(index, false)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }

        })
    }
}