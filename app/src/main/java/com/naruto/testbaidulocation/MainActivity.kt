package com.naruto.testbaidulocation

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.naruto.weather.base.ForegroundService

/**
 * @Description
 * @Author Naruto Yang
 * @CreateDate 2022/2/10 0010
 * @Note
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //点击事件
        findViewById<Button>(R.id.btn_start).setOnClickListener {
            ForegroundService.launch(this, MyService::class.java)
        }
    }
}