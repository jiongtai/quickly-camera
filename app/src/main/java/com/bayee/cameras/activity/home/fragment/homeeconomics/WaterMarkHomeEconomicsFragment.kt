package com.bayee.cameras.activity.home.fragment.homeeconomics

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.bayee.cameras.databinding.FragmentWaterMarkHomeBinding
import com.bayee.cameras.fragment.BaseViewModelFragment
import com.bayee.cameras.activity.home.recycleviewmodel.WaterMarkAdapter
import com.bayee.cameras.util.DataUtil

class WaterMarkHomeEconomicsFragment : BaseViewModelFragment<FragmentWaterMarkHomeBinding>() {

    override fun initDatum() {
        super.initDatum()
        initWaterMark()
    }

    private fun initWaterMark() {
        initBaseWaterMark()
    }

    private fun initBaseWaterMark() {
        val BaseDatas = DataUtil.HomeServiceWaterMark
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = WaterMarkAdapter(BaseDatas, requireContext())
    }

    companion object{
        fun newInstance(): WaterMarkHomeEconomicsFragment {
            val args = Bundle()
            val fragment = WaterMarkHomeEconomicsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}