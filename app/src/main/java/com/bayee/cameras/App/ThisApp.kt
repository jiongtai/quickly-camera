package com.bayee.cameras.App

import android.app.Application
import com.bayee.cameras.activity.vipcenter.VipCenterViewModel
import com.bayee.cameras.api.bean.receive.UserInfoResponse
import com.bayee.cameras.config.Config
import com.bayee.cameras.login.LoginStatusChangedEvent
import com.bayee.cameras.photo.database.SQLDatabase
import com.bayee.cameras.util.MMKVUtils
import com.drake.channel.sendEvent
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.mmkv.MMKV

class ThisApp: Application() {

    override fun onCreate() {
        super.onCreate()
        SQLDatabase.getDatabase(this);
        instance = this

        initMMKV()
    }

    /**
     * 初始化mmkv
     */
    private fun initMMKV() {
        val rootDir = MMKV.initialize(this)
    }

    fun logout() {
        logoutSilence()
        sendEvent(LoginStatusChangedEvent(false))
    }

    fun login() {
        sendEvent(LoginStatusChangedEvent(true))
    }

    private fun logoutSilence() {
        //清除登录相关信息
        MMKVUtils.setLogin(false)
    }

    lateinit var wxapi: IWXAPI
    fun initWechat(appId: String) {
        wxapi = WXAPIFactory.createWXAPI(applicationContext, null)
        wxapi.registerApp(appId)
    }


    /**分享到微信朋友圈*/
    companion object {
        var userInfo: UserInfoResponse? = null
        const val TAG = "AppContext"
        lateinit var instance: ThisApp
    }

}