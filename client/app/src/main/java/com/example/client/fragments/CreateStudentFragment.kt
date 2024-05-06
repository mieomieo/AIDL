package com.example.client.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.client.R
import com.example.client.common.areAllFieldsFilled
import com.example.client.common.areScoresValid
import com.example.client.databinding.FragmentCreateStudentBinding
import com.example.client.service.ServiceManager
import com.example.client.viewmodel.StudentViewModel
import com.example.server.IADLStudent
import com.example.server.Student
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateStudentFragment : Fragment() {
    private lateinit var binding: FragmentCreateStudentBinding
    private val viewModel: StudentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.students.observe(viewLifecycleOwner) {
        }
        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_createStudentFragment_to_mainFragment)
        }
        binding.btnSubmit.setOnClickListener {
            val allFieldsFilled = areAllFieldsFilled(
                binding.etName,
                binding.etAge,
                binding.etClass,
                binding.etMath,
                binding.etEnglish,
                binding.etLiterature,
                binding.etPhysic,
                binding.etChemistry
            )
            val scoresValid = areScoresValid(
                requireContext(),
                binding.etMath,
                binding.etEnglish,
                binding.etLiterature,
                binding.etPhysic,
                binding.etChemistry
            )
            if (allFieldsFilled && scoresValid) {
                val name = binding.etName.text.toString()
                val age = binding.etAge.text.toString().toInt()
                val className = binding.etClass.text.toString()
                val mathScore = binding.etMath.text.toString().toFloat()
                val englishScore = binding.etEnglish.text.toString().toFloat()
                val literatureScore = binding.etLiterature.text.toString().toFloat()
                val physicalScore = binding.etPhysic.text.toString().toFloat()
                val chemistryScore = binding.etChemistry.text.toString().toFloat()
                val student = Student(
                    name = name,
                    age = age,
                    className = className,
                    mathScore = mathScore,
                    englishScore = englishScore,
                    literatureScore = literatureScore,
                    physicalScore = physicalScore,
                    chemistryScore = chemistryScore
                )
                viewModel.addStudent(student)
                Toast.makeText(context, "Student added successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    R.id.action_createStudentFragment_to_mainFragment
                )
            } else {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}