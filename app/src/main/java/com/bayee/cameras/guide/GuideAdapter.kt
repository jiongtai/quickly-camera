package com.bayee.cameras.guide

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bayee.cameras.adapter.BaseFragmentPagerAdapter

data class GuidePage(val text1: String, val text2: String, val text3 : String)


class GuideAdapter(context: Context, fragmentManager: FragmentManager) :
    BaseFragmentPagerAdapter<Int>(context, fragmentManager) {

        var textData: List<GuidePage>? = null
        var datumWATER: MutableList<Int>? = null

    /**
     * 获取当前位置的数据
     *
     * @param position
     * @return
     */
    override fun getItem(position: Int): Fragment {
        val guidePage = textData?.get(position) ?: return GuideFragment.newInstance(getData(position), textData!![position], datumWATER!![position])

        return GuideFragment.newInstance(getData(position), guidePage, datumWATER!![position])
    }


    fun setTextDatum(textData: List<GuidePage>) {
        this.textData = textData;
    }

    fun setWater(datumWater: MutableList<Int>) {
        this.datumWATER = datumWater
//        Log.d("测试容量", "setWater: ${datumWATER!!.size}")
    }

}








