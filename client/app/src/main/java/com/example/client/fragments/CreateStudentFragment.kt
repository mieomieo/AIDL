package com.example.client.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.client.R
import com.example.client.common.areAllFieldsFilled
import com.example.client.common.areScoresValid
import com.example.client.databinding.FragmentCreateStudentBinding
import com.example.client.service.ServiceManager
import com.example.server.IADLStudent
import com.example.server.model.Student

class CreateStudentFragment : Fragment() {
    private lateinit var binding: FragmentCreateStudentBinding
    private var studentManagerService: IADLStudent? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateStudentBinding.inflate(inflater, container, false)
        studentManagerService = ServiceManager.getStudentManager()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                val student = Student()
                student.name = binding.etName.text.toString()
                student.age = binding.etAge.text.toString().toInt()
                student.className = binding.etClass.text.toString()
                student.mathScore = binding.etMath.text.toString().toFloat()
                student.englishScore = binding.etEnglish.text.toString().toFloat()
                student.literatureScore = binding.etLiterature.text.toString().toFloat()
                student.physicalScore = binding.etPhysic.text.toString().toFloat()
                student.chemistryScore = binding.etChemistry.text.toString().toFloat()
                addStudent(student)
                val bundle = Bundle()
                bundle.putBoolean("dataUpdated", true)
                findNavController().navigate(
                    R.id.action_createStudentFragment_to_mainFragment,
                    bundle
                )
            } else {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun addStudent(student: Student) {
        studentManagerService?.addStudent(student)
        Log.d("listStudents", studentManagerService?.allStudents.toString())
        Toast.makeText(context, "Student added successfully", Toast.LENGTH_SHORT).show()
    }

}