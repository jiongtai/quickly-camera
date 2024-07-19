package com.bayee.cameras.api.bean.receive

data class AliPayResponse(
    val code: Int,
    val message: String,
    val data: AlipayData?,
    val trace_id: String
) {
    override fun toString(): String {
        return "AliPayResponse(code=$code, message='$message', data=$data, trace_id='$trace_id')"
    }
}

data class AlipayData(
    val ali_url: String
) {
    override fun toString(): String {
        return "AlipayData(ali_url='$ali_url')"
    }
}
