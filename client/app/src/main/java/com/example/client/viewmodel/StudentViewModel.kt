package com.example.client.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.repository.StudentRepository
import com.example.client.service.ServiceManager
import com.example.server.Student
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    studentServiceConnection: ServiceManager
) : ViewModel() {
    private var _students = MutableLiveData<List<Student>>()
    val students get() = _students

    private var _searchStudents = MutableLiveData<List<Student>>()
    val searchStudents get() = _searchStudents


    private var _checkService = MutableLiveData<Boolean>()
    val checkService get() = _checkService

    init {
        studentServiceConnection.serviceConnected.observeForever {
            if (!it) {
                checkService.value = false
                Log.e("checklogg", checkService.value.toString())
            } else {
                getAllStudents()
            }
        }
    }


    fun addStudent(student: Student) {
        viewModelScope.launch {
            studentRepository.insertStudent(student)
            _students.value = studentRepository.getAllStudents()
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            studentRepository.updateStudent(student)
            _students.value = studentRepository.getAllStudents()
        }
    }

    fun searchStudents(query: String) {
        viewModelScope.launch {
            _searchStudents.value = studentRepository.searchStudents(query)
        }
    }

    fun getStudentsSortedByName() {
        viewModelScope.launch {
            _students.value = studentRepository.getStudentsSortedByName()
        }
    }

    fun getAllStudents() {
        viewModelScope.launch {
            _students.value = studentRepository.getAllStudents()
        }
    }

    fun getStudentsSortedByAverageGradeDescending() {
        viewModelScope.launch {
            _students.value = studentRepository.getStudentsSortedByAverageGradeDescending()
        }
    }

    fun getStudentsSortedByAverageGradeOfClassDescending() {
        viewModelScope.launch {
            _students.value = studentRepository.getStudentsSortedByAverageGradeOfClassDescending()
        }
    }

}