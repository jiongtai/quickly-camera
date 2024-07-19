package com.bayee.cameras.activity.vipcenter

import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresExtension
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bayee.cameras.App.ThisApp
import com.bayee.cameras.R
import com.bayee.cameras.activity.base.BaseTitleActivity
import com.bayee.cameras.activity.home.banner.BannerAdapter
import com.bayee.cameras.api.bean.receive.PaymentResponse
import com.bayee.cameras.api.bean.receive.VipData
import com.bayee.cameras.api.bean.receive.VipDataListResponse
import com.bayee.cameras.databinding.ActivityVipCenterBinding
import com.bayee.cameras.model.BaseViewModel
import com.bayee.cameras.superui.extension.shortToast
import com.bayee.cameras.util.DataUtil
import com.bayee.cameras.util.PayUtil
import com.bayee.cameras.util.ToastUtil
import com.bayee.cameras.wxapi.WechatPayStatusChangedEvent
import com.blankj.utilcode.util.NetworkUtils
import com.drake.channel.receiveEvent
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tencent.mm.opensdk.modelbase.BaseResp
import kotlinx.coroutines.launch

class VipCenterActivity : BaseTitleActivity<ActivityVipCenterBinding>() {

    private lateinit var adapter: BannerAdapter

    private lateinit var viewModel: VipCenterViewModel

    private var btn_1 = true
    private var btn_2 = false
    private var btn_3 = false

    private var selectZfb = true
    private lateinit var payNoSelected: Drawable
    private lateinit var paySelected: Drawable
    private lateinit var ivZfbPay: ImageView
    private lateinit var ivWxPay: ImageView
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var lineZfb: LinearLayout
    private lateinit var lineWx: LinearLayout
    private lateinit var tvZfbPrice: TextView
    private lateinit var tvWxPrice: TextView
    private lateinit var btnConfirmPay: AppCompatButton

    private var mVipDataList: VipDataListResponse? = null
    lateinit var mPaymentList: PaymentResponse
    lateinit var mVipData: VipData

    private lateinit var appid_wx: String

    override fun initViews() {
        super.initViews()
        initWaterAdapter()
        initPrivilegeAdapter()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun initDatum() {
        super.initDatum()
        viewModel =
            ViewModelProvider(this).get(VipCenterViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.vipDataList.collect {
                mVipDataList = it
                Log.d(TAG, "initDatum: ${mVipDataList.toString()}")
                updateVipDataListUi()
                updateSailBtnUi()
            }
        }

        lifecycleScope.launch {
            viewModel.payment.collect {
                mPaymentList = it
                Log.d(BaseViewModel.TAG, "getPayment: ${mPaymentList.data[0].app_id}")
                appid_wx = mPaymentList.data[0].app_id
//                ThisApp.instance.initWechat(appid_wx)
                updatePaymentUi()
            }
        }

        lifecycleScope.launch {
            viewModel.processAlipay.collect { data ->
                PayUtil.alipay(hostActivity, data)
            }
        }

        //支付宝支付状态改变了
        receiveEvent<AlipayStatusChangedEvent> {
            prepareAlipayStatusChanged(it)
        }

        //微信支付状态改变了
        receiveEvent<WechatPayStatusChangedEvent> {
            processWechatPayStatusChanged(it)
        }

        if (NetworkUtils.isConnected()){
            viewModel.getVipPrices()
            viewModel.getPayment()
        }else{
            ToastUtil.show(this,"请检查网络以获取VIP信息",300)
        }

        initBanner()
        initPayResource()
    }

    //支付宝支付状态改变了
    private fun prepareAlipayStatusChanged(data: AlipayStatusChangedEvent) {
        val resultStatus = data.data.resultStatus!!
        if ("9000" == resultStatus) {
            "支付成功".shortToast()
            bottomSheetDialog.dismiss()
            finish()
        } else if ("6001" == resultStatus) {
            //支付取消
            R.string.error_pay_cancel.shortToast()
        } else {
            //支付失败
            R.string.error_pay_failed.shortToast()
        }
    }

    //微信支付状态改变了
    private fun processWechatPayStatusChanged(event: WechatPayStatusChangedEvent) {
        if (BaseResp.ErrCode.ERR_OK === event.data.errCode) {
            //本地支付成功

        } else if (BaseResp.ErrCode.ERR_USER_CANCEL === event.data.errCode) {
            //支付取消
            R.string.error_pay_cancel.shortToast()

        } else {
            //支付失败
            R.string.error_pay_failed.shortToast()

        }
    }

    private fun updatePaymentUi() {
        //支付宝行
        val zfbPaymentMethod = mPaymentList.data.find { it.payment == "aliPay" }
        val zfbPayEnable = zfbPaymentMethod?.enable ?: false // 如果找不到，则默认返回 false
        Log.d(TAG, "updatePaymentUi: $zfbPayEnable")
        lineZfb.visibility = if (zfbPayEnable) View.VISIBLE else View.GONE
        //微信行
        val wxPaymentMethod = mPaymentList.data.find { it.payment == "wxPay" }
        val wxPayEnable = wxPaymentMethod?.enable ?: false // 如果找不到，则默认返回 false
        Log.d(TAG, "updatePaymentUi: $wxPayEnable")
        lineWx.visibility = if (wxPayEnable) View.VISIBLE else View.GONE
    }


    private fun initPrivilegeAdapter() {
        val layoutManager = GridLayoutManager(this, 2)
        binding.recycleview2.setLayoutManager(layoutManager)
        binding.recycleview2.adapter = VipPrivilegeAdapter(DataUtil.vip_Privilege)
    }

    private fun initWaterAdapter() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recycleview.setLayoutManager(layoutManager)
        binding.recycleview.adapter = VipWaterAdapter(DataUtil.vip_center)
    }

    private fun initPayResource() {
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.vip_bottom_dialog)
        lineZfb = bottomSheetDialog.findViewById<LinearLayout>(R.id.vip_line_zfb)!!
        lineWx = bottomSheetDialog.findViewById<LinearLayout>(R.id.vip_line_wx)!!
        ivZfbPay = bottomSheetDialog.findViewById<ImageView>(R.id.vip_icon_zfb_pay)!!
        ivWxPay = bottomSheetDialog.findViewById<ImageView>(R.id.vip_icon_wx_pay)!!
        tvZfbPrice = bottomSheetDialog.findViewById<TextView>(R.id.vip_tv_zfb_price)!!
        tvWxPrice = bottomSheetDialog.findViewById<TextView>(R.id.vip_tv_wx_price)!!
        btnConfirmPay = bottomSheetDialog.findViewById<AppCompatButton>(R.id.vip_bottom_btn)!!
        payNoSelected = ContextCompat.getDrawable(this, R.drawable.vip_icon_no)!!
        paySelected = ContextCompat.getDrawable(this, R.drawable.vip_icon_yes)!!
    }


    override fun initListeners() {
        super.initListeners()
        clickListener()
        initBottomBtnListener()
    }

    private fun clickListener() {
        initSailListener()
        initPayListener()
        initPrimaryListener()
    }

    private fun initPrimaryListener() {
        btnConfirmPay.setOnClickListener {
            if (NetworkUtils.isConnected()){
                viewModel.primary()
            }else{
                ToastUtil.show(this,"请检查网络以获取VIP信息",300)
            }
        }
    }

    private fun initPayListener() {
        updatePayUi()
        lineZfb.setOnClickListener {
            selectZfb = true
            viewModel.setChannel(0)
            updatePayUi()
        }
        lineWx.setOnClickListener {
            selectZfb = false
            viewModel.setChannel(1)
            updatePayUi()
        }
    }

    private fun updatePayUi() {
        if (selectZfb) {
            ivZfbPay.setImageDrawable(paySelected)
            ivWxPay.setImageDrawable(payNoSelected)
        } else {
            ivZfbPay.setImageDrawable(payNoSelected)
            ivWxPay.setImageDrawable(paySelected)
        }
    }

    private fun initBottomBtnListener() {
        binding.vipBottomBtn.setOnClickListener {
            bottomSheetDialog.show()
        }
    }

    private fun initSailListener() {
        updateSailBtnUi()
        binding.btnVipcenterBtn1.setOnClickListener {
            btn_2 = false
            btn_3 = false
            btn_1 = true
            updateSailBtnUi()
        }
        binding.btnVipcenterBtn2.setOnClickListener {
            btn_1 = false
            btn_3 = false
            btn_2 = true
            updateSailBtnUi()
        }
        binding.btnVipcenterBtn3.setOnClickListener {
            btn_1 = false
            btn_2 = false
            btn_3 = true
            updateSailBtnUi()
        }
    }

    private fun updateSailBtnUi() {
        binding.btnVipcenterBtn1.isChecked = btn_1
        binding.btnVipcenterBtn2.isChecked = btn_2
        binding.btnVipcenterBtn3.isChecked = btn_3
        //改变当前选中的VIP套餐
        if (mVipDataList != null) {
            mVipData = if (btn_1) {
                mVipDataList!!.data[0]
            } else if (btn_2) {
                mVipDataList!!.data[1]
            } else {
                mVipDataList!!.data[2]
            }
            updatePayPriceUi()
            viewModel.setPayId(mVipData.id)
            Log.d(TAG, "updateSailBtnUi: ${mVipData.toString()}")
        }
    }

    private fun updatePayPriceUi() {
        if (lineZfb.visibility == View.VISIBLE) {
            tvZfbPrice.text = mVipData.vipPrice.toString()
        }
        if (lineWx.visibility == View.VISIBLE) {
            tvWxPrice.text = mVipData.vipPrice.toString()
        }
    }

    private fun updateVipDataListUi() {
        initFirst()
        initSecond()
        initThird()
    }

    private fun initThird() {
        val vip_name = mVipDataList!!.data[2].vipName
        val vip_price = mVipDataList!!.data[2].vipPrice
        val vip_desc = mVipDataList!!.data[2].vipDesc
        binding.vipName3.text = vip_name
        binding.vipPrice3.text = "¥" + vip_price.toString()
        binding.vipDesc3.text = vip_desc
    }

    private fun initSecond() {
        val vip_name = mVipDataList!!.data[1].vipName
        val vip_price = mVipDataList!!.data[1].vipPrice
        val vip_desc = mVipDataList!!.data[1].vipDesc
        binding.vipName2.text = vip_name
        binding.vipPrice2.text = "¥" + vip_price.toString()
        binding.vipDesc2.text = vip_desc
    }

    private fun initFirst() {
        val vip_name = mVipDataList!!.data[0].vipName
        val vip_price = mVipDataList!!.data[0].vipPrice
        val vip_desc = mVipDataList!!.data[0].vipDesc
        binding.vipName1.text = vip_name
        binding.vipPrice1.text = "¥" + vip_price.toString()
        binding.vipDesc1.text = vip_desc
    }


    //轮播图
    private fun initBanner() {
        //创建适配器
        adapter = BannerAdapter(this@VipCenterActivity, fragmentManager = supportFragmentManager)
        //设置适配器到控件
        binding.list.adapter = adapter
        //让指示器根据列表控件配合工作
        binding.indicator.setViewPager(binding.list)
        //适配器注册数据源观察者
        adapter.registerDataSetObserver(binding.indicator.dataSetObserver)
        //准备数据
        val datum: MutableList<Int> = ArrayList()
        datum.add(R.drawable.home_banner1)
        datum.add(R.drawable.home_banner2)
        datum.add(R.drawable.home_banner3)
        //设置数据到适配器
        adapter.setDatum(datum)
    }

    companion object {
        const val TAG = "VipCenterActivity"
    }

}

