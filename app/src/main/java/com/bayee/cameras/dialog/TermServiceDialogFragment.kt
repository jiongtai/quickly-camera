package com.bayee.cameras.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bayee.cameras.R
import com.bayee.cameras.superui.process.SuperProcessUtil
import com.bayee.cameras.superui.util.SuperTextUtil
import org.xml.sax.XMLReader

class TermServiceDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dialog_term_service, container, false)
        getDialog()!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return view
    }

    override fun onResume() {
        super.onResume()
        initLink()
        initListener()
    }

    private fun initListener() {
        //同意按钮点击
        val primary = requireView().findViewById<TextView>(R.id.primary)
        primary.setOnClickListener {
            dismiss()
            Log.d(TAG, "initListener: 点击了同意")
        }

        //不同意按钮点击
        val disagree = requireView().findViewById<TextView>(R.id.disagree)
        disagree.setOnClickListener {
            dismiss()
            SuperProcessUtil.killApp()
        }
    }

    private fun initLink() {
//        val privacyPolicyUrl = "https://www.baidu.com"
//        val userAgreementUrl = "https://www.baidu.com/1"
//        val userAgreementUrl2 = "https://www.baidu.com/2"
//        val userAgreementUrl3 = "https://www.baidu.com/3"
//        val userAgreementUrl4 = "https://www.baidu.com/4"
//
//        val fullText = resources.getString(
//            com.bayee.cameras.R.string.term_service_privacy_content,
//            privacyPolicyUrl,
//            userAgreementUrl,
//            userAgreementUrl2,
//            userAgreementUrl3,
//            userAgreementUrl4
//        )
//
//        val findViewById = requireView().findViewById<TextView>(R.id.content)
//        findViewById.setText(Html.fromHtml(fullText, Html.FROM_HTML_MODE_COMPACT))


        val content = Html.fromHtml(getString(R.string.term_service_privacy_content))
        val realContent = requireView().findViewById<TextView>(R.id.content)
        realContent.text = content

        SuperTextUtil.setLinkColor(realContent, getColor(requireContext(), R.color.primary))

    }

    companion object{
        private const val TAG = "TermServiceDialogFragment"

        fun show(fragmentManager: FragmentManager) {
            val dialogFragment = TermServiceDialogFragment()
            dialogFragment.show(fragmentManager, "TermServiceDialogFragment")
        }
    }
}














