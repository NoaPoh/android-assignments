package com.example.subscribers

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.subscribers.adapters.StudentAdapter

class StudentListActivity : AppCompatActivity() {

    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val addStudentButton: Button = findViewById(R.id.addStudentButton)

        adapter = StudentAdapter(
            StudentRepository.getAllStudents(),
            onItemClicked = { position -> openStudentDetails(position) },
            onCheckChanged = { position -> toggleStudentCheck(position) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addStudentButton.setOnClickListener {
            startActivity(Intent(this, AddStudentActivity::class.java))
        }
    }

    private fun openStudentDetails(position: Int) {
        val intent = Intent(this, EditStudentActivity::class.java)
        val student = StudentRepository.getAllStudents()[position]

        intent.putExtra("student", student)
        startActivity(intent)
    }

    private fun toggleStudentCheck(position: Int) {
        val student = StudentRepository.getAllStudents()[position]
        student.isChecked = !student.isChecked
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
