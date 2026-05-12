package com.sms.model;

import java.io.Serializable;

public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    // Private fields - Encapsulation
    private String id;
    private String name;
    private int age;
    private String grade;
    private String email;

    public Student(String id, String name, int age, String grade, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.email = email;
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGrade() {
        return grade;
    }

    public String getEmail() {
        return email;
    }

    // Setter methods
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", grade='" + grade + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String toFormattedString() {
        return String.format("| %-10s | %-20s | %-5d | %-10s | %-25s |",
                id, name, age, grade, email);
    }

    public String toFileString() {
        return id + "," + name + "," + age + "," + grade + "," + email;
    }

    public static Student fromFileString(String csvLine) {
        String[] parts = csvLine.split(",");

        if (parts.length == 5) {
            String id = parts[0];
            String name = parts[1];
            int age = Integer.parseInt(parts[2]);
            String grade = parts[3];
            String email = parts[4];

            return new Student(id, name, age, grade, email);
        }

        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Student student = (Student) obj;

        return id != null && id.equals(student.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}