package com.bayee.cameras.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.LiveData
import com.bayee.cameras.api.bean.PhoneCodeSms
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.apache.commons.lang3.mutable.Mutable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.coroutineContext

object PickViewUtils {


    fun getPickViewInstance(context: Context, listener: OnTimeSelectedListener): String{
        var formattedDate = "0"
        //时间选择器
        val pvTime = TimePickerBuilder(context, object : OnTimeSelectListener {
            override fun onTimeSelect(date: Date, v: View?) {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                formattedDate = sdf.format(date)
                Log.d("这是时间", "onTimeSelect: Formatted date is $formattedDate")
                listener.onTimeSelected(formattedDate)
            }
        })
            .setTimeSelectChangeListener(object : OnTimeSelectChangeListener {
                override fun onTimeSelectChanged(date: Date) {
                    // 这里可以根据需要处理时间选择变化的逻辑
                }
            })
            .setType(booleanArrayOf(true, true, true, true, true, true))
            .setItemVisibleCount(6)
            .setLineSpacingMultiplier(2.0f)
            .isAlphaGradient(true)
            .isDialog(true)
            .build()
        pvTime.show()
        return formattedDate
    }


    interface OnTimeSelectedListener {
        fun onTimeSelected(formattedDate: String)
    }
}













