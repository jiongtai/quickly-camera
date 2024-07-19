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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DefaultNetworkService {

    /**
     * 获取在线客服
     */
    @GET("/public/onlineSupportLink")
    suspend fun getOnlineSupportLink(
        @Query(value = "fromProject") fromProject: String
    ): OnlineSupportLink


    /**
     * 获取APP包信息
     */
    @GET("/public/fromProjects")
    suspend fun getAppInfos(): AppInfoList


    /**
     * 获取APP版本
     */
    @GET("/public/version")
    suspend fun getAppVersions(
        @Query(value = "fromProject") fromProject: String
    ): AppVersionList

    /**
     * 获取手机登录验证码
     */
    @POST("/public/sms")
    suspend fun getPhoneCodeSms(
        @Body phone: SmsLoginFromUser
    ): PhoneCodeSms

    /**
     * 用户登录
     */
    @POST("/public/login")
    suspend fun login(
        @Body user: LoginUser
    ): LoginResponse

    /**
     * 获取用户数据
     */
    @GET("/app/userInfo")
    suspend fun getUserInfo(
    ): UserInfoResponse

    /**
     * 问题反馈
     */
    @POST("/app/feedback")
    suspend fun sendFeedback(
        @Body question: Question
    ): QuestionFeedBackResponse

    /**
     * 注销账号
     */
    @POST("/app/disableAccount")
    suspend fun cancelAccount(): CancelAccountResponse

    /**
     * 扣减体验次数
     */
    @POST("/app/decrFreeTime")
    suspend fun decrFreeTime(): DecrFreeTimeResponse

    /**
     * 价格列表
     */
    @POST("/app/vipPrices")
    suspend fun getVipPrices(
        @Body vipBody: VipBody
    ): VipDataListResponse

    /**
     * 获取可用支付渠道
     */
    @GET("/public/payments")
    suspend fun getPayment(
        @Query("fromProject") fromProject: Int
    ): PaymentResponse

    /**
     * 获取支付宝订单
     */
    @POST("/app/alipay")
    suspend fun getAliPayDetail(
        @Body aliPayBody: AliPayBody
    ): AliPayResponse

    /**
     * 获取微信订单
     */
    @POST("/app/wxpay")
    suspend fun getWxPayDetail(
        @Body aliPayBody: WxPayBody
    ): WxPayResponse


    companion object {
        fun createPublic(): DefaultNetworkService {
            return NetworkModule.provideRetrofitPublic(NetworkModule.provideOkHttpClientPublic())
                .create(DefaultNetworkService::class.java)
        }

        fun createApp(): DefaultNetworkService {
            return NetworkModule.provideRetrofitApp(NetworkModule.provideOkHttpClientApp())
                .create(DefaultNetworkService::class.java)
        }
    }

}














