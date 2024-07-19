package com.bayee.cameras.api.bean

data class AppVersion (
    val version_code: Int,
    val version_name: String,
    val apk_url: String,
    val from_project: Int,
    val force_update: Boolean
)