package com.bayee.cameras.api.repository

import android.util.Log
import com.bayee.cameras.util.MMKVUtils
import okhttp3.Interceptor
import okhttp3.Response
/**
 * 通过拦截器，实现添加网络请求公共参数
 */
class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //获取到request
        var request = chain.request()

        if (MMKVUtils.isLogin()) {
            //登录了
            //获取用户token
            val token = MMKVUtils.getToken()
            val timestamp = (System.currentTimeMillis() / 1000).toString()
            Log.d(TAG, "intercept: $timestamp")
            Log.d(TAG, "intercept: $token")

            request = request.newBuilder()
                .header("Authorization", MMKVUtils.getToken()!!) // 替换YOUR_TOKEN为实际的token
                .header("Timestamp", timestamp)
                .build()
        }

        //继续执行网络请求
        val response = chain.proceed(request)
        return response
    }

    companion object {
        const val TAG = "TokenInterceptor"
    }

}