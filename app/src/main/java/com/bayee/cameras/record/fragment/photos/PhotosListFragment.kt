package com.bayee.cameras.record.fragment.photos

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bayee.cameras.activity.photographActivity.CameraActivity
import com.bayee.cameras.activity.photographActivity.CameraViewModel
import com.bayee.cameras.activity.photographActivity.CameraViewModelFactory
import com.bayee.cameras.databinding.RecodeFragmentPhotosBinding
import com.bayee.cameras.fragment.BaseViewModelFragment
import com.bayee.cameras.photo.database.SQLDatabase
import com.bayee.cameras.record.RecordFragment
import com.bayee.cameras.util.Constant.WATER_TYPE
import com.bayee.cameras.util.Constant.WATER_TYPE_1_1

class PhotosListFragment : BaseViewModelFragment<RecodeFragmentPhotosBinding>(), PhotosEditCallback {

    private lateinit var viewModel: CameraViewModel
    private lateinit var db: SQLDatabase

    private  var adapter: PhotoAdapter? = null

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun initDatum() {
        super.initDatum()
        initViewModel()
        initInterface()
    }

    private fun initInterface() {
        callbackInstance = this
    }

    override fun initListeners() {
        super.initListeners()
        initClickListener()
    }

    private fun initClickListener() {
        binding.recordPhotoNodata.setOnClickListener {
            val intent = Intent(requireContext(), CameraActivity::class.java)
            intent.putExtra(WATER_TYPE, WATER_TYPE_1_1)
            startActivity(intent)
        }
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
        viewModel.allPhotos.observe(viewLifecycleOwner) {
            if (it.isEmpty()){
                binding.recordPhotoNodata.visibility = View.VISIBLE
                binding.recordPhotoHavedata.visibility = View.GONE
            }else{
                binding.recordPhotoNodata.visibility = View.GONE
                binding.recordPhotoHavedata.visibility = View.VISIBLE
                it.forEach { name ->
                    Log.d("第${name.id}路径：", "initLiveDataListener: ${name.path}")
                }
                binding.recycle.layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = PhotoAdapter(it)
                binding.recycle.adapter = adapter
            }
        }
    }


    companion object {
        var callbackInstance: PhotosEditCallback? = null

        fun newInstance(): PhotosListFragment {
            val args = Bundle()

            val fragment = PhotosListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onEditStateChanged(data: Boolean) {
        Log.d("接口测试1", "onDataReceived: $data")
        adapter?.setEditMode(data)
    }


}