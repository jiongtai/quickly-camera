package com.bayee.cameras

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bayee.cameras.dialog.NetWorkErrorDialogFragment
import com.bayee.cameras.dialog.TermServiceDialogFragment
import com.bayee.cameras.util.PickViewUtils
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test)


    }
    companion object {
        private const val TAG = "TestActivity"
    }

}






