package com.bayee.cameras.activity.vipcenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bayee.cameras.R

class VipWaterAdapter(private val dataList: List<VipWaterBean>) :
    RecyclerView.Adapter<VipWaterAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.vipcenter_iv)
        val textView: TextView = itemView.findViewById(R.id.vipcenter_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vipcenter_water_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        val drawable = ContextCompat.getDrawable(holder.imageView.context, currentItem.imageUrl)
        holder.imageView.setImageDrawable(drawable)
        holder.textView.text = currentItem.id
    }

    override fun getItemCount(): Int = dataList.size
}







