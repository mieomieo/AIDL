package com.example.aidlclientexercise.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aidlclientexercise.R
import com.example.aidlserverexercise.model.Student
import interfaces.IStudentClick

class StudentSearchAdapter(private val onStudentClick: IStudentClick? = null) :
    RecyclerView.Adapter<StudentSearchAdapter.HolderStudentSearch>() {
    private var listStudents = listOf<Student>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderStudentSearch {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.student_search_item, parent, false)
        return HolderStudentSearch(view)
    }

    override fun onBindViewHolder(holder: HolderStudentSearch, position: Int) {
        val student = listStudents[position]
        holder.txtSearchName.text = student.name
        holder.txtSearchClass.text = student.className
        holder.itemView.setOnClickListener {
            onStudentClick?.handleEditStudent(student)
        }
    }

    override fun getItemCount(): Int {
        return listStudents.size
    }

    inner class HolderStudentSearch(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtSearchName: TextView = itemView.findViewById(R.id.txtSearchName)
        val txtSearchClass: TextView = itemView.findViewById(R.id.txtSearchClass)
    }

    @Suppress("NotifyDataSetChanged")
    fun updateData(newList: List<Student>) {
        this.listStudents = newList
        notifyDataSetChanged()
    }
}