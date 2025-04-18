package com.example.subscribers

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.subscribers.adapters.StudentAdapter
import com.example.subscribers.repo.StudentRepository

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
            onCheckChanged = { position, isChecked -> changeStudentChecked(position, isChecked) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addStudentButton.setOnClickListener {
            startActivity(Intent(this, AddStudentActivity::class.java))
        }
    }

    private fun openStudentDetails(position: Int) {
        val intent = Intent(this, StudentDetailsActivity::class.java)

        intent.putExtra("position", position)
        startActivity(intent)
    }

    private fun changeStudentChecked(position: Int, isChecked:Boolean) {
        val student = StudentRepository.getAllStudents()[position]
        student.isChecked = isChecked
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
