package com.bayee.cameras.activity.home.recycleviewmodel

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bayee.cameras.R
import com.bayee.cameras.activity.photographActivity.CameraActivity
import com.bayee.cameras.util.Constant.WATER_TYPE

class WaterMarkAdapter(
    private var mWaterMarkItems: List<WaterMarkItem>,
    private val context: Context
) :
    RecyclerView.Adapter<WaterMarkAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.water_mark_background, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listItem = mWaterMarkItems[position]
        val viewStub = holder.itemView.findViewById<ViewStub>(R.id.viewStub)

        if (viewStub.parent != null) {
            (viewStub.parent as ViewGroup).removeView(viewStub)
        }

        // 根据不同的 listItem.xmlResId 动态加载布局
        val layoutInflater = LayoutInflater.from(holder.itemView.context)
        val view = layoutInflater.inflate(listItem.xmlResId, holder.itemView as ViewGroup, false)
        (holder.itemView as ViewGroup).addView(view)

        holder.textView.text = listItem.text

        // 直接在ViewHolder内部设置点击监听器
        holder.itemView.setOnClickListener {
            onItemClicked(listItem, position)
            finishCamera()
        }
    }

    private fun finishCamera() {
        if (CameraActivity.finishCameraListener != null) {
            CameraActivity.finishCameraListener!!.onFinishCamera()
        }
    }

    private fun onItemClicked(listItem: WaterMarkItem, position: Int) {
        // 根据item点击位置决定跳转到哪个Activity，这里仅为示例
        val intent = Intent(context, CameraActivity::class.java)

        // 如果需要传递数据到目标Activity
        intent.putExtra(WATER_TYPE, listItem.type)

        context.startActivity(intent)
    }

    override fun getItemCount(): Int = mWaterMarkItems.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //水印类型文字
        val textView: TextView = itemView.findViewById(R.id.textView)
        val enterCamera: FrameLayout = itemView.findViewById(R.id.water_enter_camera)
    }


}













