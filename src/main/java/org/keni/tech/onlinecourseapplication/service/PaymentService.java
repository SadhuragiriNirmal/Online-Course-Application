package org.keni.tech.onlinecourseapplication.service;

import lombok.AllArgsConstructor;
import org.keni.tech.onlinecourseapplication.dto.PaymentDto;
import org.keni.tech.onlinecourseapplication.model.Payment;
import org.keni.tech.onlinecourseapplication.model.User;
import org.keni.tech.onlinecourseapplication.repo.PaymentRepo;
import org.keni.tech.onlinecourseapplication.repo.UserRepo;
import org.keni.tech.onlinecourseapplication.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepo paymentRepo;
    private final UserRepo userRepo;
    private final MyCourseService myCourseService;
    private final PaymentDto paymentDto;
    private final SecurityUtil securityUtil;

    public Payment paymentGateWay(Payment payment) {
        Optional<User> user = userRepo.findByEmail(securityUtil.getAuthenticatedUserEmail());
        if (user.isPresent()) {
            payment.setUserId(user.get().getUserId());
            Payment savedPayment = paymentRepo.save(payment);
            myCourseService.myCourseConverter(user.get());
            return savedPayment;
        } else return null;
    }

    //payment Dto converter
    public PaymentDto paymentDtoConverter(Payment payment) {
        paymentDto.setPaymentDate(payment.getPaymentDate());
        paymentDto.setPaymentType(payment.getPaymentType());
        paymentDto.setPaymentAmount(payment.getPaymentAmount());
        paymentDto.setPaymentStatus(payment.getPaymentStatus());
        return paymentDto;
    }
}
