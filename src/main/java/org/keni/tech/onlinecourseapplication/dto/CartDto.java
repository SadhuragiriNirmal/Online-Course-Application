package org.keni.tech.onlinecourseapplication.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter
@Getter
public class CartDto {
    private int courseCount;
    private double billableAmount;
    private List<CustomerCourseDto> CustomerCourseList;
}