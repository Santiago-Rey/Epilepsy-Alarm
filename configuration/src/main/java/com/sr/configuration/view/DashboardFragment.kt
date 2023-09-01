package com.sr.configuration.view

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
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
    val mediaPlayer = MediaPlayer()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        mainViewModel.getMessageAlert()
        binding.editButton.setOnClickListener {
            mainViewModel.setMessageAlert(binding.editTextMessage.text.toString())
            if(binding.editTextMessage != null){
                Toast.makeText(requireContext(), "Mensaje guardado", Toast.LENGTH_SHORT).show()
            }
        }
        mainViewModel.getMessageInstruction()
        binding.editButton1.setOnClickListener {
            mainViewModel.setMessageInstruction(binding.addTextMessage .text.toString())
            if(binding.addTextMessage != null){
                Toast.makeText(requireContext(), "Se agregaron instrucciones", Toast.LENGTH_SHORT).show()
            }
        }
        val textView = binding.instructionsEdittext

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

       // setAlarm()
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

        mainViewModel.addInstruction.observe(viewLifecycleOwner) {
            binding.addTextMessage.setText(it)
        }
    }


    /*private fun setAlarm(){
        val selectMusicSpinner = binding.alarmSpinnerConfig

        val stopButton = binding.stopButton

        val audioResources = mapOf(
            "Alarma 1" to R.raw.alarm_one,
            "Alarma 2" to R.raw.alarm_two,
            "Alarma 3" to R.raw.alarm_three
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("Predeterminado") + audioResources.map { it.key })
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

                    stopButton.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


    }*/

}