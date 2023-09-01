package com.sr.epilepsyalarm.infrastructure

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.net.Uri
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sr.configuration.data.SharedPreferenceDataSourceImpl
import com.sr.configuration.infrastructure.actualLocationManager
import com.sr.epilepsyalarm.data.repository.MessageRepository
import com.sr.configuration.util.Constants.keySound
import com.sr.configuration.util.MediaPlayerUtil.mediaPlayer
import com.sr.configuration.util.MediaPlayerUtil.timer
import com.sr.epilepsyalarm.view.BlockMessageActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class LockedReceiver : BroadcastReceiver() {

    private var pulseCount = 3
    private var numClicks = 0
    private var lastClickTime : Long = 0L
    private lateinit var windowManager: WindowManager
    private lateinit var view: View
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var timesAlarm = 0

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_SCREEN_ON || intent?.action == Intent.ACTION_SCREEN_OFF) {
            setPulseCount(context)

        }else{
            NotificationManager.clearNotification(context)
        }
    }

    private fun startFunction(context: Context) {
        val result = goAsync()
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        actualLocationManager.getActualLocation(fusedLocationClient){ location ->
            coroutineScope.launch {
                val messageRepository = MessageRepository()
                messageRepository.saveLocation(context, location)
            }
        }
        coroutineScope.launch {
            val messageRepository = MessageRepository()
            messageRepository.sendMessage(context)
            messageRepository.sendSMS(context)
            val user = messageRepository.getUser()
            val activityIntent = Intent(context, BlockMessageActivity::class.java)
            activityIntent.putExtra("user_emergency", user)
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            // Iniciar la actividad
            context?.startActivity(activityIntent)
            result?.finish()
        }

       // showMessage(context)
        NotificationManager.showNotification(context)
        startAlarm(context)
    }



    fun setPulseCount(context: Context) {

        val result = goAsync()
        val coroutineScope = CoroutineScope(Dispatchers.Default)

        coroutineScope.launch {
            val messageRepository = MessageRepository()
            this@LockedReceiver.pulseCount = messageRepository.getPulseCount(context) // actualiza la variable numClicks con la opción seleccionada
            timePulse(context)
            result?.finish()
        }


    }

    fun timePulse(context: Context){
        val clickTime = System.currentTimeMillis()
        println("tiempo respuesta: ${clickTime - lastClickTime}")

        if ((clickTime - lastClickTime) < 5000 ) {
            if (numClicks < pulseCount) {
                numClicks++
            }

            if (numClicks == pulseCount) {
                startFunction(context)
                lastClickTime = System.currentTimeMillis() // resetea el tiempo de la última pulsación
                numClicks = 0
                lastClickTime = 0L
            }


        }else{
            numClicks = 1 //Reinicia el contador
        }
        lastClickTime = clickTime
        println(pulseCount.toString() + " numero de clicks: " + numClicks)

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
        timer = Timer()

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