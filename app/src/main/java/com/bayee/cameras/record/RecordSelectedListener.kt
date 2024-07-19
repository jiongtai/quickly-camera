package com.bayee.cameras.record

import com.bayee.cameras.photo.bean.Photo

interface RecordSelectedListener {
    fun onSelectedChanged(data: List<Photo>)
}