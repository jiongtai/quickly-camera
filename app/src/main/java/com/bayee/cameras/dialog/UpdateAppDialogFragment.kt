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
import com.bayee.cameras.databinding.FragmentDialogUpdateAppBinding

class UpdateAppDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogUpdateAppBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogUpdateAppBinding.inflate(inflater, container, false)
        getDialog()!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initListener()
    }

    private fun initListener() {
        binding.updateAppX.setOnClickListener{
            this.dismiss()
        }

    }


    companion object{
        private const val TAG = "UpdateAppDialogFragment"

        fun show(fragmentManager: FragmentManager) {
            val dialogFragment = UpdateAppDialogFragment()
            dialogFragment.show(fragmentManager, "UpdateAppDialogFragment")
        }
    }

}