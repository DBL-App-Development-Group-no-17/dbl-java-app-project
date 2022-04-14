package com.example.tutorhub;

import java.util.ArrayList;
import java.util.List;

public class Tutor extends UserRoles{
    /*
     * To add:
     * Tutor locations
     */



    /** variable declerations */
    private String contactInfo = "emailaddress";
    private float rating = 0;
    private List<String> studentHistory = new ArrayList<>();


    /**
     * adds rating to tutor
     * @throws IllegalArgumentException if {@code rating < 1 || rating > 5 }
     */
    public void addRating(float rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("rating < 1 or > 5 ");
        }
        if (this.rating < 0) {
            this.rating = rating;
        }
        else{
            this.rating = (float) (this.rating + rating)/2;
        }
    }

    public float getRating() {
        return this.rating;
    }

    /**
     * sets contact info
     * @param info to be set
     */
    public void setContactInfo(String info) { contactInfo = info; }

    /**
     * @return contactInfo
     */
    public String getContactInf() { return contactInfo; }

    /**
     * adds student to studentHistory
     * @param student to be added
     * @throws IllegalArgumentException if {@code student.isStudent() == false}
     */
    public void addStudent(String student){
       // if(!student.isStudent()) { throw new IllegalArgumentException("User is not a student"); }
        studentHistory.add(student);
    }

    /**
     * @return tutorHistory
     */
    public List<String> getStudentHistory() { return studentHistory; }



}
