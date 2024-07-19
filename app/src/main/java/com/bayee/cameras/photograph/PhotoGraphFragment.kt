package com.bayee.cameras.photograph

import android.os.Bundle
import com.bayee.cameras.databinding.PhotographFragmentBinding
import com.bayee.cameras.fragment.BaseViewModelFragment

class PhotoGraphFragment : BaseViewModelFragment<PhotographFragmentBinding>(){


    companion object {
        fun newInstance(): PhotoGraphFragment {
            val args = Bundle()

            val fragment = PhotoGraphFragment()
            fragment.arguments = args
            return fragment
        }
    }
}