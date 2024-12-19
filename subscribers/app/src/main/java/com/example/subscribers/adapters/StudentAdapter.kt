package com.example.subscribers.adapters

import Student
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.subscribers.R

class StudentAdapter(
    private val studentList: List<Student>,
    private val onItemClicked: (Int) -> Unit,
    private val onCheckChanged: (Int) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    // ViewHolder class to hold references to UI components
    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentImage: ImageView = itemView.findViewById(R.id.studentImageView)
        val studentName: TextView = itemView.findViewById(R.id.studentNameTextView)
        val studentId: TextView = itemView.findViewById(R.id.studentIdTextView)
        val studentCheckBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

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
        holder.studentImage.setImageResource(R.drawable.dog)

        // Set click listeners
        holder.itemView.setOnClickListener { onItemClicked(position) }
        holder.studentCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onCheckChanged(position)
        }
    }

    override fun getItemCount(): Int = studentList.size
}
