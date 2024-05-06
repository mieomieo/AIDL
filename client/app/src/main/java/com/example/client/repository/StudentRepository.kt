package com.example.client.repository

import com.example.client.service.ServiceManager
import com.example.server.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StudentRepository @Inject constructor(
    private val studentServiceConnection: ServiceManager
) {
    suspend fun insertStudent(student: Student) {
        return withContext(Dispatchers.IO) {
            studentServiceConnection.getStudentService()?.addStudent(student)
        }
    }

    suspend fun updateStudent(student: Student) {
        return withContext(Dispatchers.IO) {
            studentServiceConnection.getStudentService()?.updateStudent(student)
        }
    }

    suspend fun getAllStudents(): List<Student> {
        return withContext(Dispatchers.IO) {
            studentServiceConnection.getStudentService()?.getAllStudents() ?: emptyList()
        }
    }

    suspend fun getStudentsSortedByName(): List<Student> {
        return withContext(Dispatchers.IO) {
            studentServiceConnection.getStudentService()?.getStudentsSortedByName() ?: emptyList()
        }
    }

    suspend fun getStudentsSortedByAverageGradeDescending(): List<Student> {
        return withContext(Dispatchers.IO) {
            studentServiceConnection.getStudentService()
                ?.getStudentsSortedByAverageGradeDescending()
                ?: emptyList()
        }
    }

    suspend fun getStudentsSortedByAverageGradeOfClassDescending(): List<Student> {
        return withContext(Dispatchers.IO) {
            studentServiceConnection.getStudentService()
                ?.getStudentsSortedByAverageGradeOfClassDescending()
                ?: emptyList()
        }
    }

    suspend fun searchStudents(query: String): List<Student> {
        return withContext(Dispatchers.IO) {
            studentServiceConnection.getStudentService()?.searchStudents(query) ?: emptyList()
        }
    }
}