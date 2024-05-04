package com.example.aidlclientexercise.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aidlclientexercise.R
import com.example.aidlserverexercise.model.Student
import interfaces.IStudentClick

class StudentAdapter(private val onStudentClick: IStudentClick? = null) :
    RecyclerView.Adapter<StudentAdapter.HolderStudent>() {
    private var listStudents = listOf<Student>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderStudent {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.student_item, parent, false)
        return HolderStudent(view)
    }

    override fun onBindViewHolder(holder: HolderStudent, position: Int) {
        val student = listStudents[position]
        holder.txtStudentName.text = student.name
        holder.txtStudentAge.text = student.age.toString()
        holder.txtStudentClass.text = student.className
        holder.txtMathGrade.text =
            holder.itemView.resources.getString(R.string.math_grade, student.mathGrade.toString())
        holder.txtEnglishGrade.text = holder.itemView.resources.getString(
            R.string.english_grade_detail,
            student.englishGrade.toString()
        )
        holder.txtLiteratureGrade.text = holder.itemView.resources.getString(
            R.string.literature_grade,
            student.literatureGrade.toString()
        )
        holder.txtPhysicGrade.text = holder.itemView.resources.getString(
            R.string.physic_grade,
            student.physicalGrade.toString()
        )
        holder.txtChemistryGrade.text = holder.itemView.resources.getString(
            R.string.average,
            student.chemistryGrade.toString()
        )
        holder.txtAverage.text =  holder.itemView.resources.getString(
            R.string.average,
            calculateAverageGrade(student).toString()
        )
        holder.btnEdit.setOnClickListener {
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
        val btnEdit: ImageView = itemView.findViewById(R.id.btnEdit)
    }

    @Suppress("NotifyDataSetChanged")
    fun updateData(newList: List<Student>) {
        this.listStudents = newList
        notifyDataSetChanged()
    }

    private fun calculateAverageGrade(student: Student): Float {
        return (student.mathGrade + student.englishGrade + student.literatureGrade +
                student.physicalGrade + student.chemistryGrade) / 5
    }


}