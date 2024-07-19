package com.bayee.cameras.api.bean.send

data class LoginUser(
    val phone: String,
    val code: String,
    val fromProject: Int,
    val appStore: String,
    val ip: String,
    val oaid: String,
    val versionCode: Int
)

