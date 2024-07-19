package com.bayee.cameras.me

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bayee.cameras.App.ThisApp
import com.bayee.cameras.contactus.ContactUsActivity
import com.bayee.cameras.databinding.MeFragmentBinding
import com.bayee.cameras.fragment.BaseViewModelFragment
import com.bayee.cameras.activity.othersetting.OtherSettingActivity
import com.bayee.cameras.activity.photographActivity.CameraActivity
import com.bayee.cameras.activity.vipcenter.VipCenterActivity
import com.bayee.cameras.dialog.LoginOutListener
import com.bayee.cameras.dialog.ShareDialogFragment
import com.bayee.cameras.login.LoginActivity
import com.bayee.cameras.login.LoginChangeListener
import com.bayee.cameras.login.LoginStatusChangedEvent
import com.bayee.cameras.main.MainActivity
import com.bayee.cameras.main.MainViewModel
import com.bayee.cameras.questionfeedback.QuestionFeedBackActivity
import com.bayee.cameras.superui.extension.shortToast
import com.bayee.cameras.util.MMKVUtils
import com.bayee.cameras.util.ToastUtil
import com.blankj.utilcode.util.NetworkUtils
import com.drake.channel.receiveEvent
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class MeFragment : BaseViewModelFragment<MeFragmentBinding>(), LoginOutListener {

    var isLogin = false

    private lateinit var viewModel: MainViewModel

    override fun initViews() {
        super.initViews()
        initAllLayout()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun initDatum() {
        super.initDatum()
        viewModel =
            ViewModelProvider(this).get(MainViewModel::class.java)
        initViewModel(viewModel)
    }

    override fun initListeners() {
        super.initListeners()
        //好友分享
        binding.linearLayout1.setOnClickListener {
            ShareDialogFragment.show(childFragmentManager)
        }
        //意见反馈
        binding.linearLayout2.setOnClickListener {
            val intent = Intent(requireContext(),QuestionFeedBackActivity::class.java)
            startActivity(intent)
        }
        //联系我们
        binding.linearLayout3.setOnClickListener {
            val intent = Intent(requireContext(),ContactUsActivity::class.java)
            startActivity(intent)
        }
        //其他设置
        binding.linearLayout4.setOnClickListener {
            val intent = Intent(requireContext(), OtherSettingActivity::class.java)
            startActivity(intent)
        }
        //点击登录
        binding.meTextviewLogin.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        //开通
        binding.meBtnLoginBuyvip.setOnClickListener {
            startActivity(Intent(requireContext(), VipCenterActivity::class.java))
        }

        binding.meBtnBuyvip.setOnClickListener {
            ToastUtil.show(requireContext(), "请先登录",300)
        }

        receiveEvent<LoginStatusChangedEvent> {
            isLogin = it.b
            Log.d(TAG, "initListeners: ${it.b}")
            initAllLayout()
        }

        lifecycleScope.launch {
            viewModel.userInfo.collect {
                ThisApp.userInfo = it
                updateMeUi()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        initInterface()
        if (NetworkUtils.isConnected()){
            if (MMKVUtils.isLogin()){
                viewModel.getUserInfo()
            }
        }else{
            ToastUtil.show(requireContext(),"请检查网络",300)
        }

    }

    private fun initInterface() {
        loginOutListener = this
    }

    private fun updateMeUi() {
        //手机号码
        if (MMKVUtils.isLogin()){
            Log.d(TAG, "updateMeUi: ${MMKVUtils.isLogin()}")
            if (ThisApp.userInfo != null){
                binding.meLoginTextview.text = ThisApp.userInfo!!.data.phone
                binding.meBtnBuyvip.visibility = if (ThisApp.userInfo!!.data.is_vip) View.INVISIBLE else View.VISIBLE
                if (ThisApp.userInfo!!.data.is_vip){
                    binding.meLoginTextview2.text = "你已成为VIP"+"\n"+ThisApp.userInfo!!.data.vip_expire_at
                    binding.meBtnLoginBuyvip.text = "续费VIP"
                }else{
                    binding.meLoginTextview2.text = "开通会员，水印拍照修改\n无限用"+" (体验次数:"+ThisApp.userInfo!!.data.free_time+")"
                }
                if (LoginActivity.loginChangeListener != null) {
                    LoginActivity.loginChangeListener!!.closeLogin()
                }
            }else{
                Log.d(TAG, "updateMeUi: 空用户")
            }
        }
    }

    private fun initAllLayout() {
        if (isLogin) {
            loginView(true)
            notLoginView(false)
        } else {
            loginView(false)
            notLoginView(true)
        }
    }


    private fun notLoginView(boolean: Boolean) {
        binding.meBackgroundNotLogin.visibility = boolean.let { if (it) View.VISIBLE else View.INVISIBLE }
        binding.meTextviewLogin.visibility = boolean.let { if (it) View.VISIBLE else View.INVISIBLE }
        binding.meTextviewLogin.isEnabled = boolean
        binding.meHead.visibility = boolean.let { if (it) View.VISIBLE else View.INVISIBLE }
        binding.appVersion.visibility = boolean.let { if (it) View.VISIBLE else View.INVISIBLE }
        binding.meCardNotLoginBackground.visibility = boolean.let { if (it) View.VISIBLE else View.INVISIBLE }
        binding.meCrown.visibility = boolean.let { if (it) View.VISIBLE else View.INVISIBLE }
        binding.meNotLoginTextview.visibility = boolean.let { if (it) View.VISIBLE else View.INVISIBLE }
        binding.meNotLoginTextview2.visibility = boolean.let { if (it) View.VISIBLE else View.INVISIBLE }
        binding.meBtnBuyvip.visibility = boolean.let { if (it) View.VISIBLE else View.INVISIBLE }
    }

    private fun loginView(boolean: Boolean) {
        binding.meBackgroundLogin.visibility = boolean.let { if (it) View.VISIBLE else View.INVISIBLE }
        binding.meCardLoginBackground.visibility = boolean.let { if (it) View.VISIBLE else View.INVISIBLE }
        binding.meHeadLogin.visibility = boolean.let { if (it) View.VISIBLE else View.INVISIBLE }
        binding.meLoginTextview.visibility = boolean.let { if (it) View.VISIBLE else View.INVISIBLE }
        binding.meBtnLoginBuyvip.visibility = boolean.let { if (it) View.VISIBLE else View.INVISIBLE }
        binding.meLoginTextview2.visibility = boolean.let { if (it) View.VISIBLE else View.INVISIBLE }
        Log.d(TAG, "123123:+ ${ThisApp.userInfo.toString()}")

    }

    companion object {
        const val TAG = "MeFragment"

        var loginOutListener: LoginOutListener? = null

        fun newInstance(): MeFragment {
            val args = Bundle()

            val fragment = MeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun closeActivity() {
        initAllLayout()

    }

}