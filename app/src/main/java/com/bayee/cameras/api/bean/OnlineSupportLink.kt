package com.bayee.cameras.api.bean

data class OnlineSupportLink (
    val code: Int, // 响应状态码
    val message: String, // 响应消息
    val data: String, // 数据部分，这里是一个字符串，代表URL
    val traceId: String // 请求跟踪ID
)