import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Properties


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
//    id ("com.google.dagger.hilt.android") version "2.44"
}

val keystoreProperties = Properties()

// 假设你的keystore.properties文件位于项目根目录
val keystorePropertiesFile = rootProject.file("keystore.properties").apply {
    if (!exists()) {
        throw FileNotFoundException("Could not find keystore.properties file.")
    }
}
keystoreProperties.load(FileInputStream(keystorePropertiesFile))


android {
    namespace = "com.bayee.cameras"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bayee.cameras"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1001"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            // 设置支持的 SO 库架构
            abiFilters.addAll(listOf("armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }

    }

    //签名配置
    //要写到使用签名配置的前面
    signingConfigs {
        //以下两个 版本都用的是同一个
        //真实项目中可能会用多个

        create("customDebug") { // 使用不同的名字避免与默认的'debug'冲突
            storeFile = file(keystoreProperties.getProperty("storeFile"))
            storePassword = keystoreProperties.getProperty("storePassword")
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
        }


        create("release") {
            storeFile = file(keystoreProperties.getProperty("storeFile"))
            storePassword = keystoreProperties.getProperty("storePassword")
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
            System.out.println(storePassword + " release2222")
        }


    }

    buildTypes {

        getByName("debug") {
            signingConfig = signingConfigs.getByName("customDebug")
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")



            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
//        release {
//
//        }


    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true;// 这里直接赋值为true来启用viewBinding
    }
}

//val aarFiles = rootProject.file("libs").listFiles { file -> file.extension == "aar" }

dependencies {

//    aarFiles?.forEach {
//        implementation(files(it.absolutePath))
//    }

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.ads.identifier)
    implementation(libs.androidx.databinding.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //UI框架
    implementation("com.qmuiteam:qmui:2.1.0")
    //apache common lang3工具包
    implementation("org.apache.commons:commons-lang3:3.8")
    //类似TabLayout的控件
    implementation("com.github.angcyo.DslTablayout:TabLayout:3.5.3")
    implementation("com.github.angcyo.DslTablayout:ViewPager2Delegate:3.5.3")
    //okhttp
    //https://github.com/square/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    //用来打印okhttp请求日志
    //当然也可以自定义
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    //retrofit
    //https://github.com/square/retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //使用gson解析json
    //https://github.com/google/gson
    implementation("com.google.code.gson:gson:2.9.1")
    //适配retrofit使用gson解析
    //版本要和retrofit一样
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //适配器工厂


    //endregion


    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    //https://developer.android.google.cn/jetpack/androidx/releases/lifecycle?hl=zh-cn
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    //圆形指示器
    //https://github.com/ongakuer/CircleIndicator
    implementation("me.relex:circleindicator:2.1.6")
    //权限框架
    //https://github.com/guolindev/PermissionX
    implementation("com.guolindev.permissionx:permissionx:1.7.1")
    //相机
    // CameraX core library using the camera2 implementation
    val camerax_version = "1.4.0-beta02"
//    val camerax_version = "1.0.0"
    // The following line is optional, as the core library is included indirectly by camera-camera2
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    // If you want("o additionally use the CameraX Lifecycle library
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    // If you want("o additionally use the CameraX VideoCapture library
    implementation("androidx.camera:camera-video:${camerax_version}")
    // If you want("o additionally use the CameraX View class
    implementation("androidx.camera:camera-view:${camerax_version}")
    // If you want("o additionally add CameraX ML Kit Vision Integration
    implementation("androidx.camera:camera-mlkit-vision:${camerax_version}")
    // If you want("o additionally use the CameraX Extensions library
    implementation("androidx.camera:camera-extensions:${camerax_version}")


    //Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    //Rxjava
    implementation("androidx.room:room-rxjava3:2.6.1")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

//    implementation("com.google.dagger:hilt-android:2.44")
//    kapt("com.google.dagger:hilt-compiler:2.44")

    //高德
    implementation("com.amap.api:location:5.2.0")
//    implementation("com.amap.api:3dmap:latest.integration")
    implementation("com.amap.api:search:latest.integration")
//    implementation("com.amap.api:3dmap:9.2.0")
//    implementation("com.amap.api:search:9.2.0")

    //支付宝支付
    //https://opendocs.alipay.com/open/204/105296
    implementation("com.alipay.sdk:alipaysdk-android:+@aar")

    //微信支付
    //官方sdk下载文档：https://developers.weixin.qq.com/doc/oplatform/Downloads/Android_Resource.html
    //官方集成文档：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5
    implementation("com.tencent.mm.opensdk:wechat-sdk-android:+")

    //腾讯开源的高性能keyValue存储，用来替代系统的SharedPreferences
    //https://github.com/Tencent/MMKV
    implementation("com.tencent:mmkv-static:1.2.16")

    //选择器(pickview)
    //https://github.com/Bigkoo/Android-PickerView
    implementation("com.contrarywind:Android-PickerView:4.1.9")

    //通过OkHttp的拦截器机制
    //实现在应用通知栏显示网络请求功能
    //https://github.com/ChuckerTeam/chucker
    debugImplementation("com.github.chuckerteam.chucker:library:3.5.2")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:3.5.2")

    //基于协程跨界面通讯
    //https://github.com/liangjingkanji/Channel
    implementation("com.github.liangjingkanji:Channel:1.1.5")

    //序列化
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    // Moshi
    implementation("com.squareup.moshi:moshi:1.12.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    // Moshi codegen for generating adapters
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")


    //https://blog.csdn.net/ZhaiKun68/article/details/133089895
    //https://github.com/Blankj/AndroidUtilCode/blob/master/lib/utilcode/README-CN.md
    implementation("com.blankj:utilcodex:1.31.1")

    implementation("androidx.gridlayout:gridlayout:1.0.0")

    //ffmpeg
    implementation("com.arthenica:mobile-ffmpeg-full:4.4.LTS")

}












