package com.bayee.cameras.api.bean.receive

data class CancelAccountResponse(
    val code: Int,
    val message: String,
    val trace_id: String
)