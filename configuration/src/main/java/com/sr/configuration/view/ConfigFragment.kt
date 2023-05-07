package com.sr.configuration.view

import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sr.configuration.R
import com.sr.configuration.databinding.FragmentConfigBinding
import com.sr.configuration.util.IOptionSelectListener
import java.util.*


class ConfigFragment : Fragment(), IOptionSelectListener {

    private var _binding: FragmentConfigBinding? = null
    private val myViewModel: ConfigurationViewModel by activityViewModels()
    val mediaPlayer = MediaPlayer()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var pulseCount = 2
    private val mainViewModel: DashboardViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioGroup: RadioGroup = binding.rGButton
        if (findNavController().graph.id !=  R.id.navigation_graph)
            binding.nextBtn.text = "Guardar"
       else
            binding.nextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_configFragment_to_locationFragment)
            }


        radioGroup.setOnCheckedChangeListener { _, checkId ->

            when (checkId) {
                R.id.rbTimeActionAlert -> pulseCount = 2
                R.id.rbTimeActionAlert1 -> pulseCount = 3
                else -> throw IllegalArgumentException("Invalid option selected")
            }

            if (activity is IOptionSelectListener) {
                (activity as IOptionSelectListener).onOptionSelected(pulseCount)
            }


        }
        setAlarm()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setAlarm() {
        val selectMusicSpinner = binding.alarmSpinner
        val stopButton = binding.stopButton

        //UpMaxVolume()
        //flashOn()

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
        selectMusicSpinner.setSelection(position + 1)
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
                    val audioUri =
                        Uri.parse("android.resource://${requireActivity().packageName}/${audioResource}")
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

        stopButton.setOnClickListener {
            stopAlarm()
        }


    }

    override fun onOptionSelected(option: Int) {
        pulseCount = option
    }



    private fun stopAlarm() {
        // Detener la alarma si está sonando
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }

        // Mostrar botón de reproducir y ocultar botón de detener
        binding.stopButton.visibility = View.GONE
    }

}