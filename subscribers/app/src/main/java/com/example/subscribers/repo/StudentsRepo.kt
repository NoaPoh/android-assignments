object StudentRepository {
    private val students = mutableListOf<Student>()

    fun getAllStudents(): List<Student> = students

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
