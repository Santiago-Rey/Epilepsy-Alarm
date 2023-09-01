package com.sr.configuration.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sr.configuration.databinding.FragmentUserManualBinding

class UserManualFragment : Fragment() {

    private var _binding: FragmentUserManualBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserManualBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
}