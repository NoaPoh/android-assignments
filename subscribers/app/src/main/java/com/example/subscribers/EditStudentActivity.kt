package com.example.subscribers

import Student
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.subscribers.databinding.ActivityEditStudentBinding
import com.example.subscribers.repo.StudentRepository

class EditStudentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val position = intent.getIntExtra("position", -1)

        val student = StudentRepository.getStudentByIndex(position)

        binding.studentNameEditText.setText(student?.name)
        binding.studentIdEditText.setText(student?.id)
        binding.studentCheckbox.isChecked = student?.isChecked == true

        val updateListener = View.OnClickListener { _ ->
            val updatedName = binding.studentNameEditText.text.toString()
            val updatedId = binding.studentIdEditText.text.toString()
            val updatedChecked = binding.studentCheckbox.isChecked

            if (updatedName.isNotBlank() && updatedId.isNotBlank()) {
                val updatedStudent = Student(updatedId, updatedName, updatedChecked)
                StudentRepository.updateStudent(position, updatedStudent)

                goBackToStudentsList()

            } else {
                // Show error message
            }
        }

        binding.updateButton.setOnClickListener(updateListener)

        binding.deleteButton.setOnClickListener {
            StudentRepository.deleteStudent(position)
            goBackToStudentsList()
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun goBackToStudentsList() {
        val intent = Intent(this, StudentListActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Clear all activities above StudentListActivity in stack
        startActivity(intent)
        finish()
    }
}
