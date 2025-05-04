package org.keni.tech.onlinecourseapplication.controller;

import org.keni.tech.onlinecourseapplication.dto.PaymentDto;
import org.keni.tech.onlinecourseapplication.model.Payment;
import org.keni.tech.onlinecourseapplication.service.PaymentService;
import org.keni.tech.onlinecourseapplication.util.ResponseStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payment")
public class PaymentRestController {

    private final PaymentService paymentService;

    public PaymentRestController(PaymentService paymentservice) {
        this.paymentService = paymentservice;
    }

    @PostMapping("gateway")
    public ResponseEntity<ResponseStructure<PaymentDto>> paymentGateway(@RequestBody Payment payment) {
        Payment donePayment = paymentService.paymentGateWay(payment);
        PaymentDto paymentDto = paymentService.paymentDtoConverter(donePayment);
        return ResponseEntity.ok().body(new ResponseStructure<>("Payment Done Successfully", HttpStatus.OK.value(), paymentDto));
    }
}
