package com.bayee.cameras.api.bean

data class AppVersionList(
    val code: Int,
    val message: String,
    val data: AppVersion,
    val trace_id: String
)