package org.keni.tech.onlinecourseapplication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseId;
    private String courseRefAuthorId;
    private String courseName;
    private String courseDescription;
    private String courseObjective;
    private String authorName;
    private double coursePrice;
    @Lob
    private byte[] courseDemoVideo;
    @Lob
    private byte[] courseVideoResources;

    public Course() {

    }

    public Course(int courseId, String courseRefAuthorId, String courseName, String courseDescription, String courseObjective, String authorName, double coursePrice, byte[] courseDemoVideo, byte[] courseVideoResources) {
        this.courseId = courseId;
        this.courseRefAuthorId = courseRefAuthorId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.courseObjective = courseObjective;
        this.authorName = authorName;
        this.coursePrice = coursePrice;
        this.courseDemoVideo = courseDemoVideo;
        this.courseVideoResources = courseVideoResources;
    }

}
