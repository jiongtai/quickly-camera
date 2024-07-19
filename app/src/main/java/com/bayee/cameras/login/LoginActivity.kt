package com.bayee.cameras.login

import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bayee.cameras.activity.base.BaseViewModelActivity
import com.bayee.cameras.activity.photographActivity.interfaces.SuccessCameraListener
import com.bayee.cameras.api.bean.PhoneCodeSms
import com.bayee.cameras.databinding.ActivityLoginBinding
import com.bayee.cameras.dialog.LoginOutListener
import com.bayee.cameras.superui.extension.shortToast
import com.bayee.cameras.util.Constant
import com.bayee.cameras.util.IpUtils
import com.drake.channel.sendEvent
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : BaseViewModelActivity<ActivityLoginBinding>() , LoginChangeListener{

    private lateinit var viewModel: LoginViewModel

    private lateinit var phoneCodeSms: PhoneCodeSms

    private var countdownTimer: CountDownTimer? = null

    private var btn_drgee = false
    private var btn_drgee2 = true

    override fun initViews() {
        super.initViews()
        QMUIStatusBarHelper.translucent(this)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun initDatum() {
        super.initDatum()
        viewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)
        initViewModel(viewModel)
        initFlowOrLiveData()
        initInterface()
    }

    private fun initInterface() {
        loginChangeListener = this
    }

    private fun initFlowOrLiveData() {
        lifecycleScope.launch {
            viewModel.code.collect{
                phoneCodeSms = it
                Log.d(TAG, "外层验证码: ${phoneCodeSms.data!!.sms_code}")
                Log.d(TAG, "loginmessage外层验证码: ${phoneCodeSms.data!!.sms_code}")
                startCountdown()
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect{
                if (it == 0){
                    "验证码错误".shortToast()
                }else{
                    "登录成功".shortToast()
                    sendEvent(LoginStatusChangedEvent(true))
                    finish()
                }
            }
        }
    }

    override fun initListeners() {
        super.initListeners()
        clickAgreeTerm()
        clickGetCode()
        clickPrimary()
        clickBack()
    }

    private fun clickBack() {
        binding.loginBack.setOnClickListener {
            finish()
        }
    }

    private  fun clickPrimary() {
        binding.buttonLogin.setOnClickListener {
            val phone = binding.editTextPhone.text.toString()
            val inputCode = binding.editTextCode.text.toString()
            val from_project = Constant.FROM_PROJECT_1001
            val app_store = Constant.APP_STORE_HUAWEI
            val ip = IpUtils.getLocalIpAddress(this)
            val oaid = "oaid"
            val version = Constant.VERSIONCODE_1001
            Log.d(TAG, "clickPrimary: $ip")
            viewModel.login(
                phone,
                inputCode,
                from_project,
                app_store,
                ip,
                oaid,
                version
            )
        }
    }

    private fun clickGetCode() {
        binding.buttonGetCode.setOnClickListener {
            if (!btn_drgee){
                "请勾选阅读并同意 《用户协议》 《隐私政策》".shortToast()
                return@setOnClickListener
            }
            val phone = binding.editTextPhone.text.toString()
            when(phone.length){
                0 -> "请输入手机号".shortToast()
                11 -> viewModel.getCode(phone)
//                11 ->  startCountdown()
                else -> "手机号位数不合法".shortToast()
            }
        }
    }

    private fun clickAgreeTerm() {
        binding.btnDrgee.setOnClickListener {
            btn_drgee = !btn_drgee
            btn_drgee2 = !btn_drgee2
            updateIsAgree()
        }
    }

    private fun updateIsAgree() {
        binding.btnDrgee.isChecked = btn_drgee
        binding.btnDrgee2.isChecked = btn_drgee2

    }

    private fun startCountdown() {
        binding.currentTime.visibility = View.VISIBLE
        binding.buttonGetCode.text = "已发送"
        binding.buttonGetCode.isEnabled = false

        // 假设60秒倒计时
        countdownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // 更新按钮文本为剩余秒数
                binding.currentTime.text = "${millisUntilFinished / 1000}S"
            }
            override fun onFinish() {
                // 倒计时结束，恢复按钮状态
                binding.buttonGetCode.text = "获取验证码"
                binding.buttonGetCode.isEnabled = true
                binding.currentTime.visibility = View.INVISIBLE
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countdownTimer?.cancel()
    }

    companion object {
        private const val TAG = "LoginActivity"

        var loginChangeListener: LoginChangeListener? = null
        var loginOutListener: LoginOutListener? = null
    }

    override fun closeLogin() {
        finish()
    }




}









