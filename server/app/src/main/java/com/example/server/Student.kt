package com.example.server

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val age: Int,
    val className: String,
    val mathScore: Float,
    val englishScore: Float,
    val literatureScore: Float,
    val physicalScore: Float,
    val chemistryScore: Float
): Parcelable