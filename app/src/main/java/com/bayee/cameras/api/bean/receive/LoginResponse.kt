package com.bayee.cameras.api.bean.receive

data class LoginResponse(
    val code: Int,
    val message: String,
    val data: Data,
    val trace_id: String
){
    override fun toString(): String {
        return "LoginResponse(code=$code, message='$message', data=$data, trace_id='$trace_id')"
    }

}

data class Data(
    val token: String
) {
    override fun toString(): String {
        return "Data(token='$token')"
    }
}
