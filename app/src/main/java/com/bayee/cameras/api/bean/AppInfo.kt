package com.bayee.cameras.api.bean


data class AppInfo(
    val from_project: Int,
    val app_name: String,
    val ad_status: Int,
    val wxpay_status: Int,
    val alipay_status: Int,
    val enabled: Int
)