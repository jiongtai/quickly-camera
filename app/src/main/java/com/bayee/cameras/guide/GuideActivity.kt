package com.bayee.cameras.guide

import com.bayee.cameras.R
import com.bayee.cameras.activity.base.BaseViewModelActivity
import com.bayee.cameras.databinding.ActivityGuideBinding
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

class GuideActivity : BaseViewModelActivity<ActivityGuideBinding>() {

    private lateinit var adapter: GuideAdapter

    override fun initDatum() {
        super.initDatum()
        QMUIStatusBarHelper.translucent(this)
        adapter = GuideAdapter(this, supportFragmentManager)

        //设置适配器到控件
        binding.list.adapter = adapter

        //让指示器根据列表控件配合工作
//        binding.indicator.setViewPager(binding.list)

        //适配器注册数据源观察者
//        adapter.registerDataSetObserver(binding.indicator.dataSetObserver)

        //准备图片数据
//        val datum: MutableList<Int> = ArrayList()
//        datum.add(R.drawable.loading1_head,)
//        datum.add(R.drawable.loading2_head)
//        datum.add(R.drawable.loading3_head)
        val datum: MutableList<Int> = ArrayList()
        datum.add(R.drawable.loading1_background,)
        datum.add(R.drawable.loading2_background)
        datum.add(R.drawable.loading3_background)

        val datumWater: MutableList<Int> = ArrayList()
        datumWater.add(R.drawable.loading2_water,)
        datumWater.add(R.drawable.loading2_water)
        datumWater.add(R.drawable.loading3_water)

        //文字数据
        val textData: List<GuidePage> = listOf<GuidePage>(
            GuidePage("时间","地点","时间水印 一键修改"),
            GuidePage("工程","记录","日常打卡 拍照记录"),
            GuidePage("工作","水印","定位考情 工作可用"),
        )



        //设置数据到适配器
        adapter.setDatum(datum)
        adapter.setTextDatum(textData)
        adapter.setWater(datumWater)
    }


}







