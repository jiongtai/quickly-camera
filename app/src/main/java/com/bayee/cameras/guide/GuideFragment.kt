package com.bayee.cameras.guide

import android.os.Bundle
import android.view.View
import com.bayee.cameras.util.Constant
import com.bayee.cameras.databinding.FragmentGuideBinding
import com.bayee.cameras.fragment.BaseViewModelFragment


/**
 * 引导界面Fragment
 */
class GuideFragment : BaseViewModelFragment<FragmentGuideBinding>() {

    private var guidePage: GuidePage? = null

    override fun initDatum() {
        super.initDatum()
        val data = requireArguments().getInt(Constant.ID)
        val waterData = requireArguments().getInt(Constant.WATER_ID)
        binding.icon.setImageResource(data)
        binding.iconWater.setImageResource(waterData)

        binding.textView1.text = guidePage?.text1
        binding.textView2.text = guidePage?.text2
        binding.textView3.text = guidePage?.text3

        if (binding.textView1.text == "工作"){
            binding.loadingBtnSkip.visibility = View.VISIBLE
        }
    }

    companion object {
        /**
         * 创建方法
         */
        fun newInstance(data: Int, guidePage: GuidePage, waterData: Int): GuideFragment {
            val args = Bundle()
            args.putInt(Constant.ID, data)
            args.putInt(Constant.WATER_ID, waterData)
            val fragment = GuideFragment()
            fragment.arguments = args
            fragment.guidePage = guidePage
            return fragment
        }
    }
}