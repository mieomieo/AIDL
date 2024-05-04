package interfaces

import com.example.server.model.Student

interface IStudentClick {
    fun handleEditStudent(student: Student)
}