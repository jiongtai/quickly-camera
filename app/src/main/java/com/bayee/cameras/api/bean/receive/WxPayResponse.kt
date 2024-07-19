package com.bayee.cameras.api.bean.receive

// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json     = Json { allowStructuredMapKeys = true }
// val response = json.parse(Response.serializer(), jsonString)

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.StructureKind

@Serializable
data class WxPayResponse(
    val code: Int,
    val message: String,
    val data: WxPayData?,
    val trace_id: String
)

@Serializable
data class WxPayData(
    val prepayId: String,
    val partnerId: String,
    val timestamp: String,
    val nonceStr: String,
    @SerialName("package")
    val `package`: String,
    val sign: String,
    val appId: String
)
