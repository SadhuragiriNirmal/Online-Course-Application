package org.keni.tech.onlinecourseapplication.dto;

import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class MyCourseDto {
    private String myCourseName;
    private String myCourseDescription;
    private String myCourseObjective;
    @Lob
    private byte[] myCourseVideoResources;
}
