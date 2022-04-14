package com.example.tutorhub;

import java.util.ArrayList;
import java.util.List;

public abstract class UserRoles {
    /** variable declarations */
    private List<Subject> subjectTags = new ArrayList<>();

    /**
     * Add subject to subject tags
     * @param subjectTag to be added
     * @throws IllegalArgumentException if {@code subjectTags.contains(subjectTag)}
     */
    public void setSubjectTag(Subject subjectTag) {
        for (Subject x: subjectTags) {
            if (x.equals(subjectTag)) {
                return;
            }
        }
        subjectTags.add(subjectTag);
    }

    /**
     * @return subjectTags
     */
    public List<Subject> getSubjectTags(){
        return subjectTags;
    }

    /**
     * delete subject tags
     */
    public void emptyTags() {subjectTags.clear();}


}