package com.sr.epilepsyalarm.view

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sr.configuration.data.SharedPreferenceDataSourceImpl
import com.sr.configuration.infrastructure.actualLocationManager
import com.sr.configuration.util.Constants
import com.sr.configuration.util.MediaPlayerUtil
import com.sr.configuration.util.MediaPlayerUtil.timesAlarm
import com.sr.epilepsyalarm.R
import com.sr.epilepsyalarm.data.repository.MessageRepository
import com.sr.epilepsyalarm.infrastructure.NotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import kotlin.system.exitProcess

class TempActivity : AppCompatActivity() {

    private lateinit var contadorTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var timerStopped = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)

        contadorTextView = findViewById(R.id.contadorTextView)
        startTimer()

        val stopButton = findViewById<Button>(R.id.stopButtonTemp)
        stopButton.setOnClickListener {
            stopTimer()
            finishAndRemoveTask()


        }



    }
    private fun  startFunction() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        actualLocationManager.getActualLocation(fusedLocationClient){ location ->
            coroutineScope.launch {
                val messageRepository = MessageRepository()
                messageRepository.saveLocation(this@TempActivity, location)
            }
        }
        coroutineScope.launch {
            val messageRepository = MessageRepository()
            messageRepository.sendMessage(this@TempActivity)
            messageRepository.sendSMS(this@TempActivity)
            val user = messageRepository.getUser()
            val activityIntent = Intent(this@TempActivity, BlockMessageActivity::class.java)
            activityIntent.putExtra("user_emergency", user)
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            // Iniciar la actividad
            this@TempActivity.startActivity(activityIntent)

        }

        // showMessage(context)
        NotificationManager.showNotification(this)
        startAlarm()
    }

    private fun startTimer() {
        // Crear un temporizador de cuenta regresiva de 10 segundos (10000 milisegundos)
        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Actualizaciones cada segundo (puedes hacer algo aquí si lo necesitas)
                val segundosRestantes = millisUntilFinished / 1000
                contadorTextView.text = "Tiempo restante para inicio y envio de alerta: $segundosRestantes segundos"
            }

            override fun onFinish() {
                // Acción cuando el temporizador llega a cero
                finish()
                if(!timerStopped){
                    startFunction()
                    finish()

                }
            }
        }

        // Iniciar el temporizador
        countDownTimer.start()
    }



    // Función para detener manualmente el temporizador
    private fun stopTimer() {
        countDownTimer.cancel()
        timerStopped = true
    }

    fun startAlarm(){
        timesAlarm=0
        val shared  = SharedPreferenceDataSourceImpl(this)

        val audioResource : Int = shared.getDouble(Constants.keySound, com.sr.configuration.R.raw.alarm_one.toDouble()).toInt()
        val audioUri = Uri.parse("android.resource://${this.packageName}/${audioResource}")
        MediaPlayerUtil.mediaPlayer.apply {
            reset()
            setDataSource(this@TempActivity, audioUri)
            prepare()
            start()
        }

        MediaPlayerUtil.mediaPlayer.setOnCompletionListener {
            if (timesAlarm<2){
                // Reiniciamos la reproducción
                MediaPlayerUtil.mediaPlayer.seekTo(0)
                MediaPlayerUtil.mediaPlayer.start()
                timesAlarm++
            }

        }

        flashOn(this)
        upMaxVolume(this)

    }

    private fun flashOn(context: Context) {
        var isFlashOn = false

        val cameraManager = context.getSystemService(AppCompatActivity.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        MediaPlayerUtil.timer = Timer()

        MediaPlayerUtil.timer.schedule(object : TimerTask() {
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