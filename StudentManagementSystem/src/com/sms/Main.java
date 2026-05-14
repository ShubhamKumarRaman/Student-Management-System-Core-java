package com.sms;

import com.sms.model.Student;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Student> studentList = new ArrayList<>();

    private static int idCounter = 1;

    public static void main(String args[]) {
        System.out.println("=".repeat(50));
        System.out.println("=  Welcome to Student Management System  =");
        System.out.println("=".repeat(50));

        boolean running = true;

        //Main application loop
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
                    System.out.println("Press Enter to continue");
                    Scanner.nextLine();
                    break;
                case 0:
                    running = false;
                    System.out.println("Thank you for using Student Management System!");
                    System.out.println("GoodBye!");
                    break;
                default:
                    System.out.println("Invalid choice! Please enter 0-6");
                    pressEnterToContinue();
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("📚 STUDENT MANAGEMENT SYSTEM - MAIN MENU");
        System.out.println("-".repeat(50));
        System.out.println("1. ➕ Add New Student");
        System.out.println("2. 👁️ View All Students");
        System.out.println("3. 🔍 Search Student by ID");
        System.out.println("4. ✏️ Update Student Information");
        System.out.println("5. ❌ Delete Student");
        System.out.println("6. 💾 Save/Load Data (Phase 5)");
        System.out.println("0. 🚪 Exit");
        System.out.println("-".repeat(50));
    }

    private static void addStudent() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("Add new Student");
        System.out.println("=".repeat(40));

        //Generate Student Id
        System.out.println("\nChoose Id Option:");
        System.out.println("1. Auto-generated ID");
        System.out.println("2. Enter ID manually");
        int idOption = getIntInput("Your Choice (1 or 2):");

        String studentId;
        if (idOption == 1) {
            studentId = "STU" + String.format("%03d", idCounter++);
            System.out.println("Auto-generated ID: " + studentId);
        } else {
            studentId = getStringInput("Enter Student ID (e.g., S001): ");
            //Check if ID already exists
            if (findStudentById(studentId) != null) {
                System.out.println("Student with ID " + studentId + " already exists!");
                pressEnterToContinue();
                return;
            }
        }

        //Get student details
        String name = getStringInput("Enter Full Name: ");
        int age = getIntInput("Enter Age: ");
        String grade = getStringInput("Enter Grade (e.g., A, B+, 85%): ");
        String email = getStringInput("Enter Email: ");

        //Create and add Student
        Student newStudent = new Student(studentId, name, age, grade, email);
        studentList.add(newStudent);

        System.out.println("Student added successfully");
        System.out.println("Student Details: " + newStudent.toString());
        pressEnterToContinue();
    }

    //View All students
    private static void viewAllStudents() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("All Students");
        System.out.println("=".repeat(40));

        if (studentList.isEmpty()) {
            System.out.println("\nNo students found in the system!");
            System.out.println("Please add Students using Option 1.");
        } else {
            System.out.println("\nTotal Students: " + studentList.size());
            System.out.println("-".repeat(80));
            System.out.printf("| %-10s | %-20s | %-5s | %-10s | %-25s |\n", "ID", "NAME", "AGE", "GRADE", "EMAIL");
            System.out.println("-".repeat(80));

            for (Student student : studentList) {
                System.out.println(student.toFormattedString());
            }
            System.out.println("-".repeat(80));
            System.out.println("Displayed " + studentList.size() + " student(s)");
        }
        pressEnterToContinue();
    }

    //    Search Student by ID
    private static void searchStudentById() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("Search student by ID");
        System.out.println("=".repeat(40));

        String studentId = getStringInput("\nEnter student Id to search: ");
        Student foundStudent = findStudentById(studentId);

        if (foundStudent != null) {
            System.out.println("\nStudent Found!");
            System.out.println("-".repeat(50));
            System.out.println(foundStudent.toString());
            System.out.println("-".repeat(50));
        } else {
            System.out.println("\nStudent with ID: '" + studentId + "' not found!");
            System.out.println("Tip: Check the ID and try again.");
        }
        pressEnterToContinue();
    }

    //    Update Student Information
    private static void updateStudent() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("Update Student Information");
        System.out.println("=".repeat(40));

        String studentId = getStringInput("\nEnter Student Id to update: ");
        Student student = findStudentById(studentId);

        if (student == null) {
            System.out.println("\nStudent with ID: '" + studentId + "' not found!");
            pressEnterToContinue();
            return;
        }

        // Display current information
        System.out.println("\nCurrent information: ");
        System.out.println("-".repeat(40));
        System.out.println(student.toString());
        System.out.println("-".repeat(40));

        System.out.println("\nUpdate Options: ");
        System.out.println("1. Update Name");
        System.out.println("2. Update Age");
        System.out.println("3. Update Grade");
        System.out.println("4. Update Email");
        System.out.println("5. Update All Fields");
        System.out.println("0. Cancel");

        int updateChoice = getIntInput("\nSelect field to update(0-5): ");

        switch (updateChoice) {
            case 1:
                String newName = getStringInput("Enter New Name: ");
                student.setName(newName);
                System.out.println("Name updated successfully!");
                break;
            case 2:
                int newAge = getIntInput("Enter New Age: ");
                student.setAge(newAge);
                System.out.println("Age updated successfully!");
                break;
            case 3:
                String newGrade = getStringInput("Enter New Grade: ");
                student.setGrade(newGrade);
                System.out.println("Grade Updated successfully!");
                break;
            case 4:
                String newEmail = getStringInput("Enter New Email: ");
                student.setEmail(newEmail);
                System.out.println("Email updated successfully!");
                break;
            case 5:
                String updatedName = getStringInput("Enter New Name: ");
                int updatedAge = getIntInput("Enter New Age: ");
                String updatedGrade = getStringInput("Enter New Grade: ");
                String updateEmail = getStringInput("Enter New Email: ");

                student.setName(updatedName);
                student.setAge(updatedAge);
                student.setGrade(updatedGrade);
                student.setEmail(updateEmail);

                System.out.println("All fields updated successfully!");
                break;
            case 0:
                System.out.println("Update Cancelled!");
            default:
                System.out.println("Invalid Option! No changes made.");
        }

        if (updateChoice >= 1 && updateChoice <= 5 && updateChoice != 0) {
            System.out.println("\n Updated information: ");
            System.out.println(student.toString());
        }

        pressEnterToContinue();
    }

    // Delete Student
    private static void deleteStudent() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("Delete Student");
        System.out.println("=".repeat(40));

        String studentId = getStringInput("\nEnter Student ID to delete: ");
        Student student = findStudentId(studentId);

        if (student == null) {
            System.out.println("\n Student with ID '" + studentId + "' not found!");
            pressEnterToContinue();
            return;
        }

        //Show Confirmation
        System.out.println("\nWarning: You are about to delete this student: ");
        System.out.println("-".repeat(40));
        System.out.println(student.toString());
        System.out.println("-".repeat(40));

        System.out.println("\nAre you sure you want to delete? (Y/N): ");
        String confirmation = scanner.nextLine().trim().toUpperCase();

        if (confirmation.equals("Y") || confirmation.equals("YES")) {
            studentList.remove(student);
            System.out.println("\nStudent with ID: '" + studentId + "' has been deleted successfully. ");
        } else {
            System.out.println("\nDeletion cancelled. Student record is safe.");
        }

        pressEnterToContinue();
    }
}
