package com.example.subscribers.repo

import Student

object StudentRepository {
    private val students = mutableListOf<Student>()

    fun getAllStudents(): List<Student> = students

    fun getStudentByIndex(index: Int): Student? {
        return try {
            students[index]
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    fun addStudent(student: Student) {
        students.add(student)
    }

    fun updateStudent(index: Int, student: Student) {
        students[index] = student
    }

    fun deleteStudent(index: Int) {
        students.removeAt(index)
    }
}
