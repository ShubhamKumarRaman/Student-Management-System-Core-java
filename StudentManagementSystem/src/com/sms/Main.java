package com.sms;

import com.sms.model.Student;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class - Entry point for the Student Management System CLI application
 * This class handles the user interface and menu navigation
 */
public class Main {

    // Scanner for console input - declared as class-level for reuse
    private static Scanner scanner = new Scanner(System.in);

    // Temporary storage - will be replaced by StudentService in Phase 3
    private static ArrayList<Student> studentList = new ArrayList<>();

    // Counter for auto-generating student IDs
    private static int idCounter = 1;

    /**
     * Main method - Application entry point
     */
    public static void main(String[] args) {
        System.out.println("=" .repeat(50));
        System.out.println("   WELCOME TO STUDENT MANAGEMENT SYSTEM");
        System.out.println("=" .repeat(50));

        boolean running = true;

        // Main application loop
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

    /**
     * Display the main menu options
     */
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

        // Generate student ID (auto or manual)
        System.out.println("\nChoose ID option:");
        System.out.println("1. Auto-generate ID");
        System.out.println("2. Enter ID manually");
        int idOption = getIntInput("Your choice (1 or 2): ");

        String studentId;
        if (idOption == 1) {
            studentId = "STU" + String.format("%03d", idCounter++);
            System.out.println("✅ Auto-generated ID: " + studentId);
        } else {
            studentId = getStringInput("Enter Student ID (e.g., S001): ");
            // Check if ID already exists
            if (findStudentById(studentId) != null) {
                System.out.println("❌ Student with ID " + studentId + " already exists!");
                pressEnterToContinue();
                return;
            }
        }

        // Get student details
        String name = getStringInput("Enter Full Name: ");
        int age = getIntInput("Enter Age: ");
        String grade = getStringInput("Enter Grade (e.g., A, B+, 85%): ");
        String email = getStringInput("Enter Email: ");

        // Create and add student
        Student newStudent = new Student(studentId, name, age, grade, email);
        studentList.add(newStudent);

        System.out.println("\n✅ Student added successfully!");
        System.out.println("📝 Student Details: " + newStudent.toString());
        pressEnterToContinue();
    }

    /**
     * Feature 2: View all students
     */
    private static void viewAllStudents() {
        System.out.println("\n" + "=" .repeat(40));
        System.out.println("👁️ ALL STUDENTS");
        System.out.println("=" .repeat(40));

        if (studentList.isEmpty()) {
            System.out.println("\n📭 No students found in the system!");
            System.out.println("Please add students using option 1.");
        } else {
            System.out.println("\n📊 Total Students: " + studentList.size());
            System.out.println("-" .repeat(80));
            System.out.printf("| %-10s | %-20s | %-5s | %-10s | %-25s |\n",
                    "ID", "NAME", "AGE", "GRADE", "EMAIL");
            System.out.println("-" .repeat(80));

            for (Student student : studentList) {
                System.out.println(student.toFormattedString());
            }
            System.out.println("-" .repeat(80));
            System.out.println("✅ Displayed " + studentList.size() + " student(s)");
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
        Student foundStudent = findStudentById(studentId);

        if (foundStudent != null) {
            System.out.println("\n✅ Student Found!");
            System.out.println("-" .repeat(50));
            System.out.println(foundStudent.toString());
            System.out.println("-" .repeat(50));
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
        Student student = findStudentById(studentId);

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

        switch (updateChoice) {
            case 1:
                String newName = getStringInput("Enter New Name: ");
                student.setName(newName);
                System.out.println("✅ Name updated successfully!");
                break;
            case 2:
                int newAge = getIntInput("Enter New Age: ");
                student.setAge(newAge);
                System.out.println("✅ Age updated successfully!");
                break;
            case 3:
                String newGrade = getStringInput("Enter New Grade: ");
                student.setGrade(newGrade);
                System.out.println("✅ Grade updated successfully!");
                break;
            case 4:
                String newEmail = getStringInput("Enter New Email: ");
                student.setEmail(newEmail);
                System.out.println("✅ Email updated successfully!");
                break;
            case 5:
                String updatedName = getStringInput("Enter New Name: ");
                int updatedAge = getIntInput("Enter New Age: ");
                String updatedGrade = getStringInput("Enter New Grade: ");
                String updatedEmail = getStringInput("Enter New Email: ");

                student.setName(updatedName);
                student.setAge(updatedAge);
                student.setGrade(updatedGrade);
                student.setEmail(updatedEmail);
                System.out.println("✅ All fields updated successfully!");
                break;
            case 0:
                System.out.println("❌ Update cancelled.");
                break;
            default:
                System.out.println("❌ Invalid option! No changes made.");
        }

        if (updateChoice >= 1 && updateChoice <= 5 && updateChoice != 0) {
            System.out.println("\n🔄 Updated Information:");
            System.out.println(student.toString());
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
        Student student = findStudentById(studentId);

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
            studentList.remove(student);
            System.out.println("\n✅ Student with ID '" + studentId + "' has been deleted successfully!");
        } else {
            System.out.println("\n❌ Deletion cancelled. Student record is safe.");
        }

        pressEnterToContinue();
    }

    // ========== HELPER METHODS ==========

    /**
     * Helper method to find student by ID
     * @param id Student ID to search
     * @return Student object if found, null otherwise
     */
    private static Student findStudentById(String id) {
        for (Student student : studentList) {
            if (student.getId().equalsIgnoreCase(id)) {
                return student;
            }
        }
        return null;
    }

    /**
     * Helper method to get string input with prompt
     * @param prompt Message to display
     * @return User input string
     */
    private static String getStringInput(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine().trim();
    }

    /**
     * Helper method to get integer input with error handling
     * @param prompt Message to display
     * @return Integer input from user
     */
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

    /**
     * Helper method to pause execution until user presses Enter
     */
    private static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}