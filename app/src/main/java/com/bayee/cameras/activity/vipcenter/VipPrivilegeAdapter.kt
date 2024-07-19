package com.bayee.cameras.activity.vipcenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bayee.cameras.R

class VipPrivilegeAdapter(private val datas: List<VipPrivilegeBean>) :
    RecyclerView.Adapter<VipPrivilegeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.vip_iv_icon)
        val textView1: TextView = itemView.findViewById(R.id.vip_tv1)
        val textView2: TextView = itemView.findViewById(R.id.vip_tv2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vipcenter_privilege_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem  = datas[position]
        val drawable = ContextCompat.getDrawable(holder.imageView.context, currentItem.imageUrl)
        holder.imageView.setImageDrawable(drawable)
        holder.textView1.text = currentItem.text1
        holder.textView2.text = currentItem.text2

        // 定义左侧边距变量，默认为0，即不设置额外边距
        var marginLeft = 0

        // 检查当前位置是否为5、6、7、8或10，如果是，则设置左侧边距
        if (position in 4..7 || position == 9) { // 注意：列表索引从0开始，所以位置5-8对应索引4-7，位置10对应索引9
            marginLeft = holder.imageView.context.resources.getDimensionPixelSize(R.dimen.d20)
        }

        if (position == 5){
            marginLeft = holder.imageView.context.resources.getDimensionPixelSize(R.dimen.d16)
        }

        if (position == 7 || position == 9){
            marginLeft = holder.imageView.context.resources.getDimensionPixelSize(R.dimen.d24)
        }

        // 仅当需要设置边距时，才调整imageView的LayoutParams
        if (marginLeft > 0) {
            val imageViewLayoutParams = holder.imageView.layoutParams as ViewGroup.MarginLayoutParams
            imageViewLayoutParams.leftMargin = marginLeft
            holder.imageView.layoutParams = imageViewLayoutParams
        }
    }

}
