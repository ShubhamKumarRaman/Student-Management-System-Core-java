package com.sms.service;

import com.sms.model.Student;
import com.sms.exception.StudentNotFoundException;
import com.sms.exception.DuplicateStudentException;
import com.sms.exception.InvalidInputException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentService class handles all business logic and CRUD operations
 * for the Student Management System.
 * This version includes file handling integration (Phase 5).
 */
public class StudentService {

    private ArrayList<Student> studentList;
    private int idCounter;
    private boolean autoSaveEnabled = true;

    public StudentService() {
        this.studentList = new ArrayList<>();
        this.idCounter = 1;

        // Try to load data on startup
        try {
            loadData();
        } catch (Exception e) {
            System.out.println("⚠️ Could not load data: " + e.getMessage());
        }
    }

    public StudentService(ArrayList<Student> existingList) {
        this.studentList = existingList != null ? existingList : new ArrayList<>();
        this.idCounter = studentList.size() + 1;
    }

    // ========== FILE HANDLING METHODS ==========

    /**
     * Save all students to file
     * @throws IOException if save fails
     */
    public void saveData() throws IOException {
        FileStorageService.saveStudents(studentList);
    }

    /**
     * Load students from file
     * @throws IOException if load fails
     * @throws InvalidInputException if data is invalid
     */
    public void loadData() throws IOException, InvalidInputException {
        List<Student> loadedStudents = FileStorageService.loadStudents();
        if (!loadedStudents.isEmpty()) {
            studentList = new ArrayList<>(loadedStudents);
            updateIdCounter();
            System.out.println("✅ Loaded " + studentList.size() + " students from file.");
        }
    }

    /**
     * Auto-save data if enabled
     */
    private void autoSave() {
        if (autoSaveEnabled && !studentList.isEmpty()) {
            try {
                FileStorageService.saveStudents(studentList);
            } catch (IOException e) {
                System.err.println("⚠️ Auto-save failed: " + e.getMessage());
            }
        }
    }

    /**
     * Update ID counter based on existing students
     */
    private void updateIdCounter() {
        int maxId = 0;
        for (Student student : studentList) {
            String id = student.getId();
            if (id.startsWith("STU")) {
                try {
                    int num = Integer.parseInt(id.substring(3));
                    if (num > maxId) {
                        maxId = num;
                    }
                } catch (NumberFormatException e) {
                    // Ignore non-standard IDs
                }
            }
        }
        idCounter = maxId + 1;
    }

    // ========== CRUD OPERATIONS WITH AUTO-SAVE ==========

    /**
     * Add a new student with exception handling
     * @param student Student object to add
     * @throws DuplicateStudentException if student with same ID exists
     */
    public void addStudent(Student student) throws DuplicateStudentException {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        if (findStudentByIdSafe(student.getId()) != null) {
            throw new DuplicateStudentException(student.getId());
        }

        studentList.add(student);
        autoSave();
    }

    /**
     * Create and add a student with auto-generated ID
     * @param name Student name
     * @param age Student age
     * @param grade Student grade
     * @param email Student email
     * @return The created Student object
     * @throws InvalidInputException if validation fails
     */
    public Student addStudent(String name, int age, String grade, String email)
            throws InvalidInputException {

        validateStudentData(name, age, grade, email);

        String studentId = generateId();
        Student student = new Student(studentId, name, age, grade, email);
        studentList.add(student);
        autoSave();
        return student;
    }

    /**
     * Get all students
     * @return List of all students
     * @throws StudentNotFoundException if no students exist
     */
    public List<Student> getAllStudents() throws StudentNotFoundException {
        if (studentList.isEmpty()) {
            throw new StudentNotFoundException("No students found in the system", "");
        }
        return new ArrayList<>(studentList);
    }

    /**
     * Get student count
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
     * Find student by ID with exception
     * @param id Student ID to search for
     * @return Student object
     * @throws StudentNotFoundException if student not found
     */
    public Student findStudentById(String id) throws StudentNotFoundException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }

        for (Student student : studentList) {
            if (student.getId().equalsIgnoreCase(id.trim())) {
                return student;
            }
        }
        throw new StudentNotFoundException(id);
    }

    /**
     * Find student by ID without exception (returns null if not found)
     * @param id Student ID to search for
     * @return Student object or null if not found
     */
    public Student findStudentByIdSafe(String id) {
        try {
            return findStudentById(id);
        } catch (StudentNotFoundException e) {
            return null;
        }
    }

    /**
     * Search students by name (partial match)
     * @param name Name or partial name to search
     * @return List of matching students
     * @throws StudentNotFoundException if no matches found
     */
    public List<Student> searchStudentsByName(String name) throws StudentNotFoundException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Search name cannot be null or empty");
        }

        List<Student> results = new ArrayList<>();
        String searchTerm = name.toLowerCase().trim();

        for (Student student : studentList) {
            if (student.getName().toLowerCase().contains(searchTerm)) {
                results.add(student);
            }
        }

        if (results.isEmpty()) {
            throw new StudentNotFoundException("No students found with name containing '" + name + "'", "");
        }
        return results;
    }

    /**
     * Update student information
     * @param id Student ID to update
     * @param name New name (null to keep unchanged)
     * @param age New age (null to keep unchanged)
     * @param grade New grade (null to keep unchanged)
     * @param email New email (null to keep unchanged)
     * @throws StudentNotFoundException if student not found
     * @throws InvalidInputException if validation fails
     */
    public void updateStudent(String id, String name, Integer age, String grade, String email)
            throws StudentNotFoundException, InvalidInputException {

        Student student = findStudentById(id);

        // Validate new data if provided
        String finalName = name != null ? name : student.getName();
        int finalAge = age != null ? age : student.getAge();
        String finalGrade = grade != null ? grade : student.getGrade();
        String finalEmail = email != null ? email : student.getEmail();

        validateStudentData(finalName, finalAge, finalGrade, finalEmail);

        // Update fields
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

        autoSave();
    }

    /**
     * Update specific field of a student
     * @param id Student ID
     * @param field Field to update (name, age, grade, email)
     * @param value New value
     * @throws StudentNotFoundException if student not found
     * @throws InvalidInputException if validation fails or field invalid
     */
    public void updateStudentField(String id, String field, String value)
            throws StudentNotFoundException, InvalidInputException {

        if (field == null || field.trim().isEmpty()) {
            throw new InvalidInputException("Field name cannot be empty");
        }

        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException("Value cannot be empty");
        }

        Student student = findStudentById(id);

        switch (field.toLowerCase().trim()) {
            case "name":
                validateName(value);
                student.setName(value);
                break;
            case "age":
                try {
                    int age = Integer.parseInt(value);
                    validateAge(age);
                    student.setAge(age);
                } catch (NumberFormatException e) {
                    throw new InvalidInputException("Age must be a valid number", e);
                }
                break;
            case "grade":
                validateGrade(value);
                student.setGrade(value);
                break;
            case "email":
                validateEmail(value);
                student.setEmail(value);
                break;
            default:
                throw new InvalidInputException("Invalid field name: " + field +
                        ". Allowed fields: name, age, grade, email");
        }

        autoSave();
    }

    /**
     * Delete student by ID
     * @param id Student ID to delete
     * @throws StudentNotFoundException if student not found
     */
    public void deleteStudent(String id) throws StudentNotFoundException {
        Student student = findStudentById(id);
        studentList.remove(student);
        autoSave();
    }

    /**
     * Delete all students
     */
    public void deleteAllStudents() {
        studentList.clear();
        idCounter = 1;
        autoSave();
    }

    // ========== VALIDATION METHODS (SAME AS PHASE 4) ==========

    public void validateStudentData(String name, int age, String grade, String email)
            throws InvalidInputException {
        validateName(name);
        validateAge(age);
        validateGrade(grade);
        validateEmail(email);
    }

    private void validateName(String name) throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Name", "Name cannot be empty");
        }
        if (name.trim().length() < 2) {
            throw new InvalidInputException("Name", "Name must be at least 2 characters long");
        }
        if (name.trim().length() > 50) {
            throw new InvalidInputException("Name", "Name cannot exceed 50 characters");
        }
        if (!name.trim().matches("^[a-zA-Z\\s.-]+$")) {
            throw new InvalidInputException("Name", "Name can only contain letters, spaces, dots, and hyphens");
        }
    }

    private void validateAge(int age) throws InvalidInputException {
        if (age < 5) {
            throw new InvalidInputException("Age", "Age must be at least 5 years old");
        }
        if (age > 100) {
            throw new InvalidInputException("Age", "Age cannot exceed 100 years");
        }
    }

    private void validateGrade(String grade) throws InvalidInputException {
        if (grade == null || grade.trim().isEmpty()) {
            throw new InvalidInputException("Grade", "Grade cannot be empty");
        }
        if (grade.trim().length() > 10) {
            throw new InvalidInputException("Grade", "Grade cannot exceed 10 characters");
        }
    }

    private void validateEmail(String email) throws InvalidInputException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidInputException("Email", "Email cannot be empty");
        }

        String emailTrimmed = email.trim();
        if (emailTrimmed.length() > 100) {
            throw new InvalidInputException("Email", "Email cannot exceed 100 characters");
        }

        if (!emailTrimmed.contains("@") || !emailTrimmed.contains(".")) {
            throw new InvalidInputException("Email", "Email must contain '@' and '.'");
        }

        if (emailTrimmed.startsWith("@") || emailTrimmed.endsWith("@")) {
            throw new InvalidInputException("Email", "Email cannot start or end with '@'");
        }

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!emailTrimmed.matches(emailRegex)) {
            throw new InvalidInputException("Email", "Invalid email format. Example: name@domain.com");
        }
    }

    // ========== UTILITY METHODS ==========

    public String generateId() {
        return "STU" + String.format("%03d", idCounter++);
    }

    public String previewNextId() {
        return "STU" + String.format("%03d", idCounter);
    }

    public int getIdCounter() {
        return idCounter;
    }

    public String getFormattedStudentList() throws StudentNotFoundException {
        if (studentList.isEmpty()) {
            throw new StudentNotFoundException("No students found in the system", "");
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

    // ========== FILE MANAGEMENT SETTINGS ==========

    /**
     * Enable or disable auto-save
     * @param enabled true to enable auto-save, false to disable
     */
    public void setAutoSaveEnabled(boolean enabled) {
        this.autoSaveEnabled = enabled;
        System.out.println("Auto-save " + (enabled ? "enabled" : "disabled"));
    }

    /**
     * Check if auto-save is enabled
     * @return true if auto-save is enabled
     */
    public boolean isAutoSaveEnabled() {
        return autoSaveEnabled;
    }
}