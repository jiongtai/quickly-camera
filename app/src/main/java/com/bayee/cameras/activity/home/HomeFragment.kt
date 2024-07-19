package com.bayee.cameras.activity.home

import android.content.Intent
import android.os.Bundle
import com.bayee.cameras.R
import com.bayee.cameras.adapter.TabLayoutViewPager2Mediator
import com.bayee.cameras.databinding.HomeFragment2Binding
import com.bayee.cameras.databinding.HomeFragmentBinding
import com.bayee.cameras.fragment.BaseViewModelFragment
import com.bayee.cameras.activity.home.banner.BannerAdapter
import com.bayee.cameras.activity.home.waterall.WaterAllActivity
import com.bayee.cameras.activity.home.watermark.WaterMarkAdapter
import com.bayee.cameras.activity.photographActivity.CameraActivity
import com.bayee.cameras.activity.watermontage.WaterMontageActivity
import com.bayee.cameras.util.Constant.WATER_TYPE
import com.bayee.cameras.util.Constant.WATER_TYPE_1_1
import com.bayee.cameras.util.DataUtil

class HomeFragment : BaseViewModelFragment<HomeFragment2Binding>() {

    private lateinit var adapter: BannerAdapter

    override fun initDatum() {
        super.initDatum()
        initBanner()
        initWaterMARK()
    }

    override fun initListeners() {
        super.initListeners()
        initClickListener()
    }

    private fun initClickListener() {
        //图片水印
        binding.homeCons1.setOnClickListener {
            val intent = Intent(requireContext(), CameraActivity::class.java)
            intent.putExtra(WATER_TYPE, WATER_TYPE_1_1)
            startActivity(intent)
        }
        //水印拼接
        binding.homeCons3.setOnClickListener {
            startActivity(Intent(requireContext(), WaterMontageActivity::class.java))
        }
        //水印大全
        binding.homeCons4.setOnClickListener {
            startActivity(Intent(requireContext(), WaterAllActivity::class.java))
        }
    }

    //底部水印
    private fun initWaterMARK() {
        binding.apply {
            pager.adapter = WaterMarkAdapter(requireActivity(), DataUtil.categories)
            val offscreenPageLimit = DataUtil.categories.size - 1
            binding.pager.offscreenPageLimit = offscreenPageLimit
            TabLayoutViewPager2Mediator(indicator1, pager) { indicator, pager ->
            }.attach()
        }
    }

    //轮播图
    private fun initBanner() {
        //创建适配器
        adapter = BannerAdapter(requireActivity(), childFragmentManager)
        //设置适配器到控件
        binding.list.adapter = adapter
        //让指示器根据列表控件配合工作
        binding.indicator.setViewPager(binding.list)
        //适配器注册数据源观察者
        adapter.registerDataSetObserver(binding.indicator.dataSetObserver)
        //准备数据
        val datum: MutableList<Int> = ArrayList()
        datum.add(R.drawable.home_banner1)
        datum.add(R.drawable.home_banner2)
        datum.add(R.drawable.home_banner3)
        //设置数据到适配器
        adapter.setDatum(datum)
    }


    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()

            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}