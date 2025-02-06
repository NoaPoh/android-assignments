package com.example.subscribers.adapters

import Student
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.subscribers.R


// ViewHolder class to hold references to UI components
class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val studentImage: AppCompatImageView = itemView.findViewById(R.id.studentImageView)
    val studentName: AppCompatTextView = itemView.findViewById(R.id.nameTextView)
    val studentId: AppCompatTextView = itemView.findViewById(R.id.idTextView)
    val studentCheckBox: AppCompatCheckBox = itemView.findViewById(R.id.checkBox)
}

class StudentAdapter(
    private val studentList: List<Student>,
    private val onItemClicked: (Int) -> Unit,
    private val onCheckChanged: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<StudentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_row, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.studentName.text = student.name
        holder.studentId.text = "ID: ${student.id}"
        holder.studentCheckBox.isChecked = student.isChecked
        holder.studentImage.setImageResource(R.drawable.student)

        // Set click listeners
        holder.itemView.setOnClickListener { onItemClicked(position) }
        holder.studentCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onCheckChanged(position, isChecked)
        }
    }

    override fun getItemCount(): Int = studentList.size
}
