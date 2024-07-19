package com.bayee.cameras.activity.videodetail

import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.util.Log
import com.bayee.cameras.activity.base.BaseTitleActivity
import com.bayee.cameras.databinding.ActivityVideoDetailBinding


class VideoDetailActivity : BaseTitleActivity<ActivityVideoDetailBinding>() {


    override fun initDatum() {
        super.initDatum()
        val videoPath = intent.getStringExtra("VIDEO_PATH")
        Log.d(TAG, "initDatum: $videoPath")
        binding.videoDetailVv.setVideoPath(videoPath)
        binding.videoDetailVv.start()

        val thumb =
            ThumbnailUtils.createVideoThumbnail(videoPath!!, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND)
        binding.videoDetailIv.setImageBitmap(thumb)
    }


    companion object{
        private const val TAG = "VideoDetailActivity"
    }

}