package interfaces

import com.example.aidlserverexercise.model.Student

interface IStudentClick {
    fun handleEditStudent(student: Student)
}