package com.bayee.cameras.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bayee.cameras.api.bean.send.LoginUser
import com.bayee.cameras.api.bean.PhoneCodeSms
import com.bayee.cameras.api.bean.SmsLoginFromUser
import com.bayee.cameras.api.repository.DefaultNetworkRepository
import com.bayee.cameras.model.BaseViewModel
import com.bayee.cameras.util.MMKVUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class LoginViewModel: BaseViewModel() {

    private val _code = MutableSharedFlow<PhoneCodeSms>()
    val code: Flow<PhoneCodeSms> = _code

    private val _error = MutableSharedFlow<Int>()
    val error: Flow<Int> = _error

    /**
     * 获取验证码
     */
    fun getCode(phone: String) {
        Log.d(TAG, "getCode: 进入了发送验证码")//961385 990225 064977
        viewModelScope.launch(coroutineExceptionHandler) {
            val user = SmsLoginFromUser(1001, phone)
            val result = DefaultNetworkRepository.getPhoneCodeSms(user)
            if (result.code == HttpURLConnection.HTTP_OK){
                _code.emit(result)
                Log.d(TAG, "loginmessagegetCode: ${result.data!!.sms_code}")
            }else{
                Log.d(TAG, "错误: ${result.message}")
                Log.d(TAG, "错误: ${result.code}")
                Log.d(TAG, "错误: ${result.data!!.sms_code}")
            }
        }
    }

     fun login(
        phone: String,
        inputCode: String,
        fromProject: Int,
        appStore: String,
        ip: String,
        oaid: String,
        versionCode: Int
    ) {
        viewModelScope.launch {
            val user = LoginUser( phone, inputCode, fromProject, appStore, ip, oaid,versionCode)
            val result = DefaultNetworkRepository.login(user)
            Log.d(TAG, "loginmessage: ${result.toString()}")
            if (result.code == HttpURLConnection.HTTP_OK){
                MMKVUtils.setToken(result.data.token)
                MMKVUtils.setLogin(true)
                _error.emit(1)
                Log.d(TAG, "loginmessage1: ${result.data.token}")
            }else{
                Log.d(TAG, "loginmessage2: ${result.code}")
                Log.d(TAG, "loginmessage3: ${result.message}")
                _error.emit(0)
            }

        }
    }

    companion object{
        const val TAG = "LoginViewModel"
    }


}











