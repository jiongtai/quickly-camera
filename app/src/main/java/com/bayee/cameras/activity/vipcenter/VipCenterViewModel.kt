package com.bayee.cameras.activity.vipcenter

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bayee.cameras.App.ThisApp
import com.bayee.cameras.R
import com.bayee.cameras.api.bean.receive.AliPayResponse
import com.bayee.cameras.api.bean.receive.PaymentResponse
import com.bayee.cameras.api.bean.receive.VipDataListResponse
import com.bayee.cameras.api.bean.receive.WxPayData
import com.bayee.cameras.api.bean.receive.WxPayResponse
import com.bayee.cameras.api.bean.send.AliPayBody
import com.bayee.cameras.api.bean.send.VipBody
import com.bayee.cameras.api.bean.send.WxPayBody
import com.bayee.cameras.api.repository.DefaultNetworkRepository
import com.bayee.cameras.model.BaseViewModel
import com.bayee.cameras.util.Constant
import com.tencent.mm.opensdk.modelpay.PayReq
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import kotlin.math.log

class VipCenterViewModel : BaseViewModel() {

    private lateinit var mWxResponse: WxPayResponse
    private var payId = 1
    private var channel = 0 //支付渠道 0支付宝 ，1微信

    private val _vipDataList = MutableSharedFlow<VipDataListResponse>()
    val vipDataList: Flow<VipDataListResponse> = _vipDataList

    private val _payment = MutableSharedFlow<PaymentResponse>()
    val payment: Flow<PaymentResponse> = _payment

    private val _processAlipay = MutableSharedFlow<String>()
    val processAlipay: Flow<String> = _processAlipay

    fun getVipPrices() {
        instance = this
        viewModelScope.launch {
            val vipBody = VipBody(1001, "huawei")
            val result = DefaultNetworkRepository.getVipPrices(vipBody)
            Log.d(TAG, "getVipPrices: ${result.toString()}")
            if (result.code == HttpURLConnection.HTTP_OK) {
                _vipDataList.emit(result)
                Log.d(TAG, "getVipPrices: ${result.toString()}")
            } else {
                Log.d(TAG, "getVipPrices: $result.message")
                _response3.value = result.message
            }
        }
    }

    fun getPayment() {
        viewModelScope.launch {
            val result = DefaultNetworkRepository.getPayment(Constant.FROM_PROJECT_1001)
            if (result.code == HttpURLConnection.HTTP_OK) {
                _payment.emit(result)
            } else {
                _response3.value = result.message
            }
        }
    }

    //点击了支付
    fun primary() {
        viewModelScope.launch {
            val id = payId
            val fromProject = Constant.FROM_PROJECT_1001
            when (channel) {
                0 -> {
                    //支付宝支付
                    val aliPayBody = AliPayBody(fromProject, id)
                    val result = DefaultNetworkRepository.getAliPayDetail(aliPayBody)
                    Log.d(TAG, "viewModelScopePrimary: 支付宝支付)}")
                    Log.d(TAG, "primary: ${aliPayBody.toString()}")
                    Log.d(TAG, "primary: ${result.toString()}")
                    if (result.code == HttpURLConnection.HTTP_OK) {
                        Log.d(TAG, "viewModelScopePrimary: ${result.toString()}")
                        processAliPay(result)
                    } else {
                        _response3.value = result.message
                    }
                }

                1 -> {
                    //微信支付
                    val wxPayBody = WxPayBody(fromProject, id)
                    val result = DefaultNetworkRepository.getWxPayDetail(wxPayBody)
                    Log.d(TAG, "viewModelScopePrimary: 微信支付}")
                    if (result.code == HttpURLConnection.HTTP_OK) {
                        Log.d(TAG, "viewModelScopePrimary: ${result.toString()}")
                        Log.d(TAG, "viewModelScopePrimary: ${result.data!!.appId}")
                        mWxResponse = result
                        processWechat()
                    } else {
                        _response3.value = result.message
                    }
                }
            }

        }
    }

    /**
     * 处理支付宝
     * @param data
     */
    private fun processAliPay(data: AliPayResponse) {
        aliPay(data.data!!.ali_url)
    }
    private fun aliPay(data: String) {
        viewModelScope.launch {
            _processAlipay.emit(data)
        }
    }


    fun setPayId(id: Int) {
        this.payId = id
    }

    fun setChannel(channel: Int) {
        this.channel = channel
    }

    /**
     * 处理微信支付
     *
     * @param data
     */
    fun processWechat() {
        //把服务端返回的参数
        //设置到对应的字段
        val data = mWxResponse.data!!
        val request = PayReq()
        request.appId = data.appId
        request.partnerId = data.partnerId
        request.prepayId = data.prepayId
        request.nonceStr = data.nonceStr
        request.timeStamp = data.timestamp
        request.packageValue = data.`package`
        request.sign = data.sign
        ThisApp.instance.wxapi.sendReq(request)
    }

    companion object{
        lateinit var instance: VipCenterViewModel
    }

}






