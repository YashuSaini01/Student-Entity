package com.spidey.Student_Entity;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentToolService {

    private final com.spidey.Student_Entity.StudentRepository studentRepository;

    public StudentToolService(com.spidey.Student_Entity.StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Tool(name = "get_all_students", description = "Retrieve a list of all students registered in the database")
    public List<com.spidey.Student_Entity.Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Tool(name = "get_student_by_id", description = "Get a specific student by their unique ID")
    public String getStudentById(Long id) {
        Optional<com.spidey.Student_Entity.Student> student = studentRepository.findById(id);
        return student.map(com.spidey.Student_Entity.Student::toString).orElse("Student not found with ID: " + id);
    }

    @Tool(name = "create_student", description = "Add a new student into the database with name, email, and course")
    public String createStudent(String name, String email, String course) {
        com.spidey.Student_Entity.Student student = new com.spidey.Student_Entity.Student(name, email, course);
        com.spidey.Student_Entity.Student saved = studentRepository.save(student);
        return "Successfully registered student with ID: " + saved.getId();
    }

    @Tool(name = "update_student", description = "Update an existing student's details by ID")
    public String updateStudent(Long id, String name, String email, String course) {
        Optional<com.spidey.Student_Entity.Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            com.spidey.Student_Entity.Student student = optionalStudent.get();
            if (name != null) student.setName(name);
            if (email != null) student.setEmail(email);
            if (course != null) student.setCourse(course);
            studentRepository.save(student);
            return "Successfully updated student ID: " + id;
        }
        return "Student not found for update with ID: " + id;
    }

    @Tool(name = "delete_student", description = "Delete a student record from the database by their ID")
    public String deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return "Successfully deleted student with ID: " + id;
        }
        return "Student not found with ID: " + id;
    }
}