package com.bayee.cameras.activity.home.fragment.attendance

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.bayee.cameras.databinding.FragmentWaterMarkAttendanceBinding
import com.bayee.cameras.fragment.BaseViewModelFragment
import com.bayee.cameras.activity.home.recycleviewmodel.WaterMarkAdapter
import com.bayee.cameras.util.DataUtil

class WaterMarkAttendanceFragment : BaseViewModelFragment<FragmentWaterMarkAttendanceBinding>() {

    override fun initDatum() {
        super.initDatum()
        initWaterMark()
    }

    private fun initWaterMark() {
        initBaseWaterMark()
    }

    private fun initBaseWaterMark() {
        val BaseDatas = DataUtil.AttendanceWaterMark
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = WaterMarkAdapter(BaseDatas, requireContext())
    }

    companion object{
        fun newInstance(): WaterMarkAttendanceFragment {
            val args = Bundle()
            val fragment = WaterMarkAttendanceFragment()
            fragment.arguments = args
            return fragment
        }
    }
}