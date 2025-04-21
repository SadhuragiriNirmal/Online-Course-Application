package org.keni.tech.onlinecourseapplication.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class PaymentDto {
    private String paymentDate;
    private String paymentStatus;
    private String paymentType;
    private double paymentAmount;
}
