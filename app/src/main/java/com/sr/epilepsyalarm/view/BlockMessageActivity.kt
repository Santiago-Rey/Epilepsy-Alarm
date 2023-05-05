package com.sr.epilepsyalarm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.sr.configuration.databinding.FragmentInitBinding
import com.sr.epilepsyalarm.R
import com.sr.epilepsyalarm.databinding.ActivityBlockMessageBinding
import com.sr.epilepsyalarm.databinding.ActivityInitBinding

class BlockMessageActivity : AppCompatActivity() {

//    val textView: TextView = findViewById(R.id.textView)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_message)


        val instrucciones = " Con cuidado recueste a la persona en el piso.\n" +
                " Voltee a la persona suavemente hacia un lado. Esto la ayudará a respirar.\n" +
                " Retire del área alrededor de la persona los objetos duros o filosos para prevenir lesiones.\n" +
                " Ponga la cabeza de la persona sobre algo suave y plano, como una chaqueta doblada.\n" +
                " Si tiene anteojos, quíteselos.\n" +
                " Suéltele la corbata o cualquier cosa que tenga alrededor del cuello que pueda dificultar su respiración.\n" +
                " Tome el tiempo que dure la convulsión. Llame al 911 si la convulsión dura más de 5 minutos."


       // textView.setText(instrucciones)
    }
}