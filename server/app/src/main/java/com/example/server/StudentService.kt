package com.example.server
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.server.model.Student

class StudentService: Service() {
    private var currentId: Int = 1
    private val listStudents = mutableListOf<Student>()
    private val listStudentsOriginal = mutableListOf<Student>()

    private val studentManagerBinder = object : IADLStudent.Stub() {
        override fun getAllStudents(): List<Student> {
            return listStudentsOriginal
        }

        override fun addStudent(student: Student?) {
            student?.let {
                it.id = currentId
                listStudents.add(it)
                listStudentsOriginal.add(it)
                currentId++
            }
        }

        override fun updateStudent(student: Student?) {
            student?.let {
                val index = listStudentsOriginal.indexOfFirst { existingStudent ->
                    existingStudent.id == student.id
                }
                if (index != -1) {
                    listStudentsOriginal[index] = student
                }
            }
        }

        override fun getStudentsSortedByName(): MutableList<Student> {
            return listStudents.sortedBy { it.name }.toMutableList()
        }

        override fun getStudentsSortedByAverageGradeDescending(): MutableList<Student> {
            return listStudents
                .sortedByDescending { it.calculateAverageGrade() }
                .toMutableList()
        }

        override fun sortStudentsByAverageGradeOfClassDescending(): MutableList<Student> {
            val sortedList = mutableListOf<Student>()
            val classMap = mutableMapOf<String, MutableList<Student>>()

            listStudentsOriginal.forEach { student ->
                if (classMap.containsKey(student.className)) {
                    classMap[student.className]?.add(student)
                } else {
                    classMap[student.className] = mutableListOf(student)
                }
            }

            // Sắp xếp học viên trong từng lớp theo điểm trung bình giảm dần và thêm vào danh sách sắp xếp
            classMap.values.forEach { studentsInClass ->
                val sortedStudentsInClass =
                    studentsInClass.sortedByDescending { it.calculateAverageGrade() }
                sortedList.addAll(sortedStudentsInClass)
            }

            // Sắp xếp lại danh sách lớp theo điểm trung bình giảm dần
            sortedList.sortByDescending { it.calculateAverageGrade() }
            Log.d("Heheeee", sortedList.toString())
            return sortedList
        }

        override fun searchStudents(keyword: String?): MutableList<Student> {
            return listStudents.filter { it.name.contains(keyword ?: "", ignoreCase = true) }
                .toMutableList()
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return studentManagerBinder;
    }

    fun Student.calculateAverageGrade(): Float {
        val totalGrades =
            listOf(mathScore, englishScore, literatureScore, physicalScore, chemistryScore).sum()
        return totalGrades / 5
    }

}