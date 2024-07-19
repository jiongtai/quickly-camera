package com.bayee.cameras.splash

import com.bayee.cameras.activity.base.BaseViewModelActivity
import com.bayee.cameras.databinding.ActivitySplashBinding
import com.bayee.cameras.dialog.TermServiceDialogFragment
import com.bayee.cameras.superui.util.SuperDarkUtil

import com.qmuiteam.qmui.util.QMUIStatusBarHelper

/**
 * 启动界面
 */
class SplashActivity : BaseViewModelActivity<ActivitySplashBinding>() {
    override fun initViews() {
        super.initViews()
        //设置沉浸式状态栏
        QMUIStatusBarHelper.translucent(this)

        if (SuperDarkUtil.isDark(this)) {
            //状态栏文字白色
            QMUIStatusBarHelper.setStatusBarDarkMode(this)
        } else {
            //状态栏文字黑色
            QMUIStatusBarHelper.setStatusBarLightMode(this)
        }
    }

    override fun initDatum() {
        super.initDatum()

//        if (DefaultPreferenceUtil.getInstance(this).isAcceptTermsServiceAgreement) {
//            //已经同意了用户协议
//            requestPermission()
//        } else {
            showTermsServiceAgreementDialog()
//        }

    }

//    private fun requestPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            PermissionX.init(this).permissions(
//                Manifest.permission.CAMERA,
//                Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.READ_MEDIA_AUDIO,
//                Manifest.permission.READ_MEDIA_IMAGES,
//                Manifest.permission.READ_MEDIA_VIDEO,
//            )
//        } else {
//            PermissionX.init(this).permissions(
//                Manifest.permission.CAMERA,
//                Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//            )
//        }.request { allGranted, grantedList, deniedList ->  //allGranted: 一个布尔值，表示是否所有的权限都已经授予了。
//                                                            //grantedList: 一个列表，包含了用户已经授予的权限。
//                                                            //deniedList: 一个列表，包含了用户拒绝的权限。
//            if (allGranted) {
//                binding.root.postDelayed({
//                    prepareNext()
//                }, 1000)
//            } else {
//                //可以在这里弹出提示告诉用户为什么需要权限
//                finish()
//            }
//        }
//    }

//    private fun prepareNext() {
//        Log.d(TAG, "prepareNext: ")
//        AppContext.instance.onInit()
//
//        if (PreferenceUtil.isShowGuide()) {
//            startActivityAfterFinishThis(GuideActivity::class.java)
//            return
//        }
//
//        val intent = Intent()
//        if (PreferenceUtil.isLogin()) {
//            intent.setClass(hostActivity, AdActivity::class.java)
//        } else {
//            intent.setClass(hostActivity, MainActivity::class.java)
//        }
//
//        IntentUtil.cloneIntent(getIntent(), intent)
//
//        startActivity(intent)
//
//        //关闭当前界面
//        finish()
//
//        //禁用启动动画
//        overridePendingTransition(0, 0)
//    }

    private fun showTermsServiceAgreementDialog() {
        TermServiceDialogFragment.show(supportFragmentManager)
    }


    override fun pageId(): String? {
        return "Splash"
    }

    companion object {
        const val TAG = "SplashActivity"
    }
}