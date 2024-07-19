package com.bayee.cameras.record
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bayee.cameras.R
import com.bayee.cameras.databinding.RecordFragmentBinding
import com.bayee.cameras.dialog.DeleteDialogFragment
import com.bayee.cameras.fragment.BaseViewModelFragment
import com.bayee.cameras.photo.bean.Photo
import com.bayee.cameras.photo.bean.Video
import com.bayee.cameras.photo.database.SQLDatabase
import com.bayee.cameras.record.fragment.photos.PhotosListFragment
import com.bayee.cameras.record.fragment.videos.VideosListFragment
class RecordFragment : BaseViewModelFragment<RecordFragmentBinding>(), RecordSelectedListener,RecordSelectedListener2, RecordDeleteListener{

    private lateinit var viewModel: RecordViewModel

    private lateinit var db: SQLDatabase

    private var datas: MutableList<Photo>? = mutableListOf()
    private var datas2: MutableList<Video>? = mutableListOf()

    private var isEdit = false

    override fun initViews() {
        super.initViews()
        initWaterMark()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun initDatum() {
        super.initDatum()
        initViewModel()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun initViewModel() {
        db = SQLDatabase.getDatabase(requireActivity())
        val viewModelFactory =
            RecordViewModelFactory(db)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(RecordViewModel::class.java)
        initViewModel(viewModel)

        initLiveDataListener()
    }

    private fun initLiveDataListener() {
        // 在Fragment或Activity中
        viewModel.deletionResult.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) {
                // 更新UI，显示删除成功的信息或执行其他操作
                deleteSuccessUpdateUi()
                Toast.makeText(requireContext(), "照片删除成功", Toast.LENGTH_SHORT).show()
            } else {
                // 显示删除失败的信息
                Toast.makeText(requireContext(), "照片删除失败", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteSuccessUpdateUi() {
        isEdit = false
        binding.recordLineIsEdit.visibility = if (isEdit) View.VISIBLE else View.GONE
        binding.recordTvSelected.text = getString(R.string.record_selected) + 0
    }

    override fun initListeners() {
        super.initListeners()
        clickListener()
        initInterface()
    }

    private fun initInterface() {
        selectedChangedListener = this
        selectedChangedListener2 = this
        deleteListener = this
    }

    private fun clickListener() {
        binding.recordIvEditChange.setOnClickListener {
            isEdit = !isEdit
            updateIsEdit()
            PhotosListFragment.callbackInstance?.onEditStateChanged(isEdit)
            VideosListFragment.callbackInstance?.onEditStateChanged(isEdit)
        }
        binding.recordLineIsEdit.setOnClickListener {
            DeleteDialogFragment.show(requireActivity().supportFragmentManager)
        }
    }

    private fun initWaterMark() {
        binding.viewPager.adapter = RecordFragmentAdapter(requireActivity())
        binding.viewPager.isUserInputEnabled = false
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_left -> binding.viewPager.currentItem = 0
                R.id.btn_right -> binding.viewPager.currentItem = 1
            }
        }
    }

    private fun updateIsEdit() {
        if (isEdit) {
            binding.recordLineIsEdit.visibility = View.VISIBLE
        } else {
            binding.recordLineIsEdit.visibility = View.GONE
        }
    }


    companion object {
        var selectedChangedListener: RecordSelectedListener? = null
        var selectedChangedListener2: RecordSelectedListener2? = null
        var deleteListener: RecordDeleteListener? = null


        fun newInstance(): RecordFragment {
            val args = Bundle()

            val fragment = RecordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onSelectedChanged(data: List<Photo>) {
        Log.d("接口测试1", "onSelectedChanged: ${data.size}")
        updateUI(data)
    }

    private fun updateUI(data: List<Photo>) {
        datas = data as MutableList<Photo>
        binding.recordTvSelected.text = getString(R.string.record_selected) + data.size
        if (data.isNotEmpty()){
            val color = ContextCompat.getColor(requireContext(), R.color.primary)
            binding.recordLineIsEdit.setBackgroundColor(color)
        }else{
            val color = ContextCompat.getColor(requireContext(), R.color.record_line_isEdit)
            binding.recordLineIsEdit.setBackgroundColor(color)
        }
    }

    private fun updateUI2(data: List<Video>) {
        datas2 = data as MutableList<Video>
        binding.recordTvSelected.text = getString(R.string.record_selected) + data.size
        if (data.isNotEmpty()){
            val color = ContextCompat.getColor(requireContext(), R.color.primary)
            binding.recordLineIsEdit.setBackgroundColor(color)
        }else{
            val color = ContextCompat.getColor(requireContext(), R.color.record_line_isEdit)
            binding.recordLineIsEdit.setBackgroundColor(color)
        }
    }

    private fun deleteSelectedPhotos() {
        if (datas!!.isNotEmpty()){
            viewModel.deletePhotos(datas!!)
            return
        }
        if (datas2!!.isNotEmpty()){
            viewModel.deleteVideos(datas2!!)
            return
        }
    }

    override fun agreeDelete() {
        Log.d("接口测试", "agreeDelete: 同意删除回调")
        deleteSelectedPhotos()
    }

    override fun disAgreeDelete() {
        Log.d("接口测试", "agreeDelete: 取消回调")
    }

    override fun onSelectedChanged2(data: List<Video>) {
        Log.d("接口测试1", "onSelectedChanged: ${data.size}")
        updateUI2(data)
    }


}












