package com.bayee.cameras.fragment

import android.os.Build
import androidx.annotation.RequiresExtension
import com.bayee.cameras.activity.base.BaseLogicActivity
import com.bayee.cameras.api.bean.response.BaseResponse
import com.bayee.cameras.model.BaseViewModel

abstract class BaseLogicFragment : BaseCommonFragment() {
    /**
     * 获取界面方法
     *
     * @return
     */
    protected val hostActivity: BaseLogicActivity
        protected get() = requireActivity() as BaseLogicActivity


    /**
     * 初始化通用ViewModel逻辑
     */
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    protected fun initViewModel(viewModel: BaseViewModel) {
        //关闭界面
        viewModel.finishPage.observe(this) {
            hostActivity.finish()
        }

        //本地提示
        viewModel.tip.observe(this) {
            onTip(it)
        }

        //异常
        viewModel.exception.observe(this) {
            onException(it)
        }

        //网络响应业务失败
        viewModel.response.observe(this) {
            onResponse(it)
        }

        //网络响应业务失败2
        viewModel.response2.observe(this) {
            onResponse2(it)
        }

        //网络响应业务失败3
        viewModel.response3.observe(this) {
            onResponse3(it)
        }

        //加载提示
        viewModel.loading.observe(this) {
        }
    }

    open fun onTip(data: Int) {
        hostActivity.onTip(data)
        onError()
    }

    open fun onResponse(data: BaseResponse) {
        hostActivity.onResponse(data)
        onError()
    }
    open fun onResponse2(code : Int) {
        hostActivity.onResponse2(code)
        onError()
    }

    open fun onResponse3(message : String) {
        hostActivity.onResponse3(message)
        onError()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    open fun onException(data: Throwable) {
        hostActivity.onException(data)
        onError()
    }

    open fun onError() {

    }

    /**
     * 只要用户登录了，才会执行代码块
     *
     * @param data
     */
    protected fun loginAfter(data: Runnable) {
        hostActivity.loginAfter(data)
    }

    fun toLogin() {
        hostActivity.toLogin()
    }

    //region 统计
    /**
     * 当界面显示了
     */
    override fun onResume() {
        super.onResume()
//        pageId()?.let {
//            //使用极光分析
//            //统计页面
//            JAnalyticsInterface.onPageStart(hostActivity, it)
//        }
    }

    /**
     * 当页面暂停了
     * 例如：弹窗；或者切换到后台
     */
    override fun onPause() {
        super.onPause()
//        pageId()?.let {
//            //页面结束
//            JAnalyticsInterface.onPageEnd(hostActivity, it)
//        }
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