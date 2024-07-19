package com.bayee.cameras.activity.othersetting

import android.content.Intent
import android.util.Log
import android.view.View
import com.bayee.cameras.activity.base.BaseTitleActivity
import com.bayee.cameras.databinding.ActivityOtherSettingBinding
import com.bayee.cameras.dialog.CancelAccountDialogFragment
import com.bayee.cameras.dialog.LoginOutDialogFragment
import com.bayee.cameras.dialog.LoginOutListener
import com.bayee.cameras.login.LoginActivity
import com.bayee.cameras.superui.extension.shortToast
import com.bayee.cameras.util.MMKVUtils

class OtherSettingActivity : BaseTitleActivity<ActivityOtherSettingBinding>() ,LoginOutListener{

    private lateinit var viewModel: OtherSettingViewModel

    override fun initViews() {
        super.initViews()
        binding.otherSettingToolbar.toolbarTextCenter.text = "其他设置"
    }

    override fun initDatum() {
        super.initDatum()
        initInterface()
    }

    private fun initInterface() {
        loginOutListener = this
    }

    override fun initListeners() {
        super.initListeners()
        //cancal account
        binding.linearLayout1.setOnClickListener {
            if (MMKVUtils.isLogin()){
                CancelAccountDialogFragment.show(supportFragmentManager)
            }else{
                "请先登录".shortToast()
            }
        }
        binding.linearLayout2.setOnClickListener {
            val intent = Intent(this, OtherSettingWebViewActivity::class.java)
            intent.putExtra("网络url", "http://taeyeen.cn:9033/public/protocol.html?type=2&fromProject=1001")
            startActivity(intent)
        }
        binding.linearLayout3.setOnClickListener {
            val intent = Intent(this, OtherSettingWebViewActivity::class.java)
            intent.putExtra("网络url", "http://taeyeen.cn:9033/public/protocol.html?type=1&fromProject=1001")
            startActivity(intent)
        }
        //LOGIN OUT CLICK
        binding.otherSettingLoginOut.setOnClickListener {
            LoginOutDialogFragment.show(supportFragmentManager)
        }

        binding.otherSettingLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ${MMKVUtils.isLogin()}")
        if (MMKVUtils.isLogin()){
            binding.otherSettingLoginOut.visibility = View.VISIBLE
            binding.otherSettingLogin.visibility = View.GONE
        }else{
            binding.otherSettingLoginOut.visibility = View.GONE
            binding.otherSettingLogin.visibility = View.VISIBLE
        }
    }

    companion object{
        const val TAG = "OtherSettingActivity"

        var loginOutListener: LoginOutListener? = null
    }

    override fun closeActivity() {
        finish()
    }

}









