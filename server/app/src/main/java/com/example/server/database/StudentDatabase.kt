package com.example.server.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.server.dao.StudentDao
import com.example.server.Student

@Database(entities = [Student::class],version = 1)
abstract class StudentDatabase:RoomDatabase() {
    abstract fun studentDao(): StudentDao
}