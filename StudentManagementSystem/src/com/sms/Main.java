package com.sms;

import com.sms.model.Student;
import com.sms.service.StudentService;
import com.sms.service.FileStorageService;
import com.sms.exception.StudentNotFoundException;
import com.sms.exception.DuplicateStudentException;
import com.sms.exception.InvalidInputException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Main class - Entry point for the Student Management System CLI application
 * This version includes full file handling (Phase 5).
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static StudentService studentService = new StudentService();

    public static void main(String[] args) {
        System.out.println("=" .repeat(50));
        System.out.println("   WELCOME TO STUDENT MANAGEMENT SYSTEM");
        System.out.println("   📁 Data will be automatically saved");
        System.out.println("=" .repeat(50));

        // Show file status on startup
        showFileStatus();

        boolean running = true;

        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            try {
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
                        fileManagementMenu();
                        break;
                    case 7:
                        manualSave();
                        break;
                    case 8:
                        manualLoad();
                        break;
                    case 0:
                        // Save before exit
                        try {
                            System.out.println("\n💾 Saving data before exit...");
                            studentService.saveData();
                            System.out.println("✅ Data saved successfully!");
                        } catch (IOException e) {
                            System.out.println("❌ Error saving data: " + e.getMessage());
                        }
                        running = false;
                        System.out.println("\n👋 Thank you for using Student Management System!");
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("\n❌ Invalid choice! Please enter 0-8");
                        pressEnterToContinue();
                }
            } catch (Exception e) {
                System.out.println("\n❌ An unexpected error occurred: " + e.getMessage());
                System.out.println("Please try again.");
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
        System.out.println("6. 📁 File Management");
        System.out.println("7. 💾 Manual Save");
        System.out.println("8. 📂 Manual Load");
        System.out.println("0. 🚪 Exit (Auto-save)");
        System.out.println("-" .repeat(50));
        System.out.println("💡 Auto-save is " +
                (studentService.isAutoSaveEnabled() ? "✅ ENABLED" : "❌ DISABLED"));
        System.out.println("📊 Total Students: " + studentService.getStudentCount());
        System.out.println("-" .repeat(50));
    }

    /**
     * Show file status on startup
     */
    private static void showFileStatus() {
        try {
            if (FileStorageService.dataFileExists()) {
                long fileSize = FileStorageService.getFileSize();
                String lastModified = FileStorageService.getLastModifiedTime();
                System.out.println("\n📁 Data file found!");
                System.out.println("   📄 File size: " + fileSize + " bytes");
                System.out.println("   🕐 Last modified: " + lastModified);
                System.out.println("   📊 Students loaded: " + studentService.getStudentCount());
            } else {
                System.out.println("\n📭 No existing data file found. Starting fresh.");
            }
            System.out.println("   💾 Auto-save: " +
                    (studentService.isAutoSaveEnabled() ? "ENABLED" : "DISABLED"));
        } catch (Exception e) {
            System.out.println("⚠️ Could not read file status: " + e.getMessage());
        }
    }

    // ========== ADD STUDENT (SAME AS PHASE 4) ==========

    private static void addStudent() {
        System.out.println("\n" + "=" .repeat(40));
        System.out.println("➕ ADD NEW STUDENT");
        System.out.println("=" .repeat(40));

        try {
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

                name = getStringInput("Enter Full Name: ");
                age = getIntInput("Enter Age: ");
                grade = getStringInput("Enter Grade (e.g., A, B+, 85%): ");
                email = getStringInput("Enter Email: ");

                Student newStudent = studentService.addStudent(name, age, grade, email);
                System.out.println("\n✅ Student added successfully!");
                System.out.println("📝 Student Details: " + newStudent.toString());

            } else if (idOption == 2) {
                studentId = getStringInput("Enter Student ID (e.g., S001): ");

                if (studentService.findStudentByIdSafe(studentId) != null) {
                    throw new DuplicateStudentException(studentId);
                }

                name = getStringInput("Enter Full Name: ");
                age = getIntInput("Enter Age: ");
                grade = getStringInput("Enter Grade (e.g., A, B+, 85%): ");
                email = getStringInput("Enter Email: ");

                studentService.validateStudentData(name, age, grade, email);

                Student student = new Student(studentId, name, age, grade, email);
                studentService.addStudent(student);
                System.out.println("\n✅ Student added successfully!");
                System.out.println("📝 Student Details: " + student.toString());
            } else {
                System.out.println("❌ Invalid option!");
            }

        } catch (InvalidInputException e) {
            System.out.println("\n❌ Validation Error: " + e.getMessage());
        } catch (DuplicateStudentException e) {
            System.out.println("\n❌ Duplicate Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n❌ Unexpected error while adding student: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    // ========== VIEW ALL STUDENTS ==========

    private static void viewAllStudents() {
        System.out.println("\n" + "=" .repeat(40));
        System.out.println("👁️ ALL STUDENTS");
        System.out.println("=" .repeat(40));

        try {
            System.out.println("\n" + studentService.getFormattedStudentList());
        } catch (StudentNotFoundException e) {
            System.out.println("\n📭 " + e.getMessage());
            System.out.println("Please add students using option 1.");
        } catch (Exception e) {
            System.out.println("\n❌ Error retrieving students: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    // ========== SEARCH STUDENT ==========

    private static void searchStudentById() {
        System.out.println("\n" + "=" .repeat(40));
        System.out.println("🔍 SEARCH STUDENT BY ID");
        System.out.println("=" .repeat(40));

        try {
            String studentId = getStringInput("\nEnter Student ID to search: ");
            Student foundStudent = studentService.findStudentById(studentId);

            System.out.println("\n✅ Student Found!");
            System.out.println("-" .repeat(50));
            System.out.println(foundStudent.toString());
            System.out.println("-" .repeat(50));

            // Option to search by name
            System.out.println("\n🔎 Want to search by name instead?");
            System.out.print("Enter name (or press Enter to skip): ");
            String nameSearch = scanner.nextLine().trim();

            if (!nameSearch.isEmpty()) {
                try {
                    List<Student> nameResults = studentService.searchStudentsByName(nameSearch);
                    System.out.println("\n📋 Students matching '" + nameSearch + "':");
                    for (Student s : nameResults) {
                        System.out.println("  • " + s.getId() + " - " + s.getName());
                    }
                } catch (StudentNotFoundException e) {
                    System.out.println("\n❌ " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("\n❌ " + e.getMessage());
                }
            }

        } catch (StudentNotFoundException e) {
            System.out.println("\n❌ " + e.getMessage());
            System.out.println("💡 Tip: Check the ID and try again.");
        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n❌ Error searching for student: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    // ========== UPDATE STUDENT ==========

    private static void updateStudent() {
        System.out.println("\n" + "=" .repeat(40));
        System.out.println("✏️ UPDATE STUDENT INFORMATION");
        System.out.println("=" .repeat(40));

        try {
            String studentId = getStringInput("\nEnter Student ID to update: ");
            Student student = studentService.findStudentById(studentId);

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
                    studentService.updateStudentField(studentId, "name", newName);
                    System.out.println("✅ Name updated successfully!");
                    break;
                case 2:
                    String newAge = getStringInput("Enter New Age: ");
                    studentService.updateStudentField(studentId, "age", newAge);
                    System.out.println("✅ Age updated successfully!");
                    break;
                case 3:
                    String newGrade = getStringInput("Enter New Grade: ");
                    studentService.updateStudentField(studentId, "grade", newGrade);
                    System.out.println("✅ Grade updated successfully!");
                    break;
                case 4:
                    String newEmail = getStringInput("Enter New Email: ");
                    studentService.updateStudentField(studentId, "email", newEmail);
                    System.out.println("✅ Email updated successfully!");
                    break;
                case 5:
                    String updatedName = getStringInput("Enter New Name: ");
                    String updatedAge = getStringInput("Enter New Age: ");
                    String updatedGrade = getStringInput("Enter New Grade: ");
                    String updatedEmail = getStringInput("Enter New Email: ");

                    studentService.updateStudent(studentId, updatedName,
                            Integer.parseInt(updatedAge), updatedGrade, updatedEmail);
                    System.out.println("✅ All fields updated successfully!");
                    break;
                case 0:
                    System.out.println("❌ Update cancelled.");
                    break;
                default:
                    System.out.println("❌ Invalid option! No changes made.");
            }

            if (updateChoice >= 1 && updateChoice <= 5) {
                Student updatedStudent = studentService.findStudentById(studentId);
                System.out.println("\n🔄 Updated Information:");
                System.out.println(updatedStudent.toString());
            }

        } catch (StudentNotFoundException e) {
            System.out.println("\n❌ " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("\n❌ Validation Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Invalid number format for age!");
        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n❌ Error updating student: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    // ========== DELETE STUDENT ==========

    private static void deleteStudent() {
        System.out.println("\n" + "=" .repeat(40));
        System.out.println("❌ DELETE STUDENT");
        System.out.println("=" .repeat(40));

        try {
            String studentId = getStringInput("\nEnter Student ID to delete: ");
            Student student = studentService.findStudentById(studentId);

            System.out.println("\n⚠️  WARNING: You are about to delete this student:");
            System.out.println("-" .repeat(40));
            System.out.println(student.toString());
            System.out.println("-" .repeat(40));

            System.out.print("\nAre you sure you want to delete? (Y/N): ");
            String confirmation = scanner.nextLine().trim().toUpperCase();

            if (confirmation.equals("Y") || confirmation.equals("YES")) {
                studentService.deleteStudent(studentId);
                System.out.println("\n✅ Student with ID '" + studentId + "' has been deleted successfully!");
            } else {
                System.out.println("\n❌ Deletion cancelled. Student record is safe.");
            }

        } catch (StudentNotFoundException e) {
            System.out.println("\n❌ " + e.getMessage());
            System.out.println("💡 Tip: Check the ID and try again.");
        } catch (Exception e) {
            System.out.println("\n❌ Error deleting student: " + e.getMessage());
        }

        pressEnterToContinue();
    }

    // ========== FILE MANAGEMENT MENU (NEW) ==========

    private static void fileManagementMenu() {
        System.out.println("\n" + "=" .repeat(40));
        System.out.println("📁 FILE MANAGEMENT");
        System.out.println("=" .repeat(40));

        System.out.println("\n1. 📄 View File Status");
        System.out.println("2. 💾 Create Backup");
        System.out.println("3. 🔄 Restore from Backup");
        System.out.println("4. 📤 Export Data to Custom File");
        System.out.println("5. 🗑️ Delete All Data Files");
        System.out.println("6. ⚙️ Toggle Auto-Save");
        System.out.println("0. 🔙 Back to Main Menu");

        int choice = getIntInput("\nEnter your choice (0-6): ");

        switch (choice) {
            case 1:
                viewFileStatus();
                break;
            case 2:
                createBackup();
                break;
            case 3:
                restoreFromBackup();
                break;
            case 4:
                exportData();
                break;
            case 5:
                deleteDataFiles();
                break;
            case 6:
                toggleAutoSave();
                break;
            case 0:
                System.out.println("🔙 Returning to main menu...");
                break;
            default:
                System.out.println("❌ Invalid choice!");
        }

        pressEnterToContinue();
    }

    private static void viewFileStatus() {
        System.out.println("\n📄 FILE STATUS");
        System.out.println("-" .repeat(40));

        try {
            boolean exists = FileStorageService.dataFileExists();
            System.out.println("📁 Data file exists: " + (exists ? "✅ Yes" : "❌ No"));

            if (exists) {
                long size = FileStorageService.getFileSize();
                String lastModified = FileStorageService.getLastModifiedTime();
                System.out.println("📊 File size: " + size + " bytes");
                System.out.println("🕐 Last modified: " + lastModified);
            }

            boolean backupExists = FileStorageService.backupFileExists();
            System.out.println("💾 Backup exists: " + (backupExists ? "✅ Yes" : "❌ No"));

            System.out.println("💾 Auto-save: " +
                    (studentService.isAutoSaveEnabled() ? "✅ ENABLED" : "❌ DISABLED"));
            System.out.println("👥 Students in memory: " + studentService.getStudentCount());

        } catch (IOException e) {
            System.out.println("❌ Error reading file status: " + e.getMessage());
        }
    }

    private static void createBackup() {
        System.out.println("\n💾 CREATING BACKUP");
        System.out.println("-" .repeat(40));

        try {
            FileStorageService.createBackup();
        } catch (IOException e) {
            System.out.println("❌ Error creating backup: " + e.getMessage());
        }
    }

    private static void restoreFromBackup() {
        System.out.println("\n🔄 RESTORE FROM BACKUP");
        System.out.println("-" .repeat(40));
        System.out.println("⚠️ WARNING: This will replace current data!");
        System.out.print("Are you sure? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("Y") || confirm.equals("YES")) {
            boolean success = FileStorageService.restoreFromBackup();
            if (success) {
                try {
                    studentService.loadData();
                    System.out.println("✅ Data restored successfully!");
                } catch (Exception e) {
                    System.out.println("❌ Error loading restored data: " + e.getMessage());
                }
            }
        } else {
            System.out.println("❌ Restore cancelled.");
        }
    }

    private static void exportData() {
        System.out.println("\n📤 EXPORT DATA");
        System.out.println("-" .repeat(40));

        try {
            String fileName = getStringInput("Enter export file name (e.g., export.txt): ");

            // Ensure file has .txt extension
            if (!fileName.endsWith(".txt")) {
                fileName += ".txt";
            }

            List<Student> students = studentService.getAllStudents();
            FileStorageService.exportToFile(students, fileName);

        } catch (StudentNotFoundException e) {
            System.out.println("❌ No students to export!");
        } catch (IOException e) {
            System.out.println("❌ Error exporting data: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void deleteDataFiles() {
        System.out.println("\n🗑️ DELETE DATA FILES");
        System.out.println("-" .repeat(40));
        System.out.println("⚠️ WARNING: This will delete all saved data!");
        System.out.print("Are you sure? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("Y") || confirm.equals("YES")) {
            try {
                FileStorageService.deleteAllDataFiles();
                studentService.deleteAllStudents();
                System.out.println("✅ All data files deleted and memory cleared!");
            } catch (IOException e) {
                System.out.println("❌ Error deleting files: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Deletion cancelled.");
        }
    }

    private static void toggleAutoSave() {
        boolean current = studentService.isAutoSaveEnabled();
        studentService.setAutoSaveEnabled(!current);
        System.out.println("✅ Auto-save is now " +
                (studentService.isAutoSaveEnabled() ? "ENABLED" : "DISABLED"));
    }

    // ========== MANUAL SAVE/LOAD ==========

    private static void manualSave() {
        System.out.println("\n💾 MANUAL SAVE");
        System.out.println("-" .repeat(40));

        try {
            studentService.saveData();
        } catch (IOException e) {
            System.out.println("❌ Error saving data: " + e.getMessage());
        }
    }

    private static void manualLoad() {
        System.out.println("\n📂 MANUAL LOAD");
        System.out.println("-" .repeat(40));
        System.out.println("⚠️ Warning: This will replace current data in memory!");
        System.out.print("Continue? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("Y") || confirm.equals("YES")) {
            try {
                studentService.loadData();
                System.out.println("✅ Data loaded successfully!");
            } catch (IOException e) {
                System.out.println("❌ Error loading data: " + e.getMessage());
            } catch (InvalidInputException e) {
                System.out.println("❌ Invalid data in file: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Unexpected error: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Load cancelled.");
        }
    }

    // ========== HELPER METHODS ==========

    private static String getStringInput(String prompt) {
        System.out.print(prompt + " ");
        String input = scanner.nextLine().trim();

        while (input.isEmpty()) {
            System.out.println("❌ Input cannot be empty!");
            System.out.print(prompt + " ");
            input = scanner.nextLine().trim();
        }
        return input;
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("❌ Input cannot be empty!");
                    continue;
                }

                int number = Integer.parseInt(input);
                return number;
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