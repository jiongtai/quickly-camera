package com.bayee.cameras.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimeUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentFormattedTime1(): String {
        val currentDateTime = LocalDateTime.now() // 获取当前日期和时间
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // 设置日期时间格式
        return currentDateTime.format(formatter) // 格式化并返回字符串
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentFormattedTime2(): String {
        val currentDateTime = LocalDateTime.now() // 获取当前日期和时间
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // 设置日期时间格式
        return currentDateTime.format(formatter) // 格式化并返回字符串
    }

}