package com.example.client.fragments

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
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.widget.ButtonBarLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aidlclientexercise.adapter.StudentAdapter
import com.example.aidlclientexercise.adapter.StudentSearchAdapter
import com.example.client.R
import com.example.client.common.areAllFieldsFilled
import com.example.client.common.areScoresValid
import com.example.client.databinding.FragmentMainBinding
import com.example.client.service.ServiceManager
import com.example.server.IADLStudent
import com.example.server.model.Student
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import interfaces.IStudentClick

class MainFragment : Fragment(), IStudentClick {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapterStudent: StudentAdapter
    private var studentManagerService: IADLStudent? = null
    private val listStudentsTemp = mutableListOf<Student>()
    private lateinit var adapterSearchStudent: StudentSearchAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        studentManagerService = ServiceManager.getStudentManager()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterStudent = StudentAdapter(this)
        binding.rvListStudents.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvListStudents.adapter = adapterStudent
        if (studentManagerService?.allStudents?.isNotEmpty() == true) {
            studentManagerService?.allStudents?.let {
                adapterStudent.updateData(it)
            }
            binding.layoutNoDataSearch.visibility = View.GONE
            binding.layoutNoData.visibility = View.GONE
        } else {
            binding.layoutNoDataSearch.visibility = View.VISIBLE
            binding.layoutNoData.visibility = View.VISIBLE
        }

        binding.btnAddStudent.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_createStudentFragment)
        }

        binding.layoutSortText.setOnClickListener {
            showMenuSort(binding.layoutSortText, R.menu.menu)
        }

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

        binding.searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
                // Trước khi văn bản thay đổi
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isEmpty() == true) {
                    listStudentsTemp.clear()
                    studentManagerService?.let { listStudentsTemp.addAll(it.allStudents) }
                    adapterSearchStudent.updateData(listStudentsTemp)
                } else {
                    studentManagerService?.allStudents?.let {
                        filterStudents(
                            s.toString()
                        )
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Sau khi văn bản thay đổi
            }
        })
    }


    private fun refreshStudentList() {
        studentManagerService?.let {
            adapterStudent.updateData(it.allStudents)
            listStudentsTemp.addAll(it.allStudents)
        }
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
        dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_age).setText(student.age.toString())
        dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_class).setText(student.className)
        dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_math).setText(student.mathScore.toString())
        dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_english).setText(student.englishScore.toString())
        dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_literature).setText(student.literatureScore.toString())
        dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_physic).setText(student.physicalScore.toString())
        dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_chemistry).setText(student.chemistryScore.toString())

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
                val newStudent = Student().apply {
                    id = student.id
                    name = dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_name).text.toString()
                    age = dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_age).text.toString().toInt()
                    className = dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_class).text.toString()
                    mathScore = dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_math).text.toString().toFloat()
                    englishScore = dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_english).text.toString().toFloat()
                    literatureScore = dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_literature).text.toString().toFloat()
                    physicalScore = dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_physic).text.toString().toFloat()
                    chemistryScore = dialogUpdateStudent.findViewById<TextInputEditText>(R.id.et_chemistry).text.toString().toFloat()
                }

                studentManagerService?.updateStudent(newStudent)
                Toast.makeText(context, "Student updated", Toast.LENGTH_SHORT).show()
                refreshStudentList()
                dialogUpdateStudent.dismiss()
            } else {
                Toast.makeText(
                    context, "Student name, age, ... cannot be empty or scores are invalid", Toast.LENGTH_SHORT
                ).show()
            }
        }
        dialogUpdateStudent.show()
    }

    private fun filterStudents(query: String?) {
        val searchedStudents = studentManagerService?.searchStudents(query)
        if (searchedStudents != null) {
            if (searchedStudents.isEmpty()) {
                binding.rvSearchStudents.visibility = View.GONE
                binding.layoutNoDataSearch.visibility = View.VISIBLE
            } else {
                binding.rvSearchStudents.visibility = View.VISIBLE
                binding.layoutNoDataSearch.visibility = View.GONE
                listStudentsTemp.clear()
                listStudentsTemp.addAll(searchedStudents)
                adapterSearchStudent.updateData(listStudentsTemp)
            }
        }
    }

    private fun showMenuSort(v: View, @MenuRes menuRes: Int) {


        fun getStudentsSortedByName() {
            val sortedStudents = studentManagerService?.studentsSortedByName
            if (sortedStudents != null) {
                adapterStudent.updateData(sortedStudents)
            }
        }

        fun getStudentsSortedByAverageGradeDescending() {
            val sortedStudents = studentManagerService?.studentsSortedByAverageGradeDescending
            if (sortedStudents != null) {
                adapterStudent.updateData(sortedStudents)
            }
        }

        fun getStudentsSortedByAverageGradeOfClassDescending() {
            val sortedStudents =
                studentManagerService?.sortStudentsByAverageGradeOfClassDescending()
            if (sortedStudents != null) {
                adapterStudent.updateData(sortedStudents)
            }

        }

        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.option_sort_name -> {
                    getStudentsSortedByName()
                    true
                }

                R.id.option_sort_average -> {
                    getStudentsSortedByAverageGradeDescending()
                    true
                }

                R.id.option_sort_average_of_class -> {
                    getStudentsSortedByAverageGradeOfClassDescending()
                    true
                }

                R.id.option_sort_default -> {
                    studentManagerService?.let { adapterStudent.updateData(it.allStudents) }
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