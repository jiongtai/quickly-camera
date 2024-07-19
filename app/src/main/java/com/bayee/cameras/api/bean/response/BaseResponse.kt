package com.bayee.cameras.api.bean.response

/**
 * 通用网络请求响应模型
 */
open class BaseResponse {
    /**
     * 状态码
     * 等于0表示成功
     */
//    var status = 0
    var code = 0

    /**
     * 出错的提示信息
     * 发生了错误不一定有
     */
    var message: String? = null

    /**
     * 是否成功
     *
     * @return
     */
    val isSucceeded: Boolean
//        get() = status == 0
        get() = code == 0
}