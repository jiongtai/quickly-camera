package com.bayee.cameras.activity.photodetail

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bayee.cameras.R
import com.bayee.cameras.activity.base.BaseTitleActivity
import com.bayee.cameras.databinding.ActivityPhotoDetailBinding
import com.bumptech.glide.Glide

class PhotoDetailActivity : BaseTitleActivity<ActivityPhotoDetailBinding>() {
    override fun initDatum() {
        super.initDatum()
        val imagePath = intent.getStringExtra("PHOTO_PATH")
        imagePath?.let {
            Glide.with(this)
                .load(imagePath)
                .into(binding.photoDetailIv)
        } ?: run {
            Toast.makeText(this, "图片路径无效", Toast.LENGTH_SHORT).show()
        }
    }
}