package com.bayee.cameras.superui.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Locale

/**
 * Package(应用)相关工具方法
 */
object SuperPackageUtil {
    /**
     * 版本名称
     * 一般是用来显示给人类阅读的
     *
     * @param context
     * @return 一般都是3位：例如：2.0.1
     */
    fun getVersionName(context: Context): String {
        try {
            //获取应用信息
            val packageInfo = getPackageInfo(context, context.packageName)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 版本号
     *
     * @param context
     * @return 一般是整形，例如：100；使用int更方便判断大小
     */
    fun getVersionCode(context: Context): Long {
        //获取应用信息
        try {
            val packageInfo = getPackageInfo(context, context.packageName)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else packageInfo.versionCode.toLong()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 获取应用信息
     *
     * @param context
     * @param packageName
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    @Throws(PackageManager.NameNotFoundException::class)
    private fun getPackageInfo(context: Context, packageName: String): PackageInfo {
        //获取包管理器
        val packageManager = context.packageManager
        return packageManager.getPackageInfo(packageName, 0)
    }

    /**
     * 判断应用是否安装
     *
     * @param context
     * @param data
     * @return
     */
    fun isInstalled(context: Context, data: String): Boolean {
        try {
            context.packageManager.getPackageInfo(data, 0)
        } catch (x: Exception) {
            return false
        }
        return true
    }

    /**
     * 获取md5签名
     *
     * @param context
     * @return
     */
    fun getMD5Signature(context: Context): String? {
        return getSignature(context, "MD5")
    }

    /**
     * 获取SHA1签名
     *
     * @param context
     * @return
     */
    fun getSHA1Signature(context: Context): String? {
        return getSignature(context, "SHA1")
    }

    /**
     * 获取签名
     *
     * @param context
     * @return
     */
    fun getSignature(context: Context, algorithm: String): String? {
        try {
            val info = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            val cert = info.signatures[0].toByteArray()
            val md = MessageDigest.getInstance(algorithm)
            val publicKey = md.digest(cert)
            val hexString = StringBuilder()
            for (i in publicKey.indices) {
                val appendString = Integer.toHexString(0xFF and publicKey[i].toInt())
                    .uppercase()
                if (appendString.length == 1) hexString.append("0")
                hexString.append(appendString.lowercase(Locale.getDefault()))
                if (i != publicKey.size - 1) {
                    hexString.append(":")
                }
            }
            return hexString.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }
}