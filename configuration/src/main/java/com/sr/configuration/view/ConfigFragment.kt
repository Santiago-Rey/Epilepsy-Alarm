package com.sr.configuration.view

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sr.configuration.R
import com.sr.configuration.databinding.FragmentConfigBinding
import java.util.*


class ConfigFragment : Fragment() {

    private var _binding: FragmentConfigBinding? = null
    private val myViewModel: ConfigurationViewModel by activityViewModels()
    val mediaPlayer = MediaPlayer()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var pulseCount = 2
    private val mainViewModel: DashboardViewModel by viewModels()
    val audioResources = mapOf(
        "Alarma 1" to R.raw.alarm_one,
        "Alarma 2" to R.raw.alarm_two,
        "Alarma 3" to R.raw.alarm_three
    )


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
        var timeAlert = 0
        pulseCount = mainViewModel.getPulseCount(requireContext())
        when (pulseCount) {
            2 -> timeAlert = R.id.rbTimeActionAlert
            3 -> timeAlert = R.id.rbTimeActionAlert1

            else -> throw IllegalArgumentException("Invalid option selected")
        }
        radioGroup.check(timeAlert)
        radioGroup.setOnCheckedChangeListener { _, checkId ->

            when (checkId) {
                R.id.rbTimeActionAlert -> pulseCount = 2
                R.id.rbTimeActionAlert1 -> pulseCount = 3
                else -> throw IllegalArgumentException("Invalid option selected")
            }

            mainViewModel.savePulseCount(requireContext(), pulseCount)


        }
        setAlarm()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setAlarm() {
        val selectMusicSpinner = binding.alarmSpinner
        //UpMaxVolume()
        //flashOn()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, audioResources.map { it.key })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectMusicSpinner.adapter = adapter
        val position = audioResources.keys.indexOfFirst {
            audioResources[it] == mainViewModel.getAlarm(requireContext())
        }
        selectMusicSpinner.setSelection(position)

        binding.stopButton.setOnClickListener {
            stopAlarm()
        }

        binding.playButton.setOnClickListener {
            playAlarm()
        }

    }
    private fun playAlarm(){
        val selectMusicSpinner = binding.alarmSpinner
        val position = selectMusicSpinner.selectedItemPosition


        val audioResource = audioResources.toList().get(position).second
        val audioUri =
            Uri.parse("android.resource://${requireActivity().packageName}/${audioResource}")
        mediaPlayer.apply {
            reset()
            setDataSource(requireContext(), audioUri)
            prepare()
            start()
        }
        myViewModel.soundAlarm.value = audioResource



        binding.playButton.visibility = View.GONE
        binding.stopButton.visibility = View.VISIBLE
    }



    private fun stopAlarm() {
        // Detener la alarma si está sonando
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }

        // Mostrar botón de reproducir y ocultar botón de detener
        binding.stopButton.visibility = View.GONE
        binding.playButton.visibility = View.VISIBLE
    }

}