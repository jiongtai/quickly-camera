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
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.bayee.cameras.App.ThisApp
import com.bayee.cameras.R
import com.bayee.cameras.api.repository.DefaultNetworkRepository
import com.bayee.cameras.databinding.FragmentDialogCancelAccountBinding
import com.bayee.cameras.databinding.FragmentDialogUpdateAppBinding
import com.bayee.cameras.main.MainViewModel
import com.bayee.cameras.superui.extension.longToast
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection

class CancelAccountDialogFragment : DialogFragment() {

    private lateinit var scope: LifecycleCoroutineScope
    private lateinit var binding: FragmentDialogCancelAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogCancelAccountBinding.inflate(inflater, container, false)
        getDialog()!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        scope = viewLifecycleOwner.lifecycleScope
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initListener()
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun initListener() {
        //agree_btn，click
        binding.cancelAccountAgree.setOnClickListener {
            Log.d(TAG, "initListener: cancelAccountAgree")
            GlobalScope.launch {
                cancelAccount()
            }
        }
        //disagree_btn，click
        binding.cancelAccountDisAgree.setOnClickListener {
            Log.d(TAG, "initListener: cancelAccountDisagree")
            dismiss()
        }
    }

    private suspend fun cancelAccount() {
        val result = DefaultNetworkRepository.cancelAccount()
        if (result.code == HttpURLConnection.HTTP_OK){
            ThisApp.instance.logout()
            delay(300)
            dismiss()
        }else{
            result.message.longToast()
        }
    }

    companion object{
        private const val TAG = "CancelAccountDialogFragment"

        fun show(fragmentManager: FragmentManager) {
            val dialogFragment = CancelAccountDialogFragment()
            dialogFragment.show(fragmentManager, "CancelAccountDialogFragment")
        }
    }

}
















