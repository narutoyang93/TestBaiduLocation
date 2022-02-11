package com.naruto.testbaidulocation

import android.app.Application
import com.baidu.mapapi.SDKInitializer

/**
 * @Description
 * @Author Naruto Yang
 * @CreateDate 2022/2/10 0010
 * @Note
 */
class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        SDKInitializer.initialize(this)
    }
}