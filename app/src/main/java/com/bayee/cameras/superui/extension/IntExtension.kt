package com.bayee.cameras.superui.extension

import android.widget.Toast
import com.bayee.cameras.App.ThisApp

/**
 * Int 扩展toast
 */

/**
 * 短toast
 */
fun Int.shortToast() {
    Toast.makeText(ThisApp.instance, this, Toast.LENGTH_SHORT).show()
}

/**
 * 长toast
 */
fun Int.longToast() {
    Toast.makeText(ThisApp.instance, this, Toast.LENGTH_LONG).show()
}