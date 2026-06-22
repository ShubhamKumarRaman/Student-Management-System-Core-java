package com.sms.service;

import com.sms.model.Student;
import com.sms.exception.InvalidInputException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * FileStorageService handles all file operations for the Student Management System.
 * This class demonstrates:
 * - File handling with BufferedReader/BufferedWriter
 * - FileReader/FileWriter
 * - Exception handling for I/O operations
 * - CSV file parsing
 * - NIO (New I/O) for modern file operations
 */
public class FileStorageService {

    private static final String DATA_DIRECTORY = "data";
    private static final String FILE_NAME = "students.txt";
    private static final String BACKUP_FILE_NAME = "students_backup.txt";
    private static final String SEPARATOR = ",";

    /**
     * Get the full file path for the data file
     * @return Path object for the data file
     */
    private static Path getDataFilePath() {
        return Paths.get(DATA_DIRECTORY, FILE_NAME);
    }

    /**
     * Get the full file path for the backup file
     * @return Path object for the backup file
     */
    private static Path getBackupFilePath() {
        return Paths.get(DATA_DIRECTORY, BACKUP_FILE_NAME);
    }

    /**
     * Ensure the data directory exists
     * @throws IOException if directory cannot be created
     */
    private static void ensureDataDirectoryExists() throws IOException {
        Path dataDir = Paths.get(DATA_DIRECTORY);
        if (!Files.exists(dataDir)) {
            Files.createDirectories(dataDir);
            System.out.println("📁 Created data directory: " + DATA_DIRECTORY);
        }
    }

    /**
     * Save all students to file
     * @param students List of students to save
     * @throws IOException if file operation fails
     */
    public static void saveStudents(List<Student> students) throws IOException {
        // Ensure directory exists
        ensureDataDirectoryExists();

        Path filePath = getDataFilePath();

        // Create backup before saving (if file exists)
        if (Files.exists(filePath)) {
            createBackup();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            // Write header
            writer.write("ID,Name,Age,Grade,Email");
            writer.newLine();

            // Write each student
            for (Student student : students) {
                String line = student.getId() + SEPARATOR +
                        student.getName() + SEPARATOR +
                        student.getAge() + SEPARATOR +
                        student.getGrade() + SEPARATOR +
                        student.getEmail();
                writer.write(line);
                writer.newLine();
            }

            System.out.println("✅ Students saved successfully to: " + filePath.toString());
            System.out.println("📊 Total students saved: " + students.size());

        } catch (IOException e) {
            System.err.println("❌ Error saving students: " + e.getMessage());
            throw new IOException("Failed to save students to file", e);
        }
    }

    /**
     * Load students from file
     * @return List of students loaded from file
     * @throws IOException if file operation fails
     * @throws InvalidInputException if data format is invalid
     */
    public static List<Student> loadStudents() throws IOException, InvalidInputException {
        Path filePath = getDataFilePath();

        // Check if file exists
        if (!Files.exists(filePath)) {
            System.out.println("📭 No data file found. Starting with empty student list.");
            return new ArrayList<>();
        }

        List<Student> students = new ArrayList<>();
        int lineNumber = 0;
        int errorCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Skip header
                if (isHeader && line.startsWith("ID")) {
                    isHeader = false;
                    continue;
                }
                isHeader = false;

                try {
                    Student student = parseStudentFromLine(line);
                    if (student != null) {
                        students.add(student);
                    }
                } catch (InvalidInputException e) {
                    errorCount++;
                    System.err.println("⚠️ Line " + lineNumber + " skipped: " + e.getMessage());
                    System.err.println("   Content: " + line);
                } catch (Exception e) {
                    errorCount++;
                    System.err.println("⚠️ Line " + lineNumber + " skipped: Unexpected error - " + e.getMessage());
                    System.err.println("   Content: " + line);
                }
            }

            System.out.println("✅ Students loaded successfully from: " + filePath.toString());
            System.out.println("📊 Total students loaded: " + students.size());
            if (errorCount > 0) {
                System.out.println("⚠️ Skipped " + errorCount + " invalid line(s)");
            }

            return students;

        } catch (FileNotFoundException e) {
            System.out.println("📭 File not found: " + filePath.toString());
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("❌ Error reading file: " + e.getMessage());
            throw new IOException("Failed to load students from file", e);
        }
    }

    /**
     * Parse a single line of CSV data into a Student object
     * @param line CSV line with student data
     * @return Student object
     * @throws InvalidInputException if data is invalid
     */
    private static Student parseStudentFromLine(String line) throws InvalidInputException {
        String[] parts = line.split(SEPARATOR);

        if (parts.length != 5) {
            throw new InvalidInputException("Invalid data format",
                    "Expected 5 fields but found " + parts.length);
        }

        try {
            String id = parts[0].trim();
            String name = parts[1].trim();
            int age = Integer.parseInt(parts[2].trim());
            String grade = parts[3].trim();
            String email = parts[4].trim();

            // Validate data
            if (id.isEmpty()) {
                throw new InvalidInputException("ID", "ID cannot be empty");
            }
            if (name.isEmpty()) {
                throw new InvalidInputException("Name", "Name cannot be empty");
            }
            if (age < 5 || age > 100) {
                throw new InvalidInputException("Age", "Age must be between 5 and 100");
            }
            if (grade.isEmpty()) {
                throw new InvalidInputException("Grade", "Grade cannot be empty");
            }
            if (email.isEmpty() || !email.contains("@")) {
                throw new InvalidInputException("Email", "Invalid email format");
            }

            return new Student(id, name, age, grade, email);

        } catch (NumberFormatException e) {
            throw new InvalidInputException("Age", "Age must be a valid number", e);
        }
    }

    /**
     * Create a backup of the current data file
     * @throws IOException if backup operation fails
     */
    public static void createBackup() throws IOException {
        Path sourcePath = getDataFilePath();
        Path backupPath = getBackupFilePath();

        if (!Files.exists(sourcePath)) {
            System.out.println("📭 No data file to backup.");
            return;
        }

        try {
            // Copy file with overwrite option
            Files.copy(sourcePath, backupPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            System.out.println("💾 Backup created: " + backupPath.toString());
        } catch (IOException e) {
            System.err.println("⚠️ Warning: Could not create backup: " + e.getMessage());
            // Don't throw exception - backup failure shouldn't stop the main operation
        }
    }

    /**
     * Restore from backup file
     * @return true if restore successful, false otherwise
     */
    public static boolean restoreFromBackup() {
        Path backupPath = getBackupFilePath();
        Path dataPath = getDataFilePath();

        if (!Files.exists(backupPath)) {
            System.out.println("❌ No backup file found!");
            return false;
        }

        try {
            Files.copy(backupPath, dataPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ Data restored from backup successfully!");
            return true;
        } catch (IOException e) {
            System.err.println("❌ Error restoring from backup: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if data file exists
     * @return true if file exists, false otherwise
     */
    public static boolean dataFileExists() {
        return Files.exists(getDataFilePath());
    }

    /**
     * Check if backup file exists
     * @return true if backup exists, false otherwise
     */
    public static boolean backupFileExists() {
        return Files.exists(getBackupFilePath());
    }

    /**
     * Get file size in bytes
     * @return File size or -1 if file doesn't exist
     * @throws IOException if error reading file
     */
    public static long getFileSize() throws IOException {
        Path filePath = getDataFilePath();
        if (Files.exists(filePath)) {
            return Files.size(filePath);
        }
        return -1;
    }

    /**
     * Get last modified time of data file
     * @return Last modified time as string
     * @throws IOException if error reading file
     */
    public static String getLastModifiedTime() throws IOException {
        Path filePath = getDataFilePath();
        if (Files.exists(filePath)) {
            return Files.getLastModifiedTime(filePath).toString();
        }
        return "File does not exist";
    }

    /**
     * Export students to a custom file
     * @param students List of students
     * @param customFileName Custom file name
     * @throws IOException if file operation fails
     */
    public static void exportToFile(List<Student> students, String customFileName) throws IOException {
        ensureDataDirectoryExists();

        Path customPath = Paths.get(DATA_DIRECTORY, customFileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(customPath.toFile()))) {
            // Write header
            writer.write("ID,Name,Age,Grade,Email");
            writer.newLine();

            for (Student student : students) {
                String line = student.getId() + SEPARATOR +
                        student.getName() + SEPARATOR +
                        student.getAge() + SEPARATOR +
                        student.getGrade() + SEPARATOR +
                        student.getEmail();
                writer.write(line);
                writer.newLine();
            }

            System.out.println("✅ Data exported to: " + customPath.toString());
        }
    }

    /**
     * Delete all data files (including backups)
     * @throws IOException if deletion fails
     */
    public static void deleteAllDataFiles() throws IOException {
        Path dataPath = getDataFilePath();
        Path backupPath = getBackupFilePath();

        boolean dataDeleted = false;
        boolean backupDeleted = false;

        if (Files.exists(dataPath)) {
            Files.delete(dataPath);
            dataDeleted = true;
        }

        if (Files.exists(backupPath)) {
            Files.delete(backupPath);
            backupDeleted = true;
        }

        if (dataDeleted || backupDeleted) {
            System.out.println("✅ Data files deleted successfully!");
        } else {
            System.out.println("📭 No data files to delete.");
        }
    }
}