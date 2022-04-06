package com.example.tutorhub;

import android.location.Location;

import java.util.List;

public class User {
    /** User based variables */
    private String username;
    private String password;
    private Student studentRole = null;
    private Tutor tutorRole = null;
    private String name;
    private String university;
    private String phoneNumber;
    private String email;
    public LastLocation location;


    /**
     * Constructor
     */
    User(String username, String name, String password, boolean student, boolean tutor,
         String phoneNumber, String email, LastLocation location) {
        this.username = username;
        this.name = name;
        this.password = password;
        if (student) { this.studentRole = new Student(); }
        if (tutor) { this.tutorRole = new Tutor(); }
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.location = location;
    }
    User(){

    }

    User(String username, String name, String password, boolean student, boolean tutor, String phoneNumber,
         String university, String email, LastLocation location) {
        this.username = username;
        this.name = name;
        this.password = password;
        if (student) { this.studentRole = new Student(); }
        if (tutor) { this.tutorRole = new Tutor(); }
        this.phoneNumber = phoneNumber;
        this.university = university;
        this.email = email;
        this.location = location;
    }

    /**
     * Resets Password of User
     * @param newPassword to be set
     * @throws IllegalArgumentException if {@code newPassword == password}
     * @post {@code password = newPassword}
     */
    public void resetPassword(String newPassword) {
        if (password.equals(newPassword)) {
            throw new IllegalArgumentException("newPassword == password");
        }
        password = newPassword;

    }

    /**
     * Returns username of user
     * @return {@code this.username}
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns email of user
     * @return {@code this.email}
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns phone number of user
     * @return {@code this.phoneNumber}
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns name of user
     * @return {@code this.name}
     */
    public String getName() {
        return name;
    }

    /**
     * Returns password of user
     * @return {@code this.password}
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets name of user
     * @param name to be set
     * @post {@code this.name = name}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds tutor role to user
     * @param role role to add
     * @throws IllegalArgumentException if user already has role
     */
    public void addTutorRole(Tutor role) {
        if(tutorRole != null) {
            throw new IllegalArgumentException("User already is a tutor");
        }
        tutorRole = role;
    }

    /**
     * Adds student role to user
     * @param role role to add
     * @throws IllegalArgumentException if user already has role
     */
    public void addStudentRole(Student role) {
        if(studentRole != null) {
            throw new IllegalArgumentException("User already is a student");
        }
        studentRole = role;
    }

    /**
     * @return if {@code tutorRole == null}
     */
    public boolean isTutor() { return tutorRole != null; }

    /**
     * @return if {@code studentRole == null}
     */
    public boolean isStudent() { return studentRole != null; }

    /**
     * @return studentRole
     */
    public Student getStudentRole() { return studentRole; }

    /**
     * @return studentRole
     */
    public Tutor getTutorRole() { return tutorRole; }

}
