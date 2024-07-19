package com.bayee.cameras.contactus

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bayee.cameras.activity.base.BaseTitleActivity
import com.bayee.cameras.databinding.ActivityContactUsBinding
import com.bayee.cameras.dialog.NetWorkErrorDialogFragment
import com.bayee.cameras.util.Constant
import kotlinx.coroutines.launch

class ContactUsActivity : BaseTitleActivity<ActivityContactUsBinding>() {

    private lateinit var viewModel: ContactUsViewModel

    private var onlineSupportLink = ""

    override fun initViews() {
        super.initViews()
        binding.contactUsToolbar.toolbarTextCenter.text = "联系我们"
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun initDatum() {
        super.initDatum()

        viewModel =
            ViewModelProvider(this).get(ContactUsViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.link.collect{
                Log.d(TAG, "请求回调连接: ${it.data}")
                onlineSupportLink = it.data
                val intent = Intent(this@ContactUsActivity, ContactUsWebViewActivity::class.java)
                intent.putExtra(Constant.ONLINE_SUPPORT_LINK, onlineSupportLink)
                startActivity(intent)
            }
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.contactUsBtnEntry.setOnClickListener {
            viewModel.loadOnlineSupportLink()
        }
    }

    companion object{
        const val TAG = "ContactUsActivity"
    }

    override fun onError() {
        super.onError()
        NetWorkErrorDialogFragment.show(supportFragmentManager)
    }




}