package org.keni.tech.onlinecourseapplication.dto;

import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class CourseDto {
    private String courseName;
    private String CourseRefAuthorId;
    private String courseDescription;
    private String courseObjective;
    private String authorName;
    private double coursePrice;
    @Lob
    private byte[] courseDemoVideo;
    @Lob
    private byte[] courseVideoResources;
}
