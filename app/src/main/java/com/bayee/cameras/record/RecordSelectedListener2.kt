package com.bayee.cameras.record

import com.bayee.cameras.photo.bean.Photo
import com.bayee.cameras.photo.bean.Video

interface RecordSelectedListener2 {
    fun onSelectedChanged2(data: List<Video>)
}