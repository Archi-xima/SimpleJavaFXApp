package com.studentapp.view;

import com.studentapp.model.Student;

import java.util.List;
import java.util.Scanner;

/**
 * View class responsible for handling console I/O and user interactions.
 * Kept decoupled from business logic and database access.
 */
public class StudentConsoleView {
    private final Scanner scanner;

    public StudentConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    public void displayWelcomeBanner() {
        System.out.println("==================================================");
        System.out.println("          STUDENT REGISTRATION SYSTEM (MVC)       ");
        System.out.println("==================================================");
    }

    public void displayMainMenu() {
        System.out.println("\n----------------- MAIN MENU -----------------");
        System.out.println("1. Register New Student");
        System.out.println("2. View All Registered Students");
        System.out.println("3. Search Student by ID");
        System.out.println("4. Exit Application");
        System.out.print("Please enter your choice (1-4): ");
    }

    public String readUserInput() {
        return scanner.nextLine().trim();
    }

    public String promptStudentId() {
        System.out.print("Enter Student ID (e.g., STU101): ");
        return scanner.nextLine();
    }

    public String promptStudentName() {
        System.out.print("Enter Full Name: ");
        return scanner.nextLine();
    }

    public String promptStudentEmail() {
        System.out.print("Enter Email Address: ");
        return scanner.nextLine();
    }

    public String promptStudentCourse() {
        System.out.print("Enter Course/Major: ");
        return scanner.nextLine();
    }

    public void displaySuccessMessage(String message) {
        System.out.println("\n[SUCCESS] " + message);
    }

    public void displayErrorMessage(String errorMessage) {
        System.out.println("\n[ERROR] " + errorMessage);
    }

    public void displayInfoMessage(String infoMessage) {
        System.out.println("\n[INFO] " + infoMessage);
    }

    public void displayStudentDetails(Student student) {
        System.out.println("\n--- Student Details ---");
        System.out.println("ID     : " + student.getId());
        System.out.println("Name   : " + student.getName());
        System.out.println("Email  : " + student.getEmail());
        System.out.println("Course : " + student.getCourse());
        System.out.println("-----------------------");
    }

    public void displayAllStudents(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("\n[INFO] No students registered yet.");
            return;
        }

        System.out.println("\n==========================================================================");
        System.out.printf("%-12s | %-24s | %-24s | %-15s%n", "STUDENT ID", "NAME", "EMAIL", "COURSE");
        System.out.println("--------------------------------------------------------------------------");
        for (Student s : students) {
            System.out.printf("%-12s | %-24s | %-24s | %-15s%n",
                    s.getId(), s.getName(), s.getEmail(), s.getCourse());
        }
        System.out.println("==========================================================================");
        System.out.println("Total Registered: " + students.size() + " student(s)");
    }

    public void displayExitMessage() {
        System.out.println("\nThank you for using the Student Registration System. Goodbye!");
    }
}
