package com.bayee.cameras.activity.home.fragment.logistics

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.bayee.cameras.databinding.FragmentWaterMarkLogisticsBinding
import com.bayee.cameras.fragment.BaseViewModelFragment
import com.bayee.cameras.activity.home.recycleviewmodel.WaterMarkAdapter
import com.bayee.cameras.util.DataUtil

class WaterMarkLogisticsFragment : BaseViewModelFragment<FragmentWaterMarkLogisticsBinding>() {

    override fun initDatum() {
        super.initDatum()
        initWaterMark()
    }

    private fun initWaterMark() {
        initBaseWaterMark()
    }

    private fun initBaseWaterMark() {
        val BaseDatas = DataUtil.LogisticsWaterMark
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = WaterMarkAdapter(BaseDatas, requireContext())
    }

    companion object{
        fun newInstance(): WaterMarkLogisticsFragment {
            val args = Bundle()
            val fragment = WaterMarkLogisticsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}