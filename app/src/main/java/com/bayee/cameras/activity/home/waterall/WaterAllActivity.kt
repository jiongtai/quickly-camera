package com.bayee.cameras.activity.home.waterall

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bayee.cameras.activity.base.BaseViewModelActivity
import com.bayee.cameras.activity.home.watermark.WaterMarkAdapter
import com.bayee.cameras.adapter.TabLayoutViewPager2Mediator
import com.bayee.cameras.databinding.ActivityWaterAllBinding
import com.bayee.cameras.util.DataUtil

class WaterAllActivity : BaseViewModelActivity<ActivityWaterAllBinding>() {


    override fun initDatum() {
        super.initDatum()
        initWaterMARK()
        // 设置TabLayout的监听器

    }

    //水印
    private fun initWaterMARK() {
        binding.apply {
            pager.isUserInputEnabled = false
            pager.adapter = WaterMarkAdapter(this@WaterAllActivity , DataUtil.categories)
            val offscreenPageLimit = DataUtil.categories.size - 1
            binding.pager.offscreenPageLimit = offscreenPageLimit
            TabLayoutViewPager2Mediator(indicator1, pager) { indicator, pager ->
            }.attach()
        }


    }

}