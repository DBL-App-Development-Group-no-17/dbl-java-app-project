package com.example.tutorhub;

import java.util.List;

public class UserRoles {
    /** variable declerations */
    private List<Subject> subjectTags;

    /**
     * Add subject to subject tags
     * @param subjectTag to be added
     * @throws IllegalArgumentException if {@code subjectTags.contains(subjectTag)}
     */
    public void setSubjectTag(Subject subjectTag) {
        for (Subject x: subjectTags) {
            if (x.equals(subjectTag)) {
                throw new IllegalArgumentException(subjectTag + " already in list");
            }
        }
        subjectTags.add(subjectTag);
    }

    public List<Subject> getSubjectTags(){
        return subjectTags;
    }


}
