package com.bayee.cameras.util

import com.tencent.mmkv.MMKV

object MMKVUtils {

    val p: MMKV by lazy {
        MMKV.defaultMMKV()!!
    }

    /**
     * 设置历史定位
     *
     * @param value
     */
    fun setHistoryLocation(value: String?) {
        p.encode(HISTORY_LOCATION, value)
    }

    /**
     * 获取历史定位
     *
     * @return
     */
    fun getHistoryLocation(): String {
        return p.decodeString(HISTORY_LOCATION, "江苏省南京市鼓楼区北京西路68号")!!
    }

    /**
     * 设置用户Token
     *
     * 可以加密后存储，防止泄露
     * @param value
     */
    fun setToken(value: String?) {
        p.encode(TOKEN, value)
    }

    /**
     * 获取用户Token
     *
     * @return
     */
    fun getToken(): String? {
        return p.decodeString(TOKEN)
    }


    /**
     * 设置登录
     * @param value
     */
    fun setLogin(value: Boolean) {
        p.encode(ISLOGIN, value)
    }

    /**
     * 是否登录了
     *
     * @return
     */
    fun isLogin(): Boolean {
        return p.decodeBool(ISLOGIN,false)
    }

    /**
     * 登出
     */

    fun logout() {
        p.removeValueForKey(TOKEN)
    }

    private const val HISTORY_LOCATION = "user"
    private const val TOKEN = "token"
    private const val ISLOGIN = "islogin"
}















