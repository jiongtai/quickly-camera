package com.bayee.cameras.dialog

import android.R.attr.description
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bayee.cameras.App.ThisApp
import com.bayee.cameras.R
import com.bayee.cameras.contactus.ContactUsWebViewActivity
import com.bayee.cameras.databinding.FragmentDialogShareBinding
import com.bayee.cameras.util.Constant
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject


class ShareDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogShareBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogShareBinding.inflate(inflater, container, false)
        getDialog()!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initListener()
    }

    private fun initListener() {
        binding.shareX.setOnClickListener {
            dismiss()
        }
        binding.shareIconLink.setOnClickListener {
            val intent = Intent(requireContext(), ContactUsWebViewActivity::class.java)
            intent.putExtra(
                Constant.ONLINE_SUPPORT_LINK,
                "http://www.taeyeen.cn/quickly-camera-share.html"
            )
            intent.putExtra("share", "share")
            startActivity(intent)
        }
        binding.shareIconWx.setOnClickListener {
            shareWx()
        }
    }

    private fun shareWx() {
//        ThisApp.instance.initWechat()
        val webpageObject = WXWebpageObject()
        webpageObject.webpageUrl = "http://www.taeyeen.cn/quickly-camera-share.html"
        val mediaMessage = WXMediaMessage(webpageObject)
        mediaMessage.title = "水印照片视频打卡"
        mediaMessage.description = "可自定义修改时间、地点的水印相机"
        val bitmap = BitmapFactory.decodeFile(R.drawable.vip_center_water1.toString())
//        val thumBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true) //图片大小有限制，太大分享不了
        //mediaMessage.thumbData = Tools.getBitmapByte(thumBmp);
        val req = SendMessageToWX.Req()
        req.transaction = System.currentTimeMillis().toString()
        req.message = mediaMessage
        req.scene = SendMessageToWX.Req.WXSceneTimeline
        ThisApp.instance.wxapi.sendReq(req)
    }


    companion object {
        private const val TAG = "ShareDialogFragment"

        fun show(fragmentManager: FragmentManager) {
            val dialogFragment = ShareDialogFragment()
            dialogFragment.show(fragmentManager, "ShareDialogFragment")
        }
    }


}