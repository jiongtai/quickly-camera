package com.bayee.cameras.api.bean.receive

data class VipDataListResponse(
    val code: Int,
    val message: String,
    val data: List<VipData>,
    val traceId: String
) {
    override fun toString(): String {
        return "VipDataListResponse(code=$code, message='$message', data=$data, traceId='$traceId')"
    }
}

data class VipData(
    val id: Int,
    val vipName: String,
    val vipDesc: String,
    val appStore: String,
    val vipPrice: Float,
    val originPrice: Int,
    val vipMonth: Int,
    val sorted: Int,
    val fromProject: Int
) {
    override fun toString(): String {
        return "VipData(id=$id, vipName='$vipName', vipDesc='$vipDesc', appStore='$appStore', vipPrice=$vipPrice, originPrice=$originPrice, vipMonth=$vipMonth, sorted=$sorted, fromProject=$fromProject)"
    }
}
