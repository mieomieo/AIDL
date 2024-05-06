package com.example.server


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
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
