package com.bayee.cameras.record.fragment.videos

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bayee.cameras.R
import com.bayee.cameras.activity.photodetail.PhotoDetailActivity
import com.bayee.cameras.activity.videodetail.VideoDetailActivity
import com.bayee.cameras.photo.bean.Photo
import com.bayee.cameras.photo.bean.Video
import com.bayee.cameras.record.RecordFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class VideoAdapter(private val videos: List<Video>) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private val selectedVideos = mutableListOf<Video>()

    // 添加一个外部设置编辑状态的方法
    fun setEditMode(isEditMode: Boolean) {
        this.isEditMode = isEditMode
        notifyDataSetChanged() // 刷新适配器以应用新的编辑状态
    }
    private var isEditMode = false

    private val requestOptions = RequestOptions()
        .transform(RoundedCorners(32)) // 设置圆角半径，例如16dp

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoView: ImageView = itemView.findViewById(R.id.imageView_video)
        val videoIvEdit: ImageView = itemView.findViewById<ImageView>(R.id.video_iv_edit)

        fun bind(video: Video) {
//            videoView.setVideoURI(Uri.parse(video.path))
//            videoView.start()
            Glide.with(itemView.context)
                .load(video.path)
                .apply(requestOptions)
                .into(videoView)
            videoIvEdit.visibility = if (isEditMode) View.VISIBLE else View.GONE

            if (isEditMode) {
                videoView.setOnClickListener {
                    video.isSelected = !video.isSelected
                    if (video.isSelected) {
                        selectedVideos.add(video)
                    } else {
                        selectedVideos.remove(video)
                    }
                    notifyItemChanged(adapterPosition)
                    RecordFragment.selectedChangedListener2?.onSelectedChanged2(selectedVideos)
                }
                videoIvEdit.setImageDrawable(
                    if (video.isSelected)
                        ContextCompat.getDrawable(itemView.context, R.drawable.record_yes_edit)
                    else
                        ContextCompat.getDrawable(itemView.context, R.drawable.record_no_edit)
                )
            }else{
                videoView.setOnClickListener {
                    val intent = Intent(itemView.context, VideoDetailActivity::class.java)
                    intent.putExtra("VIDEO_PATH", video.path)
                    itemView.context.startActivity(intent)
                }
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_item, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.bind(video)
    }

    override fun getItemCount() = videos.size
}
