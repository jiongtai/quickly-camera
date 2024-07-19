package com.bayee.cameras.record

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bayee.cameras.record.fragment.photos.PhotosListFragment
import com.bayee.cameras.record.fragment.videos.VideosListFragment

class RecordFragmentAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        return if (position == 0){
            PhotosListFragment.newInstance()
        }else {
            VideosListFragment.newInstance()
        }
    }

    override fun getItemCount(): Int {
        // 返回页面的数量
        return 2
    }
}