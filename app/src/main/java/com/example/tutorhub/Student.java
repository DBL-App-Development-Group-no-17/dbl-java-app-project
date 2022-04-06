package com.example.tutorhub;

import java.util.ArrayList;
import java.util.List;

public class Student extends UserRoles {
    /** variable declarations */
    private String string = "test";
    private int radius;
    private List<String> tutorHistory = new ArrayList<>();

    /**
     * Sets radius of student
     * @param radius search radius
     * @throws IllegalArgumentException if {@code radius < 0}
     */
    public void setRadius (int radius) {
        if (radius < 0) {
            throw new IllegalArgumentException(radius + " < 0");
        }
        this.radius = radius;
    }

    /**
     * @return radius
     */
    public int getRadius() { return radius; }

    /**
     * adds tutor to tutorHistory
     * @param tutor to be added
     * @throws IllegalArgumentException if {@code tutor.isTutor() == false}
     */
    public void addTutor(String tutor){
       // if(!tutor.isTutor()) { throw new IllegalArgumentException("User is not a tutor"); }
        tutorHistory.add(tutor);
    }

    /**
     * @return tutorHistory
     */
    public List<String> getTutorHistory() { return tutorHistory; }

}

