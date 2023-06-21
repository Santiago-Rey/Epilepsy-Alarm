package com.sr.configuration.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sr.configuration.R
import com.sr.configuration.databinding.FragmentInitBinding

class InitFragment : Fragment() {

    private var _binding: FragmentInitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInitBinding.inflate(inflater, container, false)
        val view = binding.root

        val textView = binding.tvInstructions

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
                    " - Ayudarle a orientarse."

        textView.text = instrucciones
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initBtn.setOnClickListener {
            findNavController().navigate(R.id.action_initFragment_to_signUpUser)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}