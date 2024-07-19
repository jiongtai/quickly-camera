package com.bayee.cameras.api.bean

data class PhoneCodeSms(
    val code: Int,
    val message: String,
    val data: SmsData?,
    val trace_id: String?
)

data class SmsData(
    val sms_code: String
)
