package org.keni.tech.onlinecourseapplication.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class AdminDto {
    private String adminFirstName;
    private String adminLastName;
    private String adminName;
    private String adminEmail;
    private String adminPhone;
    private List<CourseDto> courses;
}