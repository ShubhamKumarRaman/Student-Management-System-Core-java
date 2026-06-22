package com.sms.service;

import com.sms.model.Student;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentService class handles all business logic and CRUD operations
 * for the Student Management System.
 * This class demonstrates:
 * - Encapsulation (private ArrayList)
 * - ArrayList operations
 * - CRUD implementation
 * - Business logic
 */
public class StudentService {

    // Private ArrayList to store students - Encapsulation
    private ArrayList<Student> studentList;
    private int idCounter;

    /**
     * Constructor - Initializes empty student list
     */
    public StudentService() {
        this.studentList = new ArrayList<>();
        this.idCounter = 1;
    }

    /**
     * Constructor that loads existing data
     * @param existingList List of students to initialize with
     */
    public StudentService(ArrayList<Student> existingList) {
        this.studentList = existingList != null ? existingList : new ArrayList<>();
        this.idCounter = studentList.size() + 1;
    }

    // ========== CRUD OPERATIONS ==========

    /**
     * Add a new student
     * @param student Student object to add
     * @return true if added successfully, false if duplicate ID
     */
    public boolean addStudent(Student student) {
        // Check if student with same ID already exists
        if (findStudentById(student.getId()) != null) {
            return false;
        }
        studentList.add(student);
        return true;
    }

    /**
     * Create and add a student with auto-generated ID
     * @param name Student name
     * @param age Student age
     * @param grade Student grade
     * @param email Student email
     * @return The created Student object with auto-generated ID
     */
    public Student addStudent(String name, int age, String grade, String email) {
        String studentId = generateId();
        Student student = new Student(studentId, name, age, grade, email);
        studentList.add(student);
        return student;
    }

    /**
     * Get all students
     * @return List of all students (immutable copy)
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentList); // Return copy to preserve encapsulation
    }

    /**
     * Get count of students
     * @return Total number of students
     */
    public int getStudentCount() {
        return studentList.size();
    }

    /**
     * Check if student list is empty
     * @return true if no students, false otherwise
     */
    public boolean isEmpty() {
        return studentList.isEmpty();
    }

    /**
     * Find student by ID
     * @param id Student ID to search for
     * @return Student object if found, null otherwise
     */
    public Student findStudentById(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }

        for (Student student : studentList) {
            if (student.getId().equalsIgnoreCase(id)) {
                return student;
            }
        }
        return null;
    }

    /**
     * Search students by name (partial match)
     * @param name Name or partial name to search
     * @return List of matching students
     */
    public List<Student> searchStudentsByName(String name) {
        List<Student> results = new ArrayList<>();
        if (name == null || name.isEmpty()) {
            return results;
        }

        String searchTerm = name.toLowerCase().trim();
        for (Student student : studentList) {
            if (student.getName().toLowerCase().contains(searchTerm)) {
                results.add(student);
            }
        }
        return results;
    }

    /**
     * Update student information
     * @param id Student ID to update
     * @param name New name (null to keep unchanged)
     * @param age New age (0 to keep unchanged)
     * @param grade New grade (null to keep unchanged)
     * @param email New email (null to keep unchanged)
     * @return true if updated successfully, false if student not found
     */
    public boolean updateStudent(String id, String name, Integer age, String grade, String email) {
        Student student = findStudentById(id);
        if (student == null) {
            return false;
        }

        if (name != null && !name.isEmpty()) {
            student.setName(name);
        }
        if (age != null && age > 0) {
            student.setAge(age);
        }
        if (grade != null && !grade.isEmpty()) {
            student.setGrade(grade);
        }
        if (email != null && !email.isEmpty()) {
            student.setEmail(email);
        }

        return true;
    }

    /**
     * Update specific field of a student
     * @param id Student ID
     * @param field Field to update (name, age, grade, email)
     * @param value New value
     * @return true if updated successfully, false otherwise
     */
    public boolean updateStudentField(String id, String field, String value) {
        Student student = findStudentById(id);
        if (student == null || value == null || value.isEmpty()) {
            return false;
        }

        switch (field.toLowerCase()) {
            case "name":
                student.setName(value);
                return true;
            case "age":
                try {
                    int age = Integer.parseInt(value);
                    if (age > 0) {
                        student.setAge(age);
                        return true;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
                return false;
            case "grade":
                student.setGrade(value);
                return true;
            case "email":
                student.setEmail(value);
                return true;
            default:
                return false;
        }
    }

    /**
     * Delete student by ID
     * @param id Student ID to delete
     * @return true if deleted successfully, false if not found
     */
    public boolean deleteStudent(String id) {
        Student student = findStudentById(id);
        if (student == null) {
            return false;
        }
        return studentList.remove(student);
    }

    /**
     * Delete all students (clear list)
     */
    public void deleteAllStudents() {
        studentList.clear();
        idCounter = 1;
    }

    // ========== UTILITY METHODS ==========

    /**
     * Generate auto-incrementing student ID
     * Format: STU001, STU002, etc.
     * @return New student ID
     */
    public String generateId() {
        return "STU" + String.format("%03d", idCounter++);
    }

    /**
     * Get next ID without incrementing
     * @return Next ID as string
     */
    public String previewNextId() {
        return "STU" + String.format("%03d", idCounter);
    }

    /**
     * Get current ID counter value
     * @return Current counter
     */
    public int getIdCounter() {
        return idCounter;
    }

    /**
     * Get students as a formatted table string
     * @return Formatted table of all students
     */
    public String getFormattedStudentList() {
        if (studentList.isEmpty()) {
            return "📭 No students found in the system!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("📊 Total Students: ").append(studentList.size()).append("\n");
        sb.append("-".repeat(80)).append("\n");
        sb.append(String.format("| %-10s | %-20s | %-5s | %-10s | %-25s |\n",
                "ID", "NAME", "AGE", "GRADE", "EMAIL"));
        sb.append("-".repeat(80)).append("\n");

        for (Student student : studentList) {
            sb.append(student.toFormattedString()).append("\n");
        }
        sb.append("-".repeat(80)).append("\n");
        sb.append("✅ Displayed ").append(studentList.size()).append(" student(s)");

        return sb.toString();
    }

    /**
     * Validate student data before adding/updating
     * @param name Student name
     * @param age Student age
     * @param grade Student grade
     * @param email Student email
     * @return Validation result with message
     */
    public ValidationResult validateStudentData(String name, int age, String grade, String email) {
        // Check name
        if (name == null || name.trim().isEmpty()) {
            return new ValidationResult(false, "❌ Name cannot be empty!");
        }
        if (name.length() < 2) {
            return new ValidationResult(false, "❌ Name must be at least 2 characters long!");
        }

        // Check age
        if (age < 5 || age > 100) {
            return new ValidationResult(false, "❌ Age must be between 5 and 100!");
        }

        // Check grade
        if (grade == null || grade.trim().isEmpty()) {
            return new ValidationResult(false, "❌ Grade cannot be empty!");
        }

        // Check email (basic validation)
        if (email == null || email.trim().isEmpty()) {
            return new ValidationResult(false, "❌ Email cannot be empty!");
        }
        if (!email.contains("@") || !email.contains(".")) {
            return new ValidationResult(false, "❌ Invalid email format! Email must contain @ and .");
        }

        return new ValidationResult(true, "✅ All data is valid!");
    }

    /**
     * Inner class for validation results
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}