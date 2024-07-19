package com.bayee.cameras.activity.home.fragment.all

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.bayee.cameras.activity.home.recycleviewmodel.WaterMarkAdapter
import com.bayee.cameras.databinding.FragmentWaterMarkAllBinding
import com.bayee.cameras.fragment.BaseViewModelFragment
import com.bayee.cameras.util.DataUtil

class WaterMarkAllFragment : BaseViewModelFragment<FragmentWaterMarkAllBinding>() {

    override fun initDatum() {
        super.initDatum()
        initWaterMark()
    }

    private fun initWaterMark() {
        initBaseWaterMark()
    }

    override fun onResume() {
        super.onResume()
        Log.d("onResume", "onResume: 123123123")
    }


    private fun initBaseWaterMark() {
        val BaseDatas = DataUtil.AllWaterMark
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = WaterMarkAdapter(BaseDatas, requireContext())
    }

    companion object {
        fun newInstance(): WaterMarkAllFragment {
            val args = Bundle()
            val fragment = WaterMarkAllFragment()
            fragment.arguments = args
            return fragment
        }
    }
}