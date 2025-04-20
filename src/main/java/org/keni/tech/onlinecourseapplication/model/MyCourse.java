package org.keni.tech.onlinecourseapplication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MyCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int myCourseId;
    private String myCourseName;
    private String myCourseDescription;
    private String myCourseObjective;
    private String myCourseRefAuthorId;
    @Lob
    private byte[] myCourseVideoResources;
}
