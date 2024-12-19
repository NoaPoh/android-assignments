package com.example.subscribers

import Student
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.subscribers.databinding.ActivityAddStudentBinding


class AddStudentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddStudentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)

        binding.saveButton.setOnClickListener {
            val name = binding.studentNameEditText.text.toString()
            val id = binding.studentIdEditText.text.toString()

            if (name.isNotBlank() && id.isNotBlank()) {
                val newStudent = Student(id, name)
                StudentRepository.addStudent(newStudent)
                finish()
            } else {
                // Show error message
            }
        }
    }
}
