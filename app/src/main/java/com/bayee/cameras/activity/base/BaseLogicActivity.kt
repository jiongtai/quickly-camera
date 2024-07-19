package com.bayee.cameras.activity.base


import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.bayee.cameras.R
import com.bayee.cameras.dialog.NetWorkErrorDialogFragment
import com.bayee.cameras.api.bean.response.BaseResponse
import com.bayee.cameras.model.BaseViewModel
import com.bayee.cameras.superui.extension.longToast
import com.bayee.cameras.superui.extension.shortToast
import com.bayee.cameras.superui.util.SuperDarkUtil
import com.bayee.cameras.util.ToastUtil
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 本项目的通用逻辑，例如：背景颜色等
 */
open class BaseLogicActivity : BaseCommonActivity() {

    /**
     * 获取界面方法
     *
     * @return
     */
    protected val hostActivity: BaseLogicActivity
        protected get() = this

    override fun initViews() {
        super.initViews()
        if (SuperDarkUtil.isDark(this)) {
            //状态栏文字白色
            QMUIStatusBarHelper.setStatusBarDarkMode(this)
        } else {
            //状态栏文字黑色
            QMUIStatusBarHelper.setStatusBarLightMode(this)
        }
    }

    /**
     * 初始化通用ViewModel逻辑
     */
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    protected fun initViewModel(viewModel: BaseViewModel) {
        //关闭界面
        viewModel.finishPage.observe(this) {
            finish()
        }

        //本地提示
        viewModel.tip.observe(this) {
            onTip(it)
        }

        //异常
        viewModel.exception.observe(this) {
            Log.d("异常", "initViewModel: ${it.message}")
            onException(it)
        }

        //网络响应业务失败
        viewModel.response.observe(this) {
            onResponse(it)
        }

        //网络响应业务失败,字符串
        viewModel.response3.observe(this) {
            onResponse3(it)
        }

        //加载提示
        viewModel.loading.observe(this) {
//            if (StringUtils.isNotBlank(it)) showLoading(it) else hideLoading()
        }
    }

    open fun onTip(data: Int) {
        data.shortToast()
        onError()
    }

    open fun onResponse(data: BaseResponse) {
        when (data.code) {
            401 -> {
                R.string.error_not_auth.longToast()
//                AppContext.instance.logout()
            }

            403 -> {
                R.string.error_not_permission.longToast()
            }

            404 -> {
                R.string.error_not_found.longToast()
            }
        }
        (data.message ?: getString(R.string.error_unknown)).longToast()
        onError()
    }

    open fun onResponse2(code: Int) {
        when (code) {
            401 -> {
                R.string.error_not_auth.longToast()
//                AppContext.instance.logout()
            }

            403 -> {
                R.string.error_not_permission.longToast()
            }

            404 -> {
                R.string.error_not_found.longToast()
            }
            else ->{
                ToastUtil.show(this, code.toString(),300)
            }
        }
        onError()
    }

    open fun onResponse3(message: String) {
        message.shortToast()
        onError()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    open fun onException(data: Throwable) {
//        if (NetworkUtils.isAvailableByPing()) {
            //有网络
//            R.string.error_load.longToast()
//        } else {
            //提示，你的网络好像不太好
//            R.string.error_network_not_connect.longToast()
//        }

//        when (data) {
//            is SocketException -> {
                //例如：服务器没有启动
//                R.string.error_connect_server.longToast()
//            }

//            is UnknownHostException -> {
//                域名无法解析，例如：域名写错了
//                R.string.error_unknown_host.longToast()
//            }

//            is SocketTimeoutException -> {
                //连接超时，例如：网络特别慢
//                R.string.error_network_timeout.longToast()
//            }

//            is ConnectException -> {
                //以下情况都会触发该异常：
                //服务器没有开启
                //本地网络关闭
//                if (SuperNetworkUtil.isNetworkConnected(hostActivity)) {
                    //本地有网络

                    //提示连接服务端失败
//                    R.string.error_connect_server.longToast()
//                } else {
                    //本地没有网络

                    //提示，你的网络好像不太好
//                    R.string.error_network_not_connect.longToast()
//                }
//            }

//            is HttpException -> {
//                Log.d("把我怕", "onException: ${data.message}")
//                //http异常，例如：服务端返回401，403
//                handleHttpError(data)
//            }

//            is IllegalArgumentException -> {
//                //本地参数错误
//                R.string.error_illegal_argument.shortToast()
//            }
//            is ClientException -> {
//                "阿里云OSS客户端错误：${data.localizedMessage}".longToast()
//            }
//            is ServiceException -> {
//                "阿里云OSS服务端错误：${data.localizedMessage}".longToast()
//            }
//            else -> {
//                R.string.error_unknown.shortToast()
//            }
//        }

        onError()
    }

    private fun handleHttpError(data: HttpException) {
//        AppContext.instance.getString(R.string.error_server_unknown_code, data.code()).shortToast()
    }

    open fun onError() {

    }

    /**
     * 只要用户登录了，才会执行代码块
     *
     * @param data
     */
    fun loginAfter(data: Runnable) {
//        if (PreferenceUtil.isLogin()) {
//            //已经登录了
//            data.run()
//        } else {
//            hostActivity.toLogin()
//        }
    }

    fun toLogin() {
//        startActivity(LoginHomeActivity::class.java)
    }

    //region 加载提示
    /**
     * 显示加载对话框
     */
//    open fun showLoading(data: Int) {
//        showLoading(getString(data))
//    }

    /**
     * 显示加载对话框
     */
//    open fun showLoading(data: String = getString(R.string.loading)) {
//        Log.d("TAG", "showLoading: " + data)
////        if (loadingDialog == null || loadingDialog!!.get() == null) {
////            loadingDialog = WeakReference(
////                IOSLoadingDialog()
////                    .setOnTouchOutside(false)
////
////            )
////        }
//
////        val dialogData = loadingDialog!!.get()
////        dialogData?.setHintMsg(data)
////        if (dialogData!!.dialog == null || !dialogData!!.dialog!!.isShowing) {
////            dialogData!!.show(supportFragmentManager, "LoadingDialog")
////        }
//    }

    /**
     * 隐藏加载对话框
     */
    fun hideLoading() {
//        loadingDialog?.get()?.dismiss()
    }
    //endregion

    /**
     * 获取数据库管理器
     *
     * @return
     */
//    protected open fun getOrm(): LiteORMUtil {
//        return LiteORMUtil.getInstance(hostActivity)
//    }

    //region 统计
    /**
     * 当界面显示了
     */
    override fun onResume() {
        super.onResume()

    }

    /**
     * 当页面暂停了
     * 例如：弹窗；或者切换到后台
     */
    override fun onPause() {
        super.onPause()

    }

    /**
     * 返回页面标识
     * @return
     */
    protected open fun pageId(): String? {
        return null
    }
    //endregion
}