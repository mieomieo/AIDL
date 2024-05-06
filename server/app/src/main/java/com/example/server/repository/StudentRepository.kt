package com.example.server.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.server.Student
import com.example.server.dao.StudentDao
import java.text.Normalizer
import java.util.regex.Pattern
import javax.inject.Inject

class StudentRepository @Inject constructor(private val studentDao: StudentDao) {

    suspend fun addStudent(student: Student) = studentDao.addStudent(student)
    suspend fun updateStudent(student: Student) = studentDao.updateStudent(student)

    fun getAllStudents(): List<Student> {
        return studentDao.getAllStudents()
    }

    fun getStudentsSortedByName(): List<Student> {
        return studentDao.getStudentsSortedByName()
    }

    fun getStudentsSortedByAverageGradeDescending(): List<Student> {
        return studentDao.getStudentsSortedByAverageGradeDescending()
    }

    fun getStudentsSortedByAverageGradeOfClassDescending(): List<Student> {
        return studentDao.getStudentsSortedByAverageGradeOfClassDescending()
    }

    fun searchStudents(query: String): List<Student> {
        val allStudents = studentDao.getAllStudents()
        val normalizedQuery = normalizeString(query)
        return allStudents.filter {
            normalizeString(it.name).contains(
                normalizedQuery, ignoreCase = true
            )
        }
    }

    fun normalizeString(input: String?): String {
        val nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(nfdNormalizedString).replaceAll("")
    }

}