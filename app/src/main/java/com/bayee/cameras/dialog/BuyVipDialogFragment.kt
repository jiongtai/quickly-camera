package com.bayee.cameras.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bayee.cameras.activity.vipcenter.VipCenterActivity
import com.bayee.cameras.databinding.FragmentDialogBuyVipBinding

class BuyVipDialogFragment : DialogFragment() {

    private lateinit var binding : FragmentDialogBuyVipBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogBuyVipBinding.inflate(inflater, container, false)
        getDialog()!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initListener()
    }

    private fun initListener() {
        binding.buyvipBtnBuy.setOnClickListener {
            startActivity(Intent(context, VipCenterActivity::class.java))
        }

        binding.buyvipBtnExit.setOnClickListener {
            Log.d(TAG, "initListener: 点击了退出离开")
            dismiss()
        }
    }


    companion object{
        private const val TAG = "BuyVipDialogFragment"

        fun show(fragmentManager: FragmentManager) {
            val dialogFragment = BuyVipDialogFragment()
            dialogFragment.show(fragmentManager, "BuyVipDialogFragment")
        }
    }

}









