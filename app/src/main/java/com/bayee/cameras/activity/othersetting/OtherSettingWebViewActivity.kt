package com.bayee.cameras.activity.othersetting

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bayee.cameras.R
import com.bayee.cameras.activity.base.BaseTitleActivity
import com.bayee.cameras.databinding.ActivityOtherSettingBinding
import com.bayee.cameras.databinding.ActivityOtherSettingWebViewBinding
import com.bayee.cameras.dialog.NetWorkErrorDialogFragment
import com.bayee.cameras.util.Constant

class OtherSettingWebViewActivity : BaseTitleActivity<ActivityOtherSettingWebViewBinding>() {

    override fun initViews() {
        super.initViews()

        with(binding.webView.settings) {
            javaScriptEnabled = true // 启用JavaScript
            loadWithOverviewMode = true // 缩放至屏幕大小
            useWideViewPort = true // 支持viewport标签
            defaultTextEncodingName = "utf-8"
            userAgentString = "User-Agent:Android"
            javaScriptCanOpenWindowsAutomatically = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            domStorageEnabled = true    //解决
            databaseEnabled = true//
            allowFileAccess = true
            savePassword = true
            setSupportZoom(true)
            builtInZoomControls = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
            useWideViewPort = true
        }

        // 设置WebViewClient以在应用内打开链接
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }

        // 从Intent中获取在线客服链接
        val link = intent.getStringExtra("网络url")
        if (link != null) {
            binding.webView.loadUrl(link)
        } else {
            NetWorkErrorDialogFragment.show(supportFragmentManager)
        }
    }


}