package com.example.subscribers

import Student
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.subscribers.databinding.ActivityAddStudentBinding


class AddStudentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddStudentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val listener = View.OnClickListener { v ->
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

        binding.saveButton.setOnClickListener(listener)
    }
}
