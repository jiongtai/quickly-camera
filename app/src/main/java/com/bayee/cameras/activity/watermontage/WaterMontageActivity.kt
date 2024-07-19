package com.bayee.cameras.activity.watermontage

import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bayee.cameras.R
import com.bayee.cameras.activity.base.BaseTitleActivity
import com.bayee.cameras.activity.photographActivity.CameraViewModel
import com.bayee.cameras.activity.watermontage.enter.WaterMontageEnterActivity
import com.bayee.cameras.databinding.ActivityWaterMontageBinding
import com.bayee.cameras.photo.bean.Photo
import com.bayee.cameras.photo.database.SQLDatabase
import com.bayee.cameras.record.RecordSelectedListener
import com.bayee.cameras.record.fragment.photos.PhotoAdapter
import com.bayee.cameras.util.ToastUtil

class WaterMontageActivity : BaseTitleActivity<ActivityWaterMontageBinding>(), RecordSelectedListener {

    private lateinit var viewModel: WaterMontageModel
    private lateinit var db: SQLDatabase
    private lateinit var adapter: WaterMontageAdapter

    //选择的图片列表
    private var selectedPhotos = mutableListOf<Photo>()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun initDatum() {
        super.initDatum()
        initViewModel()
        initInterface()
    }



    override fun initListeners() {
        super.initListeners()
        binding.waterMontageBtnSelected.setOnClickListener {
            if (selectedPhotos.size == 0){
                ToastUtil.show(this,"请选择1-9张图片",300)
            }else{
                val intent = Intent(this, WaterMontageEnterActivity::class.java)
                intent.putParcelableArrayListExtra("photos", ArrayList(selectedPhotos))
                startActivity(intent)
            }
        }
    }

    private fun initInterface() {
        selectedChangedListener = this
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun initViewModel() {
        db = SQLDatabase.getDatabase(this)
        val viewModelFactory =
            WaterMontageModelFactory(db)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(WaterMontageModel::class.java)
        initViewModel(viewModel)
        initLiveDataListener()
    }

    private fun initLiveDataListener() {
        viewModel.allPhotos.observe(this) {
            if (it.isNotEmpty()) {
                Log.d(TAG, "initLiveDataListener123123:${it[0].path} ")
                val layoutManager = GridLayoutManager(this, 3)
                binding.recycle.layoutManager = layoutManager
                val spacing = 10
                val itemDecoration = object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                        outRect.left = spacing
                        outRect.right = spacing
                        outRect.top = spacing
                        outRect.bottom = if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) spacing else 0
                    }
                }
                binding.recycle.addItemDecoration(itemDecoration)
                adapter = WaterMontageAdapter(it)
                binding.recycle.adapter = adapter
            } else {
                Log.d(TAG, "initLiveDataListener123123: ")
            }
        }
    }

    companion object {
        const val TAG = "WaterMontageActivity"
        var selectedChangedListener: RecordSelectedListener? = null
    }

    override fun onSelectedChanged(data: List<Photo>) {
        if (data.isEmpty()){
            binding.waterMontageBtnSelected.text = "请选择"
        }else{
            selectedPhotos = data.toMutableList()
            binding.waterMontageBtnSelected.text = "完成(" + data.size + ")"
        }
    }

}

























