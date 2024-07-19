package com.bayee.cameras.api.bean.receive


data class PaymentResponse(
    val code: Int,
    val message: String,
    val data: List<PaymentMethod>,
    val traceId: String
) {
    override fun toString(): String {
        return "PaymentResponse(code=$code, message='$message', data=$data, traceId='$traceId')"
    }
}

data class PaymentMethod(
    val fromProject: Int,
    val payment: String,
    val enable: Boolean,
    val app_id: String,
) {
    override fun toString(): String {
        return "PaymentMethod(fromProject=$fromProject, payment='$payment', enable=$enable)"
    }
}


