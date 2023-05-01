package com.sr.configuration.view

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.sr.configuration.R
import com.sr.configuration.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: DashboardViewModel by viewModels()
    private val myViewModel : ConfigurationViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        mainViewModel.getMessageAlert()

        binding.editButton.setOnClickListener {
            mainViewModel.setMessageAlert(binding.editTextMessage.text.toString())
        }
        val textView = binding.instructionsEdittext

        val instrucciones = " Con cuidado recueste a la persona en el piso.\n" +
                " Voltee a la persona suavemente hacia un lado. Esto la ayudará a respirar.\n" +
                " Retire del área alrededor de la persona los objetos duros o filosos para prevenir lesiones.\n" +
                " Ponga la cabeza de la persona sobre algo suave y plano, como una chaqueta doblada.\n" +
                " Si tiene anteojos, quíteselos.\n" +
                " Suéltele la corbata o cualquier cosa que tenga alrededor del cuello que pueda dificultar su respiración.\n" +
                " Tome el tiempo que dure la convulsión. Llame al 911 si la convulsión dura más de 5 minutos."

        textView.text = instrucciones

        setAlarm()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    private fun setObserver() {
        mainViewModel.messageAlert.observe(viewLifecycleOwner) {
            binding.editTextMessage.setText(it)
        }
    }

    private fun setAlarm(){
        val selectMusicSpinner = binding.alarmSpinnerConfig
        val mediaPlayer = MediaPlayer()

        val audioResources = mapOf(
            "alarma 1" to R.raw.alarm_one,
            "alarma 2" to R.raw.alarm_two,
            "alarma 3" to R.raw.alarm_three
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("Predeterminado")+audioResources.map { it.key })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectMusicSpinner.adapter = adapter
        val position = audioResources.keys.indexOfFirst {
            audioResources[it] == mainViewModel.getAlarm(requireContext())
        }
        selectMusicSpinner.setSelection(position+1)
        selectMusicSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    val audioName = parent.getItemAtPosition(position) as String
                    val audioResource = audioResources[audioName]
                    val audioUri = Uri.parse("android.resource://${requireActivity().packageName}/${audioResource}")
                    mediaPlayer.apply {
                        reset()
                        setDataSource(requireContext(), audioUri)
                        prepare()
                        start()
                    }
                    myViewModel.soundAlarm.value = audioResource
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


    }


}