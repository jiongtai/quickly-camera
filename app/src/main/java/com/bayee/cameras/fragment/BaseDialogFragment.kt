package com.bayee.cameras.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bayee.cameras.R

/**
 * 所有DialogFragment对话框父类
 */
abstract class BaseDialogFragment : DialogFragment() {
    /**
     * 找控件
     */
    protected open fun initViews() {}

    /**
     * 设置数据
     */
    protected open fun initDatum() {}

    /**
     * 设置监听器
     */
    protected open fun initListeners() {}

    /**
     * 返回要显示的控件
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //获取view
        val view = getLayoutView(inflater, container, savedInstanceState)
        view!!.background = ContextCompat.getDrawable(requireContext(), R.drawable.dialog_background);
        //返回view
        return view
    }

    fun intArrayToFloatArray(intArray: IntArray): FloatArray {
        val floatArray = FloatArray(intArray.size)
        for (i in intArray.indices) {
            floatArray[i] = intArray[i].toFloat()
        }
        return floatArray
    }
    open abstract fun getLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?

    /**
     * View创建了
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initDatum()
        initListeners()
    }

    fun <T : View?> findViewById(@IdRes id: Int): T {
        return requireView().findViewById(id)
    }
}