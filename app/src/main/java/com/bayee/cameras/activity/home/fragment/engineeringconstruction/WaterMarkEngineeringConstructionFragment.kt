package com.bayee.cameras.activity.home.fragment.engineeringconstruction

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.bayee.cameras.databinding.FragmentWaterMarkEngineerBinding
import com.bayee.cameras.fragment.BaseViewModelFragment
import com.bayee.cameras.activity.home.recycleviewmodel.WaterMarkAdapter
import com.bayee.cameras.util.DataUtil

class WaterMarkEngineeringConstructionFragment : BaseViewModelFragment<FragmentWaterMarkEngineerBinding>() {


    override fun initDatum() {
        super.initDatum()
        initWaterMark()
    }

    private fun initWaterMark() {
        initBaseWaterMark()
    }

    private fun initBaseWaterMark() {
        val BaseDatas = DataUtil.ConstructionWaterMark
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = WaterMarkAdapter(BaseDatas, requireContext())
    }


    companion object{
        fun newInstance(): WaterMarkEngineeringConstructionFragment {
            val args = Bundle()
            val fragment = WaterMarkEngineeringConstructionFragment()
            fragment.arguments = args
            return fragment
        }
    }
}