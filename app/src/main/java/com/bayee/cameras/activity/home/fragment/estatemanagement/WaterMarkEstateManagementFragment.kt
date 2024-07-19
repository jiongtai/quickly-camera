package com.bayee.cameras.activity.home.fragment.estatemanagement

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.bayee.cameras.databinding.FragmentWaterMarkEstateBinding
import com.bayee.cameras.fragment.BaseViewModelFragment
import com.bayee.cameras.activity.home.recycleviewmodel.WaterMarkAdapter
import com.bayee.cameras.util.DataUtil

class WaterMarkEstateManagementFragment : BaseViewModelFragment<FragmentWaterMarkEstateBinding>() {


    override fun initDatum() {
        super.initDatum()
        initWaterMark()
    }

    private fun initWaterMark() {
        initBaseWaterMark()
    }

    private fun initBaseWaterMark() {
        val BaseDatas = DataUtil.EstateManagerWaterMark
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = WaterMarkAdapter(BaseDatas, requireContext())
    }

    companion object{
        fun newInstance(): WaterMarkEstateManagementFragment {
            val args = Bundle()
            val fragment = WaterMarkEstateManagementFragment()
            fragment.arguments = args
            return fragment
        }
    }
}