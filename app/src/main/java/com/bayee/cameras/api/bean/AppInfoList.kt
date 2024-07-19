package com.bayee.cameras.api.bean

data class AppInfoList (
    val code: Int,
    val message: String,
    val data: List<AppInfo>,
    val trace_id: String
)