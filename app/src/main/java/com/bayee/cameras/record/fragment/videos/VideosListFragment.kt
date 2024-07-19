package com.bayee.cameras.record.fragment.videos

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.VideoView
import androidx.annotation.RequiresExtension
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bayee.cameras.R
import com.bayee.cameras.activity.photographActivity.CameraViewModel
import com.bayee.cameras.activity.photographActivity.CameraViewModelFactory
import com.bayee.cameras.databinding.RecodeFragmentVideoBinding
import com.bayee.cameras.fragment.BaseViewModelFragment
import com.bayee.cameras.photo.database.SQLDatabase
import com.bayee.cameras.record.fragment.photos.PhotoAdapter
import com.bayee.cameras.record.fragment.photos.PhotosEditCallback
import com.bayee.cameras.record.fragment.photos.PhotosListFragment
import com.bayee.cameras.record.fragment.photos.PhotosListFragment.Companion
import java.io.File

class VideosListFragment : BaseViewModelFragment<RecodeFragmentVideoBinding>() , PhotosEditCallback {


    private lateinit var viewModel: CameraViewModel
    private lateinit var db: SQLDatabase

    private  var adapter: VideoAdapter? = null


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun initDatum() {
        super.initDatum()
//        val videoView: VideoView = findViewById(R.id.videoView)
//        val filePath =
//            "/storage/emulated/0/Android/data/com.bayee.cameras/files/Pictures/1721108470746.mp4"
//
//        val file = File(filePath)
//        if (file.exists()) {
//            val uri =
//                FileProvider.getUriForFile(requireContext(), "com.bayee.cameras.fileprovider", file)
//            videoView.setVideoURI(uri)
//            videoView.start()
//        } else {
//            Log.e("VideoPlay", "File does not exist at $filePath")
//        }

        initViewModel()
        initInterface()
        viewModel.fetchVideosFromDatabase()
    }

    private fun initInterface() {
        callbackInstance = this
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun initViewModel() {
        db = SQLDatabase.getDatabase(requireActivity())
        val viewModelFactory =
            CameraViewModelFactory(db)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CameraViewModel::class.java)
        initViewModel(viewModel)
        initLiveDataListener()
    }

    private fun initLiveDataListener() {
        viewModel.allVideos.observe(viewLifecycleOwner) {
            if (it.isEmpty()){
                binding.recordVideoNodata.visibility = View.VISIBLE
                binding.recordVideoHavedata.visibility = View.GONE
            }else{
                binding.recordVideoNodata.visibility = View.GONE
                binding.recordVideoHavedata.visibility = View.VISIBLE
                it.forEach { name ->
                    Log.d("第${name.id}路径：", "initLiveDataListener: ${name.path}")
                }
                binding.recycle.layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = VideoAdapter(it)
                binding.recycle.adapter = adapter
            }
        }
    }


    companion object {
        var callbackInstance: PhotosEditCallback? = null

        fun newInstance(): VideosListFragment {
            val args = Bundle()

            val fragment = VideosListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onEditStateChanged(data: Boolean) {
        Log.d("接口测试1", "onDataReceived: $data")
        adapter?.setEditMode(data)
    }

}