package com.naruto.weather.base

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.naruto.testbaidulocation.R
import com.naruto.testbaidulocation.base.BaseService
import com.naruto.testbaidulocation.utils.ServiceUtil

/**
 * @Description
 * @Author Naruto Yang
 * @CreateDate 2021/12/6 0006
 * @Note
 */
abstract class ForegroundService : BaseService() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder = initNotification()
    }

    /**
     * 初始化通知
     *
     * @return
     */
    protected open fun initNotification(): NotificationCompat.Builder {
        return ServiceUtil.setForegroundService(
            this,
            getPendingIntent(),
            R.mipmap.ic_launcher,
            getNotificationId()
        )
    }

    /**
     * 更新通知
     *
     * @param operation
     */
    protected open fun updateNotification(operation: ((NotificationCompat.Builder) -> Unit)) {
        operation(notificationBuilder)
        notificationManager.notify(getNotificationId(), notificationBuilder.build())
    }


    override fun onDestroy() {
        stopForeground(true)
        super.onDestroy()
    }

    protected abstract fun getNotificationId(): Int

    protected abstract fun getPendingIntent(): PendingIntent?

    companion object {
        /**
         * 启动前台服务
         *
         * @param context
         * @param serviceIntent
         */
        fun launch(context: Context, serviceIntent: Intent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        }

        /**
         * 启动前台服务
         *
         * @param context
         * @param serviceClass
         */
        fun <T : ForegroundService> launch(
            context: Context, serviceClass: Class<T>, block: (Intent) -> Unit = {}
        ) {
            val intent = Intent(context, serviceClass).also { block(it) }
            launch(context, intent)
        }
    }
}

/**
 *
 */
fun createPendingIntentFlag(flag: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        PendingIntent.FLAG_IMMUTABLE or flag
    else flag
}

fun <T : Service> Context.stopService(serviceClass: Class<T>) {
    val intent = Intent(this, serviceClass)
    stopService(intent)
}