package com.bayee.cameras.record.fragment.photos

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bayee.cameras.R
import com.bayee.cameras.activity.photodetail.PhotoDetailActivity
import com.bayee.cameras.photo.bean.Photo
import com.bayee.cameras.record.RecordFragment
import com.bayee.cameras.util.DataUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class PhotoAdapter(private val photos: List<Photo>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private val selectedPhotos = mutableListOf<Photo>()

    // 添加一个外部设置编辑状态的方法
    fun setEditMode(isEditMode: Boolean) {
        this.isEditMode = isEditMode
        notifyDataSetChanged() // 刷新适配器以应用新的编辑状态
    }
    private var isEditMode = false

    private val requestOptions = RequestOptions()
        .transform(RoundedCorners(32)) // 设置圆角半径，例如16dp

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView_photo)
        val photoIvEdit: ImageView = itemView.findViewById<ImageView>(R.id.photo_iv_edit)

        fun bind(photo: Photo) {
            Glide.with(itemView.context)
                .load(photo.path)
//                .centerCrop()
                .apply(requestOptions)
                .into(imageView)
            photoIvEdit.visibility = if (isEditMode) View.VISIBLE else View.GONE

            if (isEditMode) {
                imageView.setOnClickListener {
                    photo.isSelected = !photo.isSelected
                    if (photo.isSelected) {
                        selectedPhotos.add(photo)
                    } else {
                        selectedPhotos.remove(photo)
                    }
                    notifyItemChanged(adapterPosition)
                    RecordFragment.selectedChangedListener?.onSelectedChanged(selectedPhotos)
                }
                photoIvEdit.setImageDrawable(
                    if (photo.isSelected)
                        ContextCompat.getDrawable(itemView.context, R.drawable.record_yes_edit)
                    else
                        ContextCompat.getDrawable(itemView.context, R.drawable.record_no_edit)
                )
            }else{
                imageView.setOnClickListener {
                    val intent = Intent(itemView.context, PhotoDetailActivity::class.java)
                    intent.putExtra("PHOTO_PATH", photo.path)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]
        Log.d("PhotoAdapter", "onBindViewHolder: ${photo.path}")
        holder.bind(photo)
    }

    override fun getItemCount(): Int = photos.size
}

















