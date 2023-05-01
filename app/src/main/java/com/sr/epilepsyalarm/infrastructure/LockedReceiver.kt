package com.sr.epilepsyalarm.infrastructure

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sr.epilepsyalarm.R
import com.sr.configuration.data.SharedPreferenceDataSourceImpl
import com.sr.epilepsyalarm.data.repository.MessageRepository
import com.sr.configuration.util.Constants.keySound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class LockedReceiver : BroadcastReceiver() {

    private var pulseCount = 2
    private var numClicks = 0
    private var lastClickTime = 0L
    private lateinit var windowManager: WindowManager
    private lateinit var view: View
    private val mediaPlayer = MediaPlayer()
    private var timesAlarm=0

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_SCREEN_ON) {
          timePulse(context)
        }else{
            NotificationManager.clearNotification(context)
        }
    }

    private fun startFunction(context: Context) {
        val result = goAsync()
        val coroutineScope = CoroutineScope(Dispatchers.Default)

        coroutineScope.launch {
            val messageRepository = MessageRepository()
            messageRepository.sendMessage(context)
            messageRepository.sendSMS(context)
            result.finish()
        }
        showMessage(context)
        NotificationManager.showNotification(context)
        startAlarm(context)
    }

    fun showMessage(context: Context){

        view = LayoutInflater.from(context).inflate(R.layout.message_notify, null)

        windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        windowManager.addView(view, layoutParams)
    }



    fun setPulseCount(option: Int) {
        pulseCount = option // actualiza la variable numClicks con la opción seleccionada
        lastClickTime = System.currentTimeMillis() // resetea el tiempo de la última pulsación

    }

    fun timePulse(context: Context){
        val clickTime = System.currentTimeMillis()
        println("tiempo respuesta: ${clickTime - lastClickTime}")

        if ((clickTime - lastClickTime) < 5000 ) {
            if (numClicks < pulseCount) {
                numClicks++
            } else if (numClicks == pulseCount) {
                startFunction(context)
                numClicks = 0
            }
            //Menos de 2 segundos
            numClicks ++

        }else{
            numClicks = 1 //Reinicia el contador
        }
        lastClickTime = clickTime

        if(numClicks == 3){
            startFunction(context)
            numClicks = 0
        }

    }

    fun startAlarm(context: Context){
        timesAlarm=0
        val shared  = SharedPreferenceDataSourceImpl(context)

        val audioResource : Int = shared.getDouble(keySound, com.sr.configuration.R.raw.alarm_one.toDouble()).toInt()
        val audioUri = Uri.parse("android.resource://${context.packageName}/${audioResource}")
        mediaPlayer.apply {
            reset()
            setDataSource(context, audioUri)
            prepare()
            start()
        }

        mediaPlayer.setOnCompletionListener {
            if (timesAlarm<2){
                // Reiniciamos la reproducción
                mediaPlayer.seekTo(0)
                mediaPlayer.start()
                timesAlarm++
            }

        }

        flashOn(context)
        upMaxVolume(context)

    }

    private fun flashOn(context: Context) {
        var isFlashOn = false

        val cameraManager = context.getSystemService(AppCompatActivity.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        val timer = Timer()

        timer.schedule(object : TimerTask() {
            override fun run() {
                isFlashOn = !isFlashOn
                cameraManager.setTorchMode(cameraId, isFlashOn)
            }
        }, 0, 500)


    }

     fun upMaxVolume(context: Context) {
        val audioManager = context.getSystemService(AppCompatActivity.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        // Aumentamos el volumen al máximo permitido
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
    }


}