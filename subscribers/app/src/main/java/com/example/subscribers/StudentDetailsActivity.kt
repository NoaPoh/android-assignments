package com.example.subscribers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.subscribers.databinding.ActivityStudentDetailsBinding
import com.example.subscribers.repo.StudentRepository

class StudentDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStudentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val position = intent.getIntExtra("position", -1)

        val student = StudentRepository.getStudentByIndex(position)

        binding.studentNameTextView.text = student?.name
        binding.studentIdTextView.text = student?.id
        binding.studentCheckbox.isChecked = student?.isChecked == true

        // makes checkbox readonly
        binding.studentCheckbox.setOnTouchListener { _, _ -> true }

        binding.editButton.setOnClickListener {
            val intent = Intent(this, EditStudentActivity::class.java)
            intent.putExtra("position", position)
            startActivity(intent)
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}
