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
import com.bayee.cameras.R
import com.bayee.cameras.databinding.FragmentDialogCancelAccountBinding
import com.bayee.cameras.databinding.FragmentDialogDeleteConfirmBinding
import com.bayee.cameras.databinding.FragmentDialogLoginOutBinding
import com.bayee.cameras.dialog.CancelAccountDialogFragment.Companion
import com.bayee.cameras.record.RecordFragment

class DeleteDialogFragment : DialogFragment() {

    private lateinit var  binding : FragmentDialogDeleteConfirmBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogDeleteConfirmBinding.inflate(inflater, container, false)
        getDialog()!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DeleteConfirmDialogTheme)
    }

    override fun onResume() {
        super.onResume()
        initListener()
    }

    private fun initListener() {
        //agree_btn，click
        binding.cancelAccountAgree.setOnClickListener {
            Log.d(TAG, "initListener: 确认删除")
            RecordFragment.deleteListener!!.agreeDelete()
            dismiss()
        }
        //disagree_btn，click
        binding.cancelAccountDisAgree.setOnClickListener {
            Log.d(TAG, "initListener: 取消")
            RecordFragment.deleteListener!!.disAgreeDelete()
            dismiss()
        }
    }

    companion object{
        private const val TAG = "LoginOutDialogFragment"

        fun show(fragmentManager: FragmentManager) {
            val dialogFragment = DeleteDialogFragment()
            dialogFragment.show(fragmentManager, "LoginOutDialogFragment")
        }
    }
}










