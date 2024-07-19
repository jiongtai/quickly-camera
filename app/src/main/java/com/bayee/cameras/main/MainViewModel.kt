package com.bayee.cameras.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bayee.cameras.App.ThisApp
import com.bayee.cameras.api.bean.AppInfoList
import com.bayee.cameras.api.bean.receive.PaymentResponse
import com.bayee.cameras.api.bean.receive.UserInfoResponse
import com.bayee.cameras.api.repository.DefaultNetworkRepository
import com.bayee.cameras.login.LoginStatusChangedEvent
import com.bayee.cameras.main.MainActivity.Companion
import com.bayee.cameras.model.BaseViewModel
import com.bayee.cameras.util.Constant
import com.bayee.cameras.util.MMKVUtils
import com.drake.channel.sendEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class MainViewModel : BaseViewModel()  {

    private val _appInfos = MutableSharedFlow<AppInfoList>()
    val appInfos: Flow<AppInfoList> = _appInfos

    private val _userInfo = MutableSharedFlow<UserInfoResponse>()
    val userInfo: Flow<UserInfoResponse> = _userInfo

    private val _payment = MutableSharedFlow<PaymentResponse>()
    val payment: Flow<PaymentResponse> = _payment

    /**
     * 获取APP包信息
     */
    fun getAppInfos() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val appInfos = DefaultNetworkRepository.getAppInfos()
            if (appInfos.code == HttpURLConnection.HTTP_OK){
//                Log.d(TAG, "getAppInfos: ${appInfos.data.size}")
//                Log.d(TAG, "getAppInfos: ${appInfos.data[0].app_name}")
            }else{
                Log.d(TAG, "getAppInfos: $appInfos.message")
                _exception.value = Exception(appInfos.message)
            }
        }
    }

    /**
     * 获取APP版本
     */
    fun getAppVersions() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val appVersions = DefaultNetworkRepository.getAppVersions("1001")
            if (appVersions.code == HttpURLConnection.HTTP_OK){
                Log.d(TAG, "getAppVersions: ${appVersions.data.apk_url}")
            }else{
                Log.d(TAG, "getAppVersions: $appVersions.message")
                _exception.value = Exception(appVersions.message)
            }
        }
    }

    /**
     * 获取用户信息
     */
    fun getUserInfo() {
        viewModelScope.launch {
            val userInfo = DefaultNetworkRepository.getUserInfo()
            if (userInfo.code == HttpURLConnection.HTTP_OK){
                Log.d(TAG, "getUserInfo: ${userInfo.toString()}")
                ThisApp.instance.login()
                _userInfo.emit(userInfo)
            }else{
                Log.d(TAG, "getUserInfo: ${userInfo.code}")
                ThisApp.instance.logout()
                _response2.value = userInfo.code
            }
        }
    }

    /**
     * 获取APP_ID
     */
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

    companion object{
        const val TAG = "MainViewModel"
    }
    
}












