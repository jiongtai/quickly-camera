package com.bayee.cameras.activity.vipcenter

/**
 * 支付宝支付状态改变了事件
 */
class AlipayStatusChangedEvent(
    /**
     * 支付状态
     */
    var data: PayResult
)