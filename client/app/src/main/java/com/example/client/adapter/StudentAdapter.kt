package com.example.aidlclientexercise.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.server.Student
import interfaces.IStudentClick
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class StudentAdapter(private val onStudentClick: IStudentClick? = null) :
    RecyclerView.Adapter<StudentAdapter.HolderStudent>() {
    private var listStudents = listOf<Student>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderStudent {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.student_item, parent, false)
        return HolderStudent(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HolderStudent, position: Int) {
        val student = listStudents[position]
        holder.txtStudentName.text = "Name:${student.name}"
        holder.txtStudentAge.text = "Age:${student.age}"
        holder.txtStudentClass.text = "Class:${student.className}"
        holder.txtMathGrade.text =
            holder.itemView.resources.getString(R.string.math_grade, student.mathScore.toString())
        holder.txtEnglishGrade.text = holder.itemView.resources.getString(
            R.string.english_grade_detail,
            student.englishScore.toString()
        )
        holder.txtLiteratureGrade.text = holder.itemView.resources.getString(
            R.string.literature_grade,
            student.literatureScore.toString()
        )
        holder.txtPhysicGrade.text = holder.itemView.resources.getString(
            R.string.physic_grade,
            student.physicalScore.toString()
        )
        holder.txtChemistryGrade.text = holder.itemView.resources.getString(
            R.string.chemistry_grade,
            student.chemistryScore.toString()
        )
        holder.txtAverage.text = holder.itemView.resources.getString(
            R.string.average,
            calculateAverageGrade(student)
        )
        holder.itemView.setOnClickListener {
            onStudentClick?.handleEditStudent(student)
        }
    }

    override fun getItemCount(): Int {
        return listStudents.size
    }

    inner class HolderStudent(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtStudentName: TextView = itemView.findViewById(R.id.txtStudentName)
        val txtStudentAge: TextView = itemView.findViewById(R.id.txtStudentAge)
        val txtStudentClass: TextView = itemView.findViewById(R.id.txtStudentClass)
        val txtMathGrade: TextView = itemView.findViewById(R.id.txtMathGrade)
        val txtEnglishGrade: TextView = itemView.findViewById(R.id.txtEnglishGrade)
        val txtLiteratureGrade: TextView = itemView.findViewById(R.id.txtLiteratureGrade)
        val txtPhysicGrade: TextView = itemView.findViewById(R.id.txtPhysicGrade)
        val txtChemistryGrade: TextView = itemView.findViewById(R.id.txtChemistryGrade)
        val txtAverage: TextView = itemView.findViewById(R.id.txtAverage)
    }

    @Suppress("NotifyDataSetChanged")
    fun updateData(newList: List<Student>) {
        this.listStudents = newList
        notifyDataSetChanged()
    }

    private fun calculateAverageGrade(student: Student): String {
        val sum = student.mathScore + student.englishScore + student.literatureScore +
                student.physicalScore + student.chemistryScore
        val average = sum / 5f
        val decimalFormat = DecimalFormat("#.##", DecimalFormatSymbols(Locale.US))
        return decimalFormat.format(average)
    }

}