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
                case2:
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
                    Scanner.nextLink();
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
}
