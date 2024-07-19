package com.bayee.cameras.api.bean.receive

data class DecrFreeTimeResponse(
    val code: Int,
    val message: String,
    val data: Any?,
    val trace_id: String
) {
    override fun toString(): String {
        return "DecrFreeTimeResponse(code=$code, message='$message', data=$data, trace_id='$trace_id')"
    }
}
