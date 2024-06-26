package com.sr.configuration.view

import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isEmpty
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sr.configuration.R
import com.sr.configuration.data.User
import com.sr.configuration.databinding.FragmentSignUpUserBinding
import com.sr.configuration.util.LetterInputFilter


class SignUpUserFragment : Fragment() {


    private var _binding: FragmentSignUpUserBinding? = null
    private val viewModelSignUp: SignUpUserViewModel by viewModels()
    private val myViewModel : ConfigurationViewModel by activityViewModels()
    private val binding get() = _binding!!
    private lateinit var spinner: Spinner
    private val items = arrayListOf("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-")
    private val itemDocumentType = arrayListOf("Tarjeta de identidad", "Cedula de ciudadania", "Cedula de extranjeria")
    private val letterFilter : LetterInputFilter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpUserBinding.inflate(inflater, container, false)
        binding.viewModel = viewModelSignUp
        binding.lifecycleOwner = viewLifecycleOwner
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
            spinner.adapter = adapter

            val adapter1 =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, itemDocumentType)
            spinnerDT.adapter = adapter1


            /*spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent?.getItemAtPosition(position).toString()

                    Toast.makeText(
                        requireContext(),
                        "Seleccionaste $selectedItem",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // no hace nada
                }
            }*/

            viewModelSignUp.obtainUser()
            setUpButton()

            lifecycleOwner?.let {
                viewModelSignUp.user.observe(it) {user ->
                    if(user != null){
                        spinner.setSelection(items.indexOf(user.blood))
                        spinnerDT.setSelection(itemDocumentType.indexOf(user.documentType))
                    }
                }
            }
        }
    }

    private fun setUpButton() {
        binding.nextBtn.setOnClickListener { sendUser() }

        if (findNavController().graph.id != R.id.navigation_graph){
            binding.nextBtn.text = "Guardar"
        }


    }

    fun sendUser() {
        if (validateUser()) {
            viewModelSignUp.saveUser(
                binding.etName.text.toString(),
                binding.etLastName.text.toString(),
                binding.spinner.selectedItem as String,
                binding.spinnerDT.selectedItem as String,
                binding.etNumId.text.toString(),
                binding.etNameContact.text.toString(),
                binding.etPhoneCountry.text.toString(),
                binding.etPhoneContact.text.toString()
            )
            if (findNavController().graph.id ==  R.id.navigation_graph)
                findNavController().navigate(R.id.action_signUpUser_to_configFragment)
            else{
                myViewModel.isDashboardActivate.value = true
            }
        } else {
            Toast.makeText(requireContext(), "Necesita llenar todos los campos", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun validateUser(): Boolean {
        var isValid = true
        if (binding.etName.text.isNotEmpty() &&
            binding.etLastName.text.isNotEmpty() &&
            binding.etNumId.text.isNotEmpty() &&
            binding.etNameContact.text.isNotEmpty() &&
            binding.etPhoneContact.text.isNotEmpty()
        ) {


            Toast.makeText(requireContext(), "Datos completos", Toast.LENGTH_LONG).show()

        } else {
            isValid = false
        }


        return isValid

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}