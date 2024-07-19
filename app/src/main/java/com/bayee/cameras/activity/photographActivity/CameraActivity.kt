package com.bayee.cameras.activity.photographActivity

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.amap.api.services.core.ServiceSettings
import com.amap.api.services.weather.LocalWeatherForecastResult
import com.amap.api.services.weather.LocalWeatherLiveResult
import com.amap.api.services.weather.WeatherSearch
import com.amap.api.services.weather.WeatherSearchQuery
import com.arthenica.mobileffmpeg.FFmpeg
import com.bayee.cameras.App.ThisApp.Companion.userInfo
import com.bayee.cameras.R
import com.bayee.cameras.activity.base.BaseTitleActivity
import com.bayee.cameras.activity.home.watermark.WaterMarkAdapter
import com.bayee.cameras.activity.photographActivity.factory.*
import com.bayee.cameras.activity.photographActivity.interfaces.FinishCameraListener
import com.bayee.cameras.activity.photographActivity.interfaces.SuccessCameraListener
import com.bayee.cameras.activity.photographActivity.product.*
import com.bayee.cameras.activity.watermontage.WaterMontageActivity
import com.bayee.cameras.adapter.TabLayoutViewPager2Mediator
import com.bayee.cameras.databinding.ActivityCameraBinding
import com.bayee.cameras.databinding.CameraWaterDialogUpdateWater31Binding
import com.bayee.cameras.databinding.CameraWaterMarkConstruction1Binding
import com.bayee.cameras.dialog.BuyVipDialogFragment
import com.bayee.cameras.dialog.CameraSuccessDialogFragment
import com.bayee.cameras.login.LoginActivity
import com.bayee.cameras.main.MainActivity
import com.bayee.cameras.photo.bean.Photo
import com.bayee.cameras.photo.bean.Video
import com.bayee.cameras.photo.database.SQLDatabase
import com.bayee.cameras.sdk.gaode.LocationService
import com.bayee.cameras.sdk.gaode.LocationService.OnLocationChangeListener
import com.bayee.cameras.util.Constant.WATER_TYPE
import com.bayee.cameras.util.Constant.WATER_TYPE_1_1
import com.bayee.cameras.util.Constant.WATER_TYPE_1_2
import com.bayee.cameras.util.Constant.WATER_TYPE_1_3
import com.bayee.cameras.util.Constant.WATER_TYPE_1_4
import com.bayee.cameras.util.Constant.WATER_TYPE_1_5
import com.bayee.cameras.util.Constant.WATER_TYPE_1_6
import com.bayee.cameras.util.Constant.WATER_TYPE_1_7
import com.bayee.cameras.util.Constant.WATER_TYPE_2_1
import com.bayee.cameras.util.Constant.WATER_TYPE_2_2
import com.bayee.cameras.util.Constant.WATER_TYPE_2_3
import com.bayee.cameras.util.Constant.WATER_TYPE_2_4
import com.bayee.cameras.util.Constant.WATER_TYPE_2_5
import com.bayee.cameras.util.Constant.WATER_TYPE_2_6
import com.bayee.cameras.util.Constant.WATER_TYPE_2_7
import com.bayee.cameras.util.Constant.WATER_TYPE_2_8
import com.bayee.cameras.util.Constant.WATER_TYPE_3_1
import com.bayee.cameras.util.Constant.WATER_TYPE_3_10
import com.bayee.cameras.util.Constant.WATER_TYPE_3_11
import com.bayee.cameras.util.Constant.WATER_TYPE_3_12
import com.bayee.cameras.util.Constant.WATER_TYPE_3_2
import com.bayee.cameras.util.Constant.WATER_TYPE_3_3
import com.bayee.cameras.util.Constant.WATER_TYPE_3_4
import com.bayee.cameras.util.Constant.WATER_TYPE_3_5
import com.bayee.cameras.util.Constant.WATER_TYPE_3_6
import com.bayee.cameras.util.Constant.WATER_TYPE_3_7
import com.bayee.cameras.util.Constant.WATER_TYPE_3_8
import com.bayee.cameras.util.Constant.WATER_TYPE_3_9
import com.bayee.cameras.util.Constant.WATER_TYPE_4_1
import com.bayee.cameras.util.Constant.WATER_TYPE_4_2
import com.bayee.cameras.util.Constant.WATER_TYPE_4_3
import com.bayee.cameras.util.Constant.WATER_TYPE_4_4
import com.bayee.cameras.util.Constant.WATER_TYPE_4_5
import com.bayee.cameras.util.Constant.WATER_TYPE_4_6
import com.bayee.cameras.util.Constant.WATER_TYPE_4_7
import com.bayee.cameras.util.Constant.mCurrentCameraType_Camera
import com.bayee.cameras.util.Constant.mCurrentCameraType_Video
import com.bayee.cameras.util.DataUtil
import com.bayee.cameras.util.MMKVUtils
import com.bayee.cameras.util.PickViewUtils
import com.bayee.cameras.util.TimeUtils
import com.bayee.cameras.util.ToastUtil
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.common.util.concurrent.ListenableFuture
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


class CameraActivity : BaseTitleActivity<ActivityCameraBinding>(), FinishCameraListener,
    PickViewUtils.OnTimeSelectedListener, WeatherSearch.OnWeatherSearchListener,
    SuccessCameraListener {

    private var initPreviewSuccessful: Boolean = false
    private lateinit var outputVideoPath: String
    private var mIsRecording: Boolean = false
    private lateinit var recording: Recording
    private lateinit var locationService: LocationService

    private var isSaveUpdateLocation = false

    private lateinit var waterBean: WaterMarkBase
    private var mCurrentType: String = WATER_TYPE_1_1

    private lateinit var mCurrentCameraType: String

    private lateinit var tv_1_1_1: TextView
    private lateinit var tv_1_1_2: TextView
    private lateinit var tv_1_1_3: TextView
    private lateinit var tv_1_1_4: TextView

    private lateinit var tv_1_2_1: TextView
    private lateinit var tv_1_2_2: TextView

    private lateinit var tv_1_3_1: TextView
    private lateinit var tv_1_3_2: TextView

    private lateinit var tv_1_4_1: TextView
    private lateinit var tv_1_4_2: TextView
    private lateinit var tv_1_4_3: TextView

    private lateinit var tv_1_5_1_shi: TextView
    private lateinit var tv_1_5_2_fen: TextView
    private lateinit var tv_1_5_3_miao: TextView
    private lateinit var tv_1_5_4: TextView

    private lateinit var tv_1_6_1: TextView
    private lateinit var tv_1_6_2: TextView
    private lateinit var tv_1_6_3: TextView
    private lateinit var tv_1_6_4: TextView
    private lateinit var tv_1_6_5: TextView
    private lateinit var tv_1_6_6: TextView
    private lateinit var line_day_1_6: LinearLayout

    private lateinit var tv_1_7_1: TextView
    private lateinit var tv_1_7_2: TextView
    private lateinit var tv_1_7_3: TextView
    private lateinit var line_birty_day_1_7: LinearLayout

    private lateinit var tv_2_1_1: TextView
    private lateinit var tv_2_1_2: TextView
    private lateinit var tv_2_1_3: TextView
    private lateinit var tv_2_1_4: TextView

    private lateinit var tv_2_2_1: TextView
    private lateinit var tv_2_2_2: TextView
    private lateinit var tv_2_2_3: TextView
    private lateinit var tv_2_2_4: TextView
    private lateinit var tv_2_2_5: TextView
    private lateinit var cons_work2: ConstraintLayout

    private lateinit var tv_3_1_1: TextView
    private lateinit var tv_3_1_2: TextView
    private lateinit var tv_3_1_3: TextView
    private lateinit var tv_3_1_4: TextView
    private lateinit var tv_3_1_5: TextView
    private lateinit var tv_3_1_6: TextView
    private lateinit var tv_3_1_7: TextView
    private lateinit var line1: LinearLayout
    private lateinit var line2: LinearLayout
    private lateinit var line3: LinearLayout
    private lateinit var line4: LinearLayout
    private lateinit var line5: LinearLayout
    private lateinit var line6: LinearLayout
    private lateinit var line7: LinearLayout

    lateinit var tv_preview_1_1_1: TextView
    lateinit var tv_preview_1_1_2: TextView
    lateinit var tv_preview_1_1_3: TextView
    lateinit var tv_preview_1_1_4: TextView

    lateinit var tv_preview_1_2_1: TextView
    lateinit var tv_preview_1_2_2: TextView

    lateinit var tv_preview_1_3_1: TextView
    lateinit var tv_preview_1_3_2: TextView

    lateinit var tv_preview_1_4_1: TextView
    lateinit var tv_preview_1_4_2: TextView
    lateinit var tv_preview_1_4_3: TextView

    lateinit var tv_preview_1_5_1_shi: TextView
    lateinit var tv_preview_1_5_2_fen: TextView
    lateinit var tv_preview_1_5_3_miao: TextView
    lateinit var tv_preview_1_5_4: TextView

    lateinit var tv_preview_1_6_1: TextView
    lateinit var tv_preview_1_6_2: TextView
    lateinit var tv_preview_1_6_3: TextView
    lateinit var tv_preview_1_6_4: TextView
    lateinit var line_preview_day_1_6: LinearLayout
    lateinit var tv_preview_1_6_5: TextView
    lateinit var tv_preview_1_6_6: TextView

    lateinit var tv_preview_1_7_1: TextView
    lateinit var tv_preview_1_7_2: TextView
    lateinit var tv_preview_1_7_3: TextView
    lateinit var line_preview_day_1_7: LinearLayout

    lateinit var tv_preview_2_1_1: TextView
    lateinit var tv_preview_2_1_2: TextView
    lateinit var tv_preview_2_1_3: TextView
    lateinit var tv_preview_2_1_4: TextView

    lateinit var tv_preview_2_2_1: TextView
    lateinit var tv_preview_2_2_2: TextView
    lateinit var tv_preview_2_2_3: TextView
    lateinit var tv_preview_2_2_4: TextView
    lateinit var tv_preview_2_2_5: TextView
    lateinit var cons_preview_work2: ConstraintLayout

    private lateinit var tv_preview_3_1_1: TextView
    private lateinit var tv_preview_3_1_2: TextView
    private lateinit var tv_preview_3_1_3: TextView
    private lateinit var tv_preview_3_1_4: TextView
    private lateinit var tv_preview_3_1_5: TextView
    private lateinit var tv_preview_3_1_6: TextView
    private lateinit var tv_preview_3_1_7: TextView
    private lateinit var line1_preview: LinearLayout
    private lateinit var line2_preview: LinearLayout
    private lateinit var line3_preview: LinearLayout
    private lateinit var line4_preview: LinearLayout
    private lateinit var line5_preview: LinearLayout
    private lateinit var line6_preview: LinearLayout
    private lateinit var line7_preview: LinearLayout

    private lateinit var viewModel: CameraViewModel

    private var mFlashIndex = 1
    private val mFlashResource = DataUtil.CameraFlashs

    private var mSlowTimeIndex = 0
    private val mSlowTimeResource = DataUtil.CameraSlowTime

    private var mPrimaryIndex = 0
    private val mPrimaryResource = DataUtil.cameraPrimary

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider
    private var lensFacing = CameraSelector.LENS_FACING_BACK//默认后置摄像头
    private var flashBoolean = ImageCapture.FLASH_MODE_OFF // 初始设置为关闭闪光灯

    private var videoCapture: VideoCapture<Recorder>? = null
    private var outputVideoFile: File? = null

    private lateinit var photoFile: File

    private val db = SQLDatabase.getDatabase(this@CameraActivity)

    private lateinit var leftCornerView: View
    private lateinit var container: LinearLayout
    private lateinit var inflater: LayoutInflater

    private lateinit var leftCornerView2: View
    private lateinit var container2: LinearLayout
    private lateinit var inflater2: LayoutInflater
    private var isFirstUpdate = false

    //经纬度
    private var mBearing: Float = 0.0f
    private var mAltitude: Double = 0.0
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0
    private var below = ""
    private var after = ""
    private var below2 = ""
    private var after2 = ""
    private var mWeather: String? = null


    //具体地址
    private var mLocal: String = "空"
    private var mCity: String = "深圳"
    private var mDistrict: String = "XX区"
    private var mStreet: String = "XX街道"

    //时间
    private var mTime: String = "0"

    private lateinit var selectWaterBottomSheetDialog: BottomSheetDialog
    private lateinit var updateWaterBottomSheetDialog: BottomSheetDialog
    private lateinit var locationBottomSheetDialog: BottomSheetDialog
    private lateinit var timeBottomSheetDialog: BottomSheetDialog
    private lateinit var titleBottomSheetDialog: BottomSheetDialog
    private lateinit var otherBottomSheetDialog: BottomSheetDialog

    private fun initInterface() {
        finishCameraListener = this
        successCameraListener = this
    }

    override fun initViews() {
        super.initViews()
        loadUserInfo()//用户信息
        initBottomLayout()//底部导航
        initPermission()//权限
        initCamera()//相机
    }

    private fun loadUserInfo() {
        userInfo
//        Log.d(TAG, "loadUserInfo: ${userInfo!!.data.toString()}")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initListeners() {
        super.initListeners()
        clickSlowTimeListener()//延迟拍照
        clickFlashListener()//闪光灯
        clickOverTurn()//翻转摄像头
        clickPrimaryListener()//拍照
        clickWaterChange()//弹出切换水印的底部页面
        clickUpdateWater()//弹出修改水印的底部页面
        clickAlbum()
        clickLocation()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun clickLocation() {
        binding.cameraLineLocation.setOnClickListener {
            isSaveUpdateLocation = false
            upDateUiWaterUi()
            ToastUtil.show(this, "定位已刷新", 300)
        }
    }

    private fun clickAlbum() {
        binding.linearLayout7.setOnClickListener {
            startActivity(Intent(this, WaterMontageActivity::class.java))
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun initDatum() {
        super.initDatum()
        initInterface()
        initViewModel()
        initGaoDeSdk()
        initWaterMark()//依据类型初始化
        initTime()
        initSelectBottomSheetDialog()//水印选择
        initUpdateBottomSheetDialog()//修改水印
        initLocationBottomSheetDialog()//初始化修改水印的定位
        initTimeBottomSheetDialog()//初始化修改水印的时间
        initTitleBottomSheetDialog()//初始化修改标题（例如心情水印标题）
        initOtherBottomSheetDialog()//其他设置
    }

    private fun initOtherBottomSheetDialog() {
        otherBottomSheetDialog = BottomSheetDialog(this)
        // 设置对话框不可取消，且点击外部区域不会关闭对话框
        otherBottomSheetDialog.setCancelable(false)
        otherBottomSheetDialog.setCanceledOnTouchOutside(false)
        when (mCurrentType) {
            WATER_TYPE_1_6 -> {
                otherBottomSheetDialog.setContentView(R.layout.camera_water_dialog_other_1_6)
                val tv_1_6_other_1_6_1 =
                    otherBottomSheetDialog.findViewById<TextView>(R.id.tv_update_day_bai_1_6)!!
                val tv_1_6_other_1_6_2 =
                    otherBottomSheetDialog.findViewById<TextView>(R.id.tv_update_day_shi_1_6)!!
                val tv_1_6_other_1_6_3 =
                    otherBottomSheetDialog.findViewById<TextView>(R.id.tv_update_day_ge_1_6)!!
                val et_1_6_other_1_6_1 =
                    otherBottomSheetDialog.findViewById<TextView>(R.id.et_update_bai_1_6)!!
                val et_1_6_other_1_6_2 =
                    otherBottomSheetDialog.findViewById<TextView>(R.id.et_update_shi_1_6)!!
                val et_1_6_other_1_6_3 =
                    otherBottomSheetDialog.findViewById<TextView>(R.id.et_update_ge_1_6)!!
                val line_update_bottom_save =
                    otherBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save_day)!!
                val line_update_bottom_back =
                    otherBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back_day)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    (waterBean as Water1_6).tv_1_6_2 = et_1_6_other_1_6_1.text.toString()
                    (waterBean as Water1_6).tv_1_6_3 = et_1_6_other_1_6_2.text.toString()
                    (waterBean as Water1_6).tv_1_6_4 = et_1_6_other_1_6_3.text.toString()
                    tv_1_6_other_1_6_1.text = (waterBean as Water1_6).tv_1_6_2
                    tv_1_6_other_1_6_2.text = (waterBean as Water1_6).tv_1_6_3
                    tv_1_6_other_1_6_3.text = (waterBean as Water1_6).tv_1_6_4
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        Log.d(TAG, "initOtherBottomSheetDialog: 123123")
                        locationService.stopLocation()
                        updateUiPreview1_6()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    otherBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_1_7 -> {
                otherBottomSheetDialog.setContentView(R.layout.camera_water_dialog_other_1_7)
                val tv_1_7_other_1_7_1 =
                    otherBottomSheetDialog.findViewById<TextView>(R.id.tv_update_day_1_7)!!
                val et_update_1_7 =
                    otherBottomSheetDialog.findViewById<EditText>(R.id.et_update_1_7)!!
                val line_update_bottom_save =
                    otherBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save_day)!!
                val line_update_bottom_back =
                    otherBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back_day)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    (waterBean as Water1_7).tv_1_7_1 = et_update_1_7.text.toString()
                    tv_1_7_other_1_7_1.text = (waterBean as Water1_7).tv_1_7_1
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        Log.d(TAG, "initOtherBottomSheetDialog: 123123")
                        locationService.stopLocation()
                        updateUiPreview1_7()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    otherBottomSheetDialog.dismiss()
                }
            }
        }

    }

    private fun initUpdateBottomSheetDialog() {
        updateWaterBottomSheetDialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
        when (mCurrentType) {
            WATER_TYPE_1_1 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_1_1)
                initWaterUiFromBottomSheetDialog1_1()
                click1_1Listener()
            }

            WATER_TYPE_1_2 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_1_2)
                initWaterUiFromBottomSheetDialog1_2()
                click1_2Listener()
            }

            WATER_TYPE_1_3 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_1_3)
                initWaterUiFromBottomSheetDialog1_3()
                click1_3Listener()
            }

            WATER_TYPE_1_4 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_1_4)
                initWaterUiFromBottomSheetDialog1_4()
                click1_4Listener()
            }

            WATER_TYPE_1_5 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_1_5)
                initWaterUiFromBottomSheetDialog1_5()
                click1_5Listener()
            }

            WATER_TYPE_1_6 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_1_6)
                initWaterUiFromBottomSheetDialog1_6()
                click1_6Listener()
            }

            WATER_TYPE_1_7 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_1_7)
                initWaterUiFromBottomSheetDialog1_7()
                click1_7Listener()
            }

            WATER_TYPE_2_1 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_2_1)
                initWaterUiFromBottomSheetDialog2_1()
                click2_1Listener()
            }

            WATER_TYPE_2_2 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_2_2)
                initWaterUiFromBottomSheetDialog2_2()
                click2_2Listener()

            }

            WATER_TYPE_2_3 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_2_2)
                initWaterUiFromBottomSheetDialog2_3()
                click2_3Listener()
            }

            WATER_TYPE_2_4 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_2_2)
                initWaterUiFromBottomSheetDialog2_4()
                click2_4Listener()
            }

            WATER_TYPE_2_5 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_2_2)
                initWaterUiFromBottomSheetDialog2_5()
                click2_5Listener()
            }

            WATER_TYPE_2_6 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_2_2)
                initWaterUiFromBottomSheetDialog2_6()
                click2_6Listener()
            }

            WATER_TYPE_2_7 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_2_7)
                initWaterUiFromBottomSheetDialog2_7()
                click2_7Listener()
            }

            WATER_TYPE_2_8 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_2_2)
                initWaterUiFromBottomSheetDialog2_8()
                click2_8Listener()
            }

            WATER_TYPE_3_1 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_3_1)
                initWaterUiFromBottomSheetDialog3_1()
                click3_1Listener()
            }

            WATER_TYPE_3_2 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_3_1)
                initWaterUiFromBottomSheetDialog3_2()
                click3_2Listener()
            }

            WATER_TYPE_3_3 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_3_1)
                initWaterUiFromBottomSheetDialog3_3()
                click3_3Listener()
            }

            WATER_TYPE_3_4 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_3_4)
                initWaterUiFromBottomSheetDialog3_4()
                click3_4Listener()
            }

            WATER_TYPE_3_5 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_3_5)
                initWaterUiFromBottomSheetDialog3_5()
                click3_5Listener()
            }

            WATER_TYPE_3_6 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_3_6)
                initWaterUiFromBottomSheetDialog3_6()
                click3_6Listener()
            }

            WATER_TYPE_3_7 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_3_7)
                initWaterUiFromBottomSheetDialog3_7()
                click3_7Listener()
            }

            WATER_TYPE_3_8 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_3_8)
                initWaterUiFromBottomSheetDialog3_8()
                click3_8Listener()
            }

            WATER_TYPE_3_9 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_2_2)
                initWaterUiFromBottomSheetDialog2_5()
                click2_5Listener()
            }

            WATER_TYPE_3_10 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_3_6)
                initWaterUiFromBottomSheetDialog3_10()
                click3_10Listener()
            }

            WATER_TYPE_3_11 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_3_11)
                initWaterUiFromBottomSheetDialog3_11()
                click3_11Listener()
            }

            WATER_TYPE_3_12 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_3_12)
                initWaterUiFromBottomSheetDialog3_12()
                click3_12Listener()
            }

            WATER_TYPE_4_1 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_4_1)
                initWaterUiFromBottomSheetDialog4_1()
                click4_1Listener()
            }

            WATER_TYPE_4_2 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_4_2)
                initWaterUiFromBottomSheetDialog4_2()
                click4_2Listener()
            }

            WATER_TYPE_4_3 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_4_3)
                initWaterUiFromBottomSheetDialog4_3()
                click4_3Listener()
            }

            WATER_TYPE_4_4 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_4_4)
                initWaterUiFromBottomSheetDialog4_4()
                click4_4Listener()
            }

            WATER_TYPE_4_5 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_4_5)
                initWaterUiFromBottomSheetDialog4_5()
                click4_5Listener()
            }

            WATER_TYPE_4_6 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_4_6)
                initWaterUiFromBottomSheetDialog4_6()
                click4_6Listener()
            }
            WATER_TYPE_4_7 -> {
                updateWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_update_water_4_6)
                initWaterUiFromBottomSheetDialog4_7()
                click4_7Listener()
            }
        }
        // 获取BottomSheetBehavior
        val bottomSheet =
            updateWaterBottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        // 设置对话框不可取消，且点击外部区域不会关闭对话框
        updateWaterBottomSheetDialog.setCancelable(false)
        updateWaterBottomSheetDialog.setCanceledOnTouchOutside(false)
        // 设置状态变化监听器
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    Log.d("收起", "onStateChanged: ")
                    if (isFirstUpdate) {
                        container2.removeAllViews()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 在滑动过程中执行的操作（如果需要）
            }
        })
        //返回键监听
        val back = updateWaterBottomSheetDialog.findViewById<View>(R.id.back)
        back!!.setOnClickListener {
            updateUiRealWater()
            container2.removeAllViews()
            updateWaterBottomSheetDialog.dismiss()
        }
    }

    private fun click1_2Listener() {
        val btn_1_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_2_1)!!
        val btn_1_2_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_2_2)!!
        btn_1_2_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_2).tv_1_2_1_View = isCheck
            updateUiPreview1_2()
        }
        btn_1_2_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_2).tv_1_2_2_View = isCheck
            updateUiPreview1_2()
        }
    }

    private fun click1_3Listener() {
        val btn_1_3_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_3_1)!!
        val btn_1_3_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_3_2)!!
        btn_1_3_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_3).tv_1_3_1_View = isCheck
            updateUiPreview1_3()
        }
        btn_1_3_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_3).tv_1_3_2_View = isCheck
            updateUiPreview1_3()
        }
    }

    private fun click1_4Listener() {
        val btn_1_4_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_4_1)!!
        val btn_1_4_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_4_2)!!
        val btn_1_4_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_4_3)!!
        Log.d(TAG, "click1_4Listener: ${btn_1_4_1.isChecked}")
        btn_1_4_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_4).tv_1_4_1_View = isCheck
            updateUiPreview1_4()
        }
        btn_1_4_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_4).tv_1_4_2_View = isCheck
            updateUiPreview1_4()
        }
        btn_1_4_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_4).tv_1_4_3_View = isCheck
            updateUiPreview1_4()
        }
    }

    private fun click1_5Listener() {
        val btn_1_5_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_5_1)!!
        Log.d(TAG, "click1_4Listener: ${btn_1_5_1.isChecked}")
        btn_1_5_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_5).tv_1_5_4_View = isCheck
            updateUiPreview1_5()
        }
    }

    private fun updateUiPreview1_2() {
        tv_preview_1_2_1.visibility =
            (waterBean as Water1_2).tv_1_2_1_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_2_2.visibility =
            (waterBean as Water1_2).tv_1_2_2_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_2_1.text = (waterBean as Water1_2).tv_1_2_1
        tv_preview_1_2_2.text = (waterBean as Water1_2).tv_1_2_2
    }

    private fun updateUiPreview1_3() {
        tv_preview_1_3_1.visibility =
            (waterBean as Water1_3).tv_1_3_1_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_3_2.visibility =
            (waterBean as Water1_3).tv_1_3_2_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_3_1.text = (waterBean as Water1_3).tv_1_3_1
        tv_preview_1_3_2.text = (waterBean as Water1_3).tv_1_3_2
    }

    private fun initWaterUiFromBottomSheetDialog1_2() {
        val water = (waterBean as Water1_2)
        val btn_1_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_2_1)
        val btn_1_2_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_2_2)
        btn_1_2_1?.isChecked = water.tv_1_2_1_View
        btn_1_2_2?.isChecked = water.tv_1_2_2_View
    }


    private fun updateUiRealWater() {
        //TODO:updateUiRealWater()
        Log.d(TAG, "updateUiRealWater: ${waterBean.toString()}")
        when (mCurrentType) {
            WATER_TYPE_1_1 -> {
                tv_1_1_1.visibility =
                    if ((waterBean as Water1_1).tv_1_1_1_View) View.VISIBLE else View.GONE
                tv_1_1_2.visibility =
                    if ((waterBean as Water1_1).tv_1_1_2_View) View.VISIBLE else View.GONE
                tv_1_1_3.visibility =
                    if ((waterBean as Water1_1).tv_1_1_3_View) View.VISIBLE else View.GONE
                tv_1_1_4.visibility =
                    if ((waterBean as Water1_1).tv_1_1_4_View) View.VISIBLE else View.GONE
                tv_1_1_1.text = (waterBean as Water1_1).tv_1_1_1
                tv_1_1_2.text = (waterBean as Water1_1).tv_1_1_2
                tv_1_1_3.text = (waterBean as Water1_1).tv_1_1_3
                tv_1_1_4.text = (waterBean as Water1_1).tv_1_1_4
            }

            WATER_TYPE_1_2 -> {
                tv_1_2_1.visibility =
                    if ((waterBean as Water1_2).tv_1_2_1_View) View.VISIBLE else View.GONE
                tv_1_2_2.visibility =
                    if ((waterBean as Water1_2).tv_1_2_2_View) View.VISIBLE else View.GONE
            }

            WATER_TYPE_1_3 -> {
                tv_1_3_1.visibility =
                    if ((waterBean as Water1_3).tv_1_3_1_View) View.VISIBLE else View.GONE
                tv_1_3_2.visibility =
                    if ((waterBean as Water1_3).tv_1_3_2_View) View.VISIBLE else View.GONE
                tv_1_3_1.text = (waterBean as Water1_3).tv_1_3_1
                tv_1_3_2.text = (waterBean as Water1_3).tv_1_3_2
            }

            WATER_TYPE_1_4 -> {
                tv_1_4_1.visibility =
                    if ((waterBean as Water1_4).tv_1_4_1_View) View.VISIBLE else View.GONE
                tv_1_4_2.visibility =
                    if ((waterBean as Water1_4).tv_1_4_2_View) View.VISIBLE else View.GONE
                tv_1_4_3.visibility =
                    if ((waterBean as Water1_4).tv_1_4_3_View) View.VISIBLE else View.GONE
                tv_1_4_1.text = (waterBean as Water1_4).tv_1_4_1
                tv_1_4_2.text = (waterBean as Water1_4).tv_1_4_2
                tv_1_4_3.text = (waterBean as Water1_4).tv_1_4_3
            }

            WATER_TYPE_1_5 -> {
                tv_1_5_4.visibility =
                    if ((waterBean as Water1_5).tv_1_5_4_View) View.VISIBLE else View.GONE
                tv_1_5_1_shi.text = (waterBean as Water1_5).tv_1_5_1_shi
                tv_1_5_2_fen.text = (waterBean as Water1_5).tv_1_5_2_fen
                tv_1_5_3_miao.text = (waterBean as Water1_5).tv_1_5_3_miao
                tv_1_5_4.text = (waterBean as Water1_5).tv_1_5_4
            }

            WATER_TYPE_1_6 -> {
                tv_1_6_1.visibility =
                    if ((waterBean as Water1_6).tv_1_6_1_View) View.VISIBLE else View.GONE
                tv_1_6_2.visibility =
                    if ((waterBean as Water1_6).tv_1_6_2_View) View.VISIBLE else View.GONE
                tv_1_6_3.visibility =
                    if ((waterBean as Water1_6).tv_1_6_3_View) View.VISIBLE else View.GONE
                tv_1_6_4.visibility =
                    if ((waterBean as Water1_6).tv_1_6_4_View) View.VISIBLE else View.GONE
                tv_1_6_5.visibility =
                    if ((waterBean as Water1_6).tv_1_6_5_View) View.VISIBLE else View.GONE
                tv_1_6_6.visibility =
                    if ((waterBean as Water1_6).tv_1_6_6_View) View.VISIBLE else View.GONE
                line_day_1_6.visibility =
                    if ((waterBean as Water1_6).tv_1_6_4_View) View.VISIBLE else View.GONE
                tv_1_6_1.text = (waterBean as Water1_6).tv_1_6_1
                tv_1_6_2.text = (waterBean as Water1_6).tv_1_6_2
                tv_1_6_3.text = (waterBean as Water1_6).tv_1_6_3
                tv_1_6_4.text = (waterBean as Water1_6).tv_1_6_4
                tv_1_6_5.text = (waterBean as Water1_6).tv_1_6_5
                tv_1_6_6.text = (waterBean as Water1_6).tv_1_6_6
            }

            WATER_TYPE_1_7 -> {
                tv_1_7_1.visibility =
                    if ((waterBean as Water1_7).tv_1_7_1_View) View.VISIBLE else View.GONE
                tv_1_7_2.visibility =
                    if ((waterBean as Water1_7).tv_1_7_2_View) View.VISIBLE else View.GONE
                tv_1_7_3.visibility =
                    if ((waterBean as Water1_7).tv_1_7_3_View) View.VISIBLE else View.GONE
                line_birty_day_1_7.visibility =
                    if ((waterBean as Water1_7).tv_1_7_1_View) View.VISIBLE else View.GONE
                tv_1_7_1.text = (waterBean as Water1_7).tv_1_7_1
                tv_1_7_2.text = (waterBean as Water1_7).tv_1_7_2
                tv_1_7_3.text = (waterBean as Water1_7).tv_1_7_3
            }

            WATER_TYPE_2_1 -> {
                tv_2_1_1.visibility =
                    if ((waterBean as Water2_1).tv_2_1_1_View) View.VISIBLE else View.GONE
                tv_2_1_2.visibility =
                    if ((waterBean as Water2_1).tv_2_1_2_View) View.VISIBLE else View.GONE
                tv_2_1_3.visibility =
                    if ((waterBean as Water2_1).tv_2_1_2_View) View.VISIBLE else View.GONE
                tv_2_1_4.visibility =
                    if ((waterBean as Water2_1).tv_2_1_4_View) View.VISIBLE else View.GONE
                tv_2_1_1.text = (waterBean as Water2_1).tv_2_1_1
                tv_2_1_2.text = (waterBean as Water2_1).tv_2_1_2
                tv_2_1_3.text = (waterBean as Water2_1).tv_2_1_3
                tv_2_1_4.text = (waterBean as Water2_1).tv_2_1_4
            }

            WATER_TYPE_2_2 -> {
                cons_work2.visibility =
                    if ((waterBean as Water2_2).tv_2_2_1_View) View.VISIBLE else View.GONE
                tv_2_2_1.visibility =
                    if ((waterBean as Water2_2).tv_2_2_1_View) View.VISIBLE else View.GONE
                tv_2_2_2.visibility =
                    if ((waterBean as Water2_2).tv_2_2_2_View) View.VISIBLE else View.GONE
                tv_2_2_3.visibility =
                    if ((waterBean as Water2_2).tv_2_2_3_View) View.VISIBLE else View.GONE
                tv_2_2_4.visibility =
                    if ((waterBean as Water2_2).tv_2_2_4_View) View.VISIBLE else View.GONE
                tv_2_2_5.visibility =
                    if ((waterBean as Water2_2).tv_2_2_5_View) View.VISIBLE else View.GONE
                tv_2_2_1.text = (waterBean as Water2_2).tv_2_2_1
                tv_2_2_2.text = (waterBean as Water2_2).tv_2_2_2
                tv_2_2_3.text = (waterBean as Water2_2).tv_2_2_3
                tv_2_2_4.text = (waterBean as Water2_2).tv_2_2_4
                tv_2_2_5.text = (waterBean as Water2_2).tv_2_2_5
            }

            WATER_TYPE_2_3 -> {
                tv_1_7_1.visibility =
                    if ((waterBean as Water2_3).tv_1_7_1_View) View.VISIBLE else View.GONE
                tv_1_7_2.visibility =
                    if ((waterBean as Water2_3).tv_1_7_2_View) View.VISIBLE else View.GONE
                tv_1_7_3.visibility =
                    if ((waterBean as Water2_3).tv_1_7_3_View) View.VISIBLE else View.GONE
                tv_1_7_1.text = (waterBean as Water2_3).tv_1_7_1
                tv_1_7_2.text = (waterBean as Water2_3).tv_1_7_2
                tv_1_7_3.text = (waterBean as Water2_3).tv_1_7_3
            }

            WATER_TYPE_2_4 -> {
                tv_1_7_1.visibility =
                    if ((waterBean as Water2_4).tv_1_7_1_View) View.VISIBLE else View.GONE
                tv_1_7_2.visibility =
                    if ((waterBean as Water2_4).tv_1_7_2_View) View.VISIBLE else View.GONE
                tv_1_7_3.visibility =
                    if ((waterBean as Water2_4).tv_1_7_3_View) View.VISIBLE else View.GONE
                tv_1_7_1.text = (waterBean as Water2_4).tv_1_7_1
                tv_1_7_2.text = (waterBean as Water2_4).tv_1_7_2
                tv_1_7_3.text = (waterBean as Water2_4).tv_1_7_3
            }

            WATER_TYPE_2_5 -> {
                line_day_1_6.visibility =
                    if ((waterBean as Water2_5).tv_1_7_3_View) View.VISIBLE else View.GONE
                tv_1_7_1.visibility =
                    if ((waterBean as Water2_5).tv_1_7_1_View) View.VISIBLE else View.GONE
                tv_1_7_2.visibility =
                    if ((waterBean as Water2_5).tv_1_7_2_View) View.VISIBLE else View.GONE
                tv_1_7_3.visibility =
                    if ((waterBean as Water2_5).tv_1_7_3_View) View.VISIBLE else View.GONE
                tv_1_7_1.text = (waterBean as Water2_5).tv_1_7_1
                tv_1_7_2.text = (waterBean as Water2_5).tv_1_7_2
                tv_1_7_3.text = (waterBean as Water2_5).tv_1_7_3
            }

            WATER_TYPE_2_6 -> {
                tv_1_7_2.visibility =
                    if ((waterBean as Water2_6).tv_1_7_2_View) View.VISIBLE else View.GONE
                tv_1_7_3.visibility =
                    if ((waterBean as Water2_6).tv_1_7_3_View) View.VISIBLE else View.GONE
                tv_1_7_1.text = (waterBean as Water2_6).tv_1_7_1
                tv_1_7_2.text = (waterBean as Water2_6).tv_1_7_2
                tv_1_7_3.text = (waterBean as Water2_6).tv_1_7_3
            }

            WATER_TYPE_2_7 -> {
                cons_work2.visibility =
                    if ((waterBean as Water2_7).tv_2_2_1_View) View.VISIBLE else View.GONE
                tv_2_2_1.visibility =
                    if ((waterBean as Water2_7).tv_2_2_1_View) View.VISIBLE else View.GONE
                tv_2_2_2.visibility =
                    if ((waterBean as Water2_7).tv_2_2_2_View) View.VISIBLE else View.GONE
                tv_2_2_3.visibility =
                    if ((waterBean as Water2_7).tv_2_2_3_View) View.VISIBLE else View.GONE
                tv_2_2_4.visibility =
                    if ((waterBean as Water2_7).tv_2_2_4_View) View.VISIBLE else View.GONE
                tv_2_2_5.visibility =
                    if ((waterBean as Water2_7).tv_2_2_5_View) View.VISIBLE else View.GONE
                tv_2_2_1.text = (waterBean as Water2_7).tv_2_2_1
                tv_2_2_2.text = (waterBean as Water2_7).tv_2_2_2
                tv_2_2_3.text = (waterBean as Water2_7).tv_2_2_3
                tv_2_2_4.text = (waterBean as Water2_7).tv_2_2_4
                tv_2_2_5.text = (waterBean as Water2_7).tv_2_2_5
            }

            WATER_TYPE_2_8 -> {
                tv_1_7_2.visibility =
                    if ((waterBean as Water2_8).tv_1_7_2_View) View.VISIBLE else View.GONE
                tv_1_7_3.visibility =
                    if ((waterBean as Water2_8).tv_1_7_3_View) View.VISIBLE else View.GONE
                tv_1_7_1.text = (waterBean as Water2_8).tv_1_7_1
                tv_1_7_2.text = (waterBean as Water2_8).tv_1_7_2
                tv_1_7_3.text = (waterBean as Water2_8).tv_1_7_3
            }

            WATER_TYPE_3_1 -> {
                line1.visibility =
                    if ((waterBean as Water3_1).tv_3_1_1_View) View.VISIBLE else View.GONE
                line2.visibility =
                    if ((waterBean as Water3_1).tv_3_1_2_View) View.VISIBLE else View.GONE
                line3.visibility =
                    if ((waterBean as Water3_1).tv_3_1_3_View) View.VISIBLE else View.GONE
                line4.visibility =
                    if ((waterBean as Water3_1).tv_3_1_4_View) View.VISIBLE else View.GONE
                line5.visibility =
                    if ((waterBean as Water3_1).tv_3_1_5_View) View.VISIBLE else View.GONE
                line6.visibility =
                    if ((waterBean as Water3_1).tv_3_1_6_View) View.VISIBLE else View.GONE
                line7.visibility =
                    if ((waterBean as Water3_1).tv_3_1_7_View) View.VISIBLE else View.GONE
                tv_3_1_1.text = (waterBean as Water3_1).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water3_1).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water3_1).tv_3_1_3
                tv_3_1_4.text = (waterBean as Water3_1).tv_3_1_4
                tv_3_1_5.text = (waterBean as Water3_1).tv_3_1_5
                tv_3_1_6.text = (waterBean as Water3_1).tv_3_1_6
                tv_3_1_7.text = (waterBean as Water3_1).tv_3_1_7
            }

            WATER_TYPE_3_2 -> {
                line1.visibility =
                    if ((waterBean as Water3_2).tv_3_1_1_View) View.VISIBLE else View.GONE
                line2.visibility =
                    if ((waterBean as Water3_2).tv_3_1_2_View) View.VISIBLE else View.GONE
                line3.visibility =
                    if ((waterBean as Water3_2).tv_3_1_3_View) View.VISIBLE else View.GONE
                line4.visibility =
                    View.GONE
                line5.visibility =
                    if ((waterBean as Water3_2).tv_3_1_5_View) View.VISIBLE else View.GONE
                line6.visibility =
                    if ((waterBean as Water3_2).tv_3_1_6_View) View.VISIBLE else View.GONE
                line7.visibility =
                    if ((waterBean as Water3_2).tv_3_1_7_View) View.VISIBLE else View.GONE
                tv_3_1_1.text = (waterBean as Water3_2).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water3_2).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water3_2).tv_3_1_3
                tv_3_1_4.text = (waterBean as Water3_2).tv_3_1_4
                tv_3_1_5.text = (waterBean as Water3_2).tv_3_1_5
                tv_3_1_6.text = (waterBean as Water3_2).tv_3_1_6
                tv_3_1_7.text = (waterBean as Water3_2).tv_3_1_7
            }

            WATER_TYPE_3_3 -> {
                line1.visibility =
                    if ((waterBean as Water3_3).tv_3_1_1_View) View.VISIBLE else View.GONE
                line2.visibility =
                    if ((waterBean as Water3_3).tv_3_1_2_View) View.VISIBLE else View.GONE
                line3.visibility =
                    if ((waterBean as Water3_3).tv_3_1_3_View) View.VISIBLE else View.GONE
                line4.visibility =
                    if ((waterBean as Water3_3).tv_3_1_4_View) View.VISIBLE else View.GONE
                line5.visibility =
                    if ((waterBean as Water3_3).tv_3_1_5_View) View.VISIBLE else View.GONE
                line6.visibility =
                    if ((waterBean as Water3_3).tv_3_1_6_View) View.VISIBLE else View.GONE
                line7.visibility =
                    if ((waterBean as Water3_3).tv_3_1_7_View) View.VISIBLE else View.GONE
                tv_3_1_1.text = (waterBean as Water3_3).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water3_3).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water3_3).tv_3_1_3
                tv_3_1_4.text = (waterBean as Water3_3).tv_3_1_4
                tv_3_1_5.text = (waterBean as Water3_3).tv_3_1_5
                tv_3_1_6.text = (waterBean as Water3_3).tv_3_1_6
                tv_3_1_7.text = (waterBean as Water3_3).tv_3_1_7
            }

            WATER_TYPE_3_4 -> {
                line1.visibility =
                    if ((waterBean as Water3_4).tv_3_1_1_View) View.VISIBLE else View.GONE
                line2.visibility =
                    if ((waterBean as Water3_4).tv_3_1_2_View) View.VISIBLE else View.GONE
                line3.visibility =
                    if ((waterBean as Water3_4).tv_3_1_3_View) View.VISIBLE else View.GONE
                line4.visibility =
                    if ((waterBean as Water3_4).tv_3_1_4_View) View.VISIBLE else View.GONE
                line5.visibility =
                    if ((waterBean as Water3_4).tv_3_1_5_View) View.VISIBLE else View.GONE
                line6.visibility =
                    if ((waterBean as Water3_4).tv_3_1_6_View) View.VISIBLE else View.GONE

                tv_3_1_1.text = (waterBean as Water3_4).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water3_4).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water3_4).tv_3_1_3
                tv_3_1_4.text = (waterBean as Water3_4).tv_3_1_4
                tv_3_1_5.text = (waterBean as Water3_4).tv_3_1_5
                tv_3_1_6.text = (waterBean as Water3_4).tv_3_1_6
            }

            WATER_TYPE_3_5 -> {
                line1.visibility =
                    if ((waterBean as Water3_5).tv_3_1_1_View) View.VISIBLE else View.GONE
                line2.visibility =
                    if ((waterBean as Water3_5).tv_3_1_2_View) View.VISIBLE else View.GONE
                line3.visibility =
                    if ((waterBean as Water3_5).tv_3_1_3_View) View.VISIBLE else View.GONE

                tv_3_1_1.text = (waterBean as Water3_5).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water3_5).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water3_5).tv_3_1_3
            }

            WATER_TYPE_3_6 -> {
                line1.visibility =
                    if ((waterBean as Water3_6).tv_3_1_1_View) View.VISIBLE else View.GONE
                line2.visibility =
                    if ((waterBean as Water3_6).tv_3_1_2_View) View.VISIBLE else View.GONE
                line3.visibility =
                    if ((waterBean as Water3_6).tv_3_1_3_View) View.VISIBLE else View.GONE

                tv_3_1_1.text = (waterBean as Water3_6).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water3_6).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water3_6).tv_3_1_3
            }

            WATER_TYPE_3_7 -> {
                line1.visibility =
                    if ((waterBean as Water3_7).tv_3_1_1_View) View.VISIBLE else View.GONE
                line2.visibility =
                    if ((waterBean as Water3_7).tv_3_1_2_View) View.VISIBLE else View.GONE
                line3.visibility =
                    if ((waterBean as Water3_7).tv_3_1_3_View) View.VISIBLE else View.GONE
                line4.visibility =
                    if ((waterBean as Water3_7).tv_3_1_4_View) View.VISIBLE else View.GONE
                tv_3_1_1.text = (waterBean as Water3_7).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water3_7).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water3_7).tv_3_1_3
                tv_3_1_4.text = (waterBean as Water3_7).tv_3_1_4
            }

            WATER_TYPE_3_8 -> {
                line1.visibility =
                    if ((waterBean as Water3_8).tv_3_1_1_View) View.VISIBLE else View.GONE
            }

            WATER_TYPE_3_9 -> {
                line_day_1_6.visibility =
                    if ((waterBean as Water2_5).tv_1_7_3_View) View.VISIBLE else View.GONE
                tv_1_7_1.visibility =
                    if ((waterBean as Water2_5).tv_1_7_1_View) View.VISIBLE else View.GONE
                tv_1_7_2.visibility =
                    if ((waterBean as Water2_5).tv_1_7_2_View) View.VISIBLE else View.GONE
                tv_1_7_3.visibility =
                    if ((waterBean as Water2_5).tv_1_7_3_View) View.VISIBLE else View.GONE
                tv_1_7_1.text = (waterBean as Water2_5).tv_1_7_1
                tv_1_7_2.text = (waterBean as Water2_5).tv_1_7_2
                tv_1_7_3.text = (waterBean as Water2_5).tv_1_7_3
            }

            WATER_TYPE_3_10 -> {
                line1.visibility =
                    if ((waterBean as Water3_10).tv_3_1_1_View) View.VISIBLE else View.GONE
                line2.visibility =
                    if ((waterBean as Water3_10).tv_3_1_2_View) View.VISIBLE else View.GONE
                line3.visibility =
                    if ((waterBean as Water3_10).tv_3_1_3_View) View.VISIBLE else View.GONE

                tv_3_1_1.text = (waterBean as Water3_10).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water3_10).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water3_10).tv_3_1_3
            }

            WATER_TYPE_3_11 -> {
                line1.visibility =
                    if ((waterBean as Water3_11).tv_3_1_1_View) View.VISIBLE else View.GONE
                line2.visibility =
                    if ((waterBean as Water3_11).tv_3_1_2_View) View.VISIBLE else View.GONE
                line3.visibility =
                    if ((waterBean as Water3_11).tv_3_1_3_View) View.VISIBLE else View.GONE

                tv_3_1_1.text = (waterBean as Water3_11).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water3_11).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water3_11).tv_3_1_3
            }

            WATER_TYPE_3_12 -> {
                line1.visibility =
                    if ((waterBean as Water3_12).tv_3_1_1_View) View.VISIBLE else View.GONE
                line2.visibility =
                    if ((waterBean as Water3_12).tv_3_1_2_View) View.VISIBLE else View.GONE
                line3.visibility =
                    if ((waterBean as Water3_12).tv_3_1_3_View) View.VISIBLE else View.GONE
                line4.visibility =
                    if ((waterBean as Water3_12).tv_3_1_4_View) View.VISIBLE else View.GONE
                line5.visibility =
                    if ((waterBean as Water3_12).tv_3_1_5_View) View.VISIBLE else View.GONE
                line6.visibility =
                    if ((waterBean as Water3_12).tv_3_1_6_View) View.VISIBLE else View.GONE
                line7.visibility =
                    if ((waterBean as Water3_12).tv_3_1_7_View) View.VISIBLE else View.GONE
                tv_3_1_1.text = (waterBean as Water3_12).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water3_12).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water3_12).tv_3_1_3
                tv_3_1_4.text = (waterBean as Water3_12).tv_3_1_4
                tv_3_1_5.text = (waterBean as Water3_12).tv_3_1_5
                tv_3_1_6.text = (waterBean as Water3_12).tv_3_1_6
                tv_3_1_7.text = (waterBean as Water3_12).tv_3_1_7
            }

            WATER_TYPE_4_1 -> {
                line1.visibility =
                    if ((waterBean as Water4_1).tv_3_1_1_View) View.VISIBLE else View.GONE
                line2.visibility =
                    if ((waterBean as Water4_1).tv_3_1_2_View) View.VISIBLE else View.GONE
                line3.visibility =
                    if ((waterBean as Water4_1).tv_3_1_3_View) View.VISIBLE else View.GONE
                line4.visibility =
                    if ((waterBean as Water4_1).tv_3_1_4_View) View.VISIBLE else View.GONE
                line5.visibility =
                    if ((waterBean as Water4_1).tv_3_1_5_View) View.VISIBLE else View.GONE
                line6.visibility =
                    if ((waterBean as Water4_1).tv_3_1_6_View) View.VISIBLE else View.GONE
                tv_3_1_1.text = (waterBean as Water4_1).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water4_1).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water4_1).tv_3_1_3
                tv_3_1_4.text = (waterBean as Water4_1).tv_3_1_4
                tv_3_1_5.text = (waterBean as Water4_1).tv_3_1_5
                tv_3_1_6.text = (waterBean as Water4_1).tv_3_1_6
            }

            WATER_TYPE_4_2 -> {
                line1.visibility =
                    if ((waterBean as Water4_2).tv_3_1_1_View) View.VISIBLE else View.GONE
                line2.visibility =
                    if ((waterBean as Water4_2).tv_3_1_2_View) View.VISIBLE else View.GONE
                line3.visibility =
                    if ((waterBean as Water4_2).tv_3_1_3_View) View.VISIBLE else View.GONE
                line4.visibility =
                    if ((waterBean as Water4_2).tv_3_1_4_View) View.VISIBLE else View.GONE
                tv_3_1_1.text = (waterBean as Water4_2).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water4_2).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water4_2).tv_3_1_3
                tv_3_1_4.text = (waterBean as Water4_2).tv_3_1_4
            }

            WATER_TYPE_4_3 -> {
                line1.visibility =
                    if ((waterBean as Water4_3).tv_3_1_1_View) View.VISIBLE else View.GONE
                line2.visibility =
                    if ((waterBean as Water4_3).tv_3_1_2_View) View.VISIBLE else View.GONE
                line3.visibility =
                    if ((waterBean as Water4_3).tv_3_1_3_View) View.VISIBLE else View.GONE
                line4.visibility =
                    if ((waterBean as Water4_3).tv_3_1_4_View) View.VISIBLE else View.GONE
                line5.visibility =
                    if ((waterBean as Water4_3).tv_3_1_5_View) View.VISIBLE else View.GONE
                line6.visibility =
                    if ((waterBean as Water4_3).tv_3_1_6_View) View.VISIBLE else View.GONE
                tv_3_1_1.text = (waterBean as Water4_3).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water4_3).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water4_3).tv_3_1_3
                tv_3_1_4.text = (waterBean as Water4_3).tv_3_1_4
                tv_3_1_5.text = (waterBean as Water4_3).tv_3_1_5
                tv_3_1_6.text = (waterBean as Water4_3).tv_3_1_6
            }

            WATER_TYPE_4_4 -> {
                line1.visibility =
                    if ((waterBean as Water4_4).tv_3_1_1_View) View.VISIBLE else View.GONE
                line2.visibility =
                    if ((waterBean as Water4_4).tv_3_1_2_View) View.VISIBLE else View.GONE
                line3.visibility =
                    if ((waterBean as Water4_4).tv_3_1_3_View) View.VISIBLE else View.GONE
                line4.visibility =
                    if ((waterBean as Water4_4).tv_3_1_4_View) View.VISIBLE else View.GONE
                line5.visibility =
                    if ((waterBean as Water4_4).tv_3_1_5_View) View.VISIBLE else View.GONE
                line6.visibility =
                    if ((waterBean as Water4_4).tv_3_1_6_View) View.VISIBLE else View.GONE
                tv_3_1_1.text = (waterBean as Water4_4).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water4_4).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water4_4).tv_3_1_3
                tv_3_1_4.text = (waterBean as Water4_4).tv_3_1_4
                tv_3_1_5.text = (waterBean as Water4_4).tv_3_1_5
                tv_3_1_6.text = (waterBean as Water4_4).tv_3_1_6
            }

            WATER_TYPE_4_5 -> {
                line1.visibility =
                    if ((waterBean as Water4_5).tv_3_1_1_View) View.VISIBLE else View.GONE
                line2.visibility =
                    if ((waterBean as Water4_5).tv_3_1_2_View) View.VISIBLE else View.GONE
                line3.visibility =
                    if ((waterBean as Water4_5).tv_3_1_3_View) View.VISIBLE else View.GONE
                line4.visibility =
                    if ((waterBean as Water4_5).tv_3_1_4_View) View.VISIBLE else View.GONE
                line5.visibility =
                    if ((waterBean as Water4_5).tv_3_1_5_View) View.VISIBLE else View.GONE
                line6.visibility =
                    if ((waterBean as Water4_5).tv_3_1_6_View) View.VISIBLE else View.GONE
                tv_3_1_1.text = (waterBean as Water4_5).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water4_5).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water4_5).tv_3_1_3
                tv_3_1_4.text = (waterBean as Water4_5).tv_3_1_4
                tv_3_1_5.text = (waterBean as Water4_5).tv_3_1_5
                tv_3_1_6.text = (waterBean as Water4_5).tv_3_1_6
            }

            WATER_TYPE_4_6 -> {
                tv_3_1_3.visibility =
                    if ((waterBean as Water4_6).tv_3_1_3_View) View.VISIBLE else View.GONE
                tv_3_1_4.visibility =
                    if ((waterBean as Water4_6).tv_3_1_4_View) View.VISIBLE else View.GONE

                tv_3_1_1.text = (waterBean as Water4_6).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water4_6).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water4_6).tv_3_1_3
                tv_3_1_4.text = (waterBean as Water4_6).tv_3_1_4
            }
            WATER_TYPE_4_7 -> {
                tv_3_1_3.visibility =
                    if ((waterBean as Water4_7).tv_3_1_3_View) View.VISIBLE else View.GONE
                tv_3_1_4.visibility =
                    if ((waterBean as Water4_7).tv_3_1_4_View) View.VISIBLE else View.GONE

                tv_3_1_1.text = (waterBean as Water4_7).tv_3_1_1
                tv_3_1_2.text = (waterBean as Water4_7).tv_3_1_2
                tv_3_1_3.text = (waterBean as Water4_7).tv_3_1_3
                tv_3_1_4.text = (waterBean as Water4_7).tv_3_1_4
            }
        }
    }

    private fun click1_1Listener() {
        val btn_1_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_1_1)!!
        val btn_1_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_1_2)!!
        val btn_1_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_1_3)!!
        val btn_1_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_1_4)!!
        Log.d(TAG, "click1_1Listener: ${btn_1_1_1.isChecked}")
        btn_1_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_1).tv_1_1_1_View = isCheck
            updateUiPreview1_1()
        }
        btn_1_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_1).tv_1_1_2_View = isCheck
            updateUiPreview1_1()
        }
        btn_1_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_1).tv_1_1_3_View = isCheck
            updateUiPreview1_1()
        }
        btn_1_1_4.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_1).tv_1_1_4_View = isCheck
            updateUiPreview1_1()
        }
    }

    private fun click1_7Listener() {
        val btn_1_7_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_7_1)!!
        val btn_1_7_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_7_2)!!
        val btn_1_7_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_7_3)!!

        btn_1_7_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_7).tv_1_7_1_View = isCheck
            updateUiPreview1_7()
        }
        btn_1_7_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_7).tv_1_7_2_View = isCheck
            updateUiPreview1_7()
        }
        btn_1_7_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_7).tv_1_7_3_View = isCheck
            updateUiPreview1_7()
        }
    }

    private fun click1_6Listener() {
        val btn_1_6_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_6_1)!!
        val btn_1_6_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_6_2)!!
        val btn_1_6_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_6_3)!!
        val btn_1_6_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_6_4)!!
        btn_1_6_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_6).tv_1_6_1_View = isCheck
            updateUiPreview1_6()
        }
        btn_1_6_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_6).tv_1_6_2_View = isCheck
            (waterBean as Water1_6).tv_1_6_3_View = isCheck
            (waterBean as Water1_6).tv_1_6_4_View = isCheck
            updateUiPreview1_6()
        }
        btn_1_6_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_6).tv_1_6_5_View = isCheck
            updateUiPreview1_6()
        }
        btn_1_6_4.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water1_6).tv_1_6_6_View = isCheck
            updateUiPreview1_6()
        }
    }

    private fun click2_1Listener() {
        val btn_2_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_1_1)!!
        val btn_2_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_1_2)!!
        btn_2_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water2_1).tv_2_1_2_View = isCheck
            (waterBean as Water2_1).tv_2_1_3_View = isCheck
            updateUiPreview2_1()
        }
        btn_2_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water2_1).tv_2_1_4_View = isCheck
            updateUiPreview2_1()
        }
    }

    private fun click2_2Listener() {
        val btn_2_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_1)!!
        val btn_2_2_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_2)!!
        btn_2_2_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water2_2).tv_2_2_1_View = isCheck
            (waterBean as Water2_2).tv_2_2_2_View = isCheck
            (waterBean as Water2_2).tv_2_2_3_View = isCheck
            (waterBean as Water2_2).tv_2_2_4_View = isCheck
            updateUiPreview2_2()
        }
        btn_2_2_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water2_2).tv_2_2_5_View = isCheck
            updateUiPreview2_2()
        }
    }

    private fun click2_3Listener() {
        val btn_2_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_1)!!
        val btn_2_2_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_2)!!
        btn_2_2_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water2_3).tv_1_7_1_View = isCheck
            (waterBean as Water2_3).tv_1_7_3_View = isCheck
            updateUiPreview2_3()
        }
        btn_2_2_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water2_3).tv_1_7_2_View = isCheck
            updateUiPreview2_3()
        }
    }

    private fun click2_4Listener() {
        val btn_2_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_1)!!
        val btn_2_2_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_2)!!
        btn_2_2_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water2_4).tv_1_7_1_View = isCheck
            (waterBean as Water2_4).tv_1_7_3_View = isCheck
            updateUiPreview2_4()
        }
        btn_2_2_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water2_4).tv_1_7_2_View = isCheck
            updateUiPreview2_4()
        }
    }

    private fun click2_5Listener() {
        val btn_2_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_1)!!
        val btn_2_2_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_2)!!
        btn_2_2_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water2_5).tv_1_7_1_View = isCheck
            (waterBean as Water2_5).tv_1_7_2_View = isCheck
            updateUiPreview2_5()
        }
        btn_2_2_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water2_5).tv_1_7_3_View = isCheck
            updateUiPreview2_5()
        }
    }

    private fun click2_6Listener() {
        val btn_2_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_1)!!
        val btn_2_2_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_2)!!
        btn_2_2_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water2_6).tv_1_7_1_View = isCheck
            (waterBean as Water2_6).tv_1_7_2_View = isCheck
            updateUiPreview2_6()
        }
        btn_2_2_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water2_6).tv_1_7_3_View = isCheck
            updateUiPreview2_6()
        }
    }

    private fun click2_7Listener() {
        val btn_2_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_1)!!
        btn_2_2_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water2_7).tv_2_2_1_View = isCheck
            (waterBean as Water2_7).tv_2_2_2_View = isCheck
            (waterBean as Water2_7).tv_2_2_3_View = isCheck
            (waterBean as Water2_7).tv_2_2_4_View = isCheck
            updateUiPreview2_7()
        }
    }

    private fun click2_8Listener() {
        val btn_2_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_1)!!
        val btn_2_2_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_2)!!
        btn_2_2_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water2_8).tv_1_7_1_View = isCheck
            (waterBean as Water2_8).tv_1_7_2_View = isCheck
            updateUiPreview2_8()
        }
        btn_2_2_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water2_8).tv_1_7_3_View = isCheck
            updateUiPreview2_8()
        }
    }

    private fun click3_1Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)!!
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)!!
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)!!
        val btn_3_1_7 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_7)!!
        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_1).tv_3_1_1_View = isCheck
            updateUiPreview3_1()
        }
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_1).tv_3_1_2_View = isCheck
            updateUiPreview3_1()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_1).tv_3_1_3_View = isCheck
            updateUiPreview3_1()
        }
        btn_3_1_4.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_1).tv_3_1_4_View = isCheck
            updateUiPreview3_1()
        }
        btn_3_1_5.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_1).tv_3_1_5_View = isCheck
            updateUiPreview3_1()
        }
        btn_3_1_6.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_1).tv_3_1_6_View = isCheck
            updateUiPreview3_1()
        }
        btn_3_1_7.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_1).tv_3_1_7_View = isCheck
            updateUiPreview3_1()
        }
    }


    private fun click3_2Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)!!
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)!!
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)!!
        val btn_3_1_7 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_7)!!
        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_2).tv_3_1_1_View = isCheck
            updateUiPreview3_2()
        }
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_2).tv_3_1_2_View = isCheck
            updateUiPreview3_2()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_2).tv_3_1_3_View = isCheck
            updateUiPreview3_2()
        }
        btn_3_1_4.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_2).tv_3_1_4_View = isCheck
            updateUiPreview3_2()
        }
        btn_3_1_5.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_2).tv_3_1_5_View = isCheck
            updateUiPreview3_2()
        }
        btn_3_1_6.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_2).tv_3_1_6_View = isCheck
            updateUiPreview3_2()
        }
        btn_3_1_7.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_2).tv_3_1_7_View = isCheck
            updateUiPreview3_2()
        }

    }

    private fun click3_3Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)!!
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)!!
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)!!
        val btn_3_1_7 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_7)!!
        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_3).tv_3_1_1_View = isCheck
            updateUiPreview3_3()
        }
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_3).tv_3_1_2_View = isCheck
            updateUiPreview3_3()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_3).tv_3_1_3_View = isCheck
            updateUiPreview3_3()
        }
        btn_3_1_4.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_3).tv_3_1_4_View = isCheck
            updateUiPreview3_3()
        }
        btn_3_1_5.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_3).tv_3_1_5_View = isCheck
            updateUiPreview3_3()
        }
        btn_3_1_6.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_3).tv_3_1_6_View = isCheck
            updateUiPreview3_3()
        }
        btn_3_1_7.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_3).tv_3_1_7_View = isCheck
            updateUiPreview3_3()
        }
    }

    private fun click3_4Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)!!
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)!!
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)!!

        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_4).tv_3_1_1_View = isCheck
            updateUiPreview3_4()
        }
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_4).tv_3_1_2_View = isCheck
            updateUiPreview3_4()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_4).tv_3_1_3_View = isCheck
            updateUiPreview3_4()
        }
        btn_3_1_4.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_4).tv_3_1_4_View = isCheck
            updateUiPreview3_4()
        }
        btn_3_1_5.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_4).tv_3_1_5_View = isCheck
            updateUiPreview3_4()
        }
        btn_3_1_6.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_4).tv_3_1_6_View = isCheck
            updateUiPreview3_4()
        }
    }

    private fun click3_5Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!

        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_5).tv_3_1_1_View = isCheck
            updateUiPreview3_5()
        }
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_5).tv_3_1_2_View = isCheck
            updateUiPreview3_5()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_5).tv_3_1_3_View = isCheck
            updateUiPreview3_5()
        }
    }

    private fun click3_6Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!

        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_6).tv_3_1_1_View = isCheck
            updateUiPreview3_6()
        }
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_6).tv_3_1_2_View = isCheck
            updateUiPreview3_6()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_6).tv_3_1_3_View = isCheck
            updateUiPreview3_6()
        }
    }

    private fun click3_7Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)!!

        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_7).tv_3_1_1_View = isCheck
            updateUiPreview3_7()
        }
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_7).tv_3_1_2_View = isCheck
            updateUiPreview3_7()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_7).tv_3_1_3_View = isCheck
            updateUiPreview3_7()
        }
        btn_3_1_4.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_7).tv_3_1_3_View = isCheck
            updateUiPreview3_7()
        }
    }

    private fun click3_8Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!

        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_8).tv_3_1_1_View = isCheck
            updateUiPreview3_8()
        }
    }

    private fun click3_10Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!

        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_10).tv_3_1_1_View = isCheck
            updateUiPreview3_10()
        }
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_10).tv_3_1_2_View = isCheck
            updateUiPreview3_10()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_10).tv_3_1_3_View = isCheck
            updateUiPreview3_10()
        }
    }

    private fun click3_11Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!

        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_11).tv_3_1_1_View = isCheck
            updateUiPreview3_11()
        }
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_11).tv_3_1_2_View = isCheck
            updateUiPreview3_11()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_11).tv_3_1_3_View = isCheck
            updateUiPreview3_11()
        }
    }

    private fun click3_12Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)!!
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)!!
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)!!
        val btn_3_1_7 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_7)!!
        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_12).tv_3_1_1_View = isCheck
            updateUiPreview3_12()
        }
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_12).tv_3_1_2_View = isCheck
            updateUiPreview3_12()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_12).tv_3_1_3_View = isCheck
            updateUiPreview3_12()
        }
        btn_3_1_4.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_12).tv_3_1_4_View = isCheck
            updateUiPreview3_12()
        }
        btn_3_1_5.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_12).tv_3_1_5_View = isCheck
            updateUiPreview3_12()
        }
        btn_3_1_6.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_12).tv_3_1_6_View = isCheck
            updateUiPreview3_12()
        }
        btn_3_1_7.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water3_12).tv_3_1_7_View = isCheck
            updateUiPreview3_12()
        }
    }

    private fun click4_1Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)!!
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)!!
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)!!
        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_1).tv_3_1_1_View = isCheck
            updateUiPreview4_1()
        }
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_1).tv_3_1_2_View = isCheck
            updateUiPreview4_1()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_1).tv_3_1_3_View = isCheck
            updateUiPreview4_1()
        }
        btn_3_1_4.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_1).tv_3_1_4_View = isCheck
            updateUiPreview4_1()
        }
        btn_3_1_5.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_1).tv_3_1_5_View = isCheck
            updateUiPreview4_1()
        }
        btn_3_1_6.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_1).tv_3_1_6_View = isCheck
            updateUiPreview4_1()
        }
    }

    private fun click4_2Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)!!
        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_2).tv_3_1_1_View = isCheck
            updateUiPreview4_2()
        }
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_2).tv_3_1_2_View = isCheck
            updateUiPreview4_2()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_2).tv_3_1_3_View = isCheck
            updateUiPreview4_2()
        }
        btn_3_1_4.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_2).tv_3_1_4_View = isCheck
            updateUiPreview4_2()
        }
    }

    private fun click4_3Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)!!
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)!!
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)!!
        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_3).tv_3_1_1_View = isCheck
            updateUiPreview4_3()
        }
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_3).tv_3_1_2_View = isCheck
            updateUiPreview4_3()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_3).tv_3_1_3_View = isCheck
            updateUiPreview4_3()
        }
        btn_3_1_4.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_3).tv_3_1_4_View = isCheck
            updateUiPreview4_3()
        }
        btn_3_1_5.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_3).tv_3_1_5_View = isCheck
            updateUiPreview4_3()
        }
        btn_3_1_6.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_3).tv_3_1_6_View = isCheck
            updateUiPreview4_3()
        }
    }

    private fun click4_4Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)!!
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)!!
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)!!
        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_4).tv_3_1_1_View = isCheck
            updateUiPreview4_4()
        }
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_4).tv_3_1_2_View = isCheck
            updateUiPreview4_4()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_4).tv_3_1_3_View = isCheck
            updateUiPreview4_4()
        }
        btn_3_1_4.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_4).tv_3_1_4_View = isCheck
            updateUiPreview4_4()
        }
        btn_3_1_5.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_4).tv_3_1_5_View = isCheck
            updateUiPreview4_4()
        }
        btn_3_1_6.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_4).tv_3_1_6_View = isCheck
            updateUiPreview4_4()
        }
    }

    private fun click4_5Listener() {
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)!!
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)!!
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)!!
        btn_3_1_1.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_5).tv_3_1_1_View = isCheck
            updateUiPreview4_5()
        }
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_5).tv_3_1_2_View = isCheck
            updateUiPreview4_5()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_5).tv_3_1_3_View = isCheck
            updateUiPreview4_5()
        }
        btn_3_1_4.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_5).tv_3_1_4_View = isCheck
            updateUiPreview4_5()
        }
        btn_3_1_5.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_5).tv_3_1_5_View = isCheck
            updateUiPreview4_5()
        }
        btn_3_1_6.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_5).tv_3_1_6_View = isCheck
            updateUiPreview4_5()
        }
    }

    private fun click4_6Listener() {
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_6).tv_3_1_3_View = isCheck
            updateUiPreview4_6()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_6).tv_3_1_4_View = isCheck
            updateUiPreview4_6()
        }

    }
    private fun click4_7Listener() {
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)!!
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)!!
        btn_3_1_2.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_7).tv_3_1_3_View = isCheck
            updateUiPreview4_7()
        }
        btn_3_1_3.setmOnCheckedChangeListener { isCheck ->
            (waterBean as Water4_7).tv_3_1_4_View = isCheck
            updateUiPreview4_7()
        }

    }

    private fun initWaterUiFromBottomSheetDialog1_1() {
        val water = (waterBean as Water1_1)
        val btn_1_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_1_1)
        val btn_1_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_1_2)
        val btn_1_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_1_3)
        val btn_1_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_1_4)
        btn_1_1_1?.isChecked = water.tv_1_1_1_View
        btn_1_1_2?.isChecked = water.tv_1_1_2_View
        btn_1_1_3?.isChecked = water.tv_1_1_3_View
        btn_1_1_4?.isChecked = water.tv_1_1_4_View
        //时间和地点行点击
        val line_location_1_1 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_location_1_1)
        val line_time_1_1 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_time_1_1)
        //切换时间
        line_time_1_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_1_1!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water1_1).tv_1_1_4
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
    }

    private fun initWaterUiFromBottomSheetDialog1_6() {
        val water = (waterBean as Water1_6)
        val btn_1_6_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_6_1)
        val btn_1_6_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_6_2)
        val btn_1_6_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_6_3)
        val btn_1_6_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_6_4)
        btn_1_6_1?.isChecked = water.tv_1_6_1_View
        btn_1_6_2?.isChecked = water.tv_1_6_2_View
        btn_1_6_3?.isChecked = water.tv_1_6_3_View
        btn_1_6_4?.isChecked = water.tv_1_6_4_View
        //时间和地点行点击
        val line_title_1_6 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_title_1_6)
        val line_day_1_6 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_day_1_6)
        val line_time_1_6 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_time_1_6)
        val line_location_1_6 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_location_1_6)
        //
        //切换时间
        line_time_1_6!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_1_6!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water1_6).tv_1_6_6
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
        //修改心情标题
        line_title_1_6!!.setOnClickListener {
            titleBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                titleBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current_mood)!!
            tv_update_current.text = (waterBean as Water1_6).tv_1_6_1
        }
        line_day_1_6!!.setOnClickListener {
            otherBottomSheetDialog.show()//进入具体的修改
            val tv_update_day_bai_1_6 =
                otherBottomSheetDialog.findViewById<TextView>(R.id.tv_update_day_bai_1_6)!!
            val tv_update_day_shi_1_6 =
                otherBottomSheetDialog.findViewById<TextView>(R.id.tv_update_day_shi_1_6)!!
            val tv_update_day_ge_1_6 =
                otherBottomSheetDialog.findViewById<TextView>(R.id.tv_update_day_ge_1_6)!!
            tv_update_day_bai_1_6.text = (waterBean as Water1_6).tv_1_6_2
            tv_update_day_shi_1_6.text = (waterBean as Water1_6).tv_1_6_3
            tv_update_day_ge_1_6.text = (waterBean as Water1_6).tv_1_6_4
        }
    }

    private fun initWaterUiFromBottomSheetDialog1_3() {
        val water = (waterBean as Water1_3)
        val btn_1_3_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_3_1)
        val btn_1_3_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_3_2)
        btn_1_3_1?.isChecked = water.tv_1_3_1_View
        btn_1_3_2?.isChecked = water.tv_1_3_2_View
        //标题和时间行点击
        val line_title_1_3 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_title_1_3)
        val line_time_1_3 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_time_1_3)
        //切换时间
        line_time_1_3!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        //修改心情标题
        line_title_1_3!!.setOnClickListener {
            titleBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                titleBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current_mood)!!
            tv_update_current.text = (waterBean as Water1_3).tv_1_3_1
        }
    }

    private fun initWaterUiFromBottomSheetDialog1_4() {
        val water = (waterBean as Water1_4)
        val btn_1_4_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_4_1)
        val btn_1_4_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_4_2)
        val btn_1_4_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_4_3)
        btn_1_4_1?.isChecked = water.tv_1_4_1_View
        btn_1_4_2?.isChecked = water.tv_1_4_2_View
        btn_1_4_3?.isChecked = water.tv_1_4_3_View
        //时间和地点行点击
        val line_time_1_4 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_time_1_4)
        val line_location_1_4 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_location_1_4)
        line_time_1_4!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_1_4!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water1_4).tv_1_4_3
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
    }

    private fun initWaterUiFromBottomSheetDialog1_5() {
        val water = (waterBean as Water1_5)
        val btn_1_5_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_5_1)
        btn_1_5_1?.isChecked = water.tv_1_5_4_View
        val line_time_1_5 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_time_1_5)
        line_time_1_5!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
    }

    private fun initWaterUiFromBottomSheetDialog1_7() {
        val water = (waterBean as Water1_7)
        val btn_1_7_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_7_1)
        val btn_1_7_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_7_2)
        val btn_1_7_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_1_7_3)
        btn_1_7_1?.isChecked = water.tv_1_7_1_View
        btn_1_7_2?.isChecked = water.tv_1_7_2_View
        btn_1_7_3?.isChecked = water.tv_1_7_3_View
        //时间和地点行点击
        val line_location_1_7 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_location_1_7)
        val line_time_1_7 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_time_1_7)
        val line_day_1_7 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_day_1_7)
        line_time_1_7!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_1_7!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water1_7).tv_1_7_3
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
        line_day_1_7!!.setOnClickListener {
            otherBottomSheetDialog.show()//进入具体的修改
            val tv_update_day_bai_1_7 =
                otherBottomSheetDialog.findViewById<TextView>(R.id.tv_update_day_1_7)!!
            tv_update_day_bai_1_7.text = (waterBean as Water1_7).tv_1_7_1
        }
    }

    private fun initWaterUiFromBottomSheetDialog2_1() {
        val water = (waterBean as Water2_1)
        val btn_2_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_1_1)
        val btn_2_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_1_2)
        btn_2_1_1?.isChecked = water.tv_2_1_2_View
        btn_2_1_2?.isChecked = water.tv_2_1_4_View
        //时间和地点行点击
        val line_location_2_1 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_location_2_1)
        val line_time_2_1 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_time_2_1)
        line_time_2_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_2_1!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water2_1).tv_2_1_4
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
    }

    private fun initWaterUiFromBottomSheetDialog2_2() {
        val water = (waterBean as Water2_2)
        val btn_2_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_1)
        val btn_2_2_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_2)
        btn_2_2_1?.isChecked = water.tv_2_2_2_View
        btn_2_2_2?.isChecked = water.tv_2_2_4_View
        //时间和地点行点击
        val line_location_2_2 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_location_2_2)
        val line_time_2_2 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_time_2_2)
        line_time_2_2!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_2_2!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water2_2).tv_2_2_5
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
    }

    private fun initWaterUiFromBottomSheetDialog2_3() {
        val water = (waterBean as Water2_3)
        val btn_2_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_1)
        val btn_2_2_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_2)
        btn_2_2_1?.isChecked = water.tv_1_7_1_View
        btn_2_2_2?.isChecked = water.tv_1_7_2_View
        //时间和地点行点击
        val line_location_2_2 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_location_2_2)
        val line_time_2_2 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_time_2_2)
        line_time_2_2!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_2_2!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water2_3).tv_1_7_2
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
    }

    private fun initWaterUiFromBottomSheetDialog2_4() {
        val water = (waterBean as Water2_4)
        val btn_2_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_1)
        val btn_2_2_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_2)
        btn_2_2_1?.isChecked = water.tv_1_7_1_View
        btn_2_2_2?.isChecked = water.tv_1_7_2_View
        //时间和地点行点击
        val line_location_2_2 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_location_2_2)
        val line_time_2_2 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_time_2_2)
        line_time_2_2!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_2_2!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water2_4).tv_1_7_2
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
    }

    private fun initWaterUiFromBottomSheetDialog2_5() {
        val water = (waterBean as Water2_5)
        val btn_2_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_1)
        val btn_2_2_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_2)
        btn_2_2_1?.isChecked = water.tv_1_7_1_View
        btn_2_2_2?.isChecked = water.tv_1_7_3_View
        //时间和地点行点击
        val line_location_2_2 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_location_2_2)
        val line_time_2_2 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_time_2_2)
        line_time_2_2!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_2_2!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water2_5).tv_1_7_3
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
    }

    private fun initWaterUiFromBottomSheetDialog2_6() {
        val water = (waterBean as Water2_6)
        val btn_2_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_1)
        val btn_2_2_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_2)
        btn_2_2_1?.isChecked = water.tv_1_7_1_View
        btn_2_2_2?.isChecked = water.tv_1_7_3_View
        //时间和地点行点击
        val line_location_2_2 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_location_2_2)
        val line_time_2_2 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_time_2_2)
        line_time_2_2!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_2_2!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water2_6).tv_1_7_3
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
    }

    private fun initWaterUiFromBottomSheetDialog2_7() {
        val water = (waterBean as Water2_7)
        val btn_2_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_1)
        btn_2_2_1?.isChecked = water.tv_2_2_1_View
        val line_time_2_2 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_time_2_2)
        line_time_2_2!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
    }

    private fun initWaterUiFromBottomSheetDialog2_8() {
        val water = (waterBean as Water2_8)
        val btn_2_2_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_1)
        val btn_2_2_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_2_2_2)
        btn_2_2_1?.isChecked = water.tv_1_7_1_View
        btn_2_2_2?.isChecked = water.tv_1_7_3_View
        //时间和地点行点击
        val line_location_2_2 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_location_2_2)
        val line_time_2_2 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.line_time_2_2)
        line_time_2_2!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_2_2!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water2_8).tv_1_7_3
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
    }

    private fun initWaterUiFromBottomSheetDialog3_1() {
        val water = (waterBean as Water3_1)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)
        val btn_3_1_7 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_7)
        btn_3_1_1?.isChecked = water.tv_3_1_1_View
        btn_3_1_2?.isChecked = water.tv_3_1_2_View
        btn_3_1_3?.isChecked = water.tv_3_1_3_View
        btn_3_1_4?.isChecked = water.tv_3_1_4_View
        btn_3_1_5?.isChecked = water.tv_3_1_5_View
        btn_3_1_6?.isChecked = water.tv_3_1_6_View
        btn_3_1_7?.isChecked = water.tv_3_1_7_View
        //时间和地点行点击
        val line_location_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_location_3_1)
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_3 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)!!
        val et_3_1_4 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)!!
        val et_3_1_5 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)!!
        val et_3_1_7 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_7)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_1).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview3_1()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_1).tv_3_1_3 = et_3_1_3.text.toString()
                    updateUiPreview3_1()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_1).tv_3_1_4 = et_3_1_4.text.toString()
                    updateUiPreview3_1()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //更新UI
                if (initPreviewSuccessful) {
                    (waterBean as Water3_1).tv_3_1_5 = et_3_1_5.text.toString()
                    updateUiPreview3_1()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_7.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_1).tv_3_1_7 = et_3_1_7.text.toString()
                    updateUiPreview3_1()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_3_1!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water3_1).tv_3_1_6
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
    }

    private fun initWaterUiFromBottomSheetDialog3_2() {
        val water = (waterBean as Water3_2)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)
        val btn_3_1_7 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_7)
        val construct_cons3_1_4 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.construct_cons3_1_4)!!
        construct_cons3_1_4.visibility = View.GONE
        val construct_line3_1_4 =
            updateWaterBottomSheetDialog.findViewById<LinearLayout>(R.id.construct_line3_1_4)!!
        construct_line3_1_4.visibility = View.GONE
        btn_3_1_1?.isChecked = water.tv_3_1_1_View
        btn_3_1_2?.isChecked = water.tv_3_1_2_View
        btn_3_1_3?.isChecked = water.tv_3_1_3_View
        btn_3_1_4?.isChecked = water.tv_3_1_4_View
        btn_3_1_5?.isChecked = water.tv_3_1_5_View
        btn_3_1_6?.isChecked = water.tv_3_1_6_View
        btn_3_1_7?.isChecked = water.tv_3_1_7_View
        //时间和地点行点击
        val line_location_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_location_3_1)
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_3 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)!!
        val et_3_1_4 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)!!
        val et_3_1_5 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)!!
        val et_3_1_7 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_7)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_2).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview3_2()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_2).tv_3_1_3 = et_3_1_3.text.toString()
                    updateUiPreview3_2()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_2).tv_3_1_4 = et_3_1_4.text.toString()
                    updateUiPreview3_2()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //更新UI
                if (initPreviewSuccessful) {
                    (waterBean as Water3_2).tv_3_1_5 = et_3_1_5.text.toString()
                    updateUiPreview3_2()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_7.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_2).tv_3_1_7 = et_3_1_7.text.toString()
                    updateUiPreview3_2()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_3_1!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water3_2).tv_3_1_6
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
    }

    private fun initWaterUiFromBottomSheetDialog3_3() {
        val water = (waterBean as Water3_3)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)
        val btn_3_1_7 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_7)
        btn_3_1_1?.isChecked = water.tv_3_1_1_View
        btn_3_1_2?.isChecked = water.tv_3_1_2_View
        btn_3_1_3?.isChecked = water.tv_3_1_3_View
        btn_3_1_4?.isChecked = water.tv_3_1_4_View
        btn_3_1_5?.isChecked = water.tv_3_1_5_View
        btn_3_1_6?.isChecked = water.tv_3_1_6_View
        btn_3_1_7?.isChecked = water.tv_3_1_7_View
        //时间和地点行点击
        val line_location_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_location_3_1)
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_3 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)!!
        val et_3_1_4 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)!!
        val et_3_1_5 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)!!
        val et_3_1_7 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_7)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_3).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview3_3()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_3).tv_3_1_3 = et_3_1_3.text.toString()
                    updateUiPreview3_3()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_3).tv_3_1_4 = et_3_1_4.text.toString()
                    updateUiPreview3_3()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //更新UI
                if (initPreviewSuccessful) {
                    (waterBean as Water3_3).tv_3_1_5 = et_3_1_5.text.toString()
                    updateUiPreview3_3()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_7.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_3).tv_3_1_7 = et_3_1_7.text.toString()
                    updateUiPreview3_3()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_3_1!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water3_3).tv_3_1_6
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
    }

    private fun initWaterUiFromBottomSheetDialog3_4() {
        val water = (waterBean as Water3_4)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)
        btn_3_1_1?.isChecked = water.tv_3_1_1_View
        btn_3_1_2?.isChecked = water.tv_3_1_2_View
        btn_3_1_3?.isChecked = water.tv_3_1_3_View
        btn_3_1_4?.isChecked = water.tv_3_1_4_View
        btn_3_1_5?.isChecked = water.tv_3_1_5_View
        btn_3_1_6?.isChecked = water.tv_3_1_6_View
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_3 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)!!
        val et_3_1_4 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)!!
        val et_3_1_5 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)!!
        val et_3_1_6 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_6)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_4).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview3_4()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_4).tv_3_1_3 = et_3_1_3.text.toString()
                    updateUiPreview3_4()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_4).tv_3_1_4 = et_3_1_4.text.toString()
                    updateUiPreview3_4()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //更新UI
                if (initPreviewSuccessful) {
                    (waterBean as Water3_4).tv_3_1_5 = et_3_1_5.text.toString()
                    updateUiPreview3_4()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_4).tv_3_1_6 = et_3_1_6.text.toString()
                    updateUiPreview3_4()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
    }

    private fun initWaterUiFromBottomSheetDialog3_5() {
        val water = (waterBean as Water3_5)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)
        btn_3_1_1?.isChecked = water.tv_3_1_1_View
        btn_3_1_2?.isChecked = water.tv_3_1_2_View
        btn_3_1_3?.isChecked = water.tv_3_1_3_View
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_3 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_5).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview3_5()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_5).tv_3_1_3 = et_3_1_3.text.toString()
                    updateUiPreview3_5()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
    }

    private fun initWaterUiFromBottomSheetDialog3_6() {
        val water = (waterBean as Water3_6)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)
        btn_3_1_1?.isChecked = water.tv_3_1_1_View
        btn_3_1_2?.isChecked = water.tv_3_1_2_View
        btn_3_1_3?.isChecked = water.tv_3_1_3_View
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_3 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_6).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview3_6()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_6).tv_3_1_3 = et_3_1_3.text.toString()
                    updateUiPreview3_6()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
    }

    private fun initWaterUiFromBottomSheetDialog3_7() {
        val water = (waterBean as Water3_7)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)
        btn_3_1_1?.isChecked = water.tv_3_1_1_View
        btn_3_1_2?.isChecked = water.tv_3_1_2_View
        btn_3_1_3?.isChecked = water.tv_3_1_3_View
        btn_3_1_4?.isChecked = water.tv_3_1_4_View
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_3 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)!!
        val et_3_1_4 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_7).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview3_7()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_7).tv_3_1_3 = et_3_1_3.text.toString()
                    updateUiPreview3_7()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_7).tv_3_1_4 = et_3_1_4.text.toString()
                    updateUiPreview3_7()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
    }

    private fun initWaterUiFromBottomSheetDialog3_8() {
        val water = (waterBean as Water3_8)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        btn_3_1_1?.isChecked = water.tv_3_1_1_View
    }

    private fun initWaterUiFromBottomSheetDialog3_10() {
        val water = (waterBean as Water3_10)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)
        btn_3_1_1?.isChecked = water.tv_3_1_1_View
        btn_3_1_2?.isChecked = water.tv_3_1_2_View
        btn_3_1_3?.isChecked = water.tv_3_1_3_View
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_3 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_10).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview3_10()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_10).tv_3_1_3 = et_3_1_3.text.toString()
                    updateUiPreview3_10()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
    }

    private fun initWaterUiFromBottomSheetDialog3_11() {
        val water = (waterBean as Water3_11)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)
        btn_3_1_1?.isChecked = water.tv_3_1_1_View
        btn_3_1_2?.isChecked = water.tv_3_1_2_View
        btn_3_1_3?.isChecked = water.tv_3_1_3_View
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_2 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_2)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_11).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview3_11()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_11).tv_3_1_2 = et_3_1_2.text.toString()
                    updateUiPreview3_11()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
    }

    private fun initWaterUiFromBottomSheetDialog3_12() {
        val water = (waterBean as Water3_12)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)
        val btn_3_1_7 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_7)
        btn_3_1_1?.isChecked = water.tv_3_1_1_View
        btn_3_1_2?.isChecked = water.tv_3_1_2_View
        btn_3_1_3?.isChecked = water.tv_3_1_3_View
        btn_3_1_4?.isChecked = water.tv_3_1_4_View
        btn_3_1_5?.isChecked = water.tv_3_1_5_View
        btn_3_1_6?.isChecked = water.tv_3_1_6_View
        btn_3_1_7?.isChecked = water.tv_3_1_7_View
        //时间和地点行点击
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_2 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_2)!!
        val et_3_1_4 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)!!
        val et_3_1_5 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)!!
        val et_3_1_6 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_6)!!
        val et_3_1_7 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_7)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_12).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview3_12()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_12).tv_3_1_2 = et_3_1_2.text.toString()
                    updateUiPreview3_12()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_12).tv_3_1_4 = et_3_1_4.text.toString()
                    updateUiPreview3_12()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //更新UI
                if (initPreviewSuccessful) {
                    (waterBean as Water3_12).tv_3_1_5 = et_3_1_5.text.toString()
                    updateUiPreview3_12()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //更新UI
                if (initPreviewSuccessful) {
                    (waterBean as Water3_12).tv_3_1_6 = et_3_1_6.text.toString()
                    updateUiPreview3_12()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_7.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water3_12).tv_3_1_7 = et_3_1_7.text.toString()
                    updateUiPreview3_12()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
    }

    private fun initWaterUiFromBottomSheetDialog4_1() {
        val water = (waterBean as Water4_1)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)

        btn_3_1_1?.isChecked = water.tv_3_1_1_View
        btn_3_1_2?.isChecked = water.tv_3_1_2_View
        btn_3_1_3?.isChecked = water.tv_3_1_3_View
        btn_3_1_4?.isChecked = water.tv_3_1_4_View
        btn_3_1_5?.isChecked = water.tv_3_1_5_View
        btn_3_1_6?.isChecked = water.tv_3_1_6_View
        //时间和地点行点击
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val line_location_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_location_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_2 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_2)!!
        val et_3_1_3 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)!!
        val et_3_1_5 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)!!
        val tv_3_1_6 = updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_6)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_1).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview4_1()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_1).tv_3_1_2 = et_3_1_2.text.toString()
                    updateUiPreview4_1()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_1).tv_3_1_3 = et_3_1_3.text.toString()
                    updateUiPreview4_1()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //更新UI
                if (initPreviewSuccessful) {
                    (waterBean as Water4_1).tv_3_1_5 = et_3_1_5.text.toString()
                    updateUiPreview4_1()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        tv_3_1_6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //更新UI
                if (initPreviewSuccessful) {
                    (waterBean as Water4_1).tv_3_1_6 = tv_3_1_6.text.toString()
                    updateUiPreview4_1()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_3_1!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water4_1).tv_3_1_6
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
    }

    private fun initWaterUiFromBottomSheetDialog4_2() {
        val water = (waterBean as Water4_2)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)
        btn_3_1_1?.isChecked = water.tv_3_1_1_View
        btn_3_1_2?.isChecked = water.tv_3_1_2_View
        btn_3_1_3?.isChecked = water.tv_3_1_3_View
        btn_3_1_4?.isChecked = water.tv_3_1_4_View
        //时间和地点行点击
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_2 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_2)!!
        val et_3_1_3 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_2).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview4_2()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_2).tv_3_1_2 = et_3_1_2.text.toString()
                    updateUiPreview4_2()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_2).tv_3_1_3 = et_3_1_3.text.toString()
                    updateUiPreview4_2()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
    }

    private fun initWaterUiFromBottomSheetDialog4_3() {
        val water = (waterBean as Water4_3)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)

        btn_3_1_1?.isChecked = water.tv_3_1_1_View
        btn_3_1_2?.isChecked = water.tv_3_1_2_View
        btn_3_1_3?.isChecked = water.tv_3_1_3_View
        btn_3_1_4?.isChecked = water.tv_3_1_4_View
        btn_3_1_5?.isChecked = water.tv_3_1_5_View
        btn_3_1_6?.isChecked = water.tv_3_1_6_View
        //时间和地点行点击
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val line_location_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_location_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_2 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_2)!!
        val et_3_1_3 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)!!
        val et_3_1_5 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)!!
        val tv_3_1_6 = updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_6)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_3).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview4_3()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_3).tv_3_1_2 = et_3_1_2.text.toString()
                    updateUiPreview4_3()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_3).tv_3_1_3 = et_3_1_3.text.toString()
                    updateUiPreview4_3()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //更新UI
                if (initPreviewSuccessful) {
                    (waterBean as Water4_3).tv_3_1_5 = et_3_1_5.text.toString()
                    updateUiPreview4_3()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        tv_3_1_6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //更新UI
                if (initPreviewSuccessful) {
                    (waterBean as Water4_3).tv_3_1_6 = tv_3_1_6.text.toString()
                    updateUiPreview4_3()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_3_1!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water4_3).tv_3_1_6
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
    }

    private fun initWaterUiFromBottomSheetDialog4_4() {
        val water = (waterBean as Water4_4)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)

        btn_3_1_1?.isChecked = water.tv_3_1_1_View
        btn_3_1_2?.isChecked = water.tv_3_1_2_View
        btn_3_1_3?.isChecked = water.tv_3_1_3_View
        btn_3_1_4?.isChecked = water.tv_3_1_4_View
        btn_3_1_5?.isChecked = water.tv_3_1_5_View
        btn_3_1_6?.isChecked = water.tv_3_1_6_View
        //时间和地点行点击
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_2 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_2)!!
        val et_3_1_3 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)!!
        val et_3_1_4 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)!!
        val et_3_1_5 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)!!
        val tv_3_1_6 = updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_6)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_4).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview4_4()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_4).tv_3_1_2 = et_3_1_2.text.toString()
                    updateUiPreview4_4()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_4).tv_3_1_3 = et_3_1_3.text.toString()
                    updateUiPreview4_4()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_4).tv_3_1_4 = et_3_1_4.text.toString()
                    updateUiPreview4_4()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //更新UI
                if (initPreviewSuccessful) {
                    (waterBean as Water4_4).tv_3_1_5 = et_3_1_5.text.toString()
                    updateUiPreview4_4()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        tv_3_1_6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //更新UI
                if (initPreviewSuccessful) {
                    (waterBean as Water4_4).tv_3_1_6 = tv_3_1_6.text.toString()
                    updateUiPreview4_4()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
    }

    private fun initWaterUiFromBottomSheetDialog4_5() {
        val water = (waterBean as Water4_5)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)
        val btn_3_1_4 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_4)
        val btn_3_1_5 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_5)
        val btn_3_1_6 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_6)

        btn_3_1_1?.isChecked = water.tv_3_1_1_View
        btn_3_1_2?.isChecked = water.tv_3_1_2_View
        btn_3_1_3?.isChecked = water.tv_3_1_3_View
        btn_3_1_4?.isChecked = water.tv_3_1_4_View
        btn_3_1_5?.isChecked = water.tv_3_1_5_View
        btn_3_1_6?.isChecked = water.tv_3_1_6_View
        //时间和地点行点击
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val line_location_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_location_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_2 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_2)!!
        val et_3_1_3 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)!!
        val et_3_1_4 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_5).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview4_5()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_5).tv_3_1_2 = et_3_1_2.text.toString()
                    updateUiPreview4_5()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_5).tv_3_1_3 = et_3_1_3.text.toString()
                    updateUiPreview4_5()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_5).tv_3_1_4 = et_3_1_4.text.toString()
                    updateUiPreview4_5()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
        line_location_3_1!!.setOnClickListener {
            locationBottomSheetDialog.show()//进入具体的修改
            val tv_update_current =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
            val tv_update_history =
                locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_history)!!
            tv_update_current.text = (waterBean as Water4_5).tv_3_1_6
            tv_update_history.text = MMKVUtils.getHistoryLocation()
        }
    }

    private fun initWaterUiFromBottomSheetDialog4_6() {
        val water = (waterBean as Water4_6)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        btn_3_1_1.isClickable = false
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)

        btn_3_1_2?.isChecked = water.tv_3_1_3_View
        btn_3_1_3?.isChecked = water.tv_3_1_4_View
        //时间和地点行点击
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_3 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_6).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview4_6()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_6).tv_3_1_4 = et_3_1_3.text.toString()
                    updateUiPreview4_6()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
    }
    private fun initWaterUiFromBottomSheetDialog4_7() {
        val water = (waterBean as Water4_7)
        val btn_3_1_1 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_1)!!
        btn_3_1_1.isClickable = false
        val btn_3_1_2 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_2)
        val btn_3_1_3 =
            updateWaterBottomSheetDialog.findViewById<com.bayee.cameras.view.SwitchButton>(R.id.btn_3_1_3)

        btn_3_1_2?.isChecked = water.tv_3_1_3_View
        btn_3_1_3?.isChecked = water.tv_3_1_4_View
        //时间和地点行点击
        val line_time_3_1 =
            updateWaterBottomSheetDialog.findViewById<ConstraintLayout>(R.id.line_time_3_1)
        val et_3_1_1 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)!!
        val et_3_1_3 = updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)!!

        et_3_1_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_7).tv_3_1_1 = et_3_1_1.text.toString()
                    updateUiPreview4_7()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        et_3_1_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (initPreviewSuccessful) {
                    (waterBean as Water4_7).tv_3_1_4 = et_3_1_3.text.toString()
                    updateUiPreview4_7()
                    updateUiRealWater()
                    isSaveUpdateLocation = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        line_time_3_1!!.setOnClickListener {
            val time = PickViewUtils.getPickViewInstance(this, this)
        }
    }

    private fun clickUpdateWater() {
        binding.cameraWaterUpdate.setOnClickListener {
            locationService.startLocation()
            isSaveUpdateLocation = false

            initUpdateUi()
            updateWaterBottomSheetDialog.show()
            loadUpdateWater()
        }
    }

    private fun initUpdateUi() {
        //TODO:更新打开bottomSheetDialog显示initUpdateUi()
        Log.d(TAG, "initUpdateUi: $mCurrentType")
        when (mCurrentType) {
            WATER_TYPE_1_1 -> {
                //时间和地点
                Log.d(TAG, "initUpdateUi: ${(waterBean as Water1_1).tv_1_1_3}")
                val tv_update_1_1_1 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_1_1_1)
                val tv_update_1_1_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_1_1_2)
                tv_update_1_1_1!!.text = (waterBean as Water1_1).tv_1_1_3
                tv_update_1_1_2!!.text = (waterBean as Water1_1).tv_1_1_4
            }

            WATER_TYPE_1_3 -> {
                val tv_update_1_3_1 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_1_3_1)
                val tv_update_1_3_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_1_3_2)
                tv_update_1_3_1!!.text = (waterBean as Water1_3).tv_1_3_1
                tv_update_1_3_2!!.text = (waterBean as Water1_3).tv_1_3_2
            }

            WATER_TYPE_1_4 -> {
                val tv_update_1_4_1 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_1_4_1)
                val tv_update_1_4_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_1_4_2)
                tv_update_1_4_1!!.text = (waterBean as Water1_4).tv_1_4_2
                tv_update_1_4_2!!.text = (waterBean as Water1_4).tv_1_4_3
            }

            WATER_TYPE_1_5 -> {
                val tv_update_1_5_1 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_1_5_1)
                Log.d(TAG, "initUpdateUi: ${(waterBean as Water1_5).tv_1_5_4}")
                tv_update_1_5_1!!.text = (waterBean as Water1_5).tv_1_5_4
            }

            WATER_TYPE_1_6 -> {
                val tv_update_1_6_1 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_1_6_1)
                val tv_update_1_6_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_1_6_2)
                val tv_update_1_6_3 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_1_6_3)
                val tv_update_1_6_4 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_1_6_4)
                tv_update_1_6_1!!.text = (waterBean as Water1_6).tv_1_6_1
                tv_update_1_6_2!!.text =
                    (waterBean as Water1_6).tv_1_6_2 +
                            (waterBean as Water1_6).tv_1_6_3 +
                            (waterBean as Water1_6).tv_1_6_4 + "天"
                tv_update_1_6_3!!.text = (waterBean as Water1_6).tv_1_6_5
                tv_update_1_6_4!!.text = (waterBean as Water1_6).tv_1_6_6
            }

            WATER_TYPE_1_7 -> {
                //时间和地点
                val tv_update_1_7_1 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_1_7_1)
                val tv_update_1_7_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_1_7_2)
                val tv_update_1_7_3 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_1_7_3)
                tv_update_1_7_1!!.text = (waterBean as Water1_7).tv_1_7_1
                tv_update_1_7_2!!.text = (waterBean as Water1_7).tv_1_7_2
                tv_update_1_7_3!!.text = (waterBean as Water1_7).tv_1_7_3
            }

            WATER_TYPE_2_1 -> {
                //时间和地点
                val tv_update_2_1_1 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_1_1)
                val tv_update_2_1_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_1_2)
                tv_update_2_1_1!!.text = (waterBean as Water2_1).tv_2_1_5
                tv_update_2_1_2!!.text = (waterBean as Water2_1).tv_2_1_4
            }

            WATER_TYPE_2_2 -> {
                //时间和地点
                val tv_update_2_2_1 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_2_1)
                val tv_update_2_2_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_2_2)
                tv_update_2_2_1!!.text = (waterBean as Water2_2).tv_2_2_4
                tv_update_2_2_2!!.text = (waterBean as Water2_2).tv_2_2_5
            }

            WATER_TYPE_2_3 -> {
                //时间和地点
                val tv_update_2_2_1 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_2_1)
                val tv_update_2_2_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_2_2)
                tv_update_2_2_1!!.text =
                    (waterBean as Water2_3).tv_1_7_3 + " " + (waterBean as Water2_3).tv_1_7_1
                tv_update_2_2_2!!.text = (waterBean as Water2_3).tv_1_7_2
            }

            WATER_TYPE_2_4 -> {
                //时间和地点
                val tv_update_2_2_1 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_2_1)
                val tv_update_2_2_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_2_2)
                tv_update_2_2_1!!.text =
                    (waterBean as Water2_4).tv_1_7_3 + " " + (waterBean as Water2_4).tv_1_7_1
                tv_update_2_2_2!!.text = (waterBean as Water2_4).tv_1_7_2
            }

            WATER_TYPE_2_5 -> {
                //时间和地点
                val tv_update_2_2_1 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_2_1)
                val tv_update_2_2_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_2_2)
                tv_update_2_2_1!!.text =
                    (waterBean as Water2_5).tv_1_7_1 + " " + (waterBean as Water2_5).tv_1_7_2
                tv_update_2_2_2!!.text = (waterBean as Water2_5).tv_1_7_3
            }

            WATER_TYPE_2_6 -> {
                //时间和地点
                val tv_update_2_2_1 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_2_1)
                val tv_update_2_2_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_2_2)
                tv_update_2_2_1!!.text =
                    (waterBean as Water2_6).tv_1_7_2 + " " + (waterBean as Water2_6).tv_1_7_1
                tv_update_2_2_2!!.text = (waterBean as Water2_6).tv_1_7_3
            }

            WATER_TYPE_2_7 -> {
                //时间和地点
                val tv_update_2_2_1 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_2_1)
                tv_update_2_2_1!!.text = (waterBean as Water2_7).tv_2_2_4
            }

            WATER_TYPE_2_8 -> {
                //时间和地点
                val tv_update_2_2_1 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_2_1)
                val tv_update_2_2_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_2_2)
                tv_update_2_2_1!!.text =
                    (waterBean as Water2_8).tv_1_7_2 + " " + (waterBean as Water2_8).tv_1_7_1
                tv_update_2_2_2!!.text = (waterBean as Water2_8).tv_1_7_3
            }

            WATER_TYPE_3_1 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val tv_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)
                val et_3_1_4 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)
                val et_3_1_5 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)
                val tv_3_1_6 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_6)
                val et_3_1_7 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_7)

                et_3_1_1!!.setText((waterBean as Water3_1).tv_3_1_1)
                tv_3_1_2!!.setText((waterBean as Water3_1).tv_3_1_2)
                et_3_1_3!!.setText((waterBean as Water3_1).tv_3_1_3)
                et_3_1_4!!.setText((waterBean as Water3_1).tv_3_1_4)
                et_3_1_5!!.setText((waterBean as Water3_1).tv_3_1_5)
                tv_3_1_6!!.setText((waterBean as Water3_1).tv_3_1_6)
                et_3_1_7!!.setText((waterBean as Water3_1).tv_3_1_7)

            }

            WATER_TYPE_3_2 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val tv_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)
                val et_3_1_4 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)
                val et_3_1_5 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)
                val tv_3_1_6 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_6)
                val et_3_1_7 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_7)

                et_3_1_1!!.setText((waterBean as Water3_2).tv_3_1_1)
                tv_3_1_2!!.setText((waterBean as Water3_2).tv_3_1_2)
                et_3_1_3!!.setText((waterBean as Water3_2).tv_3_1_3)
                et_3_1_4!!.setText((waterBean as Water3_2).tv_3_1_4)
                et_3_1_5!!.setText((waterBean as Water3_2).tv_3_1_5)
                tv_3_1_6!!.setText((waterBean as Water3_2).tv_3_1_6)
                et_3_1_7!!.setText((waterBean as Water3_2).tv_3_1_7)

            }

            WATER_TYPE_3_3 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val tv_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)
                val et_3_1_4 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)
                val et_3_1_5 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)
                val tv_3_1_6 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_6)
                val et_3_1_7 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_7)

                et_3_1_1!!.setText((waterBean as Water3_3).tv_3_1_1)
                tv_3_1_2!!.setText((waterBean as Water3_3).tv_3_1_2)
                et_3_1_3!!.setText((waterBean as Water3_3).tv_3_1_3)
                et_3_1_4!!.setText((waterBean as Water3_3).tv_3_1_4)
                et_3_1_5!!.setText((waterBean as Water3_3).tv_3_1_5)
                tv_3_1_6!!.setText((waterBean as Water3_3).tv_3_1_6)
                et_3_1_7!!.setText((waterBean as Water3_3).tv_3_1_7)

            }

            WATER_TYPE_3_4 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val tv_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)
                val et_3_1_4 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)
                val et_3_1_5 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)
                val et_3_1_6 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.et_3_1_6)
                et_3_1_1!!.setText((waterBean as Water3_4).tv_3_1_1)
                tv_3_1_2!!.setText((waterBean as Water3_4).tv_3_1_2)
                et_3_1_3!!.setText((waterBean as Water3_4).tv_3_1_3)
                et_3_1_4!!.setText((waterBean as Water3_4).tv_3_1_4)
                et_3_1_5!!.setText((waterBean as Water3_4).tv_3_1_5)
                et_3_1_6!!.setText((waterBean as Water3_4).tv_3_1_6)
            }

            WATER_TYPE_3_5 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val tv_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)
                et_3_1_1!!.setText((waterBean as Water3_5).tv_3_1_1)
                tv_3_1_2!!.setText((waterBean as Water3_5).tv_3_1_2)
                et_3_1_3!!.setText((waterBean as Water3_5).tv_3_1_3)
            }

            WATER_TYPE_3_6 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val tv_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)
                et_3_1_1!!.setText((waterBean as Water3_6).tv_3_1_1)
                tv_3_1_2!!.setText((waterBean as Water3_6).tv_3_1_2)
                et_3_1_3!!.setText((waterBean as Water3_6).tv_3_1_3)
            }

            WATER_TYPE_3_7 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val tv_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)
                val et_3_1_4 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)
                et_3_1_1!!.setText((waterBean as Water3_7).tv_3_1_1)
                tv_3_1_2!!.setText((waterBean as Water3_7).tv_3_1_2)
                et_3_1_3!!.setText((waterBean as Water3_7).tv_3_1_3)
                et_3_1_4!!.setText((waterBean as Water3_7).tv_3_1_4)
            }

            WATER_TYPE_3_9 -> {
                //时间和地点
                val tv_update_2_2_1 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_2_1)
                val tv_update_2_2_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_update_2_2_2)
                tv_update_2_2_1!!.text =
                    (waterBean as Water2_5).tv_1_7_1 + " " + (waterBean as Water2_5).tv_1_7_2
                tv_update_2_2_2!!.text = (waterBean as Water2_5).tv_1_7_3
            }

            WATER_TYPE_3_10 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val tv_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)
                et_3_1_1!!.setText((waterBean as Water3_10).tv_3_1_1)
                tv_3_1_2!!.setText((waterBean as Water3_10).tv_3_1_2)
                et_3_1_3!!.setText((waterBean as Water3_10).tv_3_1_3)
            }

            WATER_TYPE_3_11 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val et_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.et_3_1_3)
                et_3_1_1!!.setText((waterBean as Water3_11).tv_3_1_1)
                et_3_1_2!!.setText((waterBean as Water3_11).tv_3_1_2)
                et_3_1_3!!.setText((waterBean as Water3_11).tv_3_1_3)
            }

            WATER_TYPE_3_12 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val tv_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_2)
                val tv_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_3)
                val et_3_1_4 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)
                val et_3_1_5 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)
                val et_3_1_6 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_6)
                val et_3_1_7 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_7)

                et_3_1_1!!.setText((waterBean as Water3_12).tv_3_1_1)
                tv_3_1_2!!.setText((waterBean as Water3_12).tv_3_1_2)
                tv_3_1_3!!.setText((waterBean as Water3_12).tv_3_1_3)
                et_3_1_4!!.setText((waterBean as Water3_12).tv_3_1_4)
                et_3_1_5!!.setText((waterBean as Water3_12).tv_3_1_5)
                et_3_1_6!!.setText((waterBean as Water3_12).tv_3_1_6)
                et_3_1_7!!.setText((waterBean as Water3_12).tv_3_1_7)

            }

            WATER_TYPE_4_1 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val tv_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)
                val tv_3_1_4 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_4)
                val et_3_1_5 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)
                val tv_3_1_6 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_6)

                et_3_1_1!!.setText((waterBean as Water4_1).tv_3_1_1)
                tv_3_1_2!!.setText((waterBean as Water4_1).tv_3_1_2)
                et_3_1_3!!.setText((waterBean as Water4_1).tv_3_1_3)
                tv_3_1_4!!.setText((waterBean as Water4_1).tv_3_1_4)
                et_3_1_5!!.setText((waterBean as Water4_1).tv_3_1_5)
                tv_3_1_6!!.setText((waterBean as Water4_1).tv_3_1_6)
            }

            WATER_TYPE_4_2 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val tv_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)
                val tv_3_1_4 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_4)
                et_3_1_1!!.setText((waterBean as Water4_2).tv_3_1_1)
                tv_3_1_2!!.setText((waterBean as Water4_2).tv_3_1_2)
                et_3_1_3!!.setText((waterBean as Water4_2).tv_3_1_3)
                tv_3_1_4!!.setText((waterBean as Water4_2).tv_3_1_4)
            }

            WATER_TYPE_4_3 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val tv_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)
                val tv_3_1_4 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_4)
                val et_3_1_5 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)
                val tv_3_1_6 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_6)

                et_3_1_1!!.setText((waterBean as Water4_3).tv_3_1_1)
                tv_3_1_2!!.setText((waterBean as Water4_3).tv_3_1_2)
                et_3_1_3!!.setText((waterBean as Water4_3).tv_3_1_3)
                tv_3_1_4!!.setText((waterBean as Water4_3).tv_3_1_4)
                et_3_1_5!!.setText((waterBean as Water4_3).tv_3_1_5)
                tv_3_1_6!!.setText((waterBean as Water4_3).tv_3_1_6)
            }

            WATER_TYPE_4_4 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val et_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)
                val et_3_1_4 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)
                val et_3_1_5 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_5)
                val tv_3_1_6 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_6)

                et_3_1_1!!.setText((waterBean as Water4_4).tv_3_1_1)
                et_3_1_2!!.setText((waterBean as Water4_4).tv_3_1_2)
                et_3_1_3!!.setText((waterBean as Water4_4).tv_3_1_3)
                et_3_1_4!!.setText((waterBean as Water4_4).tv_3_1_4)
                et_3_1_5!!.setText((waterBean as Water4_4).tv_3_1_5)
                tv_3_1_6!!.setText((waterBean as Water4_4).tv_3_1_6)
            }

            WATER_TYPE_4_5 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val et_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)
                val et_3_1_4 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_4)
                val tv_3_1_5 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_5)
                val tv_3_1_6 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_6)

                et_3_1_1!!.setText((waterBean as Water4_5).tv_3_1_1)
                et_3_1_2!!.setText((waterBean as Water4_5).tv_3_1_2)
                et_3_1_3!!.setText((waterBean as Water4_5).tv_3_1_3)
                et_3_1_4!!.setText((waterBean as Water4_5).tv_3_1_4)
                tv_3_1_5!!.setText((waterBean as Water4_5).tv_3_1_5)
                tv_3_1_6!!.setText((waterBean as Water4_5).tv_3_1_6)
            }

            WATER_TYPE_4_6 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val tv_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)

                et_3_1_1!!.setText((waterBean as Water4_6).tv_3_1_1)
                tv_3_1_2!!.setText((waterBean as Water4_6).tv_3_1_3)
                et_3_1_3!!.setText((waterBean as Water4_6).tv_3_1_4)
            }
            WATER_TYPE_4_7 -> {
                //时间和地点
                val et_3_1_1 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_1)
                val tv_3_1_2 =
                    updateWaterBottomSheetDialog.findViewById<TextView>(R.id.tv_3_1_2)
                val et_3_1_3 =
                    updateWaterBottomSheetDialog.findViewById<EditText>(R.id.et_3_1_3)

                et_3_1_1!!.setText((waterBean as Water4_7).tv_3_1_1)
                tv_3_1_2!!.setText((waterBean as Water4_7).tv_3_1_3)
                et_3_1_3!!.setText((waterBean as Water4_7).tv_3_1_4)
            }
        }
    }

    private fun loadUpdateWater() {
        if (isFirstUpdate) {
            Log.d("isFirstUpdate", "loadUpdateWater1_1:isFirstUpdate ")
            container2.removeAllViews()
        }
        // 获取预留的容器
        container2 = binding.cameraUpdateWater
        // 加载左下角的布局
        inflater2 = layoutInflater
        loadleftCornerView2()
        // 将布局添加到容器中
        container2.addView(leftCornerView2)
        isFirstUpdate = true
    }

    private fun loadleftCornerView2() {
        //TODO:初始化preview
        when (mCurrentType) {
            WATER_TYPE_1_1 -> {
                leftCornerView2 =
                    inflater2.inflate(R.layout.camera_water_mark_base_longitude, container2, false)
                tv_preview_1_1_1 = leftCornerView2.findViewById(R.id.tv_base_longitude1)
                tv_preview_1_1_2 = leftCornerView2.findViewById(R.id.tv_base_longitude2)
                tv_preview_1_1_3 = leftCornerView2.findViewById(R.id.tv_base_longitude3)
                tv_preview_1_1_4 = leftCornerView2.findViewById(R.id.tv_base_longitude4)
                updateUiPreview1_1()
            }

            WATER_TYPE_1_2 -> {
                leftCornerView2 =
                    inflater2.inflate(R.layout.camera_water_mark_base_local, container2, false)
                tv_preview_1_2_1 = leftCornerView2.findViewById(R.id.tv_base_local1)
                tv_preview_1_2_2 = leftCornerView2.findViewById(R.id.tv_base_local2)
                updateUiPreview1_2()

            }

            WATER_TYPE_1_3 -> {
                leftCornerView2 =
                    inflater2.inflate(R.layout.camera_water_mark_base_mood, container2, false)
                tv_preview_1_3_1 = leftCornerView2.findViewById(R.id.tv_base_mood1)
                tv_preview_1_3_2 = leftCornerView2.findViewById(R.id.tv_base_mood2)
                updateUiPreview1_3()
            }

            WATER_TYPE_1_4 -> {
                leftCornerView2 =
                    inflater2.inflate(R.layout.camera_water_mark_base_weather, container2, false)
                tv_preview_1_4_1 = leftCornerView2.findViewById(R.id.tv_base_weather1)
                tv_preview_1_4_2 = leftCornerView2.findViewById(R.id.tv_base_weather2)
                tv_preview_1_4_3 = leftCornerView2.findViewById(R.id.tv_base_weather3)
                updateUiPreview1_4()
            }

            WATER_TYPE_1_5 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_base_common_time,
                        container2,
                        false
                    )
                tv_preview_1_5_1_shi = leftCornerView2.findViewById(R.id.tv_base_common_time_shi)
                tv_preview_1_5_2_fen = leftCornerView2.findViewById(R.id.tv_base_common_time_fen)
                tv_preview_1_5_3_miao = leftCornerView2.findViewById(R.id.tv_base_common_time_miao)
                tv_preview_1_5_4 = leftCornerView2.findViewById(R.id.tv_base_total_time)
                updateUiPreview1_5()
            }

            WATER_TYPE_1_6 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_base_memorial,
                        container2,
                        false
                    )
                tv_preview_1_6_1 = leftCornerView2.findViewById(R.id.tv_base_memorial_time1)
                tv_preview_1_6_2 = leftCornerView2.findViewById(R.id.tv_base_memorial_time2)
                tv_preview_1_6_3 = leftCornerView2.findViewById(R.id.tv_base_memorial_time3)
                tv_preview_1_6_4 = leftCornerView2.findViewById(R.id.tv_base_memorial_time4)
                tv_preview_1_6_5 = leftCornerView2.findViewById(R.id.tv_base_memorial_time5)
                tv_preview_1_6_6 = leftCornerView2.findViewById(R.id.tv_base_memorial_time6)
                line_preview_day_1_6 = leftCornerView2.findViewById(R.id.line_day_1_6)
                updateUiPreview1_6()
            }

            WATER_TYPE_1_7 -> {
                leftCornerView2 =
                    inflater2.inflate(R.layout.camera_water_mark_base_birthday, container2, false)
                tv_preview_1_7_1 = leftCornerView2.findViewById(R.id.tv_base_birthday1)
                tv_preview_1_7_2 = leftCornerView2.findViewById(R.id.tv_base_birthday2)
                tv_preview_1_7_3 = leftCornerView2.findViewById(R.id.tv_base_birthday3)
                line_preview_day_1_7 = leftCornerView2.findViewById(R.id.line_birty_day_1_7)
                updateUiPreview1_7()
            }

            WATER_TYPE_2_1 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_attendance_work1,
                        container2,
                        false
                    )
                tv_preview_2_1_1 = leftCornerView2.findViewById(R.id.tv_attendance_work_2_1_1)
                tv_preview_2_1_2 = leftCornerView2.findViewById(R.id.tv_attendance_work_2_1_2)
                tv_preview_2_1_3 = leftCornerView2.findViewById(R.id.tv_attendance_work_2_1_3)
                tv_preview_2_1_4 = leftCornerView2.findViewById(R.id.tv_attendance_work_2_1_4)
                updateUiPreview2_1()
            }

            WATER_TYPE_2_2 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_attendance_work2,
                        container2,
                        false
                    )
                tv_preview_2_2_1 = leftCornerView2.findViewById(R.id.tv_water_work2_time1)
                tv_preview_2_2_2 = leftCornerView2.findViewById(R.id.tv_water_work2_time2)
                tv_preview_2_2_3 = leftCornerView2.findViewById(R.id.tv_water_work2_time3)
                tv_preview_2_2_4 = leftCornerView2.findViewById(R.id.tv_water_attendance_time1)
                tv_preview_2_2_5 = leftCornerView2.findViewById(R.id.tv_water_attendance_time2)
                cons_preview_work2 = leftCornerView2.findViewById(R.id.cons_work2)
                updateUiPreview2_2()
            }

            WATER_TYPE_2_3 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_attendance_work3,
                        container2,
                        false
                    )
                tv_preview_1_7_1 = leftCornerView2.findViewById(R.id.tv_water_attendance_work3_tv1)
                tv_preview_1_7_2 = leftCornerView2.findViewById(R.id.tv_water_attendance_work3_tv2)
                tv_preview_1_7_3 = leftCornerView2.findViewById(R.id.tv_water_attendance_work3_tv3)
                updateUiPreview2_3()
            }

            WATER_TYPE_2_4 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_attendance_work4,
                        container2,
                        false
                    )
                tv_preview_1_7_1 = leftCornerView2.findViewById(R.id.tv_water_attendance_work4_tv1)
                tv_preview_1_7_2 = leftCornerView2.findViewById(R.id.tv_water_attendance_work4_tv2)
                tv_preview_1_7_3 = leftCornerView2.findViewById(R.id.tv_water_attendance_work4_tv3)
                updateUiPreview2_4()
            }

            WATER_TYPE_2_5 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_attendance_work5,
                        container2,
                        false
                    )
                tv_preview_1_7_1 =
                    leftCornerView2.findViewById(R.id.water_attendance_attendance1_tv1)
                tv_preview_1_7_2 =
                    leftCornerView2.findViewById(R.id.tv_water_attendance_attendance_tv1)
                tv_preview_1_7_3 =
                    leftCornerView2.findViewById(R.id.tv_water_attendance_attendance_tv2)
                line_birty_day_1_7 = leftCornerView2.findViewById(R.id.line_work5)
                updateUiPreview2_5()
            }

            WATER_TYPE_2_6 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_attendance_work6,
                        container2,
                        false
                    )
                tv_preview_1_7_1 =
                    leftCornerView2.findViewById(R.id.tv_water_attendance_attendance2_tv2)
                tv_preview_1_7_2 =
                    leftCornerView2.findViewById(R.id.tv_water_attendance_attendance2_tv3)
                tv_preview_1_7_3 =
                    leftCornerView2.findViewById(R.id.tv_water_attendance_attendance2_tv4)
                updateUiPreview2_6()
            }

            WATER_TYPE_2_7 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_attendance_work7,
                        container2,
                        false
                    )
                tv_preview_2_2_1 =
                    leftCornerView2.findViewById(R.id.tv_water_attendance_attendance3_tv1)
                tv_preview_2_2_2 =
                    leftCornerView2.findViewById(R.id.tv_water_attendance_attendance3_tv2)
                tv_preview_2_2_3 =
                    leftCornerView2.findViewById(R.id.tv_water_attendance_attendance3_tv3)
                tv_preview_2_2_4 =
                    leftCornerView2.findViewById(R.id.tv_water_attendance_attendance3_tv4)
                tv_preview_2_2_5 = leftCornerView2.findViewById(R.id.tv_work7)
                cons_preview_work2 = leftCornerView2.findViewById(R.id.cons_work7)
                updateUiPreview2_7()
            }

            WATER_TYPE_2_8 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_attendance_work8,
                        container2,
                        false
                    )
                tv_preview_1_7_1 = leftCornerView2.findViewById(R.id.tv_work8_1)
                tv_preview_1_7_2 =
                    leftCornerView2.findViewById(R.id.tv_water_attendance_attendance2_tv3)
                tv_preview_1_7_3 =
                    leftCornerView2.findViewById(R.id.tv_water_attendance_attendance2_tv4)
                updateUiPreview2_8()
            }

            WATER_TYPE_3_1 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_construction1,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                tv_preview_3_1_4 = leftCornerView2.findViewById(R.id.construction1_real_tv4)
                tv_preview_3_1_5 = leftCornerView2.findViewById(R.id.construction1_real_tv5)
                tv_preview_3_1_6 = leftCornerView2.findViewById(R.id.construction1_real_tv6)
                tv_preview_3_1_7 = leftCornerView2.findViewById(R.id.construction1_real_tv7)
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                line2_preview = leftCornerView2.findViewById(R.id.visible2)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                line4_preview = leftCornerView2.findViewById(R.id.visible4)
                line5_preview = leftCornerView2.findViewById(R.id.visible5)
                line6_preview = leftCornerView2.findViewById(R.id.visible6)
                line7_preview = leftCornerView2.findViewById(R.id.visible7)
                updateUiPreview3_1()
            }

            WATER_TYPE_3_2 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_construction1,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                tv_preview_3_1_4 = leftCornerView2.findViewById(R.id.construction1_real_tv4)
                tv_preview_3_1_5 = leftCornerView2.findViewById(R.id.construction1_real_tv5)
                tv_preview_3_1_6 = leftCornerView2.findViewById(R.id.construction1_real_tv6)
                tv_preview_3_1_7 = leftCornerView2.findViewById(R.id.construction1_real_tv7)
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                line2_preview = leftCornerView2.findViewById(R.id.visible2)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                line4_preview = leftCornerView2.findViewById(R.id.visible4)
                line5_preview = leftCornerView2.findViewById(R.id.visible5)
                line6_preview = leftCornerView2.findViewById(R.id.visible6)
                line7_preview = leftCornerView2.findViewById(R.id.visible7)
                updateUiPreview3_2()
            }

            WATER_TYPE_3_3 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_construction1,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                tv_preview_3_1_4 = leftCornerView2.findViewById(R.id.construction1_real_tv4)
                tv_preview_3_1_5 = leftCornerView2.findViewById(R.id.construction1_real_tv5)
                tv_preview_3_1_6 = leftCornerView2.findViewById(R.id.construction1_real_tv6)
                tv_preview_3_1_7 = leftCornerView2.findViewById(R.id.construction1_real_tv7)
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                line2_preview = leftCornerView2.findViewById(R.id.visible2)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                line4_preview = leftCornerView2.findViewById(R.id.visible4)
                line5_preview = leftCornerView2.findViewById(R.id.visible5)
                line6_preview = leftCornerView2.findViewById(R.id.visible6)
                line7_preview = leftCornerView2.findViewById(R.id.visible7)
                updateUiPreview3_3()
            }

            WATER_TYPE_3_4 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_construction4,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                tv_preview_3_1_4 = leftCornerView2.findViewById(R.id.construction1_real_tv4)
                tv_preview_3_1_5 = leftCornerView2.findViewById(R.id.construction1_real_tv5)
                tv_preview_3_1_6 = leftCornerView2.findViewById(R.id.construction1_real_tv6)
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                line2_preview = leftCornerView2.findViewById(R.id.visible2)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                line4_preview = leftCornerView2.findViewById(R.id.visible4)
                line5_preview = leftCornerView2.findViewById(R.id.visible5)
                line6_preview = leftCornerView2.findViewById(R.id.visible6)
                updateUiPreview3_4()
            }

            WATER_TYPE_3_5 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_construction5,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                line2_preview = leftCornerView2.findViewById(R.id.visible2)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                updateUiPreview3_5()
            }

            WATER_TYPE_3_6 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_construction6,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                line2_preview = leftCornerView2.findViewById(R.id.visible2)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                updateUiPreview3_6()
            }

            WATER_TYPE_3_7 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_construction7,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                tv_preview_3_1_4 = leftCornerView2.findViewById(R.id.construction1_real_tv4)
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                line2_preview = leftCornerView2.findViewById(R.id.visible2)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                line4_preview = leftCornerView2.findViewById(R.id.visible4)
                updateUiPreview3_7()
            }

            WATER_TYPE_3_8 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_construction8,
                        container2,
                        false
                    )
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                updateUiPreview3_8()
            }

            WATER_TYPE_3_9 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_attendance_work5,
                        container2,
                        false
                    )
                tv_preview_1_7_1 =
                    leftCornerView2.findViewById(R.id.water_attendance_attendance1_tv1)
                tv_preview_1_7_2 =
                    leftCornerView2.findViewById(R.id.tv_water_attendance_attendance_tv1)
                tv_preview_1_7_3 =
                    leftCornerView2.findViewById(R.id.tv_water_attendance_attendance_tv2)
                line_birty_day_1_7 = leftCornerView2.findViewById(R.id.line_work5)
                updateUiPreview2_5()
            }

            WATER_TYPE_3_10 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_construction10,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                line2_preview = leftCornerView2.findViewById(R.id.visible2)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                updateUiPreview3_10()
            }

            WATER_TYPE_3_11 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_construction11,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                line2_preview = leftCornerView2.findViewById(R.id.visible2)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                updateUiPreview3_11()
            }

            WATER_TYPE_3_12 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_construction12,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                tv_preview_3_1_4 = leftCornerView2.findViewById(R.id.construction1_real_tv4)
                tv_preview_3_1_5 = leftCornerView2.findViewById(R.id.construction1_real_tv5)
                tv_preview_3_1_6 = leftCornerView2.findViewById(R.id.construction1_real_tv6)
                tv_preview_3_1_7 = leftCornerView2.findViewById(R.id.construction1_real_tv7)
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                line2_preview = leftCornerView2.findViewById(R.id.visible2)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                line4_preview = leftCornerView2.findViewById(R.id.visible4)
                line5_preview = leftCornerView2.findViewById(R.id.visible5)
                line6_preview = leftCornerView2.findViewById(R.id.visible6)
                line7_preview = leftCornerView2.findViewById(R.id.visible7)
                updateUiPreview3_12()
            }

            WATER_TYPE_4_1 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_estate1,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                tv_preview_3_1_4 = leftCornerView2.findViewById(R.id.construction1_real_tv4)
                tv_preview_3_1_5 = leftCornerView2.findViewById(R.id.construction1_real_tv5)
                tv_preview_3_1_6 = leftCornerView2.findViewById(R.id.construction1_real_tv6)
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                line2_preview = leftCornerView2.findViewById(R.id.visible2)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                line4_preview = leftCornerView2.findViewById(R.id.visible4)
                line5_preview = leftCornerView2.findViewById(R.id.visible5)
                line6_preview = leftCornerView2.findViewById(R.id.visible6)
                updateUiPreview4_1()
            }

            WATER_TYPE_4_2 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_estate2,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                tv_preview_3_1_4 = leftCornerView2.findViewById(R.id.construction1_real_tv4)
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                line2_preview = leftCornerView2.findViewById(R.id.visible2)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                line4_preview = leftCornerView2.findViewById(R.id.visible4)
                updateUiPreview4_2()
            }

            WATER_TYPE_4_3 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_estate3,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                tv_preview_3_1_4 = leftCornerView2.findViewById(R.id.construction1_real_tv4)
                tv_preview_3_1_5 = leftCornerView2.findViewById(R.id.construction1_real_tv5)
                tv_preview_3_1_6 = leftCornerView2.findViewById(R.id.construction1_real_tv6)
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                line2_preview = leftCornerView2.findViewById(R.id.visible2)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                line4_preview = leftCornerView2.findViewById(R.id.visible4)
                line5_preview = leftCornerView2.findViewById(R.id.visible5)
                line6_preview = leftCornerView2.findViewById(R.id.visible6)
                updateUiPreview4_3()
            }

            WATER_TYPE_4_4 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_estate4,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                tv_preview_3_1_4 = leftCornerView2.findViewById(R.id.construction1_real_tv4)
                tv_preview_3_1_5 = leftCornerView2.findViewById(R.id.construction1_real_tv5)
                tv_preview_3_1_6 = leftCornerView2.findViewById(R.id.construction1_real_tv6)
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                line2_preview = leftCornerView2.findViewById(R.id.visible2)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                line4_preview = leftCornerView2.findViewById(R.id.visible4)
                line5_preview = leftCornerView2.findViewById(R.id.visible5)
                line6_preview = leftCornerView2.findViewById(R.id.visible6)
                updateUiPreview4_4()
            }

            WATER_TYPE_4_5 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_estate5,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                tv_preview_3_1_4 = leftCornerView2.findViewById(R.id.construction1_real_tv4)
                tv_preview_3_1_5 = leftCornerView2.findViewById(R.id.construction1_real_tv5)
                tv_preview_3_1_6 = leftCornerView2.findViewById(R.id.construction1_real_tv6)
                line1_preview = leftCornerView2.findViewById(R.id.visible1)
                line2_preview = leftCornerView2.findViewById(R.id.visible2)
                line3_preview = leftCornerView2.findViewById(R.id.visible3)
                line4_preview = leftCornerView2.findViewById(R.id.visible4)
                line5_preview = leftCornerView2.findViewById(R.id.visible5)
                line6_preview = leftCornerView2.findViewById(R.id.visible6)
                updateUiPreview4_5()
            }

            WATER_TYPE_4_6 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_estate6,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                tv_preview_3_1_4 = leftCornerView2.findViewById(R.id.construction1_real_tv4)
                updateUiPreview4_6()
            }
            WATER_TYPE_4_7 -> {
                leftCornerView2 =
                    inflater2.inflate(
                        R.layout.camera_water_mark_estate7,
                        container2,
                        false
                    )
                tv_preview_3_1_1 = leftCornerView2.findViewById(R.id.construction1_real_tv1)
                tv_preview_3_1_2 = leftCornerView2.findViewById(R.id.construction1_real_tv2)
                tv_preview_3_1_3 = leftCornerView2.findViewById(R.id.construction1_real_tv3)
                tv_preview_3_1_4 = leftCornerView2.findViewById(R.id.construction1_real_tv4)
                updateUiPreview4_7()
            }
        }
        initPreviewSuccessful = true
    }

    private fun updateUiPreview1_1() {
        tv_preview_1_1_1.visibility =
            (waterBean as Water1_1).tv_1_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_1_2.visibility =
            (waterBean as Water1_1).tv_1_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_1_3.visibility =
            (waterBean as Water1_1).tv_1_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_1_4.visibility =
            (waterBean as Water1_1).tv_1_1_4_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_1_1.text = (waterBean as Water1_1).tv_1_1_1
        tv_preview_1_1_2.text = (waterBean as Water1_1).tv_1_1_2
        tv_preview_1_1_3.text = (waterBean as Water1_1).tv_1_1_3
        tv_preview_1_1_4.text = (waterBean as Water1_1).tv_1_1_4
    }

    private fun updateUiPreview1_7() {
        tv_preview_1_7_1.visibility =
            (waterBean as Water1_7).tv_1_7_1_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_7_2.visibility =
            (waterBean as Water1_7).tv_1_7_2_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_7_3.visibility =
            (waterBean as Water1_7).tv_1_7_3_View.let { if (it) View.VISIBLE else View.GONE }
        line_preview_day_1_7.visibility =
            (waterBean as Water1_7).tv_1_7_1_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_7_1.text = (waterBean as Water1_7).tv_1_7_1
        tv_preview_1_7_2.text = (waterBean as Water1_7).tv_1_7_2
        tv_preview_1_7_3.text = (waterBean as Water1_7).tv_1_7_3
    }

    private fun updateUiPreview1_6() {
        tv_preview_1_6_1.visibility =
            (waterBean as Water1_6).tv_1_6_1_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_6_2.visibility =
            (waterBean as Water1_6).tv_1_6_2_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_6_3.visibility =
            (waterBean as Water1_6).tv_1_6_3_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_6_4.visibility =
            (waterBean as Water1_6).tv_1_6_4_View.let { if (it) View.VISIBLE else View.GONE }
        line_preview_day_1_6.visibility =
            (waterBean as Water1_6).tv_1_6_4_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_6_5.visibility =
            (waterBean as Water1_6).tv_1_6_5_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_6_6.visibility =
            (waterBean as Water1_6).tv_1_6_6_View.let { if (it) View.VISIBLE else View.GONE }

        tv_preview_1_6_1.text = (waterBean as Water1_6).tv_1_6_1
        tv_preview_1_6_2.text = (waterBean as Water1_6).tv_1_6_2
        tv_preview_1_6_3.text = (waterBean as Water1_6).tv_1_6_3
        tv_preview_1_6_4.text = (waterBean as Water1_6).tv_1_6_4
        tv_preview_1_6_5.text = (waterBean as Water1_6).tv_1_6_5
        tv_preview_1_6_6.text = (waterBean as Water1_6).tv_1_6_6
    }

    private fun updateUiPreview2_1() {
        tv_preview_2_1_1.visibility =
            (waterBean as Water2_1).tv_2_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_2_1_2.visibility =
            (waterBean as Water2_1).tv_2_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_2_1_3.visibility =
            (waterBean as Water2_1).tv_2_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_2_1_4.visibility =
            (waterBean as Water2_1).tv_2_1_4_View.let { if (it) View.VISIBLE else View.GONE }
        Log.d(TAG, "updateUiPreview2_1: ${waterBean.toString()}")
        tv_preview_2_1_1.text = (waterBean as Water2_1).tv_2_1_1
        tv_preview_2_1_2.text = (waterBean as Water2_1).tv_2_1_2
        tv_preview_2_1_3.text = (waterBean as Water2_1).tv_2_1_3
        tv_preview_2_1_4.text = (waterBean as Water2_1).tv_2_1_4
    }

    private fun updateUiPreview2_2() {
        tv_preview_2_2_1.visibility =
            (waterBean as Water2_2).tv_2_2_1_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_2_2_2.visibility =
            (waterBean as Water2_2).tv_2_2_2_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_2_2_3.visibility =
            (waterBean as Water2_2).tv_2_2_3_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_2_2_4.visibility =
            (waterBean as Water2_2).tv_2_2_4_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_2_2_5.visibility =
            (waterBean as Water2_2).tv_2_2_5_View.let { if (it) View.VISIBLE else View.GONE }
        cons_preview_work2.visibility =
            (waterBean as Water2_2).tv_2_2_1_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_2_2_1.text = (waterBean as Water2_2).tv_2_2_1
        tv_preview_2_2_2.text = (waterBean as Water2_2).tv_2_2_2
        tv_preview_2_2_3.text = (waterBean as Water2_2).tv_2_2_3
        tv_preview_2_2_4.text = (waterBean as Water2_2).tv_2_2_4
        tv_preview_2_2_5.text = (waterBean as Water2_2).tv_2_2_5
    }

    private fun updateUiPreview2_3() {
        tv_preview_1_7_1.visibility =
            (waterBean as Water2_3).tv_1_7_1_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_7_2.visibility =
            (waterBean as Water2_3).tv_1_7_2_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_7_3.visibility =
            (waterBean as Water2_3).tv_1_7_3_View.let { if (it) View.VISIBLE else View.GONE }

        tv_preview_1_7_1.text = (waterBean as Water2_3).tv_1_7_1
        tv_preview_1_7_2.text = (waterBean as Water2_3).tv_1_7_2
        tv_preview_1_7_3.text = (waterBean as Water2_3).tv_1_7_3
    }

    private fun updateUiPreview2_4() {
        tv_preview_1_7_1.visibility =
            (waterBean as Water2_4).tv_1_7_1_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_7_2.visibility =
            (waterBean as Water2_4).tv_1_7_2_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_7_3.visibility =
            (waterBean as Water2_4).tv_1_7_3_View.let { if (it) View.VISIBLE else View.GONE }

        tv_preview_1_7_1.text = (waterBean as Water2_4).tv_1_7_1
        tv_preview_1_7_2.text = (waterBean as Water2_4).tv_1_7_2
        tv_preview_1_7_3.text = (waterBean as Water2_4).tv_1_7_3
    }

    private fun updateUiPreview2_5() {
        line_birty_day_1_7.visibility =
            (waterBean as Water2_5).tv_1_7_3_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_7_1.visibility =
            (waterBean as Water2_5).tv_1_7_1_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_7_2.visibility =
            (waterBean as Water2_5).tv_1_7_2_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_7_3.visibility =
            (waterBean as Water2_5).tv_1_7_3_View.let { if (it) View.VISIBLE else View.GONE }

        tv_preview_1_7_1.text = (waterBean as Water2_5).tv_1_7_1
        tv_preview_1_7_2.text = (waterBean as Water2_5).tv_1_7_2
        tv_preview_1_7_3.text = (waterBean as Water2_5).tv_1_7_3
    }

    private fun updateUiPreview2_7() {
        Log.d(TAG, "updateUiPreview2_7: ${waterBean.toString()}")
        cons_preview_work2.visibility =
            (waterBean as Water2_7).tv_2_2_1_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_2_2_1.visibility =
            (waterBean as Water2_7).tv_2_2_1_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_2_2_2.visibility =
            (waterBean as Water2_7).tv_2_2_2_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_2_2_3.visibility =
            (waterBean as Water2_7).tv_2_2_3_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_2_2_4.visibility =
            (waterBean as Water2_7).tv_2_2_4_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_2_2_5.visibility =
            (waterBean as Water2_7).tv_2_2_5_View.let { if (it) View.VISIBLE else View.GONE }

        tv_preview_2_2_1.text = (waterBean as Water2_7).tv_2_2_1
        tv_preview_2_2_2.text = (waterBean as Water2_7).tv_2_2_2
        tv_preview_2_2_3.text = (waterBean as Water2_7).tv_2_2_3
        tv_preview_2_2_4.text = (waterBean as Water2_7).tv_2_2_4
        tv_preview_2_2_5.text = (waterBean as Water2_7).tv_2_2_5
    }

    private fun updateUiPreview2_6() {
        tv_preview_1_7_2.visibility =
            (waterBean as Water2_6).tv_1_7_2_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_7_3.visibility =
            (waterBean as Water2_6).tv_1_7_3_View.let { if (it) View.VISIBLE else View.GONE }

        tv_preview_1_7_1.text = (waterBean as Water2_6).tv_1_7_1
        tv_preview_1_7_2.text = (waterBean as Water2_6).tv_1_7_2
        tv_preview_1_7_3.text = (waterBean as Water2_6).tv_1_7_3
    }

    private fun updateUiPreview2_8() {
        tv_preview_1_7_2.visibility =
            (waterBean as Water2_8).tv_1_7_2_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_7_3.visibility =
            (waterBean as Water2_8).tv_1_7_3_View.let { if (it) View.VISIBLE else View.GONE }

        tv_preview_1_7_1.text = (waterBean as Water2_8).tv_1_7_1
        tv_preview_1_7_2.text = (waterBean as Water2_8).tv_1_7_2
        tv_preview_1_7_3.text = (waterBean as Water2_8).tv_1_7_3
    }

    private fun updateUiPreview3_1() {
        line1_preview.visibility =
            (waterBean as Water3_1).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        line2_preview.visibility =
            (waterBean as Water3_1).tv_3_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        line3_preview.visibility =
            (waterBean as Water3_1).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        line4_preview.visibility =
            (waterBean as Water3_1).tv_3_1_4_View.let { if (it) View.VISIBLE else View.GONE }
        line5_preview.visibility =
            (waterBean as Water3_1).tv_3_1_5_View.let { if (it) View.VISIBLE else View.GONE }
        line6_preview.visibility =
            (waterBean as Water3_1).tv_3_1_6_View.let { if (it) View.VISIBLE else View.GONE }
        line7_preview.visibility =
            (waterBean as Water3_1).tv_3_1_7_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water3_1).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water3_1).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water3_1).tv_3_1_3
        tv_preview_3_1_4.text = (waterBean as Water3_1).tv_3_1_4
        tv_preview_3_1_5.text = (waterBean as Water3_1).tv_3_1_5
        tv_preview_3_1_6.text = (waterBean as Water3_1).tv_3_1_6
        tv_preview_3_1_7.text = (waterBean as Water3_1).tv_3_1_7
    }

    private fun updateUiPreview3_2() {
        line1_preview.visibility =
            (waterBean as Water3_2).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        line2_preview.visibility =
            (waterBean as Water3_2).tv_3_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        line3_preview.visibility =
            (waterBean as Water3_2).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        line4_preview.visibility = View.GONE
        line5_preview.visibility =
            (waterBean as Water3_2).tv_3_1_5_View.let { if (it) View.VISIBLE else View.GONE }
        line6_preview.visibility =
            (waterBean as Water3_2).tv_3_1_6_View.let { if (it) View.VISIBLE else View.GONE }
        line7_preview.visibility =
            (waterBean as Water3_2).tv_3_1_7_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water3_2).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water3_2).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water3_2).tv_3_1_3
        tv_preview_3_1_4.text = (waterBean as Water3_2).tv_3_1_4
        tv_preview_3_1_5.text = (waterBean as Water3_2).tv_3_1_5
        tv_preview_3_1_6.text = (waterBean as Water3_2).tv_3_1_6
        tv_preview_3_1_7.text = (waterBean as Water3_2).tv_3_1_7
    }

    private fun updateUiPreview3_3() {
        line1_preview.visibility =
            (waterBean as Water3_3).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        line2_preview.visibility =
            (waterBean as Water3_3).tv_3_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        line3_preview.visibility =
            (waterBean as Water3_3).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        line4_preview.visibility =
            (waterBean as Water3_3).tv_3_1_4_View.let { if (it) View.VISIBLE else View.GONE }
        line5_preview.visibility =
            (waterBean as Water3_3).tv_3_1_5_View.let { if (it) View.VISIBLE else View.GONE }
        line6_preview.visibility =
            (waterBean as Water3_3).tv_3_1_6_View.let { if (it) View.VISIBLE else View.GONE }
        line7_preview.visibility =
            (waterBean as Water3_3).tv_3_1_7_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water3_3).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water3_3).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water3_3).tv_3_1_3
        tv_preview_3_1_4.text = (waterBean as Water3_3).tv_3_1_4
        tv_preview_3_1_5.text = (waterBean as Water3_3).tv_3_1_5
        tv_preview_3_1_6.text = (waterBean as Water3_3).tv_3_1_6
        tv_preview_3_1_7.text = (waterBean as Water3_3).tv_3_1_7
    }

    private fun updateUiPreview3_4() {
        line1_preview.visibility =
            (waterBean as Water3_4).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        line2_preview.visibility =
            (waterBean as Water3_4).tv_3_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        line3_preview.visibility =
            (waterBean as Water3_4).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        line4_preview.visibility =
            (waterBean as Water3_4).tv_3_1_4_View.let { if (it) View.VISIBLE else View.GONE }
        line5_preview.visibility =
            (waterBean as Water3_4).tv_3_1_5_View.let { if (it) View.VISIBLE else View.GONE }
        line6_preview.visibility =
            (waterBean as Water3_4).tv_3_1_6_View.let { if (it) View.VISIBLE else View.GONE }

        tv_preview_3_1_1.text = (waterBean as Water3_4).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water3_4).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water3_4).tv_3_1_3
        tv_preview_3_1_4.text = (waterBean as Water3_4).tv_3_1_4
        tv_preview_3_1_5.text = (waterBean as Water3_4).tv_3_1_5
        tv_preview_3_1_6.text = (waterBean as Water3_4).tv_3_1_6

    }

    private fun updateUiPreview3_5() {
        line1_preview.visibility =
            (waterBean as Water3_5).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        line2_preview.visibility =
            (waterBean as Water3_5).tv_3_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        line3_preview.visibility =
            (waterBean as Water3_5).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water3_5).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water3_5).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water3_5).tv_3_1_3
    }

    private fun updateUiPreview3_6() {
        line1_preview.visibility =
            (waterBean as Water3_6).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        line2_preview.visibility =
            (waterBean as Water3_6).tv_3_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        line3_preview.visibility =
            (waterBean as Water3_6).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water3_6).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water3_6).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water3_6).tv_3_1_3
    }

    private fun updateUiPreview3_7() {
        line1_preview.visibility =
            (waterBean as Water3_7).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        line2_preview.visibility =
            (waterBean as Water3_7).tv_3_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        line3_preview.visibility =
            (waterBean as Water3_7).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        line4_preview.visibility =
            (waterBean as Water3_7).tv_3_1_4_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water3_7).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water3_7).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water3_7).tv_3_1_3
        tv_preview_3_1_4.text = (waterBean as Water3_7).tv_3_1_4
    }

    private fun updateUiPreview3_8() {
        line1_preview.visibility =
            (waterBean as Water3_8).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
    }

    private fun updateUiPreview3_10() {
        line1_preview.visibility =
            (waterBean as Water3_10).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        line2_preview.visibility =
            (waterBean as Water3_10).tv_3_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        line3_preview.visibility =
            (waterBean as Water3_10).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water3_10).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water3_10).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water3_10).tv_3_1_3
    }

    private fun updateUiPreview3_11() {
        line1_preview.visibility =
            (waterBean as Water3_11).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        line2_preview.visibility =
            (waterBean as Water3_11).tv_3_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        line3_preview.visibility =
            (waterBean as Water3_11).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water3_11).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water3_11).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water3_11).tv_3_1_3
    }

    private fun updateUiPreview3_12() {
        line1_preview.visibility =
            (waterBean as Water3_12).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        line2_preview.visibility =
            (waterBean as Water3_12).tv_3_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        line3_preview.visibility =
            (waterBean as Water3_12).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        line4_preview.visibility =
            (waterBean as Water3_12).tv_3_1_4_View.let { if (it) View.VISIBLE else View.GONE }
        line5_preview.visibility =
            (waterBean as Water3_12).tv_3_1_5_View.let { if (it) View.VISIBLE else View.GONE }
        line6_preview.visibility =
            (waterBean as Water3_12).tv_3_1_6_View.let { if (it) View.VISIBLE else View.GONE }
        line7_preview.visibility =
            (waterBean as Water3_12).tv_3_1_7_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water3_12).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water3_12).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water3_12).tv_3_1_3
        tv_preview_3_1_4.text = (waterBean as Water3_12).tv_3_1_4
        tv_preview_3_1_5.text = (waterBean as Water3_12).tv_3_1_5
        tv_preview_3_1_6.text = (waterBean as Water3_12).tv_3_1_6
        tv_preview_3_1_7.text = (waterBean as Water3_12).tv_3_1_7
    }

    private fun updateUiPreview4_1() {
        line1_preview.visibility =
            (waterBean as Water4_1).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        line2_preview.visibility =
            (waterBean as Water4_1).tv_3_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        line3_preview.visibility =
            (waterBean as Water4_1).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        line4_preview.visibility =
            (waterBean as Water4_1).tv_3_1_4_View.let { if (it) View.VISIBLE else View.GONE }
        line5_preview.visibility =
            (waterBean as Water4_1).tv_3_1_5_View.let { if (it) View.VISIBLE else View.GONE }
        line6_preview.visibility =
            (waterBean as Water4_1).tv_3_1_6_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water4_1).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water4_1).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water4_1).tv_3_1_3
        tv_preview_3_1_4.text = (waterBean as Water4_1).tv_3_1_4
        tv_preview_3_1_5.text = (waterBean as Water4_1).tv_3_1_5
        tv_preview_3_1_6.text = (waterBean as Water4_1).tv_3_1_6
    }

    private fun updateUiPreview4_2() {
        line1_preview.visibility =
            (waterBean as Water4_2).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        line2_preview.visibility =
            (waterBean as Water4_2).tv_3_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        line3_preview.visibility =
            (waterBean as Water4_2).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        line4_preview.visibility =
            (waterBean as Water4_2).tv_3_1_4_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water4_2).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water4_2).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water4_2).tv_3_1_3
        tv_preview_3_1_4.text = (waterBean as Water4_2).tv_3_1_4
    }

    private fun updateUiPreview4_3() {
        line1_preview.visibility =
            (waterBean as Water4_3).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        line2_preview.visibility =
            (waterBean as Water4_3).tv_3_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        line3_preview.visibility =
            (waterBean as Water4_3).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        line4_preview.visibility =
            (waterBean as Water4_3).tv_3_1_4_View.let { if (it) View.VISIBLE else View.GONE }
        line5_preview.visibility =
            (waterBean as Water4_3).tv_3_1_5_View.let { if (it) View.VISIBLE else View.GONE }
        line6_preview.visibility =
            (waterBean as Water4_3).tv_3_1_6_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water4_3).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water4_3).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water4_3).tv_3_1_3
        tv_preview_3_1_4.text = (waterBean as Water4_3).tv_3_1_4
        tv_preview_3_1_5.text = (waterBean as Water4_3).tv_3_1_5
        tv_preview_3_1_6.text = (waterBean as Water4_3).tv_3_1_6
    }

    private fun updateUiPreview4_4() {
        line1_preview.visibility =
            (waterBean as Water4_4).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        line2_preview.visibility =
            (waterBean as Water4_4).tv_3_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        line3_preview.visibility =
            (waterBean as Water4_4).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        line4_preview.visibility =
            (waterBean as Water4_4).tv_3_1_4_View.let { if (it) View.VISIBLE else View.GONE }
        line5_preview.visibility =
            (waterBean as Water4_4).tv_3_1_5_View.let { if (it) View.VISIBLE else View.GONE }
        line6_preview.visibility =
            (waterBean as Water4_4).tv_3_1_6_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water4_4).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water4_4).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water4_4).tv_3_1_3
        tv_preview_3_1_4.text = (waterBean as Water4_4).tv_3_1_4
        tv_preview_3_1_5.text = (waterBean as Water4_4).tv_3_1_5
        tv_preview_3_1_6.text = (waterBean as Water4_4).tv_3_1_6
    }

    private fun updateUiPreview4_5() {
        line1_preview.visibility =
            (waterBean as Water4_5).tv_3_1_1_View.let { if (it) View.VISIBLE else View.GONE }
        line2_preview.visibility =
            (waterBean as Water4_5).tv_3_1_2_View.let { if (it) View.VISIBLE else View.GONE }
        line3_preview.visibility =
            (waterBean as Water4_5).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        line4_preview.visibility =
            (waterBean as Water4_5).tv_3_1_4_View.let { if (it) View.VISIBLE else View.GONE }
        line5_preview.visibility =
            (waterBean as Water4_5).tv_3_1_5_View.let { if (it) View.VISIBLE else View.GONE }
        line6_preview.visibility =
            (waterBean as Water4_5).tv_3_1_6_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water4_5).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water4_5).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water4_5).tv_3_1_3
        tv_preview_3_1_4.text = (waterBean as Water4_5).tv_3_1_4
        tv_preview_3_1_5.text = (waterBean as Water4_5).tv_3_1_5
        tv_preview_3_1_6.text = (waterBean as Water4_5).tv_3_1_6
    }

    private fun updateUiPreview4_6() {
        tv_preview_3_1_3.visibility =
            (waterBean as Water4_6).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_4.visibility =
            (waterBean as Water4_6).tv_3_1_4_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water4_6).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water4_6).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water4_6).tv_3_1_3
        tv_preview_3_1_4.text = (waterBean as Water4_6).tv_3_1_4

    }
    private fun updateUiPreview4_7() {
        tv_preview_3_1_3.visibility =
            (waterBean as Water4_7).tv_3_1_3_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_4.visibility =
            (waterBean as Water4_7).tv_3_1_4_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_3_1_1.text = (waterBean as Water4_7).tv_3_1_1
        tv_preview_3_1_2.text = (waterBean as Water4_7).tv_3_1_2
        tv_preview_3_1_3.text = (waterBean as Water4_7).tv_3_1_3
        tv_preview_3_1_4.text = (waterBean as Water4_7).tv_3_1_4

    }

    private fun updateUiPreview1_4() {
        tv_preview_1_4_1.visibility =
            (waterBean as Water1_4).tv_1_4_1_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_4_2.visibility =
            (waterBean as Water1_4).tv_1_4_2_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_4_3.visibility =
            (waterBean as Water1_4).tv_1_4_3_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_4_1.text = (waterBean as Water1_4).tv_1_4_1
        tv_preview_1_4_2.text = (waterBean as Water1_4).tv_1_4_2
        tv_preview_1_4_3.text = (waterBean as Water1_4).tv_1_4_3
    }

    private fun updateUiPreview1_5() {
        tv_preview_1_5_4.visibility =
            (waterBean as Water1_5).tv_1_5_4_View.let { if (it) View.VISIBLE else View.GONE }
        tv_preview_1_5_1_shi.text = (waterBean as Water1_5).tv_1_5_1_shi
        tv_preview_1_5_2_fen.text = (waterBean as Water1_5).tv_1_5_2_fen
        tv_preview_1_5_3_miao.text = (waterBean as Water1_5).tv_1_5_3_miao
        tv_preview_1_5_4.text = (waterBean as Water1_5).tv_1_5_4
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initTime() {
        mTime = TimeUtils.getCurrentFormattedTime2()
    }

    //TODO：东经纬度定位初始化initGaoDeSdk()
    private fun initGaoDeSdk() {
        locationService = LocationService(this)
        locationService.setOnLocationChangeListener(object : OnLocationChangeListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onLocationChange(
                altitude: Double,
                bearing: Float,
                latitude: Double,
                longitude: Double,
                local: String?,
                city: String?,
                district: String?,
                street: String?
            ) {
                // 定位成功，获取到经度和纬度
                Log.d("Location", "Latitude: $latitude, Longitude: $longitude, city: $city")
                mAltitude = altitude
                mBearing = bearing
                mLatitude = latitude
                mLongitude = longitude
                mLocal = local!!
                mCity = city!!
                mDistrict = district!!
                mStreet = street!!
                updateLatitudeAndLongitude()
                upDateUiWaterUi()
                initWeather()//气温
            }

            override fun onLocationError(errorCode: Int, errorMessage: String) {
                // 定位失败，打印错误信息
                Log.e("Location", "Error: $errorCode, Message: $errorMessage")
                ToastUtil.show(this@CameraActivity, "请打开定位权限", 500)
            }
        })
        locationService.startLocation()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUiWaterUi() {
        //TODO:初始化定位
        Log.d(TAG, "upDateUiWaterUi: $mCurrentType")
        when (mCurrentType) {
            WATER_TYPE_1_1 -> {
                upDateUi1_1()
            }

            WATER_TYPE_1_2 -> {
                upDateUi1_2()
            }

            WATER_TYPE_1_3 -> {
                upDateUi1_3()
            }

            WATER_TYPE_1_4 -> {
                upDateUi1_4()
            }

            WATER_TYPE_1_5 -> {
                upDateUi1_5()
            }

            WATER_TYPE_1_6 -> {
                upDateUi1_6()
            }

            WATER_TYPE_1_7 -> {
                upDateUi1_7()
            }

            WATER_TYPE_2_1 -> {
                upDateUi2_1()
            }

            WATER_TYPE_2_2 -> {
                upDateUi2_2()
            }

            WATER_TYPE_2_3 -> {
                upDateUi2_3()
            }

            WATER_TYPE_2_4 -> {
                upDateUi2_4()
            }

            WATER_TYPE_2_5 -> {
                upDateUi2_5()
            }

            WATER_TYPE_2_6 -> {
                upDateUi2_6()
            }

            WATER_TYPE_2_7 -> {
                upDateUi2_7()
            }

            WATER_TYPE_2_8 -> {
                upDateUi2_8()
            }

            WATER_TYPE_3_1 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi3_1()
                }
            }

            WATER_TYPE_3_2 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi3_2()
                }
            }

            WATER_TYPE_3_3 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi3_3()
                }
            }

            WATER_TYPE_3_4 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi3_4()
                }
            }

            WATER_TYPE_3_5 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi3_5()
                }
            }

            WATER_TYPE_3_6 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi3_6()
                }
            }

            WATER_TYPE_3_7 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi3_7()
                }
            }

            WATER_TYPE_3_9 -> {
                upDateUi2_5()
            }

            WATER_TYPE_3_10 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi3_10()
                }
            }

            WATER_TYPE_3_11 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi3_11()
                }
            }

            WATER_TYPE_3_12 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi3_12()
                }
            }

            WATER_TYPE_4_1 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi4_1()
                }
            }

            WATER_TYPE_4_2 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi4_2()
                }
            }

            WATER_TYPE_4_3 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi4_3()
                }
            }

            WATER_TYPE_4_4 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi4_4()
                }
            }

            WATER_TYPE_4_5 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi4_5()
                }
            }

            WATER_TYPE_4_6 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi4_6()
                }
            }
            WATER_TYPE_4_7 -> {
                if (!isSaveUpdateLocation) {
                    upDateUi4_7()
                }
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun initViewModel() {
        val viewModelFactory =
            CameraViewModelFactory(db)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CameraViewModel::class.java)
        initViewModel(viewModel)

        initLiveDataListener()
    }

    private fun initLiveDataListener() {
        viewModel.insertCallBack.observe(this) { photos ->
            if (photos == 1) {
                Log.d("ROOM数据库", "initLiveData: 插入成功")
                CameraSuccessDialogFragment.show(supportFragmentManager)
            }
        }
    }

    private fun clickWaterChange() {
        binding.cameraWaterChange.setOnClickListener {
            selectWaterBottomSheetDialog.show()
        }
    }


    private fun initWaterMark() {
        val type = intent.getStringExtra(WATER_TYPE)
        mCurrentType = type!!
        Log.d("temperature", " : $type")
        when (type) {
            WATER_TYPE_1_1 -> {
                val waterFactory = Water1_1Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_1_1()
            }

            WATER_TYPE_1_2 -> {
                val waterFactory = Water1_2Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_1_2()
            }

            WATER_TYPE_1_3 -> {
                val waterFactory = Water1_3Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_1_3()
            }

            WATER_TYPE_1_4 -> {
                val waterFactory = Water1_4Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_1_4()
            }

            WATER_TYPE_1_5 -> {
                val waterFactory = Water1_5Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_1_5()
            }

            WATER_TYPE_1_6 -> {
                val waterFactory = Water1_6Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_1_6()
            }

            WATER_TYPE_1_7 -> {
                val waterFactory = Water1_7Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_1_7()
            }

            WATER_TYPE_2_1 -> {
                val waterFactory = Water2_1Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_2_1()
            }

            WATER_TYPE_2_2 -> {
                val waterFactory = Water2_2Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_2_2()
            }

            WATER_TYPE_2_3 -> {
                val waterFactory = Water2_3Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_2_3()
            }

            WATER_TYPE_2_4 -> {
                val waterFactory = Water2_4Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_2_4()
            }

            WATER_TYPE_2_5 -> {
                val waterFactory = Water2_5Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_2_5()
            }

            WATER_TYPE_2_6 -> {
                val waterFactory = Water2_6Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_2_6()
            }

            WATER_TYPE_2_7 -> {
                val waterFactory = Water2_7Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_2_7()

            }

            WATER_TYPE_2_8 -> {
                val waterFactory = Water2_8Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_2_8()
            }

            WATER_TYPE_3_1 -> {
                val waterFactory = Water3_1Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_3_1()
            }

            WATER_TYPE_3_2 -> {
                val waterFactory = Water3_2Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_3_2()
            }

            WATER_TYPE_3_3 -> {
                val waterFactory = Water3_3Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_3_1()
            }

            WATER_TYPE_3_4 -> {
                val waterFactory = Water3_4Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_3_4()
            }

            WATER_TYPE_3_5 -> {
                val waterFactory = Water3_5Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_3_5()
            }

            WATER_TYPE_3_6 -> {
                val waterFactory = Water3_6Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_3_6()
            }

            WATER_TYPE_3_7 -> {
                val waterFactory = Water3_7Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_3_7()
            }

            WATER_TYPE_3_8 -> {
                val waterFactory = Water3_8Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_3_8()
            }

            WATER_TYPE_3_9 -> {
                val waterFactory = Water2_5Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_2_5()
            }

            WATER_TYPE_3_10 -> {
                val waterFactory = Water3_10Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_3_10()
            }

            WATER_TYPE_3_11 -> {
                val waterFactory = Water3_11Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_3_11()
            }

            WATER_TYPE_3_12 -> {
                val waterFactory = Water3_12Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_3_12()
            }

            WATER_TYPE_4_1 -> {
                val waterFactory = Water4_1Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_4_1()
            }

            WATER_TYPE_4_2 -> {
                val waterFactory = Water4_2Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_4_2()
            }

            WATER_TYPE_4_3 -> {
                val waterFactory = Water4_3Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_4_3()
            }

            WATER_TYPE_4_4 -> {
                val waterFactory = Water4_4Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_4_4()
            }

            WATER_TYPE_4_5 -> {
                val waterFactory = Water4_5Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_4_5()
            }

            WATER_TYPE_4_6 -> {
                val waterFactory = Water4_6Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_4_6()
            }
            WATER_TYPE_4_7 -> {
                val waterFactory = Water4_7Factory()
                waterBean = waterFactory.createWater()
                initWATER_TYPE_4_7()
            }
        }
    }

    private fun initWATER_TYPE_3_1() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_construction1, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        tv_3_1_4 = leftCornerView.findViewById(R.id.construction1_real_tv4)
        tv_3_1_5 = leftCornerView.findViewById(R.id.construction1_real_tv5)
        tv_3_1_6 = leftCornerView.findViewById(R.id.construction1_real_tv6)
        tv_3_1_7 = leftCornerView.findViewById(R.id.construction1_real_tv7)
        line1 = leftCornerView.findViewById(R.id.visible1)
        line2 = leftCornerView.findViewById(R.id.visible2)
        line3 = leftCornerView.findViewById(R.id.visible3)
        line4 = leftCornerView.findViewById(R.id.visible4)
        line5 = leftCornerView.findViewById(R.id.visible5)
        line6 = leftCornerView.findViewById(R.id.visible6)
        line7 = leftCornerView.findViewById(R.id.visible7)
    }

    private fun initWATER_TYPE_3_2() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_construction1, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        tv_3_1_4 = leftCornerView.findViewById(R.id.construction1_real_tv4)
        tv_3_1_5 = leftCornerView.findViewById(R.id.construction1_real_tv5)
        tv_3_1_6 = leftCornerView.findViewById(R.id.construction1_real_tv6)
        tv_3_1_7 = leftCornerView.findViewById(R.id.construction1_real_tv7)
        line1 = leftCornerView.findViewById(R.id.visible1)
        line2 = leftCornerView.findViewById(R.id.visible2)
        line3 = leftCornerView.findViewById(R.id.visible3)
        line4 = leftCornerView.findViewById(R.id.visible4)
        line4.visibility = View.GONE
        line5 = leftCornerView.findViewById(R.id.visible5)
        line6 = leftCornerView.findViewById(R.id.visible6)
        line7 = leftCornerView.findViewById(R.id.visible7)
    }

    private fun initWATER_TYPE_3_4() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_construction4, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        tv_3_1_4 = leftCornerView.findViewById(R.id.construction1_real_tv4)
        tv_3_1_5 = leftCornerView.findViewById(R.id.construction1_real_tv5)
        tv_3_1_6 = leftCornerView.findViewById(R.id.construction1_real_tv6)
        line1 = leftCornerView.findViewById(R.id.visible1)
        line2 = leftCornerView.findViewById(R.id.visible2)
        line3 = leftCornerView.findViewById(R.id.visible3)
        line4 = leftCornerView.findViewById(R.id.visible4)
        line5 = leftCornerView.findViewById(R.id.visible5)
        line6 = leftCornerView.findViewById(R.id.visible6)
    }

    private fun initWATER_TYPE_3_5() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_construction5, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        line1 = leftCornerView.findViewById(R.id.visible1)
        line2 = leftCornerView.findViewById(R.id.visible2)
        line3 = leftCornerView.findViewById(R.id.visible3)
    }

    private fun initWATER_TYPE_3_6() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_construction6, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        line1 = leftCornerView.findViewById(R.id.visible1)
        line2 = leftCornerView.findViewById(R.id.visible2)
        line3 = leftCornerView.findViewById(R.id.visible3)
    }

    private fun initWATER_TYPE_3_7() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_construction7, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        tv_3_1_4 = leftCornerView.findViewById(R.id.construction1_real_tv4)
        line1 = leftCornerView.findViewById(R.id.visible1)
        line2 = leftCornerView.findViewById(R.id.visible2)
        line3 = leftCornerView.findViewById(R.id.visible3)
        line4 = leftCornerView.findViewById(R.id.visible4)
    }

    private fun initWATER_TYPE_3_8() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_construction8, container, false)
        container.addView(leftCornerView)

        line1 = leftCornerView.findViewById(R.id.visible1)
    }

    private fun initWATER_TYPE_3_10() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_construction10, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        line1 = leftCornerView.findViewById(R.id.visible1)
        line2 = leftCornerView.findViewById(R.id.visible2)
        line3 = leftCornerView.findViewById(R.id.visible3)
    }

    private fun initWATER_TYPE_3_11() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_construction11, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        line1 = leftCornerView.findViewById(R.id.visible1)
        line2 = leftCornerView.findViewById(R.id.visible2)
        line3 = leftCornerView.findViewById(R.id.visible3)
    }

    private fun initWATER_TYPE_3_12() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_construction12, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        tv_3_1_4 = leftCornerView.findViewById(R.id.construction1_real_tv4)
        tv_3_1_5 = leftCornerView.findViewById(R.id.construction1_real_tv5)
        tv_3_1_6 = leftCornerView.findViewById(R.id.construction1_real_tv6)
        tv_3_1_7 = leftCornerView.findViewById(R.id.construction1_real_tv7)
        line1 = leftCornerView.findViewById(R.id.visible1)
        line2 = leftCornerView.findViewById(R.id.visible2)
        line3 = leftCornerView.findViewById(R.id.visible3)
        line4 = leftCornerView.findViewById(R.id.visible4)
        line5 = leftCornerView.findViewById(R.id.visible5)
        line6 = leftCornerView.findViewById(R.id.visible6)
        line7 = leftCornerView.findViewById(R.id.visible7)
    }

    private fun initWATER_TYPE_4_1() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_estate1, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        tv_3_1_4 = leftCornerView.findViewById(R.id.construction1_real_tv4)
        tv_3_1_5 = leftCornerView.findViewById(R.id.construction1_real_tv5)
        tv_3_1_6 = leftCornerView.findViewById(R.id.construction1_real_tv6)
        line1 = leftCornerView.findViewById(R.id.visible1)
        line2 = leftCornerView.findViewById(R.id.visible2)
        line3 = leftCornerView.findViewById(R.id.visible3)
        line4 = leftCornerView.findViewById(R.id.visible4)
        line5 = leftCornerView.findViewById(R.id.visible5)
        line6 = leftCornerView.findViewById(R.id.visible6)
    }

    private fun initWATER_TYPE_4_2() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_estate2, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        tv_3_1_4 = leftCornerView.findViewById(R.id.construction1_real_tv4)
        line1 = leftCornerView.findViewById(R.id.visible1)
        line2 = leftCornerView.findViewById(R.id.visible2)
        line3 = leftCornerView.findViewById(R.id.visible3)
        line4 = leftCornerView.findViewById(R.id.visible4)
    }

    private fun initWATER_TYPE_4_3() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_estate3, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        tv_3_1_4 = leftCornerView.findViewById(R.id.construction1_real_tv4)
        tv_3_1_5 = leftCornerView.findViewById(R.id.construction1_real_tv5)
        tv_3_1_6 = leftCornerView.findViewById(R.id.construction1_real_tv6)
        line1 = leftCornerView.findViewById(R.id.visible1)
        line2 = leftCornerView.findViewById(R.id.visible2)
        line3 = leftCornerView.findViewById(R.id.visible3)
        line4 = leftCornerView.findViewById(R.id.visible4)
        line5 = leftCornerView.findViewById(R.id.visible5)
        line6 = leftCornerView.findViewById(R.id.visible6)
    }

    private fun initWATER_TYPE_4_4() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_estate4, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        tv_3_1_4 = leftCornerView.findViewById(R.id.construction1_real_tv4)
        tv_3_1_5 = leftCornerView.findViewById(R.id.construction1_real_tv5)
        tv_3_1_6 = leftCornerView.findViewById(R.id.construction1_real_tv6)
        line1 = leftCornerView.findViewById(R.id.visible1)
        line2 = leftCornerView.findViewById(R.id.visible2)
        line3 = leftCornerView.findViewById(R.id.visible3)
        line4 = leftCornerView.findViewById(R.id.visible4)
        line5 = leftCornerView.findViewById(R.id.visible5)
        line6 = leftCornerView.findViewById(R.id.visible6)
    }

    private fun initWATER_TYPE_4_5() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_estate5, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        tv_3_1_4 = leftCornerView.findViewById(R.id.construction1_real_tv4)
        tv_3_1_5 = leftCornerView.findViewById(R.id.construction1_real_tv5)
        tv_3_1_6 = leftCornerView.findViewById(R.id.construction1_real_tv6)
        line1 = leftCornerView.findViewById(R.id.visible1)
        line2 = leftCornerView.findViewById(R.id.visible2)
        line3 = leftCornerView.findViewById(R.id.visible3)
        line4 = leftCornerView.findViewById(R.id.visible4)
        line5 = leftCornerView.findViewById(R.id.visible5)
        line6 = leftCornerView.findViewById(R.id.visible6)
    }

    private fun initWATER_TYPE_4_6() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_estate6, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        tv_3_1_4 = leftCornerView.findViewById(R.id.construction1_real_tv4)
    }
    private fun initWATER_TYPE_4_7() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_estate7, container, false)
        container.addView(leftCornerView)

        tv_3_1_1 = leftCornerView.findViewById(R.id.construction1_real_tv1)
        tv_3_1_2 = leftCornerView.findViewById(R.id.construction1_real_tv2)
        tv_3_1_3 = leftCornerView.findViewById(R.id.construction1_real_tv3)
        tv_3_1_4 = leftCornerView.findViewById(R.id.construction1_real_tv4)
    }

    //延迟时间监听
    private fun clickSlowTimeListener() {
        binding.cameraToolbar.cameraLineSlowtime.setOnClickListener {
            mSlowTimeIndex = (mSlowTimeIndex + 1) % mSlowTimeResource.size
            val drawable = ContextCompat.getDrawable(this, mSlowTimeResource[mSlowTimeIndex])
            binding.cameraToolbar.cameraIvSlowtime.setImageDrawable(drawable)
            when (mSlowTimeIndex) {
                0 -> {
                    binding.cameraToolbar.cameraTvSlowtime.text = "无延迟"
                    binding.cameraCountdown.text = ""
                }

                1 -> {
                    binding.cameraToolbar.cameraTvSlowtime.text = "延迟3秒"
                    binding.cameraCountdown.text = "3"
                }

                2 -> {
                    binding.cameraToolbar.cameraTvSlowtime.text = "延迟5秒"
                    binding.cameraCountdown.text = "5"
                }

                3 -> {
                    binding.cameraToolbar.cameraTvSlowtime.text = "延迟10秒"
                    binding.cameraCountdown.text = "10"
                }

                4 -> {
                    binding.cameraToolbar.cameraTvSlowtime.text = "延迟15秒"
                    binding.cameraCountdown.text = "15"
                }

                5 -> {
                    binding.cameraToolbar.cameraTvSlowtime.text = "延迟30秒"
                    binding.cameraCountdown.text = "30"
                }
            }
        }
    }

    //闪光灯点击监听
    private fun clickFlashListener() {
        binding.cameraToolbar.cameraLineFlash.setOnClickListener {
            mFlashIndex = (mFlashIndex + 1) % mFlashResource.size
            val drawable = ContextCompat.getDrawable(this, mFlashResource[mFlashIndex])
            binding.cameraToolbar.cameraIvFlash.setImageDrawable(drawable)
            if (mFlashIndex == 0) {
                binding.cameraToolbar.cameraTvFlash.text = "有闪光灯"
            } else if (mFlashIndex == 1) {
                binding.cameraToolbar.cameraTvFlash.text = "无闪光灯"
            }

            flashBoolean = if (flashBoolean == ImageCapture.FLASH_MODE_OFF) {
                ImageCapture.FLASH_MODE_ON
            } else {
                ImageCapture.FLASH_MODE_OFF
            }
            // 解绑现有的相机
            cameraProvider.unbindAll()
            // 重新绑定相机
            bindPreviewAndImageCapturePicture(cameraProvider)
        }
    }

    private fun clickOverTurn() {
        binding.cameraToolbar.cameraConsOverturn.setOnClickListener {
            toggleCamera()
        }
    }


    private fun initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionX.init(this).permissions(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
            )
        } else {
            PermissionX.init(this).permissions(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }.request { allGranted, grantedList, deniedList ->
            //allGranted: 一个布尔值，表示是否所有的权限都已经授予了。
            //grantedList: 一个列表，包含了用户已经授予的权限。
            //deniedList: 一个列表，包含了用户拒绝的权限。
            if (allGranted) {
                binding.root.postDelayed({
                }, 1000)
            } else {
                //可以在这里弹出提示告诉用户为什么需要权限
                finish()
            }
        }
    }


    //TODO:按下拍照键
    private fun clickPrimaryListener() {
        binding.cameraPrimary.setOnClickListener {
            if (!MMKVUtils.isLogin()) {
                startActivity(Intent(this, LoginActivity::class.java))
                return@setOnClickListener
            }//TODO:拍照判断是否vip过期
            if (userInfo == null) {
                Toast.makeText(this, "正在登录", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            when (userInfo!!.data.vip_type) {
                0 -> {
                    if (userInfo!!.data.free_time > 0) {
                        primary()
                        viewModel.decrFreeTime()
                    } else {
                        Toast.makeText(this, "体验次数用尽，请开通VIP", Toast.LENGTH_SHORT).show()
                        BuyVipDialogFragment.show(supportFragmentManager)
                    }
                }

                1 -> {
                    if (userInfo!!.data.is_vip) {
                        primary()
                    } else {
                        Toast.makeText(this, "VIP到期", Toast.LENGTH_SHORT).show()
                    }
                }

                2 -> {
                    if (userInfo!!.data.is_vip) {
                        primary()
                    } else {
                        Toast.makeText(this, "VIP到期", Toast.LENGTH_SHORT).show()
                    }
                }

                3 -> {
                    if (userInfo!!.data.is_vip) {
                        primary()
                    } else {
                        Toast.makeText(this, "VIP到期", Toast.LENGTH_SHORT).show()
                    }
                }

                4 -> {
                    if (userInfo!!.data.is_vip) {
                        primary()
                    } else {
                        Toast.makeText(this, "VIP到期", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun primary() {
        if (mSlowTimeIndex != 0) {
            var time: Long = 0
            when (mSlowTimeIndex) {
                1 -> time = 3000
                2 -> time = 5000
                3 -> time = 10000
                4 -> time = 15000
                5 -> time = 30000
            }
            startCountdown(time, { text -> binding.cameraCountdown.text = text })
        } else {
            toPrimary()
        }
    }

    private fun toPrimary() {
        when (mCurrentCameraType) {
            mCurrentCameraType_Camera -> {
                primaryPhoto()
            }

            mCurrentCameraType_Video -> {
                if (!mIsRecording) {
                    Log.d("CameraX", "primary: 开始录制")
                    mIsRecording = true
                    binding.videoCameraDoing.visibility = View.VISIBLE
                    startVideo()
                } else {
                    Log.d("CameraX", "primary: 结束录制")
                    mIsRecording = false
                    binding.videoCameraDoing.visibility = View.INVISIBLE
                    stopVideo()
                }
            }
        }
    }

    fun startCountdown(countdownTime: Long, updateText: (String) -> Unit) {
        val countDownTimer = object : CountDownTimer(countdownTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // 更新倒计时文本
                updateText("${millisUntilFinished / 1000}")
            }

            override fun onFinish() {
                // 倒计时结束时执行的代码
                updateText("0")
                Log.d("倒计时结束", "onFinish: 倒计时结束")
                binding.cameraCountdown.text = ""
                toPrimary()
            }
        }
        // 开始倒计时
        countDownTimer.start()
    }


    private fun updateLatitudeAndLongitude() {
        below = mLatitude.toInt().toString() // 获取小数点前的整数部分
        after = (mLatitude % 1 * 100).toInt().toString() // 获取小数点后两位的数字，并转换为整数
        below2 = mLongitude.toInt().toString() // 获取小数点前的整数部分
        after2 = (mLongitude % 1 * 100).toInt().toString() // 获取小数点后两位的数字，并转换为整数
    }

    private fun upDateUi1_1() {
        tv_1_1_1.text = "北纬:" + below + "°" + after + "'"
        tv_1_1_2.text = "东经:" + below2 + "°" + after2 + "'"
        tv_1_1_3.text = mTime
        tv_1_1_4.text = mLocal
        (waterBean as Water1_1).tv_1_1_1 = tv_1_1_1.text.toString()
        (waterBean as Water1_1).tv_1_1_2 = tv_1_1_2.text.toString()
        (waterBean as Water1_1).tv_1_1_3 = tv_1_1_3.text.toString()
        (waterBean as Water1_1).tv_1_1_4 = tv_1_1_4.text.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi2_1() {
        Log.d(TAG, "upDateUi2_1: ${mTime}")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        Log.d(TAG, "upDateUi2_1: $dayOfWeek")
        Log.d(TAG, "upDateUi2_1: $date")
        Log.d(TAG, "upDateUi2_1: $time")
        tv_2_1_1.text = time
        tv_2_1_2.text = date
        tv_2_1_3.text = dayOfWeek
        tv_2_1_4.text = mLocal
        (waterBean as Water2_1).tv_2_1_1 = tv_2_1_1.text.toString()
        (waterBean as Water2_1).tv_2_1_2 = tv_2_1_2.text.toString()
        (waterBean as Water2_1).tv_2_1_3 = tv_2_1_3.text.toString()
        (waterBean as Water2_1).tv_2_1_4 = mLocal
        (waterBean as Water2_1).tv_2_1_5 = mTime
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi2_2() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_2_2_1.text = hour
        tv_2_2_2.text = minute
        tv_2_2_3.text = second
        tv_2_2_4.text = date + " " + dayOfWeek
        tv_2_2_5.text = mCity + " , " + mDistrict + " , " + mStreet
        (waterBean as Water2_2).tv_2_2_1 = tv_2_2_1.text.toString()
        (waterBean as Water2_2).tv_2_2_2 = tv_2_2_2.text.toString()
        (waterBean as Water2_2).tv_2_2_3 = tv_2_2_3.text.toString()
        (waterBean as Water2_2).tv_2_2_4 = date + " " + dayOfWeek
        (waterBean as Water2_2).tv_2_2_5 = mCity + " , " + mDistrict + " , " + mStreet
        Log.d(TAG, "upDateUi2_2: ${waterBean.toString()}")
    }

    private fun upDateUi1_3() {
        tv_1_3_1.text = "记录每天心情"
        tv_1_3_2.text = mTime
        (waterBean as Water1_3).tv_1_3_1 = tv_1_3_1.text.toString()
        (waterBean as Water1_3).tv_1_3_2 = tv_1_3_2.text.toString()
    }

    private fun upDateUi1_4() {
        tv_1_4_1.text = (waterBean as Water1_4).tv_1_4_1
        tv_1_4_2.text = mTime
        tv_1_4_3.text = mLocal
        (waterBean as Water1_4).tv_1_4_1 = tv_1_4_1.text.toString()
        (waterBean as Water1_4).tv_1_4_2 = tv_1_4_2.text.toString()
        (waterBean as Water1_4).tv_1_4_3 = tv_1_4_3.text.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi1_5() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(mTime, formatter)
        val hour = dateTime.hour
        val minute = dateTime.minute
        val second = dateTime.second
        (waterBean as Water1_5).tv_1_5_4 = mTime
        (waterBean as Water1_5).tv_1_5_1_shi = hour.toString()
        (waterBean as Water1_5).tv_1_5_2_fen = minute.toString()
        (waterBean as Water1_5).tv_1_5_3_miao = second.toString()
        tv_1_5_1_shi.text = (waterBean as Water1_5).tv_1_5_1_shi
        tv_1_5_2_fen.text = (waterBean as Water1_5).tv_1_5_2_fen
        tv_1_5_3_miao.text = (waterBean as Water1_5).tv_1_5_3_miao
        tv_1_5_4.text = (waterBean as Water1_5).tv_1_5_4
    }

    private fun upDateUi1_6() {
        tv_1_6_1.text = "距离纪念日还有"
        tv_1_6_2.text = 8.toString()
        tv_1_6_3.text = 8.toString()
        tv_1_6_4.text = 8.toString()
        tv_1_6_5.text = mTime
        tv_1_6_6.text = mLocal
        (waterBean as Water1_6).tv_1_6_1 = tv_1_6_1.text.toString()
        (waterBean as Water1_6).tv_1_6_2 = tv_1_6_2.text.toString()
        (waterBean as Water1_6).tv_1_6_3 = tv_1_6_3.text.toString()
        (waterBean as Water1_6).tv_1_6_4 = tv_1_6_4.text.toString()
        (waterBean as Water1_6).tv_1_6_5 = tv_1_6_5.text.toString()
        (waterBean as Water1_6).tv_1_6_6 = tv_1_6_6.text.toString()
        Log.d(TAG, "upDateUi1_6: ${waterBean.toString()}")
    }

    private fun upDateUi1_7() {
        tv_1_7_1.text = 888.toString()
        tv_1_7_2.text = mTime
        tv_1_7_3.text = mLocal
        (waterBean as Water1_7).tv_1_7_1 = tv_1_7_1.text.toString()
        (waterBean as Water1_7).tv_1_7_2 = tv_1_7_2.text.toString()
        (waterBean as Water1_7).tv_1_7_3 = tv_1_7_3.text.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi2_3() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        tv_1_7_1.text = time
        tv_1_7_2.text = mLocal
        tv_1_7_3.text = date + " " + dayOfWeek
        (waterBean as Water2_3).tv_1_7_1 = time
        (waterBean as Water2_3).tv_1_7_2 = mLocal
        (waterBean as Water2_3).tv_1_7_3 = date + " " + dayOfWeek
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi2_4() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        tv_1_7_1.text = time
        tv_1_7_2.text = mLocal
        tv_1_7_3.text = date + " " + dayOfWeek
        (waterBean as Water2_4).tv_1_7_1 = time
        (waterBean as Water2_4).tv_1_7_2 = mLocal
        (waterBean as Water2_4).tv_1_7_3 = date + " " + dayOfWeek
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi2_5() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        tv_1_7_1.text = time
        tv_1_7_2.text = dayOfWeek + " " + date
        tv_1_7_3.text = mCity + ", " + mDistrict + ", " + mStreet
        (waterBean as Water2_5).tv_1_7_1 = time
        (waterBean as Water2_5).tv_1_7_2 = dayOfWeek + " " + date
        (waterBean as Water2_5).tv_1_7_3 = mCity + ", " + mDistrict + ", " + mStreet
        Log.d(TAG, "upDateUi2_5: ${waterBean.toString()}")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi2_6() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        tv_1_7_1.text = time
        tv_1_7_2.text = date + " " + dayOfWeek
        tv_1_7_3.text = mCity + ", " + mDistrict + ", " + mStreet
        (waterBean as Water2_6).tv_1_7_1 = time
        (waterBean as Water2_6).tv_1_7_2 = date + " " + dayOfWeek
        (waterBean as Water2_6).tv_1_7_3 = mCity + ", " + mDistrict + ", " + mStreet
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi2_7() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_2_2_1.text = hour
        tv_2_2_2.text = minute
        tv_2_2_3.text = second
        tv_2_2_4.text = dayOfWeek + " " + date
        tv_2_2_5.text = mLocal
        (waterBean as Water2_7).tv_2_2_1 = hour
        (waterBean as Water2_7).tv_2_2_2 = minute
        (waterBean as Water2_7).tv_2_2_3 = second
        (waterBean as Water2_7).tv_2_2_4 = dayOfWeek + " " + date
        (waterBean as Water2_7).tv_2_2_5 = mLocal
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi2_8() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_1_7_1.text = hour + ":" + minute
        tv_1_7_2.text = date + " " + dayOfWeek
        tv_1_7_3.text = mLocal
        (waterBean as Water2_8).tv_1_7_1 = hour + ":" + minute
        (waterBean as Water2_8).tv_1_7_2 = date + " " + dayOfWeek
        (waterBean as Water2_8).tv_1_7_3 = mLocal
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi3_1() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_3_1_1.text = "施工中"
        tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
        tv_3_1_3.text = "点击编辑"
        tv_3_1_4.text = "点击编辑"
        tv_3_1_5.text = (waterBean as Water3_1).tv_3_1_5.toString()
        tv_3_1_6.text = mLocal
        tv_3_1_7.text = "点击编辑"
        (waterBean as Water3_1).tv_3_1_1 = "施工中"
        (waterBean as Water3_1).tv_3_1_2 = date + " " + dayOfWeek + " " + hour + ":" + minute
        (waterBean as Water3_1).tv_3_1_3 = "点击编辑"
        (waterBean as Water3_1).tv_3_1_4 = "点击编辑"
        (waterBean as Water3_1).tv_3_1_6 = mLocal
        (waterBean as Water3_1).tv_3_1_7 = "点击编辑"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi3_2() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_3_1_1.text = "施工中"
        tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
        tv_3_1_3.text = "采购物品名称"
        tv_3_1_4.text = "点击编辑"
        tv_3_1_5.text = (waterBean as Water3_2).tv_3_1_5.toString()
        tv_3_1_6.text = mLocal
        tv_3_1_7.text = "点击编辑"
        (waterBean as Water3_2).tv_3_1_1 = "施工中"
        (waterBean as Water3_2).tv_3_1_2 = date + " " + dayOfWeek + " " + hour + ":" + minute
        (waterBean as Water3_2).tv_3_1_3 = "采购物品名称"
        (waterBean as Water3_2).tv_3_1_4 = "点击编辑"
        (waterBean as Water3_2).tv_3_1_6 = mLocal
        (waterBean as Water3_2).tv_3_1_7 = "点击编辑"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi3_3() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_3_1_1.text = "施工后"
        tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
        tv_3_1_3.text = "点击编辑"
        tv_3_1_4.text = "点击编辑"
        tv_3_1_5.text = (waterBean as Water3_3).tv_3_1_5.toString()
        tv_3_1_6.text = mLocal
        tv_3_1_7.text = "点击编辑"
        (waterBean as Water3_3).tv_3_1_1 = "施工后"
        (waterBean as Water3_3).tv_3_1_2 = date + " " + dayOfWeek + " " + hour + ":" + minute
        (waterBean as Water3_3).tv_3_1_3 = "点击编辑"
        (waterBean as Water3_3).tv_3_1_4 = "点击编辑"
        (waterBean as Water3_3).tv_3_1_6 = mLocal
        (waterBean as Water3_3).tv_3_1_7 = "点击编辑"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi3_4() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_3_1_1.text = "早班会"
        tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
        tv_3_1_3.text = "采购物品名称"
        tv_3_1_4.text = "输入会议主要内容"
        tv_3_1_5.text = (waterBean as Water3_4).tv_3_1_5.toString()
        tv_3_1_6.text = "输入施工单位"
        (waterBean as Water3_4).tv_3_1_1 = "早班会"
        (waterBean as Water3_4).tv_3_1_2 = date + " " + dayOfWeek + " " + hour + ":" + minute
        (waterBean as Water3_4).tv_3_1_3 = "采购物品名称"
        (waterBean as Water3_4).tv_3_1_4 = "输入会议主要内容"
        (waterBean as Water3_4).tv_3_1_6 = "输入施工单位"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi3_5() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_3_1_1.text = "水务巡查"
        tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
        tv_3_1_3.text = below + "°" + after + "'" + "E," + below2 + "°" + after2 + "'" + "N"
        (waterBean as Water3_5).tv_3_1_1 = "水务巡查"
        (waterBean as Water3_5).tv_3_1_2 = date + " " + dayOfWeek + " " + hour + ":" + minute
        (waterBean as Water3_5).tv_3_1_3 = tv_3_1_3.text.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi3_6() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_3_1_1.text = "工程水印"
        tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
        tv_3_1_3.text = "南方建筑集团"
        (waterBean as Water3_6).tv_3_1_1 = "工程水印"
        (waterBean as Water3_6).tv_3_1_2 = date + " " + dayOfWeek + " " + hour + ":" + minute
        (waterBean as Water3_6).tv_3_1_3 = "南方建筑集团"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi3_7() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_3_1_1.text = "项目名称"
        tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
        tv_3_1_3.text = "输入区域名称"
        tv_3_1_4.text = "请输入施工单位"
        (waterBean as Water3_7).tv_3_1_1 = "项目名称"
        (waterBean as Water3_7).tv_3_1_2 = date + " " + dayOfWeek + " " + hour + ":" + minute
        (waterBean as Water3_7).tv_3_1_3 = "输入区域名称"
        (waterBean as Water3_7).tv_3_1_4 = "请输入施工单位"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi3_10() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_3_1_1.text = "工程水印"
        tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
        tv_3_1_3.text = "南方建筑集团"
        (waterBean as Water3_10).tv_3_1_1 = "工程水印"
        (waterBean as Water3_10).tv_3_1_2 = date + " " + dayOfWeek + " " + hour + ":" + minute
        (waterBean as Water3_10).tv_3_1_3 = "南方建筑集团"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi3_11() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_3_1_1.text = "现场拍照"
        tv_3_1_2.text = below + "°" + after + "'" + "E"
        tv_3_1_3.text = date + " " + dayOfWeek + " " + hour + ":" + minute
        (waterBean as Water3_11).tv_3_1_1 = "现场拍照"
        (waterBean as Water3_11).tv_3_1_2 = below + "°" + after + "'" + "E"
        (waterBean as Water3_11).tv_3_1_3 = date + " " + dayOfWeek + " " + hour + ":" + minute
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi3_12() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_3_1_1.text = "工程记录"
        tv_3_1_2.text = "建筑楼顶封顶"
        tv_3_1_3.text = date + " " + dayOfWeek + " " + hour + ":" + minute
        tv_3_1_4.text = below + "°" + after + "'" + "E," + below2 + "°" + after2 + "'" + "N"
        tv_3_1_5.text = (waterBean as Water3_12).tv_3_1_5.toString()
        tv_3_1_6.text = mAltitude.toString() + "米"
        tv_3_1_7.text = mBearing.toString() + "°"
        (waterBean as Water3_12).tv_3_1_1 = "工程记录"
        (waterBean as Water3_12).tv_3_1_2 = "建筑楼顶封顶"
        (waterBean as Water3_12).tv_3_1_3 = date + " " + dayOfWeek + " " + hour + ":" + minute
        (waterBean as Water3_12).tv_3_1_4 =
            below + "°" + after + "'" + "E," + below2 + "°" + after2 + "'" + "N"
        (waterBean as Water3_12).tv_3_1_6 = mAltitude.toString() + "米"
        (waterBean as Water3_12).tv_3_1_7 = mAltitude.toString() + "°"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi4_1() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_3_1_1.text = "物业会议/培训"
        tv_3_1_2.text = "15"
        tv_3_1_3.text = "整改维修会议"
        tv_3_1_4.text = date + " " + dayOfWeek + " " + hour + ":" + minute
        tv_3_1_5.text = "输入单位名称"
        tv_3_1_6.text = mLocal
        (waterBean as Water4_1).tv_3_1_1 = "物业会议/培训"
        (waterBean as Water4_1).tv_3_1_2 = "15"
        (waterBean as Water4_1).tv_3_1_3 = "整改维修会议"
        (waterBean as Water4_1).tv_3_1_4 = date + " " + dayOfWeek + " " + hour + ":" + minute
        (waterBean as Water4_1).tv_3_1_5 = "输入单位名称"
        (waterBean as Water4_1).tv_3_1_6 = mLocal
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi4_2() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_3_1_1.text = "执勤巡逻"
        tv_3_1_2.text = "输入巡逻人姓名"
        tv_3_1_3.text = "输入巡逻区域"
        tv_3_1_4.text = date + " " + dayOfWeek + " " + hour + ":" + minute
        (waterBean as Water4_2).tv_3_1_1 = "执勤巡逻"
        (waterBean as Water4_2).tv_3_1_2 = "输入巡逻人姓名"
        (waterBean as Water4_2).tv_3_1_3 = "输入巡逻区域"
        (waterBean as Water4_2).tv_3_1_4 = date + " " + dayOfWeek + " " + hour + ":" + minute
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi4_3() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_3_1_1.text = "绿化维护"
        tv_3_1_2.text = "输入养护区域"
        tv_3_1_3.text = "输入施工内容"
        tv_3_1_4.text = date + " " + dayOfWeek + " " + hour + ":" + minute
        tv_3_1_5.text = "输入单位名称"
        tv_3_1_6.text = mLocal
        (waterBean as Water4_3).tv_3_1_1 = "绿化维护"
        (waterBean as Water4_3).tv_3_1_2 = "输入养护区域"
        (waterBean as Water4_3).tv_3_1_3 = "输入施工内容"
        (waterBean as Water4_3).tv_3_1_4 = date + " " + dayOfWeek + " " + hour + ":" + minute
        (waterBean as Water4_3).tv_3_1_5 = "输入单位名称"
        (waterBean as Water4_3).tv_3_1_6 = mLocal
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi4_4() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_3_1_1.text = "值班巡检"
        tv_3_1_2.text = "输入巡检类型"
        tv_3_1_3.text = "输入巡检内容"
        tv_3_1_4.text = "输入巡检人"
        tv_3_1_5.text = "输入巡检结果"
        tv_3_1_6.text = date + " " + dayOfWeek + " " + hour + ":" + minute
        (waterBean as Water4_4).tv_3_1_1 = "值班巡检"
        (waterBean as Water4_4).tv_3_1_2 = "输入巡检类型"
        (waterBean as Water4_4).tv_3_1_3 = "输入巡检内容"
        (waterBean as Water4_4).tv_3_1_4 = "输入巡检人"
        (waterBean as Water4_4).tv_3_1_5 = "输入巡检结果"
        (waterBean as Water4_4).tv_3_1_6 = date + " " + dayOfWeek + " " + hour + ":" + minute
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi4_5() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        tv_3_1_1.text = "日常保洁"
        tv_3_1_2.text = "输入保洁区域"
        tv_3_1_3.text = "输入保洁内容"
        tv_3_1_4.text = "输入保洁人员"
        tv_3_1_5.text = date + " " + dayOfWeek + " " + hour + ":" + minute
        tv_3_1_6.text = mLocal
        (waterBean as Water4_5).tv_3_1_1 = "日常保洁"
        (waterBean as Water4_5).tv_3_1_2 = "输入保洁区域"
        (waterBean as Water4_5).tv_3_1_3 = "输入保洁内容"
        (waterBean as Water4_5).tv_3_1_4 = "输入保洁人员"
        (waterBean as Water4_5).tv_3_1_5 = date + " " + dayOfWeek + " " + hour + ":" + minute
        (waterBean as Water4_5).tv_3_1_6 = mLocal
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi4_6() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        // 根据小时数判断时间段
        val timePeriod = when (hour) {
            (0..5).toString() -> "凌晨"
            (6..11).toString() -> "早晨"
            (12..17).toString() -> "下午"
            else -> "晚上"
        }
        tv_3_1_1.text = timePeriod
        tv_3_1_2.text = hour + ":" + minute
        tv_3_1_3.text = date + " " + dayOfWeek + " "
        tv_3_1_4.text = (waterBean as Water4_6).tv_3_1_4
        (waterBean as Water4_6).tv_3_1_1 = timePeriod
        (waterBean as Water4_6).tv_3_1_2 = hour + ":" + minute
        (waterBean as Water4_6).tv_3_1_3 = date + " " + dayOfWeek + " "
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun upDateUi4_7() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        // 解析字符串为LocalDateTime对象
        val dateTime = LocalDateTime.parse(mTime, formatter)
        // 提取星期
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
        // 提取日期
        val date = LocalDate.from(dateTime).toString()
        // 提取时分秒
        val time = LocalTime.from(dateTime).toString()
        // 提取时分秒，确保都是两位数
        val hour = String.format("%02d", dateTime.hour)
        val minute = String.format("%02d", dateTime.minute)
        val second = String.format("%02d", dateTime.second)
        // 根据小时数判断时间段
        val timePeriod = when (hour) {
            (0..5).toString() -> "凌晨"
            (6..11).toString() -> "早晨"
            (12..17).toString() -> "下午"
            else -> "晚上"
        }
        tv_3_1_1.text = timePeriod
        tv_3_1_2.text = hour + ":" + minute
        tv_3_1_3.text = date + " " + dayOfWeek + " "
        tv_3_1_4.text = (waterBean as Water4_7).tv_3_1_4
        (waterBean as Water4_7).tv_3_1_1 = timePeriod
        (waterBean as Water4_7).tv_3_1_2 = hour + ":" + minute
        (waterBean as Water4_7).tv_3_1_3 = date + " " + dayOfWeek + " "
    }

    private fun initWATER_TYPE_1_1() {
        // 获取预留的容器
        container = binding.cameraWaterMark
        // 加载左下角的布局
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_base_longitude, container, false)
        // 将布局添加到容器中
        container.addView(leftCornerView)

        tv_1_1_1 = leftCornerView.findViewById(R.id.tv_base_longitude1)
        tv_1_1_2 = leftCornerView.findViewById(R.id.tv_base_longitude2)
        tv_1_1_3 = leftCornerView.findViewById(R.id.tv_base_longitude3)
        tv_1_1_4 = leftCornerView.findViewById(R.id.tv_base_longitude4)
    }

    private fun initWATER_TYPE_2_1() {
        // 获取预留的容器
        container = binding.cameraWaterMark
        // 加载左下角的布局
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_attendance_work1, container, false)
        // 将布局添加到容器中
        container.addView(leftCornerView)

        tv_2_1_1 = leftCornerView.findViewById(R.id.tv_attendance_work_2_1_1)
        tv_2_1_2 = leftCornerView.findViewById(R.id.tv_attendance_work_2_1_2)
        tv_2_1_3 = leftCornerView.findViewById(R.id.tv_attendance_work_2_1_3)
        tv_2_1_4 = leftCornerView.findViewById(R.id.tv_attendance_work_2_1_4)
    }

    private fun initWATER_TYPE_2_2() {
        // 获取预留的容器
        container = binding.cameraWaterMark
        // 加载左下角的布局
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_attendance_work2, container, false)
        // 将布局添加到容器中
        container.addView(leftCornerView)

        tv_2_2_1 = leftCornerView.findViewById(R.id.tv_water_work2_time1)
        tv_2_2_2 = leftCornerView.findViewById(R.id.tv_water_work2_time2)
        tv_2_2_3 = leftCornerView.findViewById(R.id.tv_water_work2_time3)
        tv_2_2_4 = leftCornerView.findViewById(R.id.tv_water_attendance_time1)
        tv_2_2_5 = leftCornerView.findViewById(R.id.tv_water_attendance_time2)
        cons_work2 = leftCornerView.findViewById(R.id.cons_work2)
    }

    private fun initWATER_TYPE_2_3() {
        // 获取预留的容器
        container = binding.cameraWaterMark
        // 加载左下角的布局
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_attendance_work3, container, false)
        // 将布局添加到容器中
        container.addView(leftCornerView)

        tv_1_7_1 = leftCornerView.findViewById(R.id.tv_water_attendance_work3_tv1)
        tv_1_7_2 = leftCornerView.findViewById(R.id.tv_water_attendance_work3_tv2)
        tv_1_7_3 = leftCornerView.findViewById(R.id.tv_water_attendance_work3_tv3)
    }

    private fun initWATER_TYPE_2_4() {
        // 获取预留的容器
        container = binding.cameraWaterMark
        // 加载左下角的布局
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_attendance_work4, container, false)
        // 将布局添加到容器中
        container.addView(leftCornerView)

        tv_1_7_1 = leftCornerView.findViewById(R.id.tv_water_attendance_work4_tv1)
        tv_1_7_2 = leftCornerView.findViewById(R.id.tv_water_attendance_work4_tv2)
        tv_1_7_3 = leftCornerView.findViewById(R.id.tv_water_attendance_work4_tv3)
    }

    private fun initWATER_TYPE_2_5() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_attendance_work5, container, false)
        container.addView(leftCornerView)

        tv_1_7_1 = leftCornerView.findViewById(R.id.water_attendance_attendance1_tv1)
        tv_1_7_2 = leftCornerView.findViewById(R.id.tv_water_attendance_attendance_tv1)
        tv_1_7_3 = leftCornerView.findViewById(R.id.tv_water_attendance_attendance_tv2)
        line_day_1_6 = leftCornerView.findViewById(R.id.line_work5)
    }

    private fun initWATER_TYPE_2_6() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_attendance_work6, container, false)
        container.addView(leftCornerView)

        tv_1_7_1 = leftCornerView.findViewById(R.id.tv_water_attendance_attendance2_tv2)
        tv_1_7_2 = leftCornerView.findViewById(R.id.tv_water_attendance_attendance2_tv3)
        tv_1_7_3 = leftCornerView.findViewById(R.id.tv_water_attendance_attendance2_tv4)
    }

    private fun initWATER_TYPE_2_7() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_attendance_work7, container, false)
        container.addView(leftCornerView)

        tv_2_2_1 = leftCornerView.findViewById(R.id.tv_water_attendance_attendance3_tv1)
        tv_2_2_2 = leftCornerView.findViewById(R.id.tv_water_attendance_attendance3_tv2)
        tv_2_2_3 = leftCornerView.findViewById(R.id.tv_water_attendance_attendance3_tv3)
        tv_2_2_4 = leftCornerView.findViewById(R.id.tv_water_attendance_attendance3_tv4)
        tv_2_2_5 = leftCornerView.findViewById(R.id.tv_work7)
        cons_work2 = leftCornerView.findViewById(R.id.cons_work7)
    }

    private fun initWATER_TYPE_2_8() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_attendance_work8, container, false)
        container.addView(leftCornerView)

        tv_1_7_1 = leftCornerView.findViewById(R.id.tv_work8_1)
        tv_1_7_2 = leftCornerView.findViewById(R.id.tv_water_attendance_attendance2_tv3)
        tv_1_7_3 = leftCornerView.findViewById(R.id.tv_water_attendance_attendance2_tv4)
    }


    private fun initWATER_TYPE_1_2() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView = inflater.inflate(R.layout.camera_water_mark_base_local, container, false)
        container.addView(leftCornerView)
        tv_1_2_1 = leftCornerView.findViewById(R.id.tv_base_local1)
        tv_1_2_2 = leftCornerView.findViewById(R.id.tv_base_local2)
    }

    private fun initWATER_TYPE_1_3() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView = inflater.inflate(R.layout.camera_water_mark_base_mood, container, false)
        container.addView(leftCornerView)
        tv_1_3_1 = leftCornerView.findViewById(R.id.tv_base_mood1)
        tv_1_3_2 = leftCornerView.findViewById(R.id.tv_base_mood2)
    }

    private fun initWATER_TYPE_1_4() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView = inflater.inflate(R.layout.camera_water_mark_base_weather, container, false)
        container.addView(leftCornerView)
        tv_1_4_1 = leftCornerView.findViewById(R.id.tv_base_weather1)
        tv_1_4_2 = leftCornerView.findViewById(R.id.tv_base_weather2)
        tv_1_4_3 = leftCornerView.findViewById(R.id.tv_base_weather3)
    }

    private fun initWATER_TYPE_1_5() {
        container = binding.cameraWaterMark
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_base_common_time, container, false)
        container.addView(leftCornerView)
        tv_1_5_1_shi = leftCornerView.findViewById(R.id.tv_base_common_time_shi)
        tv_1_5_2_fen = leftCornerView.findViewById(R.id.tv_base_common_time_fen)
        tv_1_5_3_miao = leftCornerView.findViewById(R.id.tv_base_common_time_miao)
        tv_1_5_4 = leftCornerView.findViewById(R.id.tv_base_total_time)
    }

    private fun initWATER_TYPE_1_6() {
        // 获取预留的容器
        container = binding.cameraWaterMark
        // 加载左下角的布局
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_base_memorial, container, false)
        // 将布局添加到容器中
        container.addView(leftCornerView)

        tv_1_6_1 = leftCornerView.findViewById(R.id.tv_base_memorial_time1)
        tv_1_6_2 = leftCornerView.findViewById(R.id.tv_base_memorial_time2)
        tv_1_6_3 = leftCornerView.findViewById(R.id.tv_base_memorial_time3)
        tv_1_6_4 = leftCornerView.findViewById(R.id.tv_base_memorial_time4)
        tv_1_6_5 = leftCornerView.findViewById(R.id.tv_base_memorial_time5)
        tv_1_6_6 = leftCornerView.findViewById(R.id.tv_base_memorial_time6)
        line_day_1_6 = leftCornerView.findViewById(R.id.line_day_1_6)
    }

    private fun initWATER_TYPE_1_7() {
        // 获取预留的容器
        container = binding.cameraWaterMark
        // 加载左下角的布局
        inflater = layoutInflater
        leftCornerView =
            inflater.inflate(R.layout.camera_water_mark_base_birthday, container, false)
        // 将布局添加到容器中
        container.addView(leftCornerView)

        tv_1_7_1 = leftCornerView.findViewById(R.id.tv_base_birthday1)
        tv_1_7_2 = leftCornerView.findViewById(R.id.tv_base_birthday2)
        tv_1_7_3 = leftCornerView.findViewById(R.id.tv_base_birthday3)
        line_birty_day_1_7 = leftCornerView.findViewById(R.id.line_birty_day_1_7)
    }

    private fun upDateUi1_2() {
        tv_1_2_1.text = "北纬:" + below + "°" + after + "'"
        tv_1_2_2.text = "东经:" + below2 + "°" + after2 + "'"
        (waterBean as Water1_2).tv_1_2_1 = tv_1_2_1.text.toString()
        (waterBean as Water1_2).tv_1_2_2 = tv_1_2_2.text.toString()
    }

    private fun initTimeBottomSheetDialog() {
        timeBottomSheetDialog = BottomSheetDialog(this)
        timeBottomSheetDialog.setContentView(R.layout.camera_water_dialog_location)
        // 设置对话框不可取消，且点击外部区域不会关闭对话框
        timeBottomSheetDialog.setCancelable(false)
        timeBottomSheetDialog.setCanceledOnTouchOutside(false)
    }

    private fun initTitleBottomSheetDialog() {
        titleBottomSheetDialog = BottomSheetDialog(this)
        titleBottomSheetDialog.setContentView(R.layout.camera_water_dialog_mood_title)
        // 设置对话框不可取消，且点击外部区域不会关闭对话框
        titleBottomSheetDialog.setCancelable(false)
        titleBottomSheetDialog.setCanceledOnTouchOutside(false)
        when (mCurrentType) {
            WATER_TYPE_1_3 -> {
                val tv_update_current_mood =
                    titleBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current_mood)!!
                val et_update_current_mood =
                    titleBottomSheetDialog.findViewById<EditText>(R.id.et_update_current_mood)!!
                val line_update_bottom_save =
                    titleBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save_mood)!!
                val line_update_bottom_back =
                    titleBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back_mood)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current_mood.text = et_update_current_mood.editableText.toString()
                    (waterBean as Water1_3).tv_1_3_1 = et_update_current_mood.text.toString()
                }

                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water1_3).tv_1_3_1 = et_update_current_mood.text.toString()
                        Log.d(
                            TAG,
                            "initTitleBottonSheetDialog: ${(waterBean as Water1_3).tv_1_3_1.toString()}"
                        )
                        locationService.stopLocation()
                        updateUiPreview1_3()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    titleBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_1_6 -> {
                val tv_update_current_mood =
                    titleBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current_mood)!!
                val et_update_current_mood =
                    titleBottomSheetDialog.findViewById<EditText>(R.id.et_update_current_mood)!!
                val line_update_bottom_save =
                    titleBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save_mood)!!
                val line_update_bottom_back =
                    titleBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back_mood)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current_mood.text = et_update_current_mood.editableText.toString()
                    (waterBean as Water1_6).tv_1_6_1 = et_update_current_mood.text.toString()
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water1_6).tv_1_6_1 = et_update_current_mood.text.toString()
                        locationService.stopLocation()
                        updateUiPreview1_6()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    titleBottomSheetDialog.dismiss()
                }
            }

        }
    }

    //修改具体定位
    private fun initLocationBottomSheetDialog() {
        locationBottomSheetDialog = BottomSheetDialog(this)
        locationBottomSheetDialog.setContentView(R.layout.camera_water_dialog_location)
        // 设置对话框不可取消，且点击外部区域不会关闭对话框
        locationBottomSheetDialog.setCancelable(false)
        locationBottomSheetDialog.setCanceledOnTouchOutside(false)
        when (mCurrentType) {
            WATER_TYPE_1_1 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    Log.d(TAG, "initWaterUiFromBottomSheetDialog1_1: 保存点击了")
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water1_1).tv_1_1_4 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water1_1).tv_1_1_4 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview1_1()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_1_4 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    Log.d(TAG, "initWaterUiFromBottomSheetDialog1_4: 保存点击了")
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water1_4).tv_1_4_3 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                //TODO:返回布局点击监听
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water1_4).tv_1_4_3 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview1_4()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_1_6 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    Log.d(TAG, "initWaterUiFromBottomSheetDialog1_6: 保存点击了")
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water1_6).tv_1_6_6 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water1_6).tv_1_6_6 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview1_6()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_1_7 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water1_7).tv_1_7_3 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water1_7).tv_1_7_3 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview1_7()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_2_1 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water2_1).tv_2_1_4 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water2_1).tv_2_1_4 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview2_1()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_2_2 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water2_2).tv_2_2_5 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water2_2).tv_2_2_5 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview2_2()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_2_3 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water2_3).tv_1_7_2 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water2_3).tv_1_7_2 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview2_3()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_2_4 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water2_4).tv_1_7_2 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water2_4).tv_1_7_2 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview2_4()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_2_5 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water2_5).tv_1_7_3 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water2_5).tv_1_7_3 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview2_5()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_2_6 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water2_6).tv_1_7_3 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water2_6).tv_1_7_3 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview2_6()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_2_8 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water2_8).tv_1_7_3 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water2_8).tv_1_7_3 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview2_8()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_3_1 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water3_1).tv_3_1_6 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water3_1).tv_3_1_6 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview3_1()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_3_2 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water3_2).tv_3_1_6 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water3_2).tv_3_1_6 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview3_2()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_3_3 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water3_3).tv_3_1_6 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water3_3).tv_3_1_6 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview3_3()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_3_9 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water2_5).tv_1_7_3 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water2_5).tv_1_7_3 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview2_5()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_4_1 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water4_1).tv_3_1_6 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water4_1).tv_3_1_6 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview4_1()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_4_3 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water4_3).tv_3_1_6 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water4_3).tv_3_1_6 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview4_3()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }

            WATER_TYPE_4_5 -> {
                val tv_update_current =
                    locationBottomSheetDialog.findViewById<TextView>(R.id.tv_update_current)!!
                val et_update_current =
                    locationBottomSheetDialog.findViewById<EditText>(R.id.et_update_current)!!
                val line_update_bottom_save =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_save)!!
                val line_update_bottom_back =
                    locationBottomSheetDialog.findViewById<LinearLayout>(R.id.line_update_bottom_back)!!
                line_update_bottom_save.setOnClickListener {
                    isSaveUpdateLocation = true
                    tv_update_current.text = et_update_current.editableText.toString()
                    (waterBean as Water4_5).tv_3_1_6 = tv_update_current.text.toString()
                    MMKVUtils.setHistoryLocation(tv_update_current.text.toString())
                }
                line_update_bottom_back.setOnClickListener {
                    if (isSaveUpdateLocation) {
                        (waterBean as Water4_5).tv_3_1_6 = et_update_current.text.toString()
                        locationService.stopLocation()
                        updateUiPreview4_5()
                        updateUiRealWater()
                        initUpdateUi()
                    }
                    locationBottomSheetDialog.dismiss()
                }
            }
        }

    }

    override fun onFinishCamera() {
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTimeSelected(formattedDate: String) {
        //TODO:时间选择器回调onTimeSelected()
        Log.d("MyActivity", "Selected time is: $formattedDate")
        mTime = formattedDate
        updateTime(formattedDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTime(formattedDate: String) {
        when (mCurrentType) {
            WATER_TYPE_1_1 -> {
                (waterBean as Water1_1).tv_1_1_3 = formattedDate
                Log.d(TAG, "updateTime: 111${(waterBean as Water1_1).tv_1_1_3}")
                updateUiPreview1_1()
            }

            WATER_TYPE_1_3 -> {
                (waterBean as Water1_3).tv_1_3_2 = formattedDate
                Log.d(TAG, "updateTime: 111${(waterBean as Water1_3).tv_1_3_2}")
                updateUiPreview1_3()
            }

            WATER_TYPE_1_4 -> {
                (waterBean as Water1_4).tv_1_4_2 = formattedDate
                updateUiPreview1_4()
            }

            WATER_TYPE_1_5 -> {
                (waterBean as Water1_5).tv_1_5_4 = formattedDate
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val dateTime = LocalDateTime.parse(formattedDate, formatter)
                val hour = dateTime.hour
                val minute = dateTime.minute
                val second = dateTime.second
                (waterBean as Water1_5).tv_1_5_1_shi = hour.toString()
                (waterBean as Water1_5).tv_1_5_2_fen = minute.toString()
                (waterBean as Water1_5).tv_1_5_3_miao = second.toString()
                updateUiPreview1_5()
            }

            WATER_TYPE_1_6 -> {
                (waterBean as Water1_6).tv_1_6_5 = formattedDate
                updateUiPreview1_6()
            }

            WATER_TYPE_2_1 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(formattedDate, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                (waterBean as Water2_1).tv_2_1_1 = time
                (waterBean as Water2_1).tv_2_1_2 = date
                (waterBean as Water2_1).tv_2_1_3 = dayOfWeek
//                (waterBean as Water2_1).tv_2_1_4 = mLocal
                (waterBean as Water2_1).tv_2_1_5 = formattedDate
                updateUiPreview2_1()
            }

            WATER_TYPE_2_2 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                (waterBean as Water2_2).tv_2_2_1 = hour
                (waterBean as Water2_2).tv_2_2_2 = minute
                (waterBean as Water2_2).tv_2_2_3 = second
                (waterBean as Water2_2).tv_2_2_4 = date + " " + dayOfWeek
                Log.d(TAG, "updateTime: ${waterBean.toString()}")
                updateUiPreview2_2()
            }

            WATER_TYPE_2_3 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                (waterBean as Water2_3).tv_1_7_1 = hour + ":" + minute + ":" + second
                (waterBean as Water2_3).tv_1_7_3 = date + " " + dayOfWeek
                Log.d(TAG, "updateTime: ${waterBean.toString()}")
                updateUiPreview2_3()
            }

            WATER_TYPE_2_4 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                (waterBean as Water2_4).tv_1_7_1 = hour + ":" + minute + ":" + second
                (waterBean as Water2_4).tv_1_7_3 = date + " " + dayOfWeek
                Log.d(TAG, "updateTime: ${waterBean.toString()}")
                updateUiPreview2_4()
            }

            WATER_TYPE_2_5 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                (waterBean as Water2_5).tv_1_7_1 = time
                (waterBean as Water2_5).tv_1_7_2 = dayOfWeek + " " + date
                Log.d(TAG, "updateTime: ${waterBean.toString()}")
                updateUiPreview2_5()
            }

            WATER_TYPE_2_6 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                (waterBean as Water2_6).tv_1_7_1 = time
                (waterBean as Water2_6).tv_1_7_2 = date + " " + dayOfWeek
                Log.d(TAG, "updateTime: ${waterBean.toString()}")
                updateUiPreview2_6()
            }

            WATER_TYPE_2_7 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                (waterBean as Water2_7).tv_2_2_1 = hour
                (waterBean as Water2_7).tv_2_2_2 = minute
                (waterBean as Water2_7).tv_2_2_3 = second
                (waterBean as Water2_7).tv_2_2_4 = dayOfWeek + " " + date
                updateUiPreview2_7()
            }

            WATER_TYPE_2_8 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_1_7_1.text = hour + ":" + minute
                tv_1_7_2.text = date + " " + dayOfWeek
                (waterBean as Water2_8).tv_1_7_1 = hour + ":" + minute
                (waterBean as Water2_8).tv_1_7_2 = date + " " + dayOfWeek
                updateUiPreview2_8()
            }

            WATER_TYPE_3_1 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
                (waterBean as Water3_1).tv_3_1_2 =
                    date + " " + dayOfWeek + " " + hour + ":" + minute
                updateUiPreview3_1()
            }

            WATER_TYPE_3_2 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
                (waterBean as Water3_2).tv_3_1_2 =
                    date + " " + dayOfWeek + " " + hour + ":" + minute
                updateUiPreview3_2()
            }

            WATER_TYPE_3_3 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
                (waterBean as Water3_3).tv_3_1_2 =
                    date + " " + dayOfWeek + " " + hour + ":" + minute
                updateUiPreview3_3()
            }

            WATER_TYPE_3_4 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
                (waterBean as Water3_4).tv_3_1_2 =
                    date + " " + dayOfWeek + " " + hour + ":" + minute
                updateUiPreview3_4()
            }

            WATER_TYPE_3_5 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
                (waterBean as Water3_5).tv_3_1_2 =
                    date + " " + dayOfWeek + " " + hour + ":" + minute
                updateUiPreview3_5()
            }

            WATER_TYPE_3_6 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
                (waterBean as Water3_6).tv_3_1_2 =
                    date + " " + dayOfWeek + " " + hour + ":" + minute
                updateUiPreview3_6()
            }

            WATER_TYPE_3_7 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
                (waterBean as Water3_7).tv_3_1_2 =
                    date + " " + dayOfWeek + " " + hour + ":" + minute
                updateUiPreview3_7()
            }

            WATER_TYPE_3_10 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
                (waterBean as Water3_10).tv_3_1_2 =
                    date + " " + dayOfWeek + " " + hour + ":" + minute
                updateUiPreview3_10()
            }

            WATER_TYPE_3_11 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
                (waterBean as Water3_11).tv_3_1_3 =
                    date + " " + dayOfWeek + " " + hour + ":" + minute
                updateUiPreview3_11()
            }

            WATER_TYPE_3_12 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
                (waterBean as Water3_12).tv_3_1_3 =
                    date + " " + dayOfWeek + " " + hour + ":" + minute
                updateUiPreview3_12()
            }

            WATER_TYPE_4_1 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
                (waterBean as Water4_1).tv_3_1_4 =
                    date + " " + dayOfWeek + " " + hour + ":" + minute
                updateUiPreview4_1()
            }

            WATER_TYPE_4_2 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
                (waterBean as Water4_2).tv_3_1_4 =
                    date + " " + dayOfWeek + " " + hour + ":" + minute
                updateUiPreview4_2()
            }

            WATER_TYPE_4_3 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
                (waterBean as Water4_3).tv_3_1_4 =
                    date + " " + dayOfWeek + " " + hour + ":" + minute
                updateUiPreview4_3()
            }

            WATER_TYPE_4_4 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = date + " " + dayOfWeek + " " + hour + ":" + minute
                (waterBean as Water4_4).tv_3_1_6 =
                    date + " " + dayOfWeek + " " + hour + ":" + minute
                updateUiPreview4_4()
            }

            WATER_TYPE_4_5 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_5.text = date + " " + dayOfWeek + " " + hour + ":" + minute
                (waterBean as Water4_5).tv_3_1_5 =
                    date + " " + dayOfWeek + " " + hour + ":" + minute
                updateUiPreview4_5()
            }

            WATER_TYPE_4_6 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = hour + ":" + minute
                tv_3_1_4.text = date + " " + dayOfWeek + " "
                (waterBean as Water4_6).tv_3_1_2 = hour + ":" + minute
                (waterBean as Water4_6).tv_3_1_3 = date + " " + dayOfWeek + " "

                updateUiPreview4_6()
            }
            WATER_TYPE_4_7 -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 解析字符串为LocalDateTime对象
                val dateTime = LocalDateTime.parse(mTime, formatter)
                // 提取星期
                val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                // 提取日期
                val date = LocalDate.from(dateTime).toString()
                // 提取时分秒
                val time = LocalTime.from(dateTime).toString()
                // 提取时分秒，确保都是两位数
                val hour = String.format("%02d", dateTime.hour)
                val minute = String.format("%02d", dateTime.minute)
                val second = String.format("%02d", dateTime.second)
                tv_3_1_2.text = hour + ":" + minute
                tv_3_1_4.text = date + " " + dayOfWeek + " "
                (waterBean as Water4_7).tv_3_1_2 = hour + ":" + minute
                (waterBean as Water4_7).tv_3_1_3 = date + " " + dayOfWeek + " "

                updateUiPreview4_7()
            }
        }
        initUpdateUi()
        updateUiRealWater()
    }


    companion object {
        private const val TAG = "CameraActivity"
        var finishCameraListener: FinishCameraListener? = null
        var successCameraListener: SuccessCameraListener? = null
    }

    private fun initWeather() {
        ServiceSettings.updatePrivacyShow(this, true, true);
        ServiceSettings.updatePrivacyAgree(this, true);

        //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
        var mquery = WeatherSearchQuery(mCity, WeatherSearchQuery.WEATHER_TYPE_LIVE)
        var mweathersearch = WeatherSearch(this)
        mweathersearch.setOnWeatherSearchListener(this)
        mweathersearch.setQuery(mquery)
        mweathersearch.searchWeatherAsyn() //异步搜索
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateWeather(temperature: String?) {
        when (mCurrentType) {
            WATER_TYPE_1_4 -> {
                (waterBean as Water1_4).tv_1_4_1 = temperature.toString()
                if (isSaveUpdateLocation) {
                    upDateUi1_4()
                }
            }

            WATER_TYPE_3_1 -> {
                (waterBean as Water3_1).tv_3_1_5 = temperature.toString()
                if (!isSaveUpdateLocation) {
                    upDateUi3_1()
                }
            }

            WATER_TYPE_3_2 -> {
                (waterBean as Water3_2).tv_3_1_5 = temperature.toString()
                if (!isSaveUpdateLocation) {
                    upDateUi3_2()
                }
            }

            WATER_TYPE_3_3 -> {
                (waterBean as Water3_3).tv_3_1_5 = temperature.toString()
                if (!isSaveUpdateLocation) {
                    upDateUi3_3()
                }
            }

            WATER_TYPE_3_4 -> {
                (waterBean as Water3_4).tv_3_1_5 = temperature.toString()
                if (!isSaveUpdateLocation) {
                    upDateUi3_4()
                }
            }

            WATER_TYPE_3_12 -> {
                (waterBean as Water3_12).tv_3_1_5 = temperature.toString()
                if (!isSaveUpdateLocation) {
                    upDateUi3_12()
                }
            }

            WATER_TYPE_4_6 -> {
                if (!isSaveUpdateLocation) {
                    (waterBean as Water4_6).tv_3_1_4 = mWeather + " " + temperature.toString()
                    upDateUi4_6()
                }
            }
            WATER_TYPE_4_7 -> {
                if (!isSaveUpdateLocation) {
                    (waterBean as Water4_7).tv_3_1_4 = mWeather + " " + temperature.toString()
                    upDateUi4_7()
                }
            }
        }
    }

    override fun onWeatherForecastSearched(p0: LocalWeatherForecastResult?, p1: Int) {

    }

    //TODO:天气回调
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onWeatherLiveSearched(weatherLiveResult: LocalWeatherLiveResult?, rCode: Int) {
        if (rCode == 1000) {
            if (weatherLiveResult?.liveResult != null) {
                val weatherLive = weatherLiveResult.liveResult
                val temperature = weatherLive.temperature + "°C"
                mWeather = weatherLive.weather //多云 晴天 等
                updateWeather(temperature)
            } else {
                Toast.makeText(this, rCode, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, rCode, Toast.LENGTH_SHORT).show()
        }
    }

    override fun cameraSuccess() {
        finish()
        MainActivity.instance.setBottomIndex(2)
    }

    private fun initSelectBottomSheetDialog() {
        selectWaterBottomSheetDialog = BottomSheetDialog(this)
        selectWaterBottomSheetDialog.setContentView(R.layout.camera_water_dialog_change_water)
        val indicator1 = selectWaterBottomSheetDialog.findViewById<TabLayout>(R.id.indicator1)!!
        val pager = selectWaterBottomSheetDialog.findViewById<ViewPager2>(R.id.pager)
        pager!!.adapter = WaterMarkAdapter(this, DataUtil.categories)
        val offscreenPageLimit = DataUtil.categories.size - 1
        pager.offscreenPageLimit = offscreenPageLimit
        TabLayoutViewPager2Mediator(indicator1, pager) { indicator, pager ->
        }.attach()
    }

    /**
     * 相机相关
     */
    private fun initCamera() {
        mCurrentCameraType = mCurrentCameraType_Camera
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            when (mCurrentCameraType) {
                mCurrentCameraType_Camera -> {
                    bindPreviewAndImageCapturePicture(cameraProvider)
                }

                mCurrentCameraType_Video -> {
                    bindPreviewAndVideoCapture((cameraProvider))
                }
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreviewAndImageCapturePicture(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(binding.camera.getSurfaceProvider())
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
        // 创建ImageCapture实例
        imageCapture = ImageCapture.Builder()
            .setTargetRotation(windowManager.defaultDisplay.rotation)
            .setFlashMode(flashBoolean) // 初始设置为关闭闪光灯
            .build()
        // 绑定预览和图像捕获到生命周期
        cameraProvider.bindToLifecycle(
            this as LifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
    }

    private fun toggleCamera() {
        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
        // 解绑现有的相机
        cameraProvider.unbindAll()
        // 重新绑定相机
        bindPreviewAndImageCapturePicture(cameraProvider)
    }

    private fun primaryPhoto() {
        // 创建输出文件路径
        photoFile = createImageFile(applicationContext)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        // 捕获照片
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.d(TAG, "Photo captured: ${photoFile.absolutePath}")
                    // 读取EXIF旋转信息
                    var exif: ExifInterface? = null
                    try {
                        exif = ExifInterface(photoFile.absolutePath)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    // 获取旋转角度
                    val orientation = exif?.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED
                    )
                    // 读取拍摄的照片并创建 Bitmap
                    val photoBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    // 根据EXIF旋转照片
                    val rotatedPhotoBitmap = rotateBitmap(
                        photoBitmap,
                        orientation ?: ExifInterface.ORIENTATION_UNDEFINED
                    )
                    // 将水印视图转换为 Bitmap
                    val watermarkBitmap = createWatermarkBitmap(leftCornerView)
                    // 合成水印和照片
                    val finalBitmap = combineImages(rotatedPhotoBitmap, watermarkBitmap)
                    // 保存合成后的照片到原始的 photoFile
                    try {
                        val out = FileOutputStream(photoFile)
                        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        out.flush()
                        out.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    // 更新数据库或其他操作
                    insertPhotoDataBase()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                    // 处理拍照失败的情况
                }
            })
    }

    // 根据EXIF旋转Bitmap
    fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            // 如果不需要其他旋转，可以省略其他情况
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun createWatermarkBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun combineImages(background: Bitmap, watermark: Bitmap): Bitmap {
        val combined = Bitmap.createBitmap(background.width, background.height, background.config)
        val canvas = Canvas(combined)
        canvas.drawBitmap(background, 0f, 0f, null)

        // 设置水印的缩放比例
        val scaleFactor = 2.0f

        // 创建缩放矩阵
        val scaleMatrix = Matrix()
        scaleMatrix.postScale(scaleFactor, scaleFactor)

        // 创建缩放后的水印 Bitmap
        val scaledWatermark = Bitmap.createBitmap(
            watermark,
            0,
            0,
            watermark.width,
            watermark.height,
            scaleMatrix,
            true
        )

        // 计算放大后水印的位置
        val watermarkX = 300f // 水印的左边与图片的左边对齐
        val watermarkY =
            -300f + background.height - scaledWatermark.height.toFloat() // 水印的底部与图片的底部对齐

        // 绘制放大后的水印
        canvas.drawBitmap(scaledWatermark, watermarkX, watermarkY, null)
        return combined
    }

    private fun insertPhotoDataBase() {
        lifecycleScope.launch {
            val photo = Photo(0, photoFile.absolutePath)
            viewModel.insertPhoto(photo)
        }
    }

    private fun insertVideoDataBase() {
        lifecycleScope.launch {
            val video = Video(0, photoFile.absolutePath)
            viewModel.insertVideo(video)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {
        // 创建图片文件的目录（如果不存在）
        val mediaDir =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.apply { mkdirs() }
        return File(mediaDir, "${System.currentTimeMillis()}.jpg").also { it.createNewFile() }
    }

    @Throws(IOException::class)
    private fun createVideoFile(context: Context): File {
        // 创建图片文件的目录（如果不存在）
        val mediaDir =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.apply { mkdirs() }
        return File(mediaDir, "${System.currentTimeMillis()}.mp4").also { it.createNewFile() }
    }


    private fun initBottomLayout() {
        val tabPhoto = binding.cameraTabLayout.newTab().apply { setText("照片") }
        val tabVideo = binding.cameraTabLayout.newTab().apply { setText("视频") }
        binding.cameraTabLayout.addTab(tabPhoto)
        binding.cameraTabLayout.addTab(tabVideo)
        binding.cameraTabLayout.addOnTabSelectedListener(onTabSelectedListener)
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            mPrimaryIndex = (mPrimaryIndex + 1) % mPrimaryResource.size
            val drawable =
                ContextCompat.getDrawable(this@CameraActivity, mPrimaryResource[mPrimaryIndex])
            binding.cameraPrimary.setImageDrawable(drawable)

            if (mCurrentCameraType == mCurrentCameraType_Camera) {
                mCurrentCameraType = mCurrentCameraType_Video
            } else {
                mCurrentCameraType = mCurrentCameraType_Camera
            }
            cameraProvider.unbindAll()
            when (mCurrentCameraType) {
                mCurrentCameraType_Camera -> {
                    bindPreviewAndImageCapturePicture(cameraProvider)
                }

                mCurrentCameraType_Video -> {
                    bindPreviewAndVideoCapture(cameraProvider)
                }
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {

        }

        override fun onTabReselected(tab: TabLayout.Tab) {

        }
    }

    private fun bindPreviewAndVideoCapture(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(binding.camera.getSurfaceProvider())
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        // 创建VideoCapture实例
        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HD))
            .build()
        videoCapture = VideoCapture.withOutput(recorder)

        // 绑定预览和视频捕获到生命周期
        cameraProvider.bindToLifecycle(
            this as LifecycleOwner,
            cameraSelector,
            preview,
            videoCapture
        )
    }

    fun startVideo() {
        val name = "CameraX-recording-" +
                System.currentTimeMillis() + ".mp4"
        val contentValues = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, name)
        }
        photoFile = createVideoFile(applicationContext)
        val mediaStoreOutput = FileOutputOptions.Builder(photoFile).build()
        recording = if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ToastUtil.show(this, "权限检查", 300)
            return
        } else {
            videoCapture!!.output
                .prepareRecording(this, mediaStoreOutput)
                .withAudioEnabled()
                .start(
                    ContextCompat.getMainExecutor(this),
                    object : Consumer<VideoRecordEvent> {
                        override fun accept(event: VideoRecordEvent) {
                            when (event) {
                                is VideoRecordEvent.Start -> {
                                    Log.d("CameraX", "Recording started.")
                                    ToastUtil.show(this@CameraActivity, "开始录制", 300)
                                }

                                is VideoRecordEvent.Finalize -> {
                                    Log.d("CameraX", "Recording stopped.")
                                    ToastUtil.show(this@CameraActivity, "已结束录制", 300)
                                    Log.d("CameraX", "${photoFile.absolutePath}")
                                    addVideoWater(photoFile.absolutePath)
//                                    insertVideoDataBase()
                                }
                            }
                        }
                    }
                )
        }
    }

    fun stopVideo() {
        recording.stop()
    }

    fun addVideoWater(absolutePath: String) {
        // 假设您已经有一个布局资源ID R.layout.watermark_layout
        val context = this
        val watermarkBitmap = createWatermarkBitmap2(leftCornerView)
        val watermarkImagePath = context.cacheDir.absolutePath + "/watermark.png"
        saveBitmapAsPng(watermarkBitmap, watermarkImagePath)

        val inputVideoPath = absolutePath
        outputVideoPath =
            "/storage/emulated/0/Android/data/com.bayee.cameras/files/Pictures/processed_" + System.currentTimeMillis() + ".mp4"

        addWatermarkToVideo(inputVideoPath, outputVideoPath, watermarkImagePath)

    }

    fun saveBitmapAsPng(bitmap: Bitmap, filePath: String) {
        val fileOutputStream = FileOutputStream(filePath)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
    }

    fun addWatermarkToVideo(
        inputVideoPath: String,
        outputVideoPath: String,
        watermarkImagePath: String
    ) {
        val command = arrayOf(
            "-i", inputVideoPath,
            "-i", watermarkImagePath,
            "-filter_complex", "overlay=10:H-h-10", // W-w-10 和 10 分别是水印的X和Y坐标
            "-preset", "fast",
            "-y", // 覆盖输出文件
            outputVideoPath
        )

        FFmpeg.executeAsync(command) { executionId, returnCode ->
            if (returnCode == com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS) {
                // 水印添加成功
                insertVideoDataBaseWater()
            } else {
                // 水印添加失败
            }
        }
    }

    private fun insertVideoDataBaseWater() {
        lifecycleScope.launch {
            val video = Video(0, outputVideoPath)
            viewModel.insertVideo(video)
        }
    }

    fun createWatermarkBitmap2(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        // 缩小Bitmap
        val scaledBitmap =
            Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, false)
        bitmap.recycle() // 释放原始Bitmap的内存
        return scaledBitmap
    }


}










