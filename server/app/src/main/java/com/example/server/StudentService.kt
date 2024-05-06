package com.example.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.room.Room
import com.example.server.dao.StudentDao
import com.example.server.database.StudentDatabase
import com.example.server.Student
import com.example.server.repository.StudentRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StudentService : Service() {

    @Inject
    lateinit var studentRepository: StudentRepository

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    override fun onBind(intent: Intent): IBinder {
        Log.e("bind service", "runnnnnnnnnnnnnnnnnn")
        return studentManagerBinder;
    }

    private val studentManagerBinder = object : IADLStudent.Stub() {


        override fun addStudent(student: Student) {
            coroutineScope.launch {
                student.let {
                    studentRepository.addStudent(it)
                }
            }
        }

        override fun updateStudent(student: Student?) {
            coroutineScope.launch {
                student?.let {
                    studentRepository.updateStudent(it)
                }
            }
        }

        override fun getAllStudents(): List<Student> {
            return studentRepository.getAllStudents()
        }

        override fun getStudentsSortedByName(): List<Student> {
            return studentRepository.getStudentsSortedByName()
        }

        override fun getStudentsSortedByAverageGradeDescending(): List<Student> {
            return studentRepository.getStudentsSortedByAverageGradeDescending()
        }

        override fun getStudentsSortedByAverageGradeOfClassDescending(): List<Student> {
            return studentRepository.getStudentsSortedByAverageGradeOfClassDescending()
        }

        override fun searchStudents(query: String?): List<Student> {
            return studentRepository.searchStudents(query!!)
        }


    }
}


