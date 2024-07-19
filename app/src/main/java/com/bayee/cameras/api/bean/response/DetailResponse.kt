package com.bayee.cameras.api.bean.response

import com.bayee.cameras.model.BaseViewModel


/**
 * 详情网络请求解析类
 * 继承BaseResponse
 * 定义了一个泛型T
 */
class DetailResponse<T> : BaseResponse() {
    /**
     * 真实数据
     * 类似是泛型
     */
    var data: T? = null
}

suspend fun <T> DetailResponse<T>.onSuccess(
    viewModel: BaseViewModel,
    action: suspend (data: T?) -> Unit
): Unit {
    if (isSucceeded) {
        action(data)
    } else {
        viewModel._response.value = this
    }
}