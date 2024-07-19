package com.bayee.cameras.activity.home.watermark

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bayee.cameras.category.Category
import com.bayee.cameras.activity.home.content.ContentFragment
import com.bayee.cameras.activity.home.fragment.all.WaterMarkAllFragment
import com.bayee.cameras.activity.home.fragment.attendance.WaterMarkAttendanceFragment
import com.bayee.cameras.activity.home.fragment.base.WaterMarkBaseFragment
import com.bayee.cameras.activity.home.fragment.decoration.WaterMarkDecorationFragment
import com.bayee.cameras.activity.home.fragment.engineeringconstruction.WaterMarkEngineeringConstructionFragment
import com.bayee.cameras.activity.home.fragment.estatemanagement.WaterMarkEstateManagementFragment
import com.bayee.cameras.activity.home.fragment.homeeconomics.WaterMarkHomeEconomicsFragment
import com.bayee.cameras.activity.home.fragment.logistics.WaterMarkLogisticsFragment

/**
 * 发现界面适配器
 */
class WaterMarkAdapter(fragmentActivity: FragmentActivity, private val datum: List<Category>) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return datum.size
    }

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return WaterMarkAllFragment.newInstance()
        } else if (position == 1) {
            return WaterMarkBaseFragment.newInstance()
        } else if (position == 2) {
            return WaterMarkAttendanceFragment.newInstance()
        } else if (position == 3) {
            return WaterMarkEngineeringConstructionFragment.newInstance()
        } else if (position == 4) {
            return WaterMarkEstateManagementFragment.newInstance()
        } else if (position == 5) {
            return WaterMarkDecorationFragment.newInstance()
        } else if (position == 6) {
            return WaterMarkHomeEconomicsFragment.newInstance()
        } else
            return WaterMarkLogisticsFragment.newInstance()
    }


//    override fun createFragment(position: Int): Fragment {
//        return ContentFragment.newInstance(datum[position].id)
//    }


}







