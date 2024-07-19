package com.bayee.cameras.api.repository

import com.bayee.cameras.App.ThisApp
import com.bayee.cameras.util.Constant
import com.bayee.cameras.util.JSONUtil
import com.chuckerteam.chucker.api.ChuckerInterceptor
//import com.chuckerteam.chucker.api.ChuckerInterceptor

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 网络相关依赖提供类
 *
 *
 * 例如：OkHttp，retrofit依赖
 */
object NetworkModule {
    /**
     * 提供OkHttpClient
     */
    fun provideOkHttpClientApp(): OkHttpClient {
        //初始化okhttp
        val okhttpClientBuilder = OkHttpClient.Builder()

        //添加公共网络请求参数拦截器
        okhttpClientBuilder.addInterceptor(TokenInterceptor())

            okhttpClientBuilder.addInterceptor(
                ChuckerInterceptor.Builder(ThisApp.instance).build()
            )

        return okhttpClientBuilder.build()
    }

    fun provideOkHttpClientPublic(): OkHttpClient {
        //初始化okhttp
        val okhttpClientBuilder = OkHttpClient.Builder()

        //配置缓存
//        val cache = Cache(AppContext.instance.cacheDir, Config.NETWORK_CACHE_SIZE)
//        okhttpClientBuilder.cache(cache)

//        okhttpClientBuilder.connectTimeout(10, TimeUnit.SECONDS) //连接超时时间
//            .writeTimeout(10, TimeUnit.SECONDS) //写，也就是将数据发送到服务端超时时间
//            .readTimeout(10, TimeUnit.SECONDS) //读，将服务端的数据下载到本地

        //网络签名加密插件
//        okhttpClientBuilder.addInterceptor(NetworkSecurit yInterceptor())

        //添加公共网络请求参数拦截器
//        okhttpClientBuilder.addInterceptor(TokenInterceptor())

//        if (Config.DEBUG) {
//            //调试模式
//
//            //创建okhttp日志拦截器
//            val loggingInterceptor = HttpLoggingInterceptor()
//
//            //设置日志等级
//            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//
//            //添加到网络框架中
//            okhttpClientBuilder.addInterceptor(loggingInterceptor)
//
//            //添加chucker实现应用内显示网络请求信息拦截器
//            okhttpClientBuilder.addInterceptor(
//                ChuckerInterceptor.Builder(AppContext.instance).build()
//            )
//        }
        return okhttpClientBuilder.build()
    }

    /**
     * 提供Retrofit实例
     *
     * @param okHttpClient
     * @return
     */
    fun provideRetrofitPublic(okHttpClient: OkHttpClient?): Retrofit {
        return Retrofit.Builder() //让retrofit使用okhttp
            .client(okHttpClient) //配置的okhttp配置
            .baseUrl(Constant.BaseUrl) //使用gson解析json
            //包括请求参数和响应
            .addConverterFactory(GsonConverterFactory.create(JSONUtil.createGson())) //创建retrofit
            .build()
    }

    fun provideRetrofitApp(okHttpClient: OkHttpClient?): Retrofit {
        return Retrofit.Builder() //让retrofit使用okhttp
            .client(okHttpClient) //配置的okhttp配置
            .baseUrl(Constant.BaseUrl) //使用gson解析json
            //包括请求参数和响应
            .addConverterFactory(GsonConverterFactory.create(JSONUtil.createGson())) //创建retrofit
            .build()
    }

}