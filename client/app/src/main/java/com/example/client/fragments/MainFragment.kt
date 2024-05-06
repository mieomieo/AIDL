package com.example.client.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aidlclientexercise.adapter.StudentAdapter
import com.example.aidlclientexercise.adapter.StudentSearchAdapter
import com.example.client.R
import com.example.client.common.areAllFieldsFilled
import com.example.client.common.areScoresValid
import com.example.client.databinding.FragmentMainBinding
import com.example.client.service.ServiceManager
import com.example.client.viewmodel.StudentViewModel
import com.example.server.IADLStudent
import com.example.server.Student
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import interfaces.IStudentClick

@AndroidEntryPoint
class MainFragment : Fragment(), IStudentClick {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: StudentViewModel by viewModels()
    private lateinit var adapterStudent: StudentAdapter
    private var studentManagerService: IADLStudent? = null
    private lateinit var adapterSearchStudent: StudentSearchAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterStudent = StudentAdapter(this)
        binding.rvListStudents.adapter = adapterStudent
        binding.rvListStudents.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        adapterSearchStudent = context?.let {
            StudentSearchAdapter(object : IStudentClick {
                override fun handleEditStudent(student: Student) {
                    showUpdateDialog(student)
                }
            })
        }!!
        binding.rvSearchStudents.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvSearchStudents.adapter = adapterSearchStudent
        viewModel.searchStudents.observe(viewLifecycleOwner) { students ->
            adapterSearchStudent.updateData(students)
        }

        viewModel.students.observe(viewLifecycleOwner) { students ->
            if (students.isNotEmpty()) {
                binding.layoutNoDataSearch.visibility = View.GONE
                binding.layoutNoData.visibility = View.GONE
            } else {
                binding.layoutNoDataSearch.visibility = View.VISIBLE
                binding.layoutNoData.visibility = View.VISIBLE
            }
            adapterStudent.updateData(students)
        }


        binding.btnAddStudent.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_createStudentFragment)
        }

        binding.layoutSortText.setOnClickListener {
            showMenuSort(binding.layoutSortText, R.menu.menu)
        }



        binding.searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchStudents(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }


    private fun showUpdateDialog(student: Student) {
        val dialogUpdateStudent = Dialog(requireContext())
        dialogUpdateStudent.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogUpdateStudent.setContentView(R.layout.fragment_create_student)

        val window = dialogUpdateStudent.window ?: return
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawableResource(R.drawable.rounded_corner)

        val btnSubmit = dialogUpdateStudent.findViewById<MaterialButton>(R.id.btn_submit)
        val btnClose = dialogUpdateStudent.findViewById<MaterialButton>(R.id.btn_cancel)
        dialogUpdateStudent.findViewById<TextView>(R.id.title_dialog).text =
            getString(R.string.update_student)
        dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_name).setText(student.name)
        dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_age)
            .setText(student.age.toString())
        dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_class)
            .setText(student.className)
        dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_math)
            .setText(student.mathScore.toString())
        dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_english)
            .setText(student.englishScore.toString())
        dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_literature)
            .setText(student.literatureScore.toString())
        dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_physic)
            .setText(student.physicalScore.toString())
        dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_chemistry)
            .setText(student.chemistryScore.toString())

        btnClose.setOnClickListener {
            dialogUpdateStudent.dismiss()
        }

        btnSubmit.setOnClickListener {
            if (areAllFieldsFilled(
                    dialogUpdateStudent.findViewById(R.id.et_name),
                    dialogUpdateStudent.findViewById(R.id.et_age),
                    dialogUpdateStudent.findViewById(R.id.et_class),
                    dialogUpdateStudent.findViewById(R.id.et_math),
                    dialogUpdateStudent.findViewById(R.id.et_english),
                    dialogUpdateStudent.findViewById(R.id.et_literature),
                    dialogUpdateStudent.findViewById(R.id.et_physic),
                    dialogUpdateStudent.findViewById(R.id.et_chemistry)
                ) && areScoresValid(
                    requireContext(),
                    dialogUpdateStudent.findViewById(R.id.et_math),
                    dialogUpdateStudent.findViewById(R.id.et_english),
                    dialogUpdateStudent.findViewById(R.id.et_literature),
                    dialogUpdateStudent.findViewById(R.id.et_physic),
                    dialogUpdateStudent.findViewById(R.id.et_chemistry)
                )
            ) {
                val name =
                    dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_name).text.toString()
                val age =
                    dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_age).text.toString()
                        .toInt()
                val className =
                    dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_class).text.toString()
                val mathScore =
                    dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_math).text.toString()
                        .toFloat()
                val englishScore =
                    dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_english).text.toString()
                        .toFloat()
                val literatureScore =
                    dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_literature).text.toString()
                        .toFloat()
                val physicalScore =
                    dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_physic).text.toString()
                        .toFloat()
                val chemistryScore =
                    dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_chemistry).text.toString()
                        .toFloat()
                val newStudent = Student(
                    id = student.id,
                    name = name,
                    age = age,
                    className = className,
                    mathScore = mathScore,
                    englishScore = englishScore,
                    literatureScore = literatureScore,
                    physicalScore = physicalScore,
                    chemistryScore = chemistryScore
                )
                viewModel.updateStudent(newStudent)
                Toast.makeText(context, "Student updated", Toast.LENGTH_SHORT).show()
                dialogUpdateStudent.dismiss()
            } else {
                Toast.makeText(
                    context,
                    "Student name, age, ... cannot be empty or scores are invalid",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        dialogUpdateStudent.show()
    }


    private fun showMenuSort(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.option_sort_name -> {
                    viewModel.getStudentsSortedByName()
                    true
                }

                R.id.option_sort_average -> {
                    viewModel.getStudentsSortedByAverageGradeDescending()
                    true
                }

                R.id.option_sort_average_of_class -> {
                    viewModel.getStudentsSortedByAverageGradeOfClassDescending()
                    true
                }

                else -> false
            }
        }
        popup.setOnDismissListener {

        }

        popup.show()

    }

    override fun handleEditStudent(student: Student) {
        showUpdateDialog(student)
    }


}