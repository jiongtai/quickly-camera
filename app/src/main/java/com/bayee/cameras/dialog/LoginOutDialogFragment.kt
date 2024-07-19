package com.bayee.cameras.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bayee.cameras.App.ThisApp
import com.bayee.cameras.activity.othersetting.OtherSettingActivity
import com.bayee.cameras.databinding.FragmentDialogLoginOutBinding
import com.bayee.cameras.login.LoginStatusChangedEvent
import com.bayee.cameras.me.MeFragment
import com.bayee.cameras.superui.extension.shortToast
import com.drake.channel.sendEvent

class LoginOutDialogFragment : DialogFragment() {

    private lateinit var  binding : FragmentDialogLoginOutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogLoginOutBinding.inflate(inflater, container, false)
        getDialog()!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initListener()
    }

    private fun initListener() {
        //agree_btn，click
        binding.cancelAccountAgree.setOnClickListener {
            Log.d(TAG, "initListener: cancelAccountAgree")
            ThisApp.instance.logout()
            sendEvent(LoginStatusChangedEvent(false))
            "退出成功".shortToast()
            dismiss()
            OtherSettingActivity.loginOutListener?.closeActivity()
        }
        //disagree_btn，click
        binding.cancelAccountDisAgree.setOnClickListener {
            Log.d(TAG, "initListener: cancelAccountDisagree")
            dismiss()
        }
    }

    companion object{
        private const val TAG = "LoginOutDialogFragment"


        fun show(fragmentManager: FragmentManager) {
            val dialogFragment = LoginOutDialogFragment()
            dialogFragment.show(fragmentManager, "LoginOutDialogFragment")
        }
    }
}










