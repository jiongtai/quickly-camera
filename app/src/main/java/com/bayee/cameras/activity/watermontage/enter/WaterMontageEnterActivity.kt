package com.bayee.cameras.activity.watermontage.enter

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.gridlayout.widget.GridLayout
import com.bayee.cameras.R
import com.bayee.cameras.activity.base.BaseTitleActivity
import com.bayee.cameras.databinding.ActivityWaterMontageEnterBinding
import com.bayee.cameras.photo.bean.Photo
import com.bayee.cameras.util.ToastUtil
import com.bumptech.glide.Glide

class WaterMontageEnterActivity : BaseTitleActivity<ActivityWaterMontageEnterBinding>() {


    //选择的图片列表
    private var selectedPhotos = mutableListOf<Photo>()

    override fun initListeners() {
        super.initListeners()
        //返回键
        binding.waterMontageToolbar.back.setOnClickListener {
            finish()
        }
        //保存分享按键
        binding.waterMontageToolbar.waterMontageEnterButton.setOnClickListener {
            mergeAndSaveImagesToGallery()
        }
    }

    override fun initDatum() {
        super.initDatum()
        val datas = intent.getParcelableArrayListExtra<Photo>("photos")
        if (datas != null) {
            selectedPhotos = datas
            Log.d(TAG, "initDatum: ${datas.size}")
        }
        loadImagesIntoGrid()
    }

    private fun loadImagesIntoGrid() {
        val screenWidthDp = resources.displayMetrics.widthPixels / resources.displayMetrics.density
        selectedPhotos.forEachIndexed { index, photo ->
            if (photo.isSelected) {
                val imageView = ImageView(this)
                // 设置宽度和高度
                imageView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, // 宽度匹配父容器
                    ViewGroup.LayoutParams.WRAP_CONTENT //
                )
                binding.gridLayout.addView(imageView)
                Glide.with(this)
                    .load(photo.path)
                    .override(screenWidthDp.toInt(), 0) // 只指定宽度，高度设为0表示自适应
                    .into(imageView)
            }
        }
    }


    private fun mergeAndSaveImagesToGallery() {
        // 测量GridLayout的大小
        binding.gridLayout.measure(
            View.MeasureSpec.makeMeasureSpec(resources.displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val gridWidth = binding.gridLayout.measuredWidth
        val gridHeight = binding.gridLayout.measuredHeight
        // 创建一个Bitmap，大小等于GridLayout的大小
        val mergedBitmap = Bitmap.createBitmap(gridWidth, gridHeight, Bitmap.Config.ARGB_8888)
        // 创建一个Canvas，用于在Bitmap上绘制
        val canvas = Canvas(mergedBitmap)
        // 绘制GridLayout中的所有ImageView到Bitmap上
        for (i in 0 until binding.gridLayout.childCount) {
            val child = binding.gridLayout.getChildAt(i)
            Log.d(TAG, "mergeAndSaveImagesToGallery: $i")
            if (child is ImageView) {
                // 保存Canvas当前状态
                canvas.save()
                // 将Canvas的坐标原点平移到当前child的左上角
                canvas.translate(child.left.toFloat(), child.top.toFloat())
                // 绘制child到Canvas上
                child.draw(canvas)
                // 恢复Canvas状态
                canvas.restore()
            }
        }
        // 保存Bitmap到相册
        saveBitmapToGallery(mergedBitmap)
    }


    private fun saveBitmapToGallery(bitmap: Bitmap) {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "merged_image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        val resolver = contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            resolver.openOutputStream(it).use { outputStream ->
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    ToastUtil.show(this, "已保存到相册",500)
                }
            }
        }
        if (uri == null){
            ToastUtil.show(this, "保存失败，请检查内存或者联系客户",500)
        }
    }

    override fun showBackMenu() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    companion object{
        const val TAG = "WaterMontageEnterActivity"
    }

}