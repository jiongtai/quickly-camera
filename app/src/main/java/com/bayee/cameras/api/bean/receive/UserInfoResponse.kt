package com.bayee.cameras.api.bean.receive

data class UserInfoResponse(
    val code: Int,
    val message: String,
    val data: UserData,
    val trace_id: String
)

data class UserData(
    val user_id: Int,
    val phone: String,
    val from_project: Int,
    val enabled: Boolean,
    val is_vip: Boolean,
    val vip_type: Int,
    val vip_expire_at: String,
    val is_vip_expired: Boolean,
    val app_store: String,
    val oaid: String,
    val version_code: Int,
    val free_time: Int,
    val register_time: String,
    val last_login_time: String
) {
    override fun toString(): String {
        return "UserData(user_id=$user_id, phone='$phone', from_project=$from_project, enabled=$enabled, is_vip=$is_vip, vip_type=$vip_type, vip_expire_at='$vip_expire_at', is_vip_expired=$is_vip_expired, app_store='$app_store', oaid='$oaid', version_code=$version_code, free_time=$free_time, register_time='$register_time', last_login_time='$last_login_time')"
    }
}
