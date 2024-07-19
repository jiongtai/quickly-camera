package com.bayee.cameras.api.bean

//获取验证码实体
data class SmsLoginFromUser (
    val from_project : Int,
    val phone : String,
)