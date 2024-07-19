package com.bayee.cameras.util

import android.content.Context
import android.widget.Toast

object ToastUtil {

    private var currentToast: Toast? = null

    fun show(context: Context, message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        currentToast?.cancel()
        currentToast = Toast.makeText(context, message, duration)
        currentToast?.show()
    }
}