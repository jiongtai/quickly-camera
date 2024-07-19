package com.bayee.cameras.api.bean.send

data class WxPayBody (
    /**
     * APP包标识
     */
    val fromProject: Int,

    /**
     * 价格配置ID，/app/vipPrices接口获取
     */
    val id: Int
) {
    override fun toString(): String {
        return "AliPayBody(fromProject=$fromProject, id=$id)"
    }
}
