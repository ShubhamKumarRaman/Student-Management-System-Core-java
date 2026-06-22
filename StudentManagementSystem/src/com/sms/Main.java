package com.sms;

import com.sms.model.Student;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class - Entry point for the Student Management System CLI application
 * This class handles the user interface and menu navigation
 */
import com.sms.model.Student;
import com.sms.service.StudentService;
import java.util.List;

/**
 * Main class - Entry point for the Student Management System CLI application
 * This version uses StudentService for all business logic (Phase 3)
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static StudentService studentService = new StudentService();

    public static void main(String[] args) {
        System.out.println("=" .repeat(50));
        System.out.println("   WELCOME TO STUDENT MANAGEMENT SYSTEM");
        System.out.println("=" .repeat(50));

        boolean running = true;

        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewAllStudents();
                    break;
                case 3:
                    searchStudentById();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 6:
                    System.out.println("\n📁 Save/Load feature - Coming in Phase 5!");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    break;
                case 0:
                    running = false;
                    System.out.println("\n👋 Thank you for using Student Management System!");
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("\n❌ Invalid choice! Please enter 0-6");
                    pressEnterToContinue();
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n" + "-" .repeat(50));
        System.out.println("📚 STUDENT MANAGEMENT SYSTEM - MAIN MENU");
        System.out.println("-" .repeat(50));
        System.out.println("1. ➕ Add New Student");
        System.out.println("2. 👁️ View All Students");
        System.out.println("3. 🔍 Search Student by ID");
        System.out.println("4. ✏️ Update Student Information");
        System.out.println("5. ❌ Delete Student");
        System.out.println("6. 💾 Save/Load Data (Phase 5)");
        System.out.println("0. 🚪 Exit");
        System.out.println("-" .repeat(50));
    }

    /**
     * Feature 1: Add a new student
     */
    private static void addStudent() {
        System.out.println("\n" + "=" .repeat(40));
        System.out.println("➕ ADD NEW STUDENT");
        System.out.println("=" .repeat(40));

        System.out.println("\nChoose ID option:");
        System.out.println("1. Auto-generate ID");
        System.out.println("2. Enter ID manually");
        int idOption = getIntInput("Your choice (1 or 2): ");

        String studentId;
        String name, grade, email;
        int age;

        if (idOption == 1) {
            studentId = studentService.generateId();
            System.out.println("✅ Auto-generated ID: " + studentId);

            // Get student details
            name = getStringInput("Enter Full Name: ");
            age = getIntInput("Enter Age: ");
            grade = getStringInput("Enter Grade (e.g., A, B+, 85%): ");
            email = getStringInput("Enter Email: ");

            // Validate data
            StudentService.ValidationResult validation =
                    studentService.validateStudentData(name, age, grade, email);

            if (!validation.isValid()) {
                System.out.println("\n❌ " + validation.getMessage());
                pressEnterToContinue();
                return;
            }

            // Add student using service
            Student newStudent = studentService.addStudent(name, age, grade, email);
            System.out.println("\n✅ Student added successfully!");
            System.out.println("📝 Student Details: " + newStudent.toString());

        } else {
            studentId = getStringInput("Enter Student ID (e.g., S001): ");

            // Check if ID already exists
            if (studentService.findStudentById(studentId) != null) {
                System.out.println("\n❌ Student with ID " + studentId + " already exists!");
                pressEnterToContinue();
                return;
            }

            // Get student details
            name = getStringInput("Enter Full Name: ");
            age = getIntInput("Enter Age: ");
            grade = getStringInput("Enter Grade (e.g., A, B+, 85%): ");
            email = getStringInput("Enter Email: ");

            // Validate data
            StudentService.ValidationResult validation =
                    studentService.validateStudentData(name, age, grade, email);

            if (!validation.isValid()) {
                System.out.println("\n❌ " + validation.getMessage());
                pressEnterToContinue();
                return;
            }

            // Create and add student
            Student student = new Student(studentId, name, age, grade, email);
            if (studentService.addStudent(student)) {
                System.out.println("\n✅ Student added successfully!");
                System.out.println("📝 Student Details: " + student.toString());
            } else {
                System.out.println("\n❌ Failed to add student!");
            }
        }

        pressEnterToContinue();
    }

    /**
     * Feature 2: View all students
     */
    private static void viewAllStudents() {
        System.out.println("\n" + "=" .repeat(40));
        System.out.println("👁️ ALL STUDENTS");
        System.out.println("=" .repeat(40));

        if (studentService.isEmpty()) {
            System.out.println("\n📭 No students found in the system!");
            System.out.println("Please add students using option 1.");
        } else {
            System.out.println("\n" + studentService.getFormattedStudentList());
        }

        pressEnterToContinue();
    }

    /**
     * Feature 3: Search student by ID
     */
    private static void searchStudentById() {
        System.out.println("\n" + "=" .repeat(40));
        System.out.println("🔍 SEARCH STUDENT BY ID");
        System.out.println("=" .repeat(40));

        String studentId = getStringInput("\nEnter Student ID to search: ");
        Student foundStudent = studentService.findStudentById(studentId);

        if (foundStudent != null) {
            System.out.println("\n✅ Student Found!");
            System.out.println("-" .repeat(50));
            System.out.println(foundStudent.toString());
            System.out.println("-" .repeat(50));

            // Option to search by name as well
            System.out.println("\n🔎 Want to search by name instead?");
            System.out.print("Enter name (or press Enter to skip): ");
            String nameSearch = scanner.nextLine().trim();
            if (!nameSearch.isEmpty()) {
                List<Student> nameResults = studentService.searchStudentsByName(nameSearch);
                if (!nameResults.isEmpty()) {
                    System.out.println("\n📋 Students matching '" + nameSearch + "':");
                    for (Student s : nameResults) {
                        System.out.println("  • " + s.getId() + " - " + s.getName());
                    }
                } else {
                    System.out.println("❌ No students found with name containing '" + nameSearch + "'");
                }
            }
        } else {
            System.out.println("\n❌ Student with ID '" + studentId + "' not found!");
            System.out.println("💡 Tip: Check the ID and try again.");
        }

        pressEnterToContinue();
    }

    /**
     * Feature 4: Update student information
     */
    private static void updateStudent() {
        System.out.println("\n" + "=" .repeat(40));
        System.out.println("✏️ UPDATE STUDENT INFORMATION");
        System.out.println("=" .repeat(40));

        String studentId = getStringInput("\nEnter Student ID to update: ");
        Student student = studentService.findStudentById(studentId);

        if (student == null) {
            System.out.println("\n❌ Student with ID '" + studentId + "' not found!");
            pressEnterToContinue();
            return;
        }

        // Display current information
        System.out.println("\n📌 Current Information:");
        System.out.println("-" .repeat(40));
        System.out.println(student.toString());
        System.out.println("-" .repeat(40));

        System.out.println("\n📝 Update Options:");
        System.out.println("1. Update Name");
        System.out.println("2. Update Age");
        System.out.println("3. Update Grade");
        System.out.println("4. Update Email");
        System.out.println("5. Update All Fields");
        System.out.println("0. Cancel");

        int updateChoice = getIntInput("\nSelect field to update (0-5): ");
        boolean updated = false;

        switch (updateChoice) {
            case 1:
                String newName = getStringInput("Enter New Name: ");
                updated = studentService.updateStudentField(studentId, "name", newName);
                if (updated) System.out.println("✅ Name updated successfully!");
                break;
            case 2:
                String newAge = getStringInput("Enter New Age: ");
                updated = studentService.updateStudentField(studentId, "age", newAge);
                if (updated) System.out.println("✅ Age updated successfully!");
                break;
            case 3:
                String newGrade = getStringInput("Enter New Grade: ");
                updated = studentService.updateStudentField(studentId, "grade", newGrade);
                if (updated) System.out.println("✅ Grade updated successfully!");
                break;
            case 4:
                String newEmail = getStringInput("Enter New Email: ");
                updated = studentService.updateStudentField(studentId, "email", newEmail);
                if (updated) System.out.println("✅ Email updated successfully!");
                break;
            case 5:
                String updatedName = getStringInput("Enter New Name: ");
                String updatedAge = getStringInput("Enter New Age: ");
                String updatedGrade = getStringInput("Enter New Grade: ");
                String updatedEmail = getStringInput("Enter New Email: ");

                // Update all fields
                boolean nameUpdated = studentService.updateStudentField(studentId, "name", updatedName);
                boolean ageUpdated = studentService.updateStudentField(studentId, "age", updatedAge);
                boolean gradeUpdated = studentService.updateStudentField(studentId, "grade", updatedGrade);
                boolean emailUpdated = studentService.updateStudentField(studentId, "email", updatedEmail);

                if (nameUpdated || ageUpdated || gradeUpdated || emailUpdated) {
                    System.out.println("✅ All fields updated successfully!");
                    updated = true;
                }
                break;
            case 0:
                System.out.println("❌ Update cancelled.");
                break;
            default:
                System.out.println("❌ Invalid option! No changes made.");
        }

        if (updated) {
            Student updatedStudent = studentService.findStudentById(studentId);
            System.out.println("\n🔄 Updated Information:");
            System.out.println(updatedStudent.toString());
        }

        pressEnterToContinue();
    }

    /**
     * Feature 5: Delete student
     */
    private static void deleteStudent() {
        System.out.println("\n" + "=" .repeat(40));
        System.out.println("❌ DELETE STUDENT");
        System.out.println("=" .repeat(40));

        String studentId = getStringInput("\nEnter Student ID to delete: ");
        Student student = studentService.findStudentById(studentId);

        if (student == null) {
            System.out.println("\n❌ Student with ID '" + studentId + "' not found!");
            pressEnterToContinue();
            return;
        }

        // Show confirmation
        System.out.println("\n⚠️  WARNING: You are about to delete this student:");
        System.out.println("-" .repeat(40));
        System.out.println(student.toString());
        System.out.println("-" .repeat(40));

        System.out.print("\nAre you sure you want to delete? (Y/N): ");
        String confirmation = scanner.nextLine().trim().toUpperCase();

        if (confirmation.equals("Y") || confirmation.equals("YES")) {
            if (studentService.deleteStudent(studentId)) {
                System.out.println("\n✅ Student with ID '" + studentId + "' has been deleted successfully!");
            } else {
                System.out.println("\n❌ Failed to delete student!");
            }
        } else {
            System.out.println("\n❌ Deletion cancelled. Student record is safe.");
        }

        pressEnterToContinue();
    }

    // ========== HELPER METHODS ==========

    private static String getStringInput(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int input = Integer.parseInt(scanner.nextLine().trim());
                return input;
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a valid number.");
            }
        }
    }

    private static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}