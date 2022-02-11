package com.naruto.testbaidulocation

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.naruto.weather.base.ForegroundService
import com.naruto.weather.base.createPendingIntentFlag

/**
 * @Description
 * @Author Naruto Yang
 * @CreateDate 2022/2/10 0010
 * @Note
 */
private val PERMISSIONS =
    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
private const val TAG = "MyService"

class MyService : ForegroundService() {
    private val client = LocationClient(this)

    override fun getNotificationId(): Int = 1000

    override fun getPendingIntent(): PendingIntent? = PendingIntent.getActivity(
        this, 0, Intent(this, MainActivity::class.java),
        createPendingIntentFlag(PendingIntent.FLAG_UPDATE_CURRENT)
    )

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initBD()
        client.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        client.stop()
    }

    /**
     * 初始化百度定位工具
     */
    private fun initBD() {
        client.locOption = LocationClientOption()

        client.registerLocationListener(object : BDAbstractLocationListener() {
            override fun onReceiveLocation(result: BDLocation) {
                client.stop()
                result.run {
                    Log.i(TAG, "onReceiveLocation: 定位成功：经纬度=($latitude, $longitude)")
                    toast("定位成功：经纬度=($latitude, $longitude)")
                }
            }

            override fun onLocDiagnosticMessage(p0: Int, p1: Int, p2: String?) {
                super.onLocDiagnosticMessage(p0, p1, p2)
                client.stop()
                val msg = "定位失败：locType=$p0;diagnosticType=$p1;diagnosticMessage=$p2"
                Log.e(TAG, "onLocDiagnosticMessage: $msg")
                toast(msg)
            }
        })
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}