package com.example.server.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.server.Student

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStudent(student: Student)

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("SELECT * FROM students")
    fun getAllStudents(): List<Student>

    @Query("SELECT * FROM students ORDER BY name ASC ")
     fun getStudentsSortedByName(): List<Student>

    @Query("SELECT * FROM students ORDER BY ((mathScore+physicalScore+englishScore+literatureScore+chemistryScore)/5) DESC")
    fun getStudentsSortedByAverageGradeDescending(): List<Student>



    @Query("SELECT * FROM students ORDER BY className ASC, (mathScore + englishScore + literatureScore + physicalScore + chemistryScore) DESC")
    fun getStudentsSortedByAverageGradeOfClassDescending(): List<Student>


}