package com.bayee.cameras.activity.watermontage

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bayee.cameras.R
import com.bayee.cameras.activity.photodetail.PhotoDetailActivity
import com.bayee.cameras.activity.photographActivity.CameraActivity
import com.bayee.cameras.photo.bean.Photo
import com.bayee.cameras.record.RecordFragment
import com.bayee.cameras.util.Constant.WATER_TYPE
import com.bayee.cameras.util.Constant.WATER_TYPE_1_1
import com.bumptech.glide.Glide

class WaterMontageAdapter(private val imagePaths: List<Photo>) :
    RecyclerView.Adapter<WaterMontageAdapter.MyViewHolder>() {

    //选中的图片列表
    private val selectedPhotos = mutableListOf<Photo>()


    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            if (viewType == 0) R.layout.item_water_montage_0_image
            else R.layout.item_water_montage_1_image, parent, false
        )
        return when (viewType) {
            0 -> CameraImageViewHolder(itemView)
            else -> DatabaseImageViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return imagePaths.size + 1
    }

    abstract class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(position: Int)
    }

    inner class CameraImageViewHolder(itemView: View) : MyViewHolder(itemView) {
        override fun bind(position: Int) {
            val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.water_montage_camera)
            constraintLayout.setOnClickListener {
                val intent = Intent(itemView.context, CameraActivity::class.java)
                intent.putExtra(WATER_TYPE, WATER_TYPE_1_1)
                itemView.context.startActivity(intent)
            }
        }
    }

    inner class DatabaseImageViewHolder(itemView: View) : MyViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageView_water_1_photo)

        override fun bind(position: Int) {
            val photo = imagePaths[position - 1]
            bindImage(photo.path)
            val water_montage_extends = itemView.findViewById<ImageView>(R.id.water_montage_extends)
            val imageView =  itemView.findViewById<ImageView>(R.id.imageView_water_1_photo)
            val water_montage_line = itemView.findViewById<LinearLayout>(R.id.water_montage_line)
            val water_montage_tv = itemView.findViewById<TextView>(R.id.water_montage_tv)
            //放大扩展查看图片
            water_montage_extends.setOnClickListener {
                val intent = Intent(itemView.context, PhotoDetailActivity::class.java)
                intent.putExtra("PHOTO_PATH", photo.path)
                itemView.context.startActivity(intent)
            }
            imageView.setOnClickListener {
                photo.isSelected = !photo.isSelected
                if (photo.isSelected) {
                    selectedPhotos.add(photo)
                } else {
                    selectedPhotos.remove(photo)
                }
                water_montage_line.visibility = if (photo.isSelected) View.VISIBLE else View.GONE
                water_montage_tv.text = selectedPhotos.size.toString()
                WaterMontageActivity.selectedChangedListener?.onSelectedChanged(selectedPhotos)
            }

        }

        private fun bindImage(imagePath: String?) {
            Glide.with(itemView.context)
                .load(imagePath)
                .fitCenter()
                .into(imageView)
        }
    }
}

