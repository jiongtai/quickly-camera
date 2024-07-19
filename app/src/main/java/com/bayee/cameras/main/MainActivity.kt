package com.bayee.cameras.main

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.angcyo.tablayout.delegate2.ViewPager2Delegate
import com.bayee.cameras.App.ThisApp
import com.bayee.cameras.App.ThisApp.Companion.userInfo
import com.bayee.cameras.R
import com.bayee.cameras.activity.base.BaseViewModelActivity
import com.bayee.cameras.activity.photographActivity.CameraActivity
import com.bayee.cameras.activity.vipcenter.VipCenterActivity
import com.bayee.cameras.activity.watermontage.WaterMontageActivity
import com.bayee.cameras.databinding.ActivityMainBinding
import com.bayee.cameras.databinding.ItemTabBinding
import com.bayee.cameras.dialog.CameraSuccessDialogFragment
import com.bayee.cameras.dialog.ShareDialogFragment
import com.bayee.cameras.login.LoginActivity
import com.bayee.cameras.main.MainViewModel.Companion
import com.bayee.cameras.model.BaseViewModel
import com.bayee.cameras.superui.extension.shortToast
import com.bayee.cameras.util.Constant.WATER_TYPE
import com.bayee.cameras.util.Constant.WATER_TYPE_1_1
import com.bayee.cameras.util.MMKVUtils
import com.bayee.cameras.util.ToastUtil
import com.blankj.utilcode.util.NetworkUtils
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class MainActivity : BaseViewModelActivity<ActivityMainBinding>() {

    private lateinit var viewModel: MainViewModel

    private lateinit var appid_wx: String

    override fun initViews() {
        super.initViews()
        QMUIStatusBarHelper.translucent(this)
        initNavigationBottom()
//        startActivity(VipCenterActivity::class.java)
//        startActivity(WaterMontageActivity::class.java)
        Log.d(TAG, "initViews: ${MMKVUtils.getToken()}")
        Log.d(TAG, "initViews: ${MMKVUtils.isLogin()}")
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun initDatum() {
        super.initDatum()
        MainActivity.instance = this

        viewModel =
            ViewModelProvider(this).get(MainViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.userInfo.collect {
                ThisApp.userInfo = it
                if (ThisApp.userInfo!!.code != HttpURLConnection.HTTP_OK) {
                    "登录信息过期，请重新登录".shortToast()
                    if (LoginActivity.loginChangeListener != null) {
                        LoginActivity.loginChangeListener!!.closeLogin()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.payment.collect {
                appid_wx = it.data[0].app_id
                ThisApp.instance.initWechat(appid_wx)
                Log.d(TAG, "initDatum: $appid_wx")
            }
        }

        if (!NetworkUtils.isConnected()) {
            ToastUtil.show(this, "请检查你的网络", 200)
            return
        }else{
            viewModel.getPayment()
        }

//        //获取APP包信息
//        viewModel.getAppInfos()
//        //获取APP版本
//        viewModel.getAppVersions()
        loadingUserInfo()
    }

    fun loadingUserInfo() {
        if (MMKVUtils.isLogin()) {
            Log.d(TAG, "loadingUserInfo: ")
            viewModel.getUserInfo()
        }
    }


    //初始化底部导航栏
    private fun initNavigationBottom() {
        binding.content.apply {
            pager.offscreenPageLimit = indicatorTitles.size     //4个页面
            pager.adapter = MainAdapter(this@MainActivity, indicatorTitles.size)
            // 禁止ViewPager2的水平滑动
            pager.isUserInputEnabled = false
        }
        //底部tab
        for (i in indicatorTitles.indices) {
            ItemTabBinding.inflate(layoutInflater).apply {
                content.setText(indicatorTitles[i])
                icon.setImageResource(indicatorIcons[i])
                binding.content.indicator.addView(root)
            }
        }
        ViewPager2Delegate.install(binding.content.pager, binding.content.indicator, false)//联动
        initBottomClick()
    }

    private fun initBottomClick() {
        binding.content.indicator.configTabLayoutConfig {
            onSelectItemView = { itemView, index, select, fromUser ->
                Log.d(TAG, "initNavigationBottom:  $index ! $select ! $fromUser")
                // 当索引为1且select为false时，拦截并返回true
                if (index == 1 && select) {
                    val intent = Intent(this@MainActivity, CameraActivity::class.java)
                    intent.putExtra(WATER_TYPE, WATER_TYPE_1_1)
                    startActivity(intent)
                    true
                } else {
                    false
                }
            }
        }
    }

    fun setBottomIndex(index: Int) {
        binding.content.pager.setCurrentItem(index, false)
    }

    companion object {

        private const val TAG = "MainActivity"

        lateinit var instance: MainActivity

        private val indicatorTitles =
            intArrayOf(
                R.string.home,
                R.string.photograph,
                R.string.record,
                R.string.me
            )
        private val indicatorIcons = intArrayOf(
            R.drawable.selector_tab_home,
            R.drawable.selector_tab_photograph,
            R.drawable.selector_tab_record,
            R.drawable.selector_tab_me
        )

    }

}