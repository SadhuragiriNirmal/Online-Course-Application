package org.keni.tech.onlinecourseapplication.dto;

import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class CustomerCourseDto {
    private String courseName;
    private String courseRefAuthorId;
    private String courseDescription;
    private String courseObjective;
    private String authorName;
    @Lob
    private byte[] courseDemoVideo;
    private double coursePrice;
}
