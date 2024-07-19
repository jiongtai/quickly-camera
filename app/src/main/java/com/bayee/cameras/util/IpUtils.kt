package com.bayee.cameras.util

import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient

object IpUtils {
    fun getLocalIpAddress(context: Context): String {
        try {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifiManager != null) {
                val wifiInfo = wifiManager.connectionInfo
                val ipAddress = wifiInfo.ipAddress
                val ipHostAddress = Formatter.formatIpAddress(ipAddress)
                Log.e("本机ip", "本机IP：" + ipHostAddress)
                return ipHostAddress
            }
        } catch (e: Exception) {
           println(e)
        }
        return "" // 如果发生异常，返回空字符串
    }

    fun getOaid(context: Context): String? {
        val oaidInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
        return oaidInfo.id
    }


}