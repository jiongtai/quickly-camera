package com.bayee.cameras.activity.home.banner

import android.os.Bundle
import com.bayee.cameras.databinding.FragmentBannerBinding
import com.bayee.cameras.fragment.BaseViewModelFragment
import com.bayee.cameras.util.Constant

class BannerFragment : BaseViewModelFragment<FragmentBannerBinding>() {
    override fun initDatum() {
        super.initDatum()
        val data = requireArguments().getInt(Constant.ID)
        binding.icon.setImageResource(data)
    }

    companion object {
        /**
         * 创建方法
         */
        fun newInstance(data: Int): BannerFragment {
            val args = Bundle()
            args.putInt(Constant.ID, data)

            val fragment = BannerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}