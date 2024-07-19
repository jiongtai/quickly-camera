package com.bayee.cameras.activity.photographActivity.product

import com.bayee.cameras.util.Constant


abstract class WaterMarkBase{
    constructor()
    abstract fun getWaterMarkType(): String
}

class Water1_1: WaterMarkBase() {

    var tv_1_1_1 = ""   //北纬
    var tv_1_1_2 = ""   //东京
    var tv_1_1_3 = ""   //时间
    var tv_1_1_4 = ""   //地点

    var tv_1_1_1_View = true
    var tv_1_1_2_View = true
    var tv_1_1_3_View = true
    var tv_1_1_4_View = true


    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_1_1
    }

    override fun toString(): String {
        return "Water1_1(tv_1_1_1='$tv_1_1_1', tv_1_1_2='$tv_1_1_2', tv_1_1_3='$tv_1_1_3', tv_1_1_4='$tv_1_1_4', tv_1_1_1_View=$tv_1_1_1_View, tv_1_1_2_View=$tv_1_1_2_View, tv_1_1_3_View=$tv_1_1_3_View, tv_1_1_4_View=$tv_1_1_4_View)"
    }


}


class Water1_2: WaterMarkBase() {

    var tv_1_2_1 = ""
    var tv_1_2_2 = ""

    var tv_1_2_1_View = true
    var tv_1_2_2_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_1_2
    }
}

//心情水印
class Water1_3: WaterMarkBase() {

    var tv_1_3_1 = ""//心情
    var tv_1_3_2 = ""//时间

    var tv_1_3_1_View = true
    var tv_1_3_2_View = true



    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_1_3
    }

    override fun toString(): String {
        return "Water1_3(tv_1_3_1='$tv_1_3_1', tv_1_3_2='$tv_1_3_2', tv_1_3_1_View=$tv_1_3_1_View, tv_1_3_2_View=$tv_1_3_2_View)"
    }
}

//天气水印
class Water1_4: WaterMarkBase() {

    var tv_1_4_1 = ""//温度
    var tv_1_4_2 = ""//时间
    var tv_1_4_3 = ""//地点

    var tv_1_4_1_View = true
    var tv_1_4_2_View = true
    var tv_1_4_3_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_1_4
    }
}

//时间水印
class Water1_5: WaterMarkBase() {

    var tv_1_5_1_shi = ""//时
    var tv_1_5_2_fen = ""//分
    var tv_1_5_3_miao = ""//秒
    var tv_1_5_4 = ""//总时间

    var tv_1_5_4_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_1_5
    }
}

//纪念日水印
class Water1_6: WaterMarkBase() {

    var tv_1_6_1 = ""//标题
    var tv_1_6_2  = ""//白
    var tv_1_6_3  = ""//十
    var tv_1_6_4  = ""//个
    var tv_1_6_5 = ""//时间
    var tv_1_6_6 = ""//地点


    var tv_1_6_1_View = true//标题
    var tv_1_6_2_View = true//白
    var tv_1_6_3_View = true//十
    var tv_1_6_4_View = true//个
    var tv_1_6_5_View = true//时间
    var tv_1_6_6_View = true//地点
    var line_1_6_day_View = true//天数的整个布局

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_1_6
    }

}

class Water1_7: WaterMarkBase() {

    var tv_1_7_1 = ""//天数
    var tv_1_7_2  = ""//时间
    var tv_1_7_3  = ""//地点

    var tv_1_7_1_View = true//标题
    var tv_1_7_2_View = true//白
    var tv_1_7_3_View = true//十
    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_1_7
    }
}

class Water2_1: WaterMarkBase() {

    var tv_2_1_1 = ""//时间
    var tv_2_1_2  = ""//日期
    var tv_2_1_3  = ""//星期
    var tv_2_1_4  = ""//地点
    var tv_2_1_5  = ""//总时间

    var tv_2_1_1_View = true//标题
    var tv_2_1_2_View = true//
    var tv_2_1_3_View = true//
    var tv_2_1_4_View = true//
    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_2_1
    }
}

class Water2_2: WaterMarkBase() {

    var tv_2_2_1 = ""//时间
    var tv_2_2_2  = ""//分
    var tv_2_2_3  = ""//秒
    var tv_2_2_4  = ""//日期星期
    var tv_2_2_5  = ""// 地点

    var tv_2_2_1_View = true//标题
    var tv_2_2_2_View = true//
    var tv_2_2_3_View = true//
    var tv_2_2_4_View = true//
    var tv_2_2_5_View = true//
    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_2_2
    }
}

class Water2_3: WaterMarkBase() {   //借用七

    var tv_1_7_1 = ""//时分
    var tv_1_7_2  = ""//地点
    var tv_1_7_3  = ""//日期星期

    var tv_1_7_1_View = true//标题
    var tv_1_7_2_View = true//白
    var tv_1_7_3_View = true//十
    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_2_3
    }
}

class Water2_4: WaterMarkBase() {   //借用七

    var tv_1_7_1 = ""//时分
    var tv_1_7_2  = ""//地点
    var tv_1_7_3  = ""//日期星期

    var tv_1_7_1_View = true
    var tv_1_7_2_View = true
    var tv_1_7_3_View = true
    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_2_4
    }
}

class Water2_5: WaterMarkBase() {   //借用七

    var tv_1_7_1 = ""//时分
    var tv_1_7_2  = ""//
    var tv_1_7_3  = ""//地点

    var tv_1_7_1_View = true
    var tv_1_7_2_View = true
    var tv_1_7_3_View = true
    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_2_5
    }
}

class Water2_6: WaterMarkBase() {   //借用七

    var tv_1_7_1 = ""//时分
    var tv_1_7_2  = ""//
    var tv_1_7_3  = ""//地点

    var tv_1_7_1_View = true
    var tv_1_7_2_View = true
    var tv_1_7_3_View = true
    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_2_6
    }
}

class Water2_7: WaterMarkBase() {   //借用2_2

    var tv_2_2_1 = ""//时
    var tv_2_2_2  = ""//分
    var tv_2_2_3  = ""//秒
    var tv_2_2_4  = ""//日期星期
    var tv_2_2_5  = ""// 地点

    var tv_2_2_1_View = true//标题
    var tv_2_2_2_View = true//
    var tv_2_2_3_View = true//
    var tv_2_2_4_View = true//
    var tv_2_2_5_View = true//
    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_2_7
    }
}

class Water2_8: WaterMarkBase() {   //借用1_7

    var tv_1_7_1 = ""//时分
    var tv_1_7_2  = ""//日期星期
    var tv_1_7_3  = ""//地点

    var tv_1_7_1_View = true
    var tv_1_7_2_View = true
    var tv_1_7_3_View = true


    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_2_8
    }
}

class Water3_1: WaterMarkBase() {

    var tv_3_1_1 = "默认"//标题
    var tv_3_1_2  = "默认"//时间日期星期
    var tv_3_1_3  = "默认"//施工区域
    var tv_3_1_4  = "默认"//施工内容
    var tv_3_1_5  = "默认"//天气
    var tv_3_1_6  = "默认"//地点
    var tv_3_1_7  = "默认"//施工单位

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true
    var tv_3_1_4_View = true
    var tv_3_1_5_View = true
    var tv_3_1_6_View = true
    var tv_3_1_7_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_3_1
    }
}
class Water3_2: WaterMarkBase() {

    var tv_3_1_1 = "默认"//标题
    var tv_3_1_2  = "默认"//时间日期星期
    var tv_3_1_3  = "默认"//施工区域
    var tv_3_1_4  = "默认"//施工内容
    var tv_3_1_5  = "默认"//天气
    var tv_3_1_6  = "默认"//地点
    var tv_3_1_7  = "默认"//施工单位

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true
    var tv_3_1_4_View = true
    var tv_3_1_5_View = true
    var tv_3_1_6_View = true
    var tv_3_1_7_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_3_2
    }
}

class Water3_3: WaterMarkBase() {

    var tv_3_1_1 = "默认"//标题
    var tv_3_1_2  = "默认"//时间日期星期
    var tv_3_1_3  = "默认"//施工区域
    var tv_3_1_4  = "默认"//施工内容
    var tv_3_1_5  = "默认"//天气
    var tv_3_1_6  = "默认"//地点
    var tv_3_1_7  = "默认"//施工单位

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true
    var tv_3_1_4_View = true
    var tv_3_1_5_View = true
    var tv_3_1_6_View = true
    var tv_3_1_7_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_3_3
    }
}
class Water3_4: WaterMarkBase() {   //借用3_1

    var tv_3_1_1 = "默认"//标题
    var tv_3_1_2  = "默认"//时间日期星期
    var tv_3_1_3  = "默认"//施工班组
    var tv_3_1_4  = "默认"//施工人数
    var tv_3_1_5  = "默认"//天气
    var tv_3_1_6  = "默认"//劳务单位

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true
    var tv_3_1_4_View = true
    var tv_3_1_5_View = true
    var tv_3_1_6_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_3_4
    }
}

class Water3_5: WaterMarkBase() {   //借用3_1

    var tv_3_1_1 = "默认"//标题
    var tv_3_1_2  = "默认"//时间日期星期
    var tv_3_1_3  = "默认"//施工班组

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_3_5
    }

}

class Water3_6: WaterMarkBase() {   //借用3_1

    var tv_3_1_1 = "默认"//标题
    var tv_3_1_2  = "默认"//时间日期星期
    var tv_3_1_3  = "默认"//工程地点

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_3_6
    }
}

class Water3_7: WaterMarkBase() {   //借用3_1

    var tv_3_1_1 = "默认"//标题
    var tv_3_1_2  = "默认"//时间日期星期
    var tv_3_1_3  = "默认"//施工区域
    var tv_3_1_4  = "默认"//施工单位

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true
    var tv_3_1_4_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_3_7
    }
}

class Water3_8: WaterMarkBase() {   //借用3_1

    var tv_3_1_1 = "默认"//标题

    var tv_3_1_1_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_3_8
    }
}

class Water3_10: WaterMarkBase() {   //借用3_1

    var tv_3_1_1 = "默认"//标题
    var tv_3_1_2  = "默认"//时间日期星期
    var tv_3_1_3  = "默认"//工程地点

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_3_10
    }
}

class Water3_11: WaterMarkBase() {   //借用3_1

    var tv_3_1_1 = "默认"//标题
    var tv_3_1_2  = "默认"//经度
    var tv_3_1_3  = "默认"//时间

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_3_11
    }
}
class Water3_12: WaterMarkBase() {

    var tv_3_1_1 = "默认"//工程记录
    var tv_3_1_2  = "默认"//施工内容
    var tv_3_1_3  = "默认"//时间
    var tv_3_1_4  = ""//经纬度
    var tv_3_1_5  = ""//天气
    var tv_3_1_6  = ""//海拔
    var tv_3_1_7  = ""//方位角

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true
    var tv_3_1_4_View = true
    var tv_3_1_5_View = true
    var tv_3_1_6_View = true
    var tv_3_1_7_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_3_12
    }
}
class Water4_1: WaterMarkBase() {

    var tv_3_1_1 = "默认"
    var tv_3_1_2  = "默认"
    var tv_3_1_3  = "默认"
    var tv_3_1_4  = ""//
    var tv_3_1_5  = ""//
    var tv_3_1_6  = ""//地点

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true
    var tv_3_1_4_View = true
    var tv_3_1_5_View = true
    var tv_3_1_6_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_4_1
    }
}
class Water4_2: WaterMarkBase() {

    var tv_3_1_1 = "默认"
    var tv_3_1_2  = "默认"
    var tv_3_1_3  = "默认"
    var tv_3_1_4  = ""//

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true
    var tv_3_1_4_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_4_2
    }
}
class Water4_3: WaterMarkBase() {

    var tv_3_1_1 = "默认"
    var tv_3_1_2  = "默认"
    var tv_3_1_3  = "默认"
    var tv_3_1_4  = ""//时间
    var tv_3_1_5  = ""//
    var tv_3_1_6  = ""//地点

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true
    var tv_3_1_4_View = true
    var tv_3_1_5_View = true
    var tv_3_1_6_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_4_3
    }
}
class Water4_4: WaterMarkBase() {

    var tv_3_1_1 = "默认"
    var tv_3_1_2  = "默认"
    var tv_3_1_3  = "默认"
    var tv_3_1_4  = ""//
    var tv_3_1_5  = ""//
    var tv_3_1_6  = ""//时间

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true
    var tv_3_1_4_View = true
    var tv_3_1_5_View = true
    var tv_3_1_6_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_4_4
    }
}

class Water4_5: WaterMarkBase() {

    var tv_3_1_1 = "默认"
    var tv_3_1_2  = "默认"
    var tv_3_1_3  = "默认"
    var tv_3_1_4  = ""//
    var tv_3_1_5  = ""//时间
    var tv_3_1_6  = ""//地点

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true
    var tv_3_1_4_View = true
    var tv_3_1_5_View = true
    var tv_3_1_6_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_4_5
    }
}

class Water4_6: WaterMarkBase() {

    var tv_3_1_1 = " "
    var tv_3_1_2  = " "
    var tv_3_1_3  = " "
    var tv_3_1_4  = " "//
    var tv_3_1_5  = " "//时间

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true
    var tv_3_1_4_View = true
    var tv_3_1_5_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_4_6
    }
}
class Water4_7: WaterMarkBase() {

    var tv_3_1_1 = " "
    var tv_3_1_2  = " "
    var tv_3_1_3  = " "
    var tv_3_1_4  = " "//
    var tv_3_1_5  = " "//时间

    var tv_3_1_1_View = true
    var tv_3_1_2_View = true
    var tv_3_1_3_View = true
    var tv_3_1_4_View = true
    var tv_3_1_5_View = true

    override fun getWaterMarkType(): String {
        return Constant.WATER_TYPE_4_7
    }
}





