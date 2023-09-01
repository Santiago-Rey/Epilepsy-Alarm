package com.sr.epilepsyalarm.infrastructure

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sr.epilepsyalarm.R
import com.sr.epilepsyalarm.view.MainActivity
import com.sr.configuration.util.Constants.channelId

object NotificationManager {


    fun showNotification(context: Context?) {
        // Crea la notificación
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)


        context?.let {
            val builder = NotificationCompat.Builder(it, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("EPILEPSYALARM")
                .setContentText("Toque para abrir la aplicacion")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return
                }
                notify(0, builder.build())
            }
        }


    }

    fun clearNotification(context: Context?) {
        // Elimina la notificación
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)
    }
}