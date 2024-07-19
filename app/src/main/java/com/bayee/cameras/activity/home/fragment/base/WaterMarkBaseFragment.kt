package com.bayee.cameras.activity.home.fragment.base

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.bayee.cameras.databinding.FragmentWaterMarkBaseBinding
import com.bayee.cameras.fragment.BaseViewModelFragment
import com.bayee.cameras.activity.home.recycleviewmodel.WaterMarkAdapter
import com.bayee.cameras.util.DataUtil

class WaterMarkBaseFragment : BaseViewModelFragment<FragmentWaterMarkBaseBinding>() {

    override fun initDatum() {
        super.initDatum()
        initWaterMark()
    }

    private fun initWaterMark() {
        initBaseWaterMark()
    }

    private fun initBaseWaterMark() {
        val BaseDatas = DataUtil.BaseWaterMark
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = WaterMarkAdapter(BaseDatas, requireContext())
    }

    companion object {
        fun newInstance(): WaterMarkBaseFragment {
            val args = Bundle()
            val fragment = WaterMarkBaseFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

