package com.bayee.cameras.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.bayee.cameras.App.ThisApp
import com.drake.channel.sendEvent
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 * 微信支付回调界面
 */
class WXPayEntryActivity : Activity(), IWXAPIEventHandler {

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        setContentView(textView)
        processIntent(intent)
    }

    protected override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        processIntent(intent)
    }

    /**
     * 处理微信回调结果
     *
     * @param intent
     */
    private fun processIntent(intent: Intent) {
        ThisApp.instance.wxapi.handleIntent(intent, this)
    }

    /**
     * 请求微信，会回调该方法
     *
     * @param baseReq
     */
    override fun onReq(baseReq: BaseReq) {

    }

    /**
     * 微信回调
     *
     * @param baseResp
     */
    override fun onResp(baseResp: BaseResp) {
//        Timber.d("onResp %d %d %s", baseResp.getType(), baseResp.errCode, baseResp.errStr)
        if (baseResp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            //支付回调

            //发送通知，支付界面处理
            sendEvent(
                WechatPayStatusChangedEvent(baseResp)
            )

            finish()
        }
    }

    companion object {
        private const val TAG = "WXPayEntryActivity"
    }
}