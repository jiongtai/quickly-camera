package com.bayee.cameras.api.repository

import com.bayee.cameras.api.bean.AppInfoList
import com.bayee.cameras.api.bean.AppVersionList
import com.bayee.cameras.api.bean.OnlineSupportLink
import com.bayee.cameras.api.bean.PhoneCodeSms
import com.bayee.cameras.api.bean.SmsLoginFromUser
import com.bayee.cameras.api.bean.receive.AliPayResponse
import com.bayee.cameras.api.bean.receive.CancelAccountResponse
import com.bayee.cameras.api.bean.receive.DecrFreeTimeResponse
import com.bayee.cameras.api.bean.receive.LoginResponse
import com.bayee.cameras.api.bean.receive.PaymentResponse
import com.bayee.cameras.api.bean.receive.QuestionFeedBackResponse
import com.bayee.cameras.api.bean.receive.UserInfoResponse
import com.bayee.cameras.api.bean.receive.VipDataListResponse
import com.bayee.cameras.api.bean.receive.WxPayResponse
import com.bayee.cameras.api.bean.send.AliPayBody
import com.bayee.cameras.api.bean.send.LoginUser
import com.bayee.cameras.api.bean.send.Question
import com.bayee.cameras.api.bean.send.VipBody
import com.bayee.cameras.api.bean.send.WxPayBody

object DefaultNetworkRepository {

    private val servicePublic: DefaultNetworkService by lazy {
        DefaultNetworkService.createPublic()
    }

    private val serviceApp: DefaultNetworkService by lazy {
        DefaultNetworkService.createApp()
    }

    /**
     * 获取在线客服
     */
    suspend fun getOnlineSupportLink(fromProject: String): OnlineSupportLink {
        return servicePublic.getOnlineSupportLink(fromProject)
    }

    /**
     * 获取APP包信息
     */
    suspend fun getAppInfos(): AppInfoList {
        return servicePublic.getAppInfos()
    }

    /**
     * 获取APP版本
     */
    suspend fun getAppVersions(fromProject: String): AppVersionList {
        return servicePublic.getAppVersions(fromProject)
    }

    /**
     * 登录手机获取验证码
     */
    suspend fun getPhoneCodeSms(phone: SmsLoginFromUser): PhoneCodeSms {
        return servicePublic.getPhoneCodeSms(phone)
    }

    /**
     * 用户登录
     */
    suspend fun login(user: LoginUser): LoginResponse {
        return servicePublic.login(user)
    }

    /**
     * 获取用户信息
     */
    suspend fun getUserInfo(): UserInfoResponse {
        return serviceApp.getUserInfo()
    }

    /**
     * 问题反馈
     */
    suspend fun sendFeedback(question: Question): QuestionFeedBackResponse {
        return serviceApp.sendFeedback(question)
    }

    /**
     * 注销账号
     */
    suspend fun cancelAccount(): CancelAccountResponse {
        return serviceApp.cancelAccount()
    }

    /**
     * 减少使用次数
     */
    suspend fun decrFreeTime(): DecrFreeTimeResponse {
        return serviceApp.decrFreeTime()
    }

    /**
     * 价格列表
     */
    suspend fun getVipPrices(vipBody: VipBody): VipDataListResponse {
        return serviceApp.getVipPrices(vipBody)
    }

    /**
     *  获取可用支付渠道
     */
    suspend fun getPayment(fromProject: Int): PaymentResponse {
        return servicePublic.getPayment(fromProject)
    }

    /**
     * 获取支付支付宝订单详情
     */
    suspend fun getAliPayDetail(aliPayBody: AliPayBody): AliPayResponse {
        return serviceApp.getAliPayDetail(aliPayBody)
    }

    /**
     * 获取微信支付订单详情
     */
    suspend fun getWxPayDetail(aliPayBody: WxPayBody): WxPayResponse {
        return serviceApp.getWxPayDetail(aliPayBody)
    }

}













