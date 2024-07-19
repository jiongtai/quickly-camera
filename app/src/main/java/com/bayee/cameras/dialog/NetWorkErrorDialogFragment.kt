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

class NetWorkErrorDialogFragment : DialogFragment()  {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_network_error, container, false)
        getDialog()!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return view
    }

    companion object{
        private const val TAG = "NetWorkErrorDialogFragment"

        fun show(fragmentManager: FragmentManager) {
            val dialogFragment = NetWorkErrorDialogFragment()
            dialogFragment.show(fragmentManager, "NetWorkErrorDialogFragment")
        }
    }

}












