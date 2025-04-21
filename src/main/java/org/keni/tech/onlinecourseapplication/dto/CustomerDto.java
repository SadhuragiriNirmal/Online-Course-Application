package org.keni.tech.onlinecourseapplication.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter
@Getter
public class CustomerDto {
    private String customerFirstName;
    private String customerLastName;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private CartDto cartDto;
    private List<MyCourseDto> myCourses;
    private String token;
}