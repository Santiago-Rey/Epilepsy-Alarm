package com.sr.epilepsyalarm.view

import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.sr.configuration.domain.UserModel
import com.sr.configuration.util.MediaPlayerUtil.mediaPlayer
import com.sr.configuration.util.MediaPlayerUtil.timer
import com.sr.epilepsyalarm.R


class BlockMessageActivity : AppCompatActivity() {

//    val textView: TextView = findViewById(R.id.textView)

    private lateinit var mensaje: TextView
    private lateinit var dataTV: TextView
    private lateinit var lastaNameTV: TextView
    private lateinit var rHTV: TextView
    private lateinit var nameEmergencyTV: TextView
    private lateinit var numberEmergencyTV: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_message)
        val userData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user_emergency", UserModel::class.java)
        } else {
            intent.getParcelableExtra<UserModel>("user_emergency")
        }
        mensaje = findViewById(R.id.mensaje)
        dataTV = findViewById(R.id.nameUserTV)
        lastaNameTV = findViewById(R.id.lastNameTV)
        rHTV = findViewById(R.id.rHTV)
        nameEmergencyTV = findViewById(R.id.nameEmergencyTV)
        numberEmergencyTV = findViewById(R.id.phoneEmergencyTV)
        dataTV.text = ("Nombre paciente: " + userData?.name) ?: "Nombre usuario"
        lastaNameTV.text = (" " + userData?.lastName) ?: "Apellido usuario"
        rHTV.text = ("Tipo de sangre: " + userData?.blood) ?: "rH usuario"
        nameEmergencyTV.text =
            ("Contacto de emergencia: " + userData?.nameEmergency) ?: "contacto emergencia usuario"
        numberEmergencyTV.text =
            ("Numero contacto: " + userData?.numberEmergency) ?: "Telefono Emergencia usuario"

        val instrucciones =
                " - INSTRUCCIONES PARA MANEJAR EL ATAQUE.\n\n" +
                " - Mantener la calma.\n" +
                " - No abandonar a la persona.\n" +
                " - Mover a la persona solo si está en peligro.\n" +
                " - Recostarla preferiblemente en el suelo.\n" +
                " - Colocar de medio lado.\n" +
                " - Proteger la cabeza de golpes.\n" +
                " - Alejar a los curiosos.\n" +
                " - Observar detenidamente la crisis para poder describirla al personal de salud.\n" +
                " - Medir el tiempo.\n" +
                " - Tranquilizar a la persona cuando se recupere.\n" +
                " - Ayudarle a orientarse. \n\n " +
                " DATOS DEL USUARIO. \n "

        mensaje.text = instrucciones
        val stopButton = findViewById<Button>(R.id.stopButton)
        stopButton.setOnClickListener {
            stopAlarm()
            flashOff()
            finish()

        }
    }
    private fun flashOff() {
        timer.cancel()
        val cameraManager = getSystemService(AppCompatActivity.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        cameraManager.setTorchMode(cameraId, false)

    }


    private fun stopAlarm() {
        // Detener la alarma si está sonando
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }

    }
}