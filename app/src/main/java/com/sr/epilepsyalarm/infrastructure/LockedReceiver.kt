package com.sr.epilepsyalarm.infrastructure

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sr.epilepsyalarm.data.repository.MessageRepository
import com.sr.epilepsyalarm.view.TempActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LockedReceiver : BroadcastReceiver() {

    private var pulseCount = 3
    private var numClicks = 0
    private var lastClickTime : Long = 0L

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_SCREEN_ON || intent?.action == Intent.ACTION_SCREEN_OFF) {
            setPulseCount(context)

        }else{
            NotificationManager.clearNotification(context)
        }
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
        val activityIntent = Intent(context, TempActivity ::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if ((clickTime - lastClickTime) < 5000 ) {
            if (numClicks < pulseCount) {
                numClicks++
            }

            if (numClicks == pulseCount) {


                context.startActivity(activityIntent)

                //startFunction(context)

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






}