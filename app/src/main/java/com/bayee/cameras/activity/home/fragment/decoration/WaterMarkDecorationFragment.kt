package com.bayee.cameras.activity.home.fragment.decoration

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.bayee.cameras.databinding.FragmentWaterMarkDecorationBinding
import com.bayee.cameras.fragment.BaseViewModelFragment
import com.bayee.cameras.activity.home.recycleviewmodel.WaterMarkAdapter
import com.bayee.cameras.util.DataUtil

class WaterMarkDecorationFragment : BaseViewModelFragment<FragmentWaterMarkDecorationBinding>() {

    override fun initDatum() {
        super.initDatum()
        initWaterMark()
    }

    private fun initWaterMark() {
        initBaseWaterMark()
    }

    private fun initBaseWaterMark() {
        val BaseDatas = DataUtil.DecorationWaterMark
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = WaterMarkAdapter(BaseDatas, requireContext())
    }


    companion object{
        fun newInstance(): WaterMarkDecorationFragment {
            val args = Bundle()
            val fragment = WaterMarkDecorationFragment()
            fragment.arguments = args
            return fragment
        }
    }
}