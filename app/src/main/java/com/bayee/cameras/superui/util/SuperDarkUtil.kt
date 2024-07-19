package com.bayee.cameras.superui.util

import android.content.Context
import android.content.res.Configuration

/**
 * 深色模型工具类
 */
object SuperDarkUtil {
    /**
     * 是否是深色模型
     */
    fun isDark(context: Context): Boolean {
        return context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}