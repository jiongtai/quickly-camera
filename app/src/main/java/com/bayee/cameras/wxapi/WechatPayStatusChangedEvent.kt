package com.bayee.cameras.wxapi

import com.tencent.mm.opensdk.modelbase.BaseResp

/**
 * 微信支付状态改变了事件
 */
class WechatPayStatusChangedEvent(
    /**
     * 支付状态
     */
    var data: BaseResp
)