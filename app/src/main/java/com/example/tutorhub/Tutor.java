package com.example.tutorhub;

import java.util.ArrayList;
import java.util.List;

public class Tutor extends UserRoles{
    /*
     * To add:
     * Tutor location
     */



    /** variable declerations */
    private String contactInfo;
    private List<String> studentHistory = new ArrayList<>();
    private float rating = -1;

    /**
     * adds rating to tutor
     * @throws IllegalArgumentException if {@code rating < 1 || rating > 5 }
     */
    public void addRating(int rating) {
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
    public void addTutor(String student){
       // if(!student.isStudent()) { throw new IllegalArgumentException("User is not a student"); }
        studentHistory.add(student);
    }

    /**
     * @return tutorHistory
     */
    public List<String> getStudentHistory() { return studentHistory; }



}
