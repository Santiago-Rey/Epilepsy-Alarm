package com.sr.epilepsyalarm.infrastructure

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.sr.epilepsyalarm.R
import com.sr.configuration.util.Constants.channelId
import com.sr.configuration.util.Constants.channelName

class ButtonService : Service() {


    private val lockedReceiver= LockedReceiver()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY - 1


        registerReceiver(lockedReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(lockedReceiver)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(getString(R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        startForeground(1, notificationBuilder.build())


        return START_STICKY
    }


}