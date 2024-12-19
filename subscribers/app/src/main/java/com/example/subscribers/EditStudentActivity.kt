package com.example.subscribers

import Student
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.subscribers.databinding.ActivityEditStudentBinding

class EditStudentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)
        binding = ActivityEditStudentBinding.inflate(layoutInflater)

        val student = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("student", Student::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("student") as? Student
        }

        binding.studentNameEditText.setText(student?.name)
        binding.studentIdEditText.setText(student?.id)

        binding.updateButton.setOnClickListener {
            val updatedName = binding.studentNameEditText.text.toString()
            val updatedId = binding.studentIdEditText.text.toString()
            val index = StudentRepository.getAllStudents().withIndex().find { it.value == student }?.index ?: 0

            if (updatedName.isNotBlank() && updatedId.isNotBlank()) {
                val updatedStudent = Student(updatedId, updatedName)
                StudentRepository.updateStudent(index, updatedStudent)
                finish()
            } else {
                // Show error message
            }
        }

        binding.deleteButton.setOnClickListener {
            val index = StudentRepository.getAllStudents().withIndex().find { it.value == student }?.index ?: 0

            student?.id?.let { id ->
                StudentRepository.deleteStudent(index)
            }
            finish()
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}
