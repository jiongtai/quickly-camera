package com.bayee.cameras.activity.home.content

import android.os.Bundle
import com.bayee.cameras.databinding.FragmentWatermarkContentBinding
import com.bayee.cameras.fragment.BaseViewModelFragment
import com.bayee.cameras.util.Constant

class ContentFragment : BaseViewModelFragment<FragmentWatermarkContentBinding>() {




    companion object {
        const val TAG = "ContentFragment"

        fun newInstance(
            categoryId: String? = null,
            index: Int = Constant.VALUE_NO
        ): ContentFragment {
            val args = Bundle()
            args.putInt(Constant.STYLE, index)  //存index, key:Constant.STYLE,
            categoryId?.let {
                args.putString(Constant.ID, it) //存categoryId, key:Constant.ID
            }

            val fragment = ContentFragment()
            fragment.arguments = args
            return fragment
        }
    }
}