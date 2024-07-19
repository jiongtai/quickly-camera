package com.bayee.cameras.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bayee.cameras.R
import com.bayee.cameras.activity.photographActivity.CameraActivity
import com.bayee.cameras.databinding.FragmentDialogCameraSuccessBinding
import com.bayee.cameras.databinding.FragmentDialogUpdateAppBinding

class CameraSuccessDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogCameraSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogCameraSuccessBinding.inflate(inflater, container, false)
        getDialog()!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initListener()
    }

    private fun initListener() {
        binding.btnCameraSuccContinue.setOnClickListener {
            this.dismiss()
        }
        binding.btnCameraSuccCheck.setOnClickListener {
            if (CameraActivity.successCameraListener != null) {
                CameraActivity.successCameraListener!!.cameraSuccess()
            }
        }
    }


    companion object{
        private const val TAG = "CameraSuccessDialogFragment"

        fun show(fragmentManager: FragmentManager) {
            val dialogFragment = CameraSuccessDialogFragment()
            dialogFragment.show(fragmentManager, "CameraSuccessDialogFragment")
        }
    }

}