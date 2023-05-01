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

        val instrucciones = " Con cuidado recueste a la persona en el piso.\n" +
                " Voltee a la persona suavemente hacia un lado. Esto la ayudará a respirar.\n" +
                " Retire del área alrededor de la persona los objetos duros o filosos para prevenir lesiones.\n" +
                " Ponga la cabeza de la persona sobre algo suave y plano, como una chaqueta doblada.\n" +
                " Si tiene anteojos, quíteselos.\n" +
                " Suéltele la corbata o cualquier cosa que tenga alrededor del cuello que pueda dificultar su respiración.\n" +
                " Tome el tiempo que dure la convulsión. Llame al 911 si la convulsión dura más de 5 minutos."

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