package com.bayee.cameras.util

import com.bayee.cameras.R
import com.bayee.cameras.category.Category
import com.bayee.cameras.activity.home.recycleviewmodel.WaterMarkItem
import com.bayee.cameras.activity.vipcenter.VipPrivilegeBean
import com.bayee.cameras.activity.vipcenter.VipWaterBean

object DataUtil {
    val categories: List<Category> = listOf(
        Category.create(
            "0", "全部"
        ), Category.create(
            "1", "基础通用"
        ), Category.create(
            "2", "考勤打卡"
        ), Category.create(
            "3", "工程建筑"
        ), Category.create(
            "4", "物业管理"
        ), Category.create(
            "5", "施工装修"
        ), Category.create(
            "6", "家政服务"
        ), Category.create(
            "7", "物流运输"
        )
    )

    val BaseWaterMark: List<WaterMarkItem> = listOf(
        WaterMarkItem(R.layout.water_mark_base_longitude, "经纬度水印","1_1"),
        WaterMarkItem(R.layout.water_mark_base_local, "地点水印","1_2"),
        WaterMarkItem(R.layout.water_mark_base_mood, "心情水印","1_3"),
        WaterMarkItem(R.layout.water_mark_base_weather, "天气水印","1_4"),
        WaterMarkItem(R.layout.water_mark_base_common_time, "时间水印","1_5"),
        WaterMarkItem(R.layout.water_mark_base_memorial_time, "纪念日水印","1_6"),
        WaterMarkItem(R.layout.water_mark_base_birthday_time, "生日水印","1_7"),
    )

    val AttendanceWaterMark: List<WaterMarkItem> = listOf(
        WaterMarkItem(R.layout.water_mark_attendance_work1, "上班打卡","2_1"),
        WaterMarkItem(R.layout.water_mark_attendance_work2, "下班打卡","2_2"),
        WaterMarkItem(R.layout.water_mark_attendance_work3, "上班打卡","2_3"),
        WaterMarkItem(R.layout.water_mark_attendance_work4, "上班打卡","2_4"),
        WaterMarkItem(R.layout.water_mark_attendance_attendance1, "考勤水印","2_5"),
        WaterMarkItem(R.layout.water_mark_attendance_attendance2, "考勤水印","2_6"),
        WaterMarkItem(R.layout.water_mark_attendance_attendance3, "考勤水印","2_7"),
        WaterMarkItem(R.layout.water_mark_attendance_attendance4, "考勤水印","2_8"),
    )

    val ConstructionWaterMark: List<WaterMarkItem> = listOf(
        WaterMarkItem(R.layout.water_mark_construction_doing, "施工中","3_1"),
        WaterMarkItem(R.layout.water_mark_construction_before, "施工前","3_2"),
        WaterMarkItem(R.layout.water_mark_construction_under, "施工后","3_3"),
        WaterMarkItem(R.layout.water_mark_construction_early_meeting, "早班会","3_4"),
        WaterMarkItem(R.layout.water_mark_construction_affairs, "水务巡查水印","3_5"),
        WaterMarkItem(R.layout.water_mark_construction_green, "工程水印（绿色）","3_6"),
        WaterMarkItem(R.layout.water_mark_construction_simple, "简约水印","3_7"),
        WaterMarkItem(R.layout.water_mark_construction_yellow, "施工水印","3_8"),
        WaterMarkItem(R.layout.water_mark_construction_attendance, "考勤打卡","3_9"),
        WaterMarkItem(R.layout.water_mark_construction_construction, "工程水印","3_10"),
        WaterMarkItem(R.layout.water_mark_construction_yaun_dao, "元道水印","3_11"),
        WaterMarkItem(R.layout.water_mark_construction_record, "工程记录","3_12"),
    )

    val EstateManagerWaterMark: List<WaterMarkItem> = listOf(
        WaterMarkItem(R.layout.water_mark_estate_conference_training, "物业会议培训","4_1"),
        WaterMarkItem(R.layout.water_mark_estate_patrol, "执勤巡逻","4_2"),
        WaterMarkItem(R.layout.water_mark_estate_green_protect, "绿化养护","4_3"),
        WaterMarkItem(R.layout.water_mark_estate_inspection, "值班巡检","4_4"),
        WaterMarkItem(R.layout.water_mark_estate_daily_clean, "日常保洁","4_5"),
        WaterMarkItem(R.layout.water_mark_estate_safe_manager, "客服服务","4_6"),
        WaterMarkItem(R.layout.water_mark_estate_kefu_service, "安全管理","4_7"),
        WaterMarkItem(R.layout.water_mark_estate_wuye_meetnig, "物业会议记录","4_8"),
        WaterMarkItem(R.layout.water_mark_estate_disinfect, "已消毒","4_9"),
        WaterMarkItem(R.layout.water_mark_estate_fault, "故障维修","4_10"),
        WaterMarkItem(R.layout.water_mark_estate_evelator, "电梯维修","4_11"),
        WaterMarkItem(R.layout.water_mark_estate_fire, "消防巡检","4_12"),
    )

    val DecorationWaterMark: List<WaterMarkItem> = listOf(
        WaterMarkItem(R.layout.water_mark_decoration_start1, "开工大吉","5_1"),
        WaterMarkItem(R.layout.water_mark_decoration_start2, "今日宜开工","5_2"),
        WaterMarkItem(R.layout.water_mark_decoration_start3, "开工大吉","5_3"),
        WaterMarkItem(R.layout.water_mark_decoration_start4, "施工进度","5_4"),
        WaterMarkItem(R.layout.water_mark_decoration_start5, "项目巡检","5_5"),
        WaterMarkItem(R.layout.water_mark_decoration_start6, "材料进场","5_6"),
        WaterMarkItem(R.layout.water_mark_decoration_start7, "厨房","5_7"),
        WaterMarkItem(R.layout.water_mark_decoration_start8, "客厅","5_8"),
        WaterMarkItem(R.layout.water_mark_decoration_start9, "卫生间","5_9"),
        WaterMarkItem(R.layout.water_mark_decoration_start10, "主卧","5_10"),
        WaterMarkItem(R.layout.water_mark_decoration_start11, "次卧","5_11"),
    )

    val HomeServiceWaterMark: List<WaterMarkItem> = listOf(
        WaterMarkItem(R.layout.water_mark_home_service1, "签收凭证","6_1"),
        WaterMarkItem(R.layout.water_mark_home_service2, "签收凭证","6_2"),
        WaterMarkItem(R.layout.water_mark_home_service3, "环卫记录","6_3"),
        WaterMarkItem(R.layout.water_mark_home_service4, "电器清洁","6_4"),
        WaterMarkItem(R.layout.water_mark_home_service5, "家庭电器维修","6_5"),
        WaterMarkItem(R.layout.water_mark_home_service6, "看护服务","6_6"),
    )

    val LogisticsWaterMark: List<WaterMarkItem> = listOf(
        WaterMarkItem(R.layout.water_mark_logistic1, "装载记录","7_1"),
        WaterMarkItem(R.layout.water_mark_logistic2, "外卖已送达","7_2"),
        WaterMarkItem(R.layout.water_mark_logistic3, "外卖已送达","7_3"),
        WaterMarkItem(R.layout.water_mark_logistic4, "快递已送达","7_4"),
        WaterMarkItem(R.layout.water_mark_logistic5, "民航巡检水印","7_5"),
        WaterMarkItem(R.layout.water_mark_logistic6, "物流配送","7_6"),
        WaterMarkItem(R.layout.water_mark_logistic7, "车辆报销记录","7_7"),
        WaterMarkItem(R.layout.water_mark_logistic8, "车辆检查","7_8"),
        WaterMarkItem(R.layout.water_mark_logistic9, "冷链配送","7_9"),
    )

    val AllWaterMark = BaseWaterMark +
            AttendanceWaterMark +
            ConstructionWaterMark +
            EstateManagerWaterMark +
            DecorationWaterMark +
            HomeServiceWaterMark +
            LogisticsWaterMark

    val CameraFlashs = listOf(
        R.drawable.camera_flash_have,
        R.drawable.camera_flash_not,
    )

    val CameraSlowTime = listOf(
        R.drawable.camera_slow_0,
        R.drawable.camera_slow_1,
        R.drawable.camera_slow_2,
        R.drawable.camera_slow_3,
        R.drawable.camera_slow_4,
        R.drawable.camera_slow_5,
    )

    val cameraPrimary = listOf(
        R.drawable.camera_primary_blue,
        R.drawable.camera_primary_red,
    )

    val record_isSelected = listOf(
        R.drawable.record_no_edit,
        R.drawable.record_yes_edit,
    )

    val vip_center : List<VipWaterBean> = listOf(
        VipWaterBean(R.drawable.vip_center_water1,"基础通用"),
        VipWaterBean(R.drawable.vip_center_water2,"考勤打卡"),
        VipWaterBean(R.drawable.vip_center_water3,"工程建筑"),
        VipWaterBean(R.drawable.vip_center_water4,"施工装修"),
        VipWaterBean(R.drawable.vip_center_water5,"物业管理"),
        VipWaterBean(R.drawable.vip_center_water6,"家政服务"),
        VipWaterBean(R.drawable.vip_center_water7,"物流运输"),
    )

    val vip_Privilege : List<VipPrivilegeBean> = listOf(
        VipPrivilegeBean(R.drawable.vip_icon1,"永久VIP","畅享全部特权"),
        VipPrivilegeBean(R.drawable.vip_icon2,"水印照片","拍照加水印"),
        VipPrivilegeBean(R.drawable.vip_icon3,"水印视频","视频加水印"),
        VipPrivilegeBean(R.drawable.vip_icon4,"海量水印","20+水印种类"),
        VipPrivilegeBean(R.drawable.vip_icon5,"水印拼接","水印图片一键处理"),
        VipPrivilegeBean(R.drawable.vip_icon6,"自定义修改","一键修改水印"),
        VipPrivilegeBean(R.drawable.vip_icon7,"无限水印","全部水印无限使用"),
        VipPrivilegeBean(R.drawable.vip_icon8,"无限保存","水印添加无限保存"),
        VipPrivilegeBean(R.drawable.vip_icon9,"丰富场景","畅享全部特权"),
        VipPrivilegeBean(R.drawable.vip_icon10,"水印大全","各种分类水印场景"),

    )

}








